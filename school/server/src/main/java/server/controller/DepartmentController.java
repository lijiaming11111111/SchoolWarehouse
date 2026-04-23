package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.department.InsertDepartmentDTO;
import com.school.dto.department.PageSelectDepartmentDTO;
import com.school.dto.department.UpdateDepartmentDTO;
import com.school.dto.warehouse.InsertWarehouseDTO;
import com.school.dto.warehouse.PageSelectWarehouseDTO;
import com.school.dto.warehouse.UpdateWarehouseDTO;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.deparrtment.SelectDepartmentVO;
import com.school.vo.warehouse.SelectWarehouseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.DepartmentService;

@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
@Tag(name = "部门接口")
@CrossOrigin
@RestController
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/insertDepartment")
    @Operation(summary = "插入部门")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> insertDepartment(@Validated @RequestBody InsertDepartmentDTO insertDepartmentDTO) {
        return Result.success("插入成功", departmentService.insertDepartment(insertDepartmentDTO));
    }

    @PostMapping("/updateDepartment")
    @Operation(summary = "更新部门")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateDepartment(@RequestBody UpdateDepartmentDTO updateDepartmentDTO) {
        return Result.success("更新成功", departmentService.updateDepartment(updateDepartmentDTO));
    }

    @PostMapping("/deleteDepartment")
    @Operation(summary = "删除部门")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> deleteDepartment(@RequestParam String id) {
        return Result.success("删除成功", departmentService.deleteDepartment(id));
    }

    @PostMapping("/pageSelectDepartment")
    @Operation(summary = "分页查询部门")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<SelectDepartmentVO>> pageSelectDepartment(@RequestBody PageSelectDepartmentDTO pageSelectDepartmentDTO) {
        return Result.success("查询成功", departmentService.pageSelectDepartment(pageSelectDepartmentDTO));
    }

}
