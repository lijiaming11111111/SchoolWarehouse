package com.school.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.school.enums.item.borrow.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemBorrow {

    /**
     * 借还记录ID
     */
    @Schema(description ="id")
    private String id;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID")
    private String itemId;

    /**
     * 借用人当时的姓名
     */
    @Schema(description = "借用人当时的姓名")
    private String borrowerUserName;

    /**
     * 借用人用户ID
     */
    @Schema(description = "借用人用户ID")
    private String borrowerUserId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private String departmentId;

    /**
     * 借用数量
     */
    @Schema(description = "借用数量")
    private Long quantity;

    /**
     * 借用时间
     */
    @Schema(description = "借用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowTime;

    /**
     * 预计归还时间
     */
    @Schema(description = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnTime;

    /**
     * 实际归还时间
     */
    @Schema(description = "实际归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnTime;

    /**
     * 设备状态(1=借用 2=归还 3=逾期)
     */
    @Schema(description = "设备状态(1=借用 2=归还 3=逾期)")
    private Status status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
