package com.smartNotes.vo.user;

import com.smartNotes.enums.user.Gender;
import com.smartNotes.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurrentUserDataVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "性别（0=男 1=女）")
    private Gender gender;

    @Schema(description = "电话号码")
    private String telephone;

    @Schema(description = "用户头像")
    private FileDataVO photo;

}
