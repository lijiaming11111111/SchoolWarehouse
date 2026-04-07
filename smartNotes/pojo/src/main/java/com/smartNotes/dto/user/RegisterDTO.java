package com.smartNotes.dto.user;

import com.smartNotes.entity.GroupInfo;
import com.smartNotes.enums.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterDTO {
    @Schema(description = "账号名称")
    @NotBlank(message = "账号名称不能为空")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号必须是11位数字")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String telephone;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "性别（0=男 1=女）")
    @NotNull(message = "性别不能为空")
    private Gender gender;
}
