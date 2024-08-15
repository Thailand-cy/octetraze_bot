package io.tonme.minebot.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liufei
 * @version 1.0
 * @project Hotfi-server-cloud
 * @description TODO RedisTemplate工具类
 * @date 2023/8/16 23:00:25
 */
@Slf4j
@Component
@DependsOn(value = {"redisTemplateConfig", "springUtils"})
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @PostConstruct
    public void init() {
        redisTemplate = SpringUtils.getBean("redisTemplateConfig");
    }

    /**
     * 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value
     * 不是长整型 ,将报错;
     * @param key
     * @param number
     */
    public static Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 是否存在key;
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        if (redisTemplate != null) {
            return redisTemplate.hasKey(key);
        }
        return false;

    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(String key, long timeout, TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    /**
     * 删除单个key
     *
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean delKey(String key) {
        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }

    /**
     * 删除多个key
     *
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static long delKeys(Collection<String> keys) {
        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }

    /**
     * 存入普通对象
     *
     * @param key Redis键
     * @param value 值
     */
    public static void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.MINUTES);
    }

    // 存储普通对象操作

    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public static void setValueTimeout(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public static void setValueTimeout(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public static <T> T getValue(String key) {
        return (T)redisTemplate.opsForValue().get(key);
    }


    public static String getString(String key) {
        return redisTemplate.opsForValue().get(key, 0, redisTemplate.opsForValue().size(key) - 1);
    }


    /**
     * 通过RedisTemplate实现模糊查询key前缀
     * @param key 键，用于模糊查询
     * @return 符合条件的key列表
     */
    public static List<String> getKeyByPrefix(String key){
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys != null && keys.size() > 0){
            return keys.stream().collect(Collectors.toList());
        }
        return null;
    }


    // 存储Hash操作

    /**
     * 同步递增
     * @param key 键
     * @param hKey hash键
     * @param num 正负值
     * @return 当前值
     */
    public static Long hashIncrement(String key, String hKey, long num) {
        return redisTemplate.opsForHash().increment(key, hKey, num);
    }

    /**
     * 确定哈希hashKey是否存在
     * @param key 键
     * @param hkey hash键
     * @return true=存在；false=不存在
     */
    public static boolean hasHashKey(String key,String hkey) {
        Boolean ret = redisTemplate.opsForHash().hasKey(key, hkey);
        return ret != null && ret;
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public static void hashPut(String key, String hKey, Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 往Hash中存入多个数据
     *
     * @param key Redis键
     * @param values Hash键值对
     */
    public static void hashPutAll(String key, Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T hashGet(String key, String hKey) {
        return (T)redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @return Hash对象
     */
    public static Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hashMultiGet(String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获取Hash中的所有数据
     *
     * @param key Redis键
     * @return Hash对象集合
     */
    public static List<Object> hashAllGet(String key){
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return redisTemplate.opsForHash().multiGet(key, keys);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键集合
     * @return Hash对象集合
     */
    public static long hashDeleteKeys(String key, String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey);
    }

    // 存储Set相关操作

    /**
     * 往Set中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long setSet(String key, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除Set中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long setDel(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 获取set中的所有对象
     *
     * @param key Redis键
     * @return set集合
     */
    public static Set<Object> getSetAll(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // 存储ZSet相关操作

    /**
     * 往ZSet中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long zsetSet(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除ZSet中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long zsetDel(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }

    // 存储List相关操作

    /**
     * 往List中存入数据
     *
     * @param key Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public static long listPush(String key, Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(String key, Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(String key, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public static List<Object> listGet(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * list中是否包含指定值
     * @param key
     * @param value
     * @return
     */
    public static boolean listHasKey(String key, String value) {
        // 获取列表中的所有元素
        Long size = redisTemplate.opsForList().size(key);
        for (int i = 0; i < size; i++) {
            Object element = redisTemplate.opsForList().index(key, i);
            if (element != null) {
                String e = (String) element;
                if (e.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 加锁，自旋重试三次
     * @param lockKey 锁键
     * @param requestId 锁ID
     * @param time 过期时间-秒
     * @return 锁状态
     */
    public static boolean spinlock(String lockKey, String requestId, long time) {
        boolean locked = false;
        int tryCount = 3;
        while (!locked && tryCount > 0) {
            locked = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, time, TimeUnit.SECONDS));
            tryCount--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("线程被中断" + Thread.currentThread().getId(), e);
            }
        }
        return locked;
    }

    /**
     * 使用lua脚本解锁，不会解除别人锁
     *
     * @param lockKey   锁键
     * @param requestId 锁ID
     */
    public static void unlockLua(String lockKey, String requestId) {
        if (lockKey == null || requestId == null ) {
            return;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //用于解锁的lua脚本位置
        redisScript.setLocation(new ClassPathResource("unlock.lua"));
        redisScript.setResultType(Long.class);
        //没有指定序列化方式，默认使用上面配置的
        redisTemplate.execute(redisScript, List.of(lockKey), requestId);
    }



}
