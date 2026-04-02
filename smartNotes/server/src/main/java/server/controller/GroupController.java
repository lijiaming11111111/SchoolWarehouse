package server.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smartNotes.context.BaseContext;
import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.entity.GroupApply;
import com.smartNotes.enums.role.ApplyStatus;
import com.smartNotes.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public Result<String> createGroup(@Valid @RequestBody CreateGroupDTO createGroupDTO) {
        String newGroupId = groupService.createGroup(createGroupDTO);
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


}
