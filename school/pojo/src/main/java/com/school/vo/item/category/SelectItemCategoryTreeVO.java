package com.school.vo.item.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SelectItemCategoryTreeVO {
    @Schema(description = "设备分类ID")
    @NotBlank(message = "设备分类ID为空")
    private String id;

    @Schema(description = "设备分类名称")
    private String itemCategoryName;

    @Schema(description = "父权限ID，0为顶级")
    private String parentId;

    @Schema(description = "子分类列表")
    private List<SelectItemCategoryTreeVO> children;
}
