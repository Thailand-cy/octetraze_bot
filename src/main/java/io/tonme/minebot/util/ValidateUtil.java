package io.tonme.minebot.util;

import cn.hutool.core.lang.Validator;
import io.tonme.minebot.enums.HttpsCodeEnum;
import io.tonme.minebot.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Program: 异常
 * @Description: 自定义抛出异常
 * @Author: LF
 * @Date: 2022/5/26 12:31
 **/
@Slf4j
public class ValidateUtil {
    /**
     * 判断是否合法
     * @param flag
     * @param httpsCodeEnum
     */
    public static void thrown(Boolean flag, HttpsCodeEnum httpsCodeEnum){
        if (flag)
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()));
    }

    /**
     * 判断是否合法，带有回调开关
     * @param flag
     * @param httpsCodeEnum
     */
    public static void thrown(Boolean flag, Integer callbackType, HttpsCodeEnum httpsCodeEnum){
        if (flag)
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()), callbackType);
    }
    public static void thrown(Integer callbackType, HttpsCodeEnum httpsCodeEnum){
        throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()), callbackType);
    }
    public static void thrown(Boolean flag, Integer callbackType, String msg){
        if (flag)
            throw new BusinessException(501, msg);
    }
    public static void thrown(Integer callbackType, String msg){
        throw new BusinessException(501, msg);
    }

    /**
     * 判断是否合法,添加后缀
     * @param flag
     * @param httpsCodeEnum
     */
    public static void thrown(Boolean flag, HttpsCodeEnum httpsCodeEnum,String ... parms){
        if (flag)
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()) + Arrays.toString(parms));
    }

    /**
     * 不做判断，直接抛出
     * @param httpsCodeEnum
     */
    public static void thrown(HttpsCodeEnum httpsCodeEnum){
        throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()));
    }

    /**
     * 自定义msg前缀
     * @param flag
     * @param key
     * @param httpsCodeEnum
     */
    public static void thrown(Boolean flag, String key, HttpsCodeEnum httpsCodeEnum){
        if (flag)
            throw new BusinessException(httpsCodeEnum.getCode(), key + " " + MessageUtil.get(httpsCodeEnum.getCodeStr()));
    }

    /**
     * 自定义msg前缀
     * @param flag
     * @param key
     * @param httpsCodeEnum
     */
    public static void thrown(Boolean flag, Integer callbackType, String key, HttpsCodeEnum httpsCodeEnum){
        if (flag)
            throw new BusinessException(httpsCodeEnum.getCode(), key + " " + MessageUtil.get(httpsCodeEnum.getCodeStr()), callbackType);
    }

    public static void thrown(Boolean flag, String message){
        if (flag)
            throw new BusinessException(501, message);
    }

    /**
     * 手动破除运行异常，被飞书捕获
     */
    public static void thrownRunException(String message) {
        throw new RuntimeException(message);
    }

    /**
     * 条件满足返回数据
     * @param value
     * @param httpsCodeEnum
     * @return
     * @param <T>
     */
    public static <T> T thrownReturn(T value, HttpsCodeEnum httpsCodeEnum) {
        if (Validator.isNull(value))
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()));
        return value;
    }

    /**
     * 条件满足返回数据, 带有Rpc请求需要返回的回调类型
     * @param value
     * @param httpsCodeEnum
     * @return
     * @param <T>
     */
    public static <T> T thrownReturn(T value, Integer callbackType, HttpsCodeEnum httpsCodeEnum) {
        if (Validator.isNull(value))
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()), callbackType);
        return value;
    }

    /**
     * 条件满足返回数据
     * @param value
     * @return
     * @param <T>
     */
    public static <T> T thrownReturn(T value, String message) {
        if (Validator.isNull(value))
            throw new BusinessException(501, message);
        return value;
    }

    /**
     * 如果不为NULL
     * @param value
     * @param httpsCodeEnum
     * @return
     * @param <T>
     */
    public static <T> void thrownNotEmpty(T value, HttpsCodeEnum httpsCodeEnum) {
        if (Validator.isNotEmpty(value))
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()));
    }
    public static <T> void thrownNotEmpty(T value, String message) {
        if (Validator.isNotEmpty(value))
            throw new BusinessException(501, message);
    }

    /**
     * 如果为NULL
     * @param value
     * @return
     * @param <T>
     */
    public static <T> void thrownEmpty(T value, HttpsCodeEnum httpsCodeEnum) {
        if (Validator.isEmpty(value))
            throw new BusinessException(httpsCodeEnum.getCode(), MessageUtil.get(httpsCodeEnum.getCodeStr()));
    }
    public static <T> void thrownEmpty(T value, String message) {
        if (Validator.isEmpty(value))
            throw new BusinessException(501, message);
    }

}
