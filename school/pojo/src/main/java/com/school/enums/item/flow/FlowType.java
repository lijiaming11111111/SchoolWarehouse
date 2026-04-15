package com.school.enums.item.flow;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "类型(1=入库 2=出库 3=借用 4=归还)")
public enum FlowType {

    /**
     * 设备状态-正常
     */
    PUT(1, "入库"),

    /**
     * 设备状态-借用中
     */
    STOCK(2, "出库"),
    /**
     * 设备状态-维修
     */
    BORROW(3, "借用"),
    /**
     * 设备状态-报废
     */
    RETURN(4, "归还");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;
}
