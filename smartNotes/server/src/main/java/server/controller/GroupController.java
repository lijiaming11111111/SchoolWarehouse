package server.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smartNotes.context.BaseContext;
import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.dto.group.PageQueryGroupDTO;
import com.smartNotes.entity.GroupApply;
import com.smartNotes.enums.role.ApplyStatus;
import com.smartNotes.result.PageResult;
import com.smartNotes.result.Result;
import com.smartNotes.vo.group.PageQueryGroupVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.service.GroupService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "群聊")
@Validated
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/createGroup")
    @Operation(summary = "创建群聊")
    @ApiOperationSupport(author = "燕怡明")
    public Result<String> createGroup(@RequestPart("dto")@Valid CreateGroupDTO dto,
                                      @RequestPart("photo") MultipartFile photo) {
        String newGroupId = groupService.createGroup(dto, photo);
        return Result.success("创建成功",newGroupId);
    }

    @PostMapping("/join")
    @Operation(summary = "加入群聊")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> joinGroup(@Valid@Schema(description = "群组id") @RequestParam("groupId")
                                         @NotBlank(message = "群组id不能为空") String groupId,
                                     @Valid@Schema(description = "用户id") @RequestParam("userId")
                                     @NotBlank(message = "用户id不能为空") String userId) {
        groupService.joinGroup(groupId, userId);
        return Result.success("加入成功",true);
    }

    @PostMapping("/applyJoinGroup")
    @Operation(summary = "申请加入群聊")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> applyJoinGroup(@Valid@Schema(description = "群组id") @RequestParam
                                              @Parameter(description = "群组id")
                                     @NotBlank(message = "群组id不能为空") String groupId) {
        groupService.applyJoinGroup(groupId);
        return Result.success("申请成功",true);
    }

    @GetMapping("/getCheckApplies")
    @Operation(summary = "群主/管理员查看【所有待我审核的申请】")
    @ApiOperationSupport(author = "燕怡明")
    public Result<List<GroupApply>> getCheckApplies() {
        List<GroupApply> groupApplies = groupService.getCheckApplies();
        return Result.success("获取申请列表成功",groupApplies);
    }

    @GetMapping("/getMyApplys")
    @Operation(summary = "查看我发起的申请")
    @ApiOperationSupport(author = "燕怡明")
    public Result<List<GroupApply>> getMyApplys() {
        List<GroupApply> groupApplies = groupService.getMyApplys();
        return Result.success("获取申请列表成功",groupApplies);
    }

    @PostMapping("/auditApply")
    @Operation(summary = "同意/拒绝 审核申请")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> auditApply(@Valid@Schema(description = "申请id") @RequestParam@Parameter(description = "申请id")
                                          @NotBlank(message = "申请id不能为空") String applyId,
                                          @Valid@Schema(description = "审核状态") @RequestParam
                                          @Parameter(description = "申请状态（0=待审核 1=同意 2=拒绝）")
                                          @NotNull(message = "审核状态不能为空") ApplyStatus applyStatus) {
        groupService.auditApply(applyId,applyStatus);
        return Result.success("审核成功",true);
    }

    @PostMapping("/kickMember")
    @Operation(summary = "踢出群聊 ")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> kickMember(@Valid@Schema(description = "群组id") @RequestParam
                                          @Parameter(description = "群组id")
                                          @NotBlank(message = "群组id不能为空") String groupId,
                                          @Valid@Schema(description = "用户id") @RequestParam
                                          @Parameter(description = "被踢出的用户id")
                                          @NotBlank(message = "被踢出的用户id不能为空") String targetUserId) {
        groupService.kickMember(groupId, targetUserId);
        return Result.success("踢出成功",true);
    }

    @PostMapping("/manageAdmin")
    @Operation(summary = "设置/取消管理员 ")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> manageAdmin(@Valid@Schema(description = "群组id") @RequestParam
                                      @Parameter(description = "群组id")
                                      @NotBlank(message = "群组id不能为空") String groupId,
                                      @Valid@Schema(description = "用户id") @RequestParam
                                      @Parameter(description = "被踢出的用户id")
                                      @NotBlank(message = "被踢出的用户id不能为空") String targetUserId,
                                       @Valid@Schema(description = "操作类型(1设置管理员,2 取消管理员)") @RequestParam
                                           @Parameter(description = "操作类型(1设置管理员,2 取消管理员)")
                                           @NotNull(message = "操作类型不能为空") Integer type) {
        groupService.manageAdmin(groupId, targetUserId,type);
        return Result.success("操作成功",true);
    }


    @PostMapping("/sendGroupMessage")
    @Operation(summary = "发送群消息")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Boolean> sendGroupMessage(
            @RequestParam@Parameter(description = "群组id",required = true)String groupId,
            @RequestParam@Parameter(description = "消息内容",required = true)String content,
            @RequestParam(required = false)@Parameter(description = "文件")MultipartFile file)  {
        groupService.sendGroupMessage(groupId, content,file);
        return Result.success("发送成功",true);
    }

    @PostMapping("/pageQueryGroup")
    @Operation(summary = "分页查询群组")
    @ApiOperationSupport(author = "燕怡明")
    public Result<PageResult<PageQueryGroupVO>> pageQueryGroup(@Valid @RequestBody PageQueryGroupDTO queryDTO) {
        PageResult<PageQueryGroupVO> pageResult = groupService.pageQueryGroup(queryDTO);
        return Result.success("查询成功", pageResult);
    }


}
