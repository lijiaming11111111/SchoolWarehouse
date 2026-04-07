package server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartNotes.annotation.CleanUpFilesOnError;
import com.smartNotes.annotation.FilePreSignature;
import com.smartNotes.context.BaseContext;
import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.dto.group.PageQueryGroupDTO;
import com.smartNotes.entity.*;
import com.smartNotes.enums.group.Mute;
import com.smartNotes.enums.role.ApplyStatus;
import com.smartNotes.enums.role.Role;
import com.smartNotes.exception.BaseException;
import com.smartNotes.result.PageResult;
import com.smartNotes.vo.group.PageQueryGroupVO;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.mapper.*;
import server.service.FileService;
import server.service.GroupService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;

    private final GroupMemberMapper groupMemberMapper;

    private final GroupApplyMapper groupApplyMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserMapper userMapper;

    private final FileService fileService;

    private final ChatMessageMapper chatMessageMapper;
    @Override
    @CleanUpFilesOnError
    public String createGroup(CreateGroupDTO createGroupDTO, MultipartFile photo) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(String.valueOf(IdWorker.getId()));
        groupInfo.setGroupName(createGroupDTO.getGroupName());
        groupInfo.setOwnerId(BaseContext.getCurrentUserId().toString());
        if (photo==null){
            throw new BaseException("请上传群聊头像");
        }
        groupInfo.setGroupPhotoId(fileService.upload(photo).toString());
        groupMapper.createGroup(groupInfo);
        groupMemberMapper.joinGroupOwner(groupInfo.getId(), BaseContext.getCurrentUserId().toString());
        groupMemberMapper.batchJoin(groupInfo.getId(), createGroupDTO.getUserIdList());
        return groupInfo.getId();
    }

    @Override
    public Boolean joinGroup(String groupId, String userId) {
        GroupMember oldGroupMember = groupMemberMapper.getGroupMember(groupId, userId);
        if (oldGroupMember != null) {
            throw new BaseException("已加入群组,无需重复加入");
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setId(String.valueOf(IdWorker.getId()));
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMemberMapper.joinGroup(groupMember);
        return true;
    }

    @Override
    public Boolean applyJoinGroup(String groupId) {
        // 1. 校验：是否已在群内
        Long inGroup = groupMemberMapper.selectCount(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId().toString())
        );
        if (inGroup > 0) {
            throw new BaseException("你已在群内");
        }

        // 2. 校验：是否已有待审核申请
        Long applying = groupApplyMapper.selectCount(
                new LambdaQueryWrapper<GroupApply>()
                        .eq(GroupApply::getGroupId, groupId)
                        .eq(GroupApply::getUserId, BaseContext.getCurrentUserId().toString())
                        .eq(GroupApply::getApplyStatus, ApplyStatus.PENDING)
        );
        if (applying > 0) {
            throw new BaseException("已提交申请，请勿重复申请");
        }

        // 3. 保存申请记录
        GroupApply apply = new GroupApply();
        apply.setGroupId(groupId);
        apply.setUserId(BaseContext.getCurrentUserId().toString());
        apply.setApplyStatus(ApplyStatus.PENDING); // 0=待审核
        apply.setApplyTime(LocalDateTime.now());
        groupApplyMapper.insert(apply);

        // ====================== 关键：推送给【群主(3) + 所有管理员(2)】 ======================
        // 查询该群所有有权限的人：role IN (2,3)
        List<GroupMember> adminList = groupMemberMapper.selectList(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .in(GroupMember::getRole, 2, 3) // 2=管理员，3=群主，都有权审核
        );

        // 逐个推送申请消息
        for (GroupMember admin : adminList) {
            simpMessagingTemplate.convertAndSendToUser(
                    admin.getUserId().toString(),
                    "/queue/groupApply",
                    apply
            );
        }
        return true;
    }

    @Override
    public List<GroupApply> getCheckApplies() {
        // 找到我有权限的群：我是管理员(2) 或 群主(3)
        List<GroupMember> adminGroups = groupMemberMapper.selectList(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId().toString())
                        .in(GroupMember::getRole, 2, 3) // 2=管理员，3=群主，都能审核
        );

        if (adminGroups.isEmpty()) return List.of();

        // 查询这些群的所有待审核申请
        return groupApplyMapper.selectList(
                new LambdaQueryWrapper<GroupApply>()
                        .in(GroupApply::getGroupId,
                                adminGroups.stream().map(GroupMember::getGroupId).toArray())
                        .eq(GroupApply::getApplyStatus, ApplyStatus.PENDING)
                        .orderByDesc(GroupApply::getApplyTime)
        );
    }

    @Override
    public List<GroupApply> getMyApplys() {
        return groupApplyMapper.selectList(
                new LambdaQueryWrapper<GroupApply>()
                        .eq(GroupApply::getUserId, BaseContext.getCurrentUserId().toString())
                        .orderByDesc(GroupApply::getApplyTime)
        );
    }

    @Override
    public Boolean auditApply(String applyId, ApplyStatus applyStatus) {
        // status 传入 1=同意  2=拒绝

        GroupApply apply = groupApplyMapper.selectById(applyId);
        if (apply == null || apply.getApplyStatus() != ApplyStatus.PENDING) {
            throw new BaseException("申请不存在或已处理");
        }

        // 校验权限：管理员(2) + 群主(3)
        GroupMember admin = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, apply.getGroupId())
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId().toString())
                        .in(GroupMember::getRole, 2, 3)
        );
        if (admin == null) {
            throw new BaseException("无权限审核");
        }

        // 更新状态
        groupApplyMapper.update(null,
                new LambdaUpdateWrapper<GroupApply>()
                        .eq(GroupApply::getId, applyId)
                        .set(GroupApply::getApplyStatus, applyStatus)
        );

        // ============== 同意才加入群 ==============
        if (applyStatus == ApplyStatus.AGREE) {
            joinGroup(apply.getGroupId(), apply.getUserId());
        }

        // ============== 推送结果给申请人 ==============
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", applyStatus == ApplyStatus.AGREE ? "agree" : "refuse");
        msg.put("groupId", apply.getGroupId());
        msg.put("applyId", apply.getId());
        msg.put("content", applyStatus == ApplyStatus.AGREE ? "你的入群申请已通过" : "你的入群申请已被拒绝");

        // ====================== 推送 + 确认 ======================
        System.out.println("【推送准备】给用户 " + apply.getUserId() + " 发送审核结果，内容：" + msg);
        try {
            simpMessagingTemplate.convertAndSendToUser(
                    apply.getUserId().toString(),
                    "/queue/applyResult",
                    msg
            );
            System.out.println("【推送成功】给用户 " + apply.getUserId() + " 发送审核结果完成");
        } catch (Exception e) {
            System.err.println("【推送失败】给用户 " + apply.getUserId() + " 发送审核结果，错误：" + e.getMessage());
            throw new BaseException("消息推送失败");
        }
        return true;
    }

    @Override
    public Boolean kickMember(String groupId, String targetUserId) {
        // 1. 不能踢自己
        if (targetUserId.equals(BaseContext.getCurrentUserId())) {
            throw new BaseException("不能踢出自己");
        }

        // 2. 判断操作者是否为 群主/管理员
        GroupMember admin = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId())
                        .in(GroupMember::getRole, 2, 3)
        );
        if (admin == null) {
            throw new BaseException("无权限操作");
        }

        // 3. 要踢的人的信息
        GroupMember target = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, targetUserId)
        );
        if (target == null) {
            throw new BaseException("该用户不在群内");
        }

        // 4. 不能踢群主
        if (target.getRole() == Role.GROUP_OWNER) {
            throw new BaseException("不能踢出群主");
        }

        // 5. 管理员不能踢管理员
        if (admin.getRole() == Role.ADMIN && target.getRole() == Role.ADMIN) {
            throw new BaseException("管理员不能踢出管理员");
        }

        // 6. 执行踢出（删除群成员）
        groupMemberMapper.delete(new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, targetUserId)
        );

        // ==================== 推送被踢通知 ====================
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "kick");
        msg.put("groupId", groupId);
        msg.put("content", "你已被管理员踢出群聊");

        try {
            simpMessagingTemplate.convertAndSendToUser(
                    targetUserId.toString(),
                    "/queue/groupNotify",
                    msg
            );
            System.out.println("【推送成功】用户 " + targetUserId + " 已被踢出群聊");
        } catch (Exception e) {
            System.err.println("【推送失败】踢出通知：" + e.getMessage());
            throw new BaseException("消息推送失败");
        }
        return true;
    }

    @Override
    public Boolean manageAdmin(String groupId, String targetUserId, Integer type) {
        // 1. 只有群主(role=3)能操作
        GroupMember operator = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId())
        );

        if (operator == null || operator.getRole() != Role.GROUP_OWNER) {
            throw new BaseException("只有群主可以设置或取消管理员");
        }

        // 2. 目标用户必须在群内
        GroupMember target = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, targetUserId)
        );

        if (target == null) {
            throw new BaseException("该用户不在群内");
        }

        // 3. 不能操作自己
        if (targetUserId.equals(BaseContext.getCurrentUserId())) {
            throw new BaseException("不能对自己进行管理员操作");
        }

        if (type == 1) {
            // 类型1：设置管理员
            if (target.getRole() == Role.ADMIN) {
                throw new BaseException("该用户已是管理员，无需重复设置");
            }
        } else {
            // 类型2：取消管理员
            if (target.getRole() == Role.NORMAL) {
                throw new BaseException("该用户已是普通成员，无需取消");
            }
        }

        // 4. 确定新角色
        // type=1 → 管理员(2)
        // type=2 → 普通成员(1)
        Role newRole = type == 1 ? Role.ADMIN : Role.NORMAL;

        // 5. 更新角色
        groupMemberMapper.update(null,
                new LambdaUpdateWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, targetUserId)
                        .set(GroupMember::getRole, newRole)
        );

        // 6. 推送通知
        String content = type == 1
                ? "你已被群主设置为群管理员"
                : "你的群管理员身份已被取消";

        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "adminChange");
        msg.put("groupId", groupId);
        msg.put("content", content);

        try {
            simpMessagingTemplate.convertAndSendToUser(
                    targetUserId.toString(),
                    "/queue/groupNotify",
                    msg
            );
            System.out.println("【推送成功】管理员变更通知 -> " + targetUserId);
        } catch (Exception e) {
            System.err.println("【推送失败】管理员变更通知：" + e.getMessage());
            throw new BaseException("消息推送失败");
        }
        return true;
    }

    @Override
    @CleanUpFilesOnError
    public Boolean sendGroupMessage(String groupId, String content, MultipartFile file) {
        // 1. 是否在群内
        GroupMember member = groupMemberMapper.selectOne(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, groupId)
                        .eq(GroupMember::getUserId, BaseContext.getCurrentUserId())
        );
        if (member == null) {
            throw new RuntimeException("你不在该群内");
        }

        // 2. 是否禁言
        if (Mute.MUTE.equals(member.getMute())) {
            throw new RuntimeException("你已被禁言，无法发送消息");
        }

        ChatMessage message = new ChatMessage();
        message.setGroupId(groupId);
        message.setSenderId(BaseContext.getCurrentUserId().toString());
        message.setContent(content);// 文字 + Emoji 全都存在这里
        message.setSendTime(LocalDateTime.now());
        if (file != null) {
            message.setFileId(fileService.upload(file).toString());
        }

        // 4. 存入数据库
        chatMessageMapper.insert(message);

        // 5. 推送给群所有人
        Map<String, Object> msg = new HashMap<>();
        msg.put("userId", BaseContext.getCurrentUserId());
        msg.put("content", message.getContent());
        msg.put("time", message.getSendTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        try {
            simpMessagingTemplate.convertAndSend("/topic/group/" + groupId, msg);
            System.out.println("【推送成功】群消息 -> " + groupId);
        } catch (Exception e) {
            System.err.println("【推送失败】群消息：" + e.getMessage());
            throw new BaseException("消息推送失败");
        }
        return true;
    }

    @Override
    @FilePreSignature
    public PageResult<PageQueryGroupVO> pageQueryGroup(PageQueryGroupDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<PageQueryGroupVO> pageQueryGroupPage = groupMapper.pageQueryGroup(queryDTO);
        return new PageResult<>(pageQueryGroupPage.getTotal(),pageQueryGroupPage.getResult());
    }


}
