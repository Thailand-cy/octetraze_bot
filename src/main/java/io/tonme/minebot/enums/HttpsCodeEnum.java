package io.tonme.minebot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bd
 * @project hot-fi-server
 * @description TODO Http状态码
 * @date 2023/8/15 16:07:43
 */
@Getter
@AllArgsConstructor
public enum HttpsCodeEnum {
    SUCCESS(200, "成功"),
    PARAMS_ERROR(401, "非法的请求数据"),
    ILLEGAL_REQUEST(410, "非法请求(请勿频繁或者通过非正常途径请求服务)"),
    SYSTEM_BUYS(500, "系统繁忙，请稍后再试"),
    CURRENT_LIMIT(501, "服务器压力过大，请稍后重试！"),
    OPERATION_FAIL(505, "操作失败，请重试。"),
    INVALID_REQUEST(506, "无效的请求数据。"),
    USER_NULL(777,  "用户信息为空"),
    REPEATED_CALL(999, "重复调用"),

    // Login Status
    LOGIN_NOT_TOKEN(1001,  "未登录"),
    LOGIN_INVALID_TOKEN(1002,  "登陆状态无效"),
    LOGIN_ACCOUNT_BANNED(1003,  "登陆失败，账号已被禁止登陆！"),

    // AUTH
    AUTH_LOGIN_VERIFY_FAIL(601, "登录验证失败,请重试。"),
    AUTH_NIKENAME_FAIL(602, "昵称格式错误，请按照提示输入。"),
    AUTH_NIKENAME_EXIST(603, "昵称已被使用。"),
    AUTH_NIKENAME_SETTING(604, "昵称已设置，不可再次修改。"),
    AUTH_X_AUTH_FAIL(605, "X授权失败。"),
    AUTH_USER_COUNTRY_EXIST(606, "已锁定阵营，不可以更换新阵营。"),
    AUTH_INVITE_BIND(607, "已经有绑定了邀请人，不可绑定其他用户。"),
    AUTH_INVITE_CODE_FAIL(608, "邀请码不正确，绑定失败，"),

    //market
    MARKET_NO_ITEM(10001,  "商品不存在"),
    MARKET_CANT_BUT(10002,  "库存不足,无法购买"),
    MARKET_CANT_BUT_DAY(10003,  "今天购买已达上限"),

    //mine
    MINE_CAP_MAX_LIMIT(20001,"Mint capacity level has reached the upper limit"),
    MINE_TOKEN_NOT_ENOUGH(20002,"Not enough token resource"),
    MINE_POINT_NOT_ENOUGH(20003,"Not enough point resource"),

    ;

    private Integer code;
    private String message;

    public String getCodeStr() {
        return String.valueOf(code);
    }

}
