package io.tonme.minebot.exception;

import lombok.Getter;

/**
 * @author liufei
 * @version 1.0
 * @project Hotfi-server-cloud
 * @description 自定义业务异常类
 * @date 2023/8/17 22:49:14
 */
@Getter
public class BusinessException extends RuntimeException{

    // 状态码
    private Integer code;
    // 重试状态
    private Integer callbackEnded;

    /**
     *
     * @param code  状态码
     * @param message   异常说明
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message, Integer callbackEnded) {
        super(message);
        this.code = code;
        this.callbackEnded = callbackEnded;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

}
