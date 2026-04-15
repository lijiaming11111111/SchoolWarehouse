package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.dto.item.flow.PageSelectItemFlowDTO;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.ItemFlowService;

@RestController
@RequestMapping("/itemFlow")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@Validated
@Tag(name = "设备库存流水")
public class ItemFlowController {

    private final ItemFlowService itemFlowService;

    @PostMapping("/pageSelectItemFlow")
    @Operation(summary = "分页查询设备库存流水")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<PageSelectItemFlowVO>> pageSelectItemFlow(@Validated @RequestBody PageSelectItemFlowDTO pageSelectItemFlowDTO) {
        return Result.success("查询成功",itemFlowService.pageSelectItemCategory(pageSelectItemFlowDTO));
    }
}
