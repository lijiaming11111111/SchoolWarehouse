package com.school.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginVerifyData {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "密码")
    private String password;

}