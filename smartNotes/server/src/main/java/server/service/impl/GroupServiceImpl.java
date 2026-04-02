package server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.smartNotes.context.BaseContext;
import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.entity.GroupApply;
import com.smartNotes.entity.GroupInfo;
import com.smartNotes.entity.GroupMember;
import com.smartNotes.enums.role.ApplyStatus;
import com.smartNotes.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.mapper.GroupApplyMapper;
import server.mapper.GroupMapper;
import server.mapper.GroupMemberMapper;
import server.service.GroupService;

import java.time.LocalDateTime;
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
    @Override
    public String createGroup(CreateGroupDTO createGroupDTO) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(String.valueOf(IdWorker.getId()));
        groupInfo.setGroupName(createGroupDTO.getGroupName());
        groupInfo.setOwnerId(BaseContext.getCurrentUserId().toString());
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


}
