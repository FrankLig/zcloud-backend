package com.bom.zcloudbackend.common;

import lombok.Data;

/**
 * <p>
 * 统一结果返回
 * </p>
 *
 * @author Frank Liang
 * @param <T>
 */
@Data
public class RespResult<T> {

    private Boolean success = true;
    private Integer code;
    private String message;
    private T data;

    public static RespResult success() {
        RespResult result = new RespResult();
        result.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return result;
    }

    public static RespResult setResult(ResultCodeEnum result) {
        RespResult r = new RespResult();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    public static RespResult fail() {
        RespResult result = new RespResult();
        result.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        result.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        result.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return result;
    }

    public RespResult data(T param) {
        this.setData(param);
        return this;
    }

    public RespResult message(String message) {
        this.setMessage(message);
        return this;
    }


}
