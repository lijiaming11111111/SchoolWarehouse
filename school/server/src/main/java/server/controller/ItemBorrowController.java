package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.item.borrow.PageSelectItemBorrowDTO;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.item.borrow.PageSelectItemBorrowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.ItemBorrowService;

@RestController
@RequestMapping("/itemBorrow")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="设备借用流水")
@Slf4j
@Validated
public class ItemBorrowController {

    private final ItemBorrowService itemBorrowService;

    @PostMapping("/pageSelectItemBorrow")
    @Operation(summary = "分页查询设备借用流水")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<PageSelectItemBorrowVO>> pageSelectItemBorrow(@Validated @RequestBody PageSelectItemBorrowDTO pageSelectItemBorrowDTO) {
        return Result.success("查询成功",itemBorrowService.pageSelectItemBorrow(pageSelectItemBorrowDTO));
    }
}
