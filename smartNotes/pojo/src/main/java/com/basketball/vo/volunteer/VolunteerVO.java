package com.basketball.vo.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VolunteerVO {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "名")
    private String firstName;
    @Schema(description = "姓")
    private String lastName;
    @Schema(description = "性别")
    private String gender;
    @Schema(description = "出生日期,格式为yyyy-MM-dd", pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Schema(description = "电话")
    private String telephone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "职位")
    private String occupation;
    @Schema(description = "赛事id")
    private Long eventId;
}
