package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.warehouse.InsertWarehouseDTO;
import com.school.dto.warehouse.PageSelectWarehouseDTO;
import com.school.dto.warehouse.UpdateWarehouseDTO;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.warehouse.SelectWarehouseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "仓库")
@Validated
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("/insertWarehouse")
    @Operation(summary = "插入仓库")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> insertWarehouse(@Validated @RequestBody InsertWarehouseDTO insertWarehouseDTO) {
        return Result.success("插入成功", warehouseService.insertWarehouse(insertWarehouseDTO));
    }

    @PostMapping("/updateWarehouse")
    @Operation(summary = "更新仓库")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateWarehouse(@RequestBody UpdateWarehouseDTO updateWarehouseDTO) {
        return Result.success("更新成功", warehouseService.updateWarehouse(updateWarehouseDTO));
    }

    @PostMapping("/deleteWarehouse")
    @Operation(summary = "删除仓库")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> deleteWarehouse(@RequestParam String id) {
        return Result.success("删除成功", warehouseService.deleteWarehouse(id));
    }

    @PostMapping("/pageSelectWarehouse")
    @Operation(summary = "分页查询仓库")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<SelectWarehouseVO>> pageSelectWarehouse(@RequestBody PageSelectWarehouseDTO pageSelectWarehouseDTO) {
        return Result.success("查询成功", warehouseService.pageSelectWarehouse(pageSelectWarehouseDTO));
    }
}
