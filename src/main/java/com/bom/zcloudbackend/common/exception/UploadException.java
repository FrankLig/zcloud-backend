package com.bom.zcloudbackend.common.exception;

/**
 * <p>
 *     上传异常处理类
 * </p>
 *
 * @author Frank Liang
 */
public class UploadException extends RuntimeException {

    public UploadException(Throwable cause) {
        super("上传出现了异常", cause);
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
