package com.school.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ItemCategory {
    @Schema(description = "设备分类ID")
    private String id;

    @Schema(description = "设备分类名称")
    private String itemCategoryName;

    @Schema(description = "父权限ID，0为顶级")
    private String parentId;
}
