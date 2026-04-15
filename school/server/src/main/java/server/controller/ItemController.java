package server.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.item.*;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.item.SelectItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.ItemService;

@RestController
@RequestMapping("/item")
@Validated
@Tag(name = "设备信息")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/insertItem")
    @Operation(summary = "插入设备信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> insertItem(@Validated @RequestBody InsertItemDTO insertItemDTO) {
        return Result.success("插入成功",itemService.insertItem(insertItemDTO));
    }

    @PostMapping("/updateItem")
    @Operation(summary = "更新设备信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateItem(@Validated @RequestBody UpdateItemDTO updateItemDTO) {
        return Result.success("更新设备成功", itemService.updateItem(updateItemDTO));
    }

    @PostMapping("/deleteItem")
    @Operation(summary = "删除设备信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> deleteItem(@RequestParam String id) {
        return Result.success("删除设备成功",itemService.deleteItem(id));
    }

    @PostMapping("/selectItem")
    @Operation(summary = "查询设备信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<SelectItemVO> selectItem(@RequestParam String id) {
        return Result.success("查询设备成功", itemService.selectItem(id));
    }

    @PostMapping("/updateItemStatus")
    @Operation(summary = "修改设备信息状态")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateItemStatus(@Validated @RequestBody UpdateItemStatusDTO updateItemStatusDTO) {
        return Result.success("修改设备状态成功", itemService.updateItemStatus(updateItemStatusDTO));
    }

    @PostMapping("/pageSelectItem")
    @Operation(summary = "分页查询设备信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<SelectItemVO>> pageSelectItem(@Validated @RequestBody PageSelectItemDTO pageSelectItemDTO) {
        return Result.success("分页查询设备成功成功", itemService.pageSelectItem(pageSelectItemDTO));
    }

    @PostMapping("/putItem")
    @Operation(summary = "入库设备")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> putItem(@Validated @RequestBody PutItemDTO putItemDTO) {
        return Result.success("入库设备成功", itemService.putItem(putItemDTO));
    }

    @PostMapping("/stockItem")
    @Operation(summary = "出库设备")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> stockItem(@Validated @RequestBody StockItemDTO stockItemDTO) {
        return Result.success("出库设备成功", itemService.stockItem(stockItemDTO));
    }
}
