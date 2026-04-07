package com.smartNotes.entity;

import com.smartNotes.enums.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class User {
    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "账号名称")
    private String name;

    @Schema(description = "手机号")
    private String telephone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户头像ID")
    private String photoId;

    @Schema(description = "性别（0=男 1=女）")
    private Gender gender;
}
