package xyz.zhangxing.spring.extend;

import java.lang.annotation.*;

/**
 * 标识在方法上面，用于动态操作缓存
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheManage {

    /**
     * 缓存的key
     * */
    String key();

    /**
     * 缓存组件的配置文件
     * 可能需要从多个缓存组件中获取数据
     */
    String configFile() default "cache.conf";

    boolean isClearCache() default false;

    String clearCacheFlag() default  "";
}
