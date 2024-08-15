package io.tonme.minebot.util;

import io.swagger.v3.oas.annotations.media.Schema;
import io.tonme.minebot.enums.HttpsCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author liufei
 * @version 1.0
 * @project Hotfi-server-cloud
 * @description 统一响应对象
 * @date 2023/8/17 23:00:36
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 7862084985014817596L;
    @Schema(name = "code", description = "响应码")
    private Integer code;
    @Schema(name = "message", description = "响应说明")
    private String message;
    @Schema(name = "是否重试(RPC接口使用)", hidden = true)
    private Integer callbackEnded;
    @Schema(name = "data", description = "响应数据")
    private T data;

    /**
     * 成功响应
     * @param data  响应数据sdm,fnkjsdhfjsdhfb
     * @return  响应数据对象
     * @param <T>  泛型定义
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(200)
                .setMessage("success")
                .setData(data);
    }

    /**
     * 失败响应
     * @param code  状态码
     * @param message  响应消息
     * @return  响应数据对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message)
                .setData(null);
    }

    /**
     * 失败响应
     * @param code  状态码
     * @param message  响应消息
     * @return  响应数据对象
     */
    public static <T> Result<T> error(Integer code, String message, Integer callbackEnded) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message)
                .setCallbackEnded(callbackEnded)
                .setData(null);
    }

    /**
     * 失败响应
     * @param codeEnum 状态枚举
     * @return  响应数据对象
     */
    public static <T> Result<T> error(HttpsCodeEnum codeEnum) {
        return new Result<T>()
                .setCode(codeEnum.getCode())
                .setMessage(MessageUtil.get(codeEnum.getCodeStr()))
                .setData(null);
    }

}
