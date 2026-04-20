package com.school.enums.item.borrow;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "设备状态(1=借用 2=归还 3=逾期)")
public enum Status {

    BORROW(1, "借用"),

    RETURN(2, "归还"),

    OVERDUE(3,"逾期");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;
}
