package com.basketball.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserLoginData {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "当前Token")
    private String token;
}
