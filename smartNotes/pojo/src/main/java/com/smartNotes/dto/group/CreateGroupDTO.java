package com.smartNotes.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateGroupDTO {
    @Schema(description = "群组名称")
    @NotBlank(message = "群组名称不能为空")
    private String groupName;

    @Schema(description = "用户id列表")
    @NotEmpty(message = "用户表不能为空")
    private List<String> userIdList;
}
