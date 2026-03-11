package com.basketball.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurrentUserDataVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "名")
    private String firstName;

    @Schema(description = "性")
    private String lastName;

    @Schema(description = "电话号码")
    private String telephone;

}
