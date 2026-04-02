package com.smartNotes.enums.role;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "申请状态（0=待审核 1=同意 2=拒绝）")
public enum ApplyStatus {
    /**
     * 申请状态-待审核
     */
    PENDING(0, "待审核"),
    /**
     * 申请状态-同意
     */
    AGREE(1, "同意"),
    /**
     * 申请状态-拒绝
     */
    REJECT(2, "拒绝");


    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

    // 根据code获取枚举
    public static ApplyStatus getByCode(Integer code) {
        for (ApplyStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }

}
