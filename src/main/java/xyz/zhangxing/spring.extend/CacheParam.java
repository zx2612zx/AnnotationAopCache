package xyz.zhangxing.spring.extend;

import java.lang.annotation.*;

/**
 * 指定某个方法参数的名称
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheParam {
    String value() default  "";
}
