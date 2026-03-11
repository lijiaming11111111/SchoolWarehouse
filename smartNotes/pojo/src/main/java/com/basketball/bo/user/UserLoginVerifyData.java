package com.basketball.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserLoginVerifyData {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "名")
    private String firstName;

    @Schema(description = "姓")
    private String lastName;

    @Schema(description = "密码")
    private String password;


}