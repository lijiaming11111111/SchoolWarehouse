package com.school.vo.user;

import com.school.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CurrentUserDataVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "电话号码")
    private String telephone;

    @Schema(description = "头像文件ID")
    private FileDataVO face;
}
