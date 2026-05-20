package com.school.vo.item.borrow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.school.enums.item.borrow.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PageSelectItemBorrowVO {
    @Schema(description = "借还记录ID")
    private String id;

    @Schema(description = "设备ID")
    private String itemId;

    @Schema(description = "借用人当时的姓名")
    private String borrowerUserName;

    @Schema(description = "借用人用户ID")
    private String borrowerUserId;

    @Schema(description = "部门ID")
    private String departmentId;

    @Schema(description = "借用数量")
    private Long quantity;

    @Schema(description = "借用时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate borrowTime;

    @Schema(description = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnTime;

    @Schema(description = "实际归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnTime;

    @Schema(description = "归还状态")
    private Status status;

    @Schema(description = "备注")
    private String remark;
}
