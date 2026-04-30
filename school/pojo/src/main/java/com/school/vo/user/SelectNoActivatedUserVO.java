package com.school.vo.user;

import com.school.enums.user.Gender;
import com.school.enums.user.StatusEnum;
import com.school.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SelectNoActivatedUserVO {
    @Schema(description = "用户id")
    private String id;

    @Schema(description = "邮箱")
    private String email;
}
