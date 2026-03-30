package com.school.entity;

import com.school.enums.user.Gender;
import com.school.enums.user.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class User {
    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "性别枚举对象(1女,0男)")
    private Gender gender;

    @Schema(description = "联系电话")
    @Size(max = 11, message = "联系电话长度超过限制")
    private String telephone;

    @Schema(description = "密码")
    @Size(min = 8, max = 32, message = "请输入8~32位密码")
    private String password;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态枚举类(1,正常 0,禁用)")
    private StatusEnum statusEnum;

    @Schema(description = "部门id")
    private String departmentId;

    @Schema(description = "头像文件ID")
    private Long imageAddress;

    @Schema(description = "排序")
    private Integer sort;
}
