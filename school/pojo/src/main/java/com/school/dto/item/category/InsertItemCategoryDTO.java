package com.school.dto.item.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InsertItemCategoryDTO {
    @Schema(description = "设备分类名称")
    @NotBlank(message = "设备分类名称为空")
    private String itemCategoryName;

    @Schema(description = "父权限ID，0为顶级")
    @NotBlank(message = "父权限ID为空")
    private String parentId;
}
