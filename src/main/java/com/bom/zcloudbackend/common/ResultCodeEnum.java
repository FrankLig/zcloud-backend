package com.bom.zcloudbackend.common;

import lombok.Getter;

/**
 * <p>
 * 返回结果枚举类
 * </p>
 *
 * @author bom
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 20000, "success"),
    UNKNOWN_ERROR(false, 20001, "unknown_error"),
    PARAM_ERROR(false, 20002, "param_error"),
    NULL_POINT(false, 20003, "nullPoint_error"),
    INDEX_OUT_OF_BOUNDS(false, 20004, "outOfIndex_error");


    private final Boolean success;

    private final Integer code;

    private final String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
