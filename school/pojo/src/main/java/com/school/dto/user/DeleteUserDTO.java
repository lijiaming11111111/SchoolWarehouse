package com.school.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteUserDTO {
    @Schema(description = "需要删除的用户ID", required = true)
    @NotNull(message = "未传入需要删除的用户")
    private Long id;
}
