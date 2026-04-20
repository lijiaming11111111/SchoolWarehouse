package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.role.*;
import com.school.entity.Role;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.role.PageSelectRoleVO;
import com.school.vo.role.SelectRoleIdVO;
import com.school.vo.role.SelectRolePermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.FileService;
import server.service.RoleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "角色")
@Validated
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/insertRole")
    @Operation(summary = "插入角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> insertRole(@Valid @RequestBody InsertRoleDTO insertRoleDTO) {
        return Result.success("插入成功", roleService.insertRole(insertRoleDTO));
    }

    @PostMapping("/updateRole")
    @Operation(summary = "更新角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateRole(@Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        return Result.success("更新成功", roleService.updateRole(updateRoleDTO));
    }

    @PostMapping("/deleteRole")
    @Operation(summary = "删除角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> deleteRole(@Valid @RequestParam String id) {
        return Result.success("删除成功", roleService.deleteRole(id));
    }

    @PostMapping("/selectById")
    @Operation(summary = "根据ID查询角色详情")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<SelectRoleIdVO> selectRoleId(@Valid @RequestParam String id) {
        return Result.success("查询成功", roleService.selectRoleId(id));
    }

    @PostMapping("/pageSelectRole")
    @Operation(summary = "分页查询角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<PageSelectRoleVO>> pageSelectRole(@Valid @RequestBody PageSelectRoleDTO pageSelectRoleDTO) {
        return Result.success("查询成功", roleService.pageSelectRole(pageSelectRoleDTO));
    }

    @PostMapping("/assignRolePerm")
    @Operation(summary = "分配角色权限")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> assignRolePerm(@Valid @RequestBody AssignRolePermDTO assignRolePermDTO) {
        return Result.success("分配成功", roleService.assignRolePerm(assignRolePermDTO));
    }

    @PostMapping("selectRolePermissionId")
    @Operation(summary = "查询角色权限")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<SelectRolePermissionVO> selectRolePermissionId(@Valid @RequestParam String id) {
        return Result.success("查询成功", roleService.selectRolePermissionId(id));
    }

    @PostMapping("/updateRoleUser")
    @Operation(summary = "修好用户的角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateRoleUser(@Valid @RequestBody UpdateRoleUserDTO updateRoleUserDTO) {
        return Result.success("分配成功", roleService.updateRoleUser(updateRoleUserDTO));
    }

    @PostMapping("/insertRoleUser")
    @Operation(summary = "插入用户的角色")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> insertRoleUser(@Valid @RequestBody InsertRoleUserDTO insertRoleUserDTO) {
        return Result.success("插入成功", roleService.insertRoleUser(insertRoleUserDTO));
    }
}