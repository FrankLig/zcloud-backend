package com.bom.zcloudbackend.common.exception;

import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public RespResult error(NullPointerException e) {
        e.printStackTrace();
        log.error("全局异常拦截，发生空指针异常",e);
        return RespResult.setResult(ResultCodeEnum.NULL_POINT);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public RespResult error(IndexOutOfBoundsException e) {
        e.printStackTrace();
        log.error("全局异常拦截，发生数组下标越界异常",e);
        return RespResult.setResult(ResultCodeEnum.INDEX_OUT_OF_BOUNDS);
    }

    public RespResult error(Exception e) {
        e.printStackTrace();
        log.error("全局异常拦截，发生未知异常",e);
        return RespResult.setResult(ResultCodeEnum.UNKNOWN_ERROR);
    }
}
