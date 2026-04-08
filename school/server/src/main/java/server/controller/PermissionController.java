package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.permission.InsertPermissionDTO;
import com.school.dto.permission.UpdatePermissionDTO;
import com.school.result.Result;
import com.school.vo.permission.SelectPermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.PermissionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/permission")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name="权限")
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/insertPermission")
    @Operation(summary="插入权限")
    @ApiOperationSupport(author="厉佳铭")
    public Result<String> insertPermission(@Valid @RequestBody InsertPermissionDTO insertPermissionDTO) {
        return Result.success("插入成功",permissionService.insertPermission(insertPermissionDTO));
    }

    @PostMapping("/updatePermission")
    @Operation(summary="更新权限")
    @ApiOperationSupport(author="厉佳铭")
    public Result<Boolean> updatePermission(@Valid @RequestBody UpdatePermissionDTO updatePermissionDTO) {
        return Result.success("更新成功",permissionService.updatePermission(updatePermissionDTO));
    }

    @PostMapping("/deletePermission")
    @Operation(summary="删除权限")
    @ApiOperationSupport(author="厉佳铭")
    public Result<Boolean> deletePermission(@Valid @RequestParam String id) {
        return Result.success("删除成功",permissionService.deletePermission(id));
    }

    @PostMapping("/selectPermission")
    @Operation(summary="查询权限")
    @ApiOperationSupport(author="厉佳铭")
    public Result<List<SelectPermissionVO>> selectPermission() {
        return Result.success("查询成功",permissionService.selectPermission());
    }
}
