package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.item.category.InsertItemCategoryDTO;
import com.school.dto.item.category.PageSelectItemCategoryDTO;
import com.school.dto.item.category.SelectItemCategoryTreeDTO;
import com.school.dto.item.category.UpdateItemCategoryDTO;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.category.SelectItemCategoryTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.ItemCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/itemCategory")
@CrossOrigin
@Tag(name = "设备分类")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @PostMapping("/insertItemCategory")
    @Operation(summary="插入设备分类")
    @ApiOperationSupport(author="厉佳铭")
    public Result<String> insertItemCategory(@Valid @RequestBody InsertItemCategoryDTO insertItemCategoryDTO) {
        return Result.success("插入成功",itemCategoryService.insertItemCategory(insertItemCategoryDTO));
    }

    @PostMapping("/updateItemCategory")
    @Operation(summary="更新设备分类")
    @ApiOperationSupport(author="厉佳铭")
    public Result<Boolean> updateItemCategory(@Valid @RequestBody UpdateItemCategoryDTO updateItemCategoryDTO) {
        return Result.success("更新成功",itemCategoryService.updateItemCategory(updateItemCategoryDTO));
    }

    @PostMapping("/deleteItemCategory")
    @Operation(summary="删除设备分类")
    @ApiOperationSupport(author="厉佳铭")
    public Result<Boolean> deleteItemCategory(@RequestParam String id) {
        return Result.success("删除成功",itemCategoryService.deleteItemCategory(id));
    }

    @PostMapping("/selectItemCategory")
    @Operation(summary="分页查询设备分类")
    @ApiOperationSupport(author="厉佳铭")
    public Result<PageResult<PageSelectItemCategoryVO>> pageSelectItemCategory
            (@Valid @RequestBody PageSelectItemCategoryDTO pageSelectItemCategoryDTO) {
        return Result.success("查询成功",itemCategoryService.pageSelectItemCategory(pageSelectItemCategoryDTO));
    }

    @PostMapping("/selectItemCategoryTree")
    @Operation(summary="设备分类树形结构")
    @ApiOperationSupport(author="厉佳铭")
    public Result<List<SelectItemCategoryTreeVO>> selectItemCategoryTree() {
        return Result.success("查询成功",itemCategoryService.selectItemCategoryTree());
    }
}
