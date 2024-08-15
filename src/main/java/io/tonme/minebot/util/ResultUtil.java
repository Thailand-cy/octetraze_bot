package io.tonme.minebot.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * @author bd
 * @project Hotfi-server-cloud
 * @description TODO 分布式返回对象校验工具
 * @date 2023/9/18 20:23:12
 */
@Slf4j
public class ResultUtil {

    /**
     * 校验返回值
     * 异常直接抛出
     * 正常则返回数据
     * @param result 返回对象
     * @return 调用返回数据
     * @param <T> 泛型定义
     */
    public static <T> T check(Result<T> result) {
        // 异常返回
        if (result.getCode() != 200) {
            log.error("返回异常:[{}]", JSONObject.toJSONString(result));
            // 运行异常
            if (result.getCode() == 510) {
                throw new RuntimeException(result.getMessage());
            }
            // 业务抛出
            throw new RuntimeException(result.getMessage());
        }
        // 请求返回数据
        return result.getData();
    }

    /**
     * 校验返回值
     * 异常直接抛出
     * 正常则返回数据
     * @param result 返回对象
     * @return 调用返回数据
     * @param <T> 泛型定义
     */
    public static <T> T check(ResponseEntity<T> result) {
        // 异常返回
        if (result.getStatusCode().value() != 200) {
            log.error("返回异常:[{}]", JSONObject.toJSONString(result));
            // 运行异常
            if (result.getStatusCode().value() == 510) {
                throw new RuntimeException("Server api logic error, please try again later");
            }
            // 业务抛出
            throw new RuntimeException("Server api error");
        }
        // 请求返回数据
        return result.getBody();
    }

}
