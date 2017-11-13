package xyz.zhangxing.spring.extend;

import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存反射工具类
 */
public class CacheUtil {
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 从method的参数列表中检测指定的CacheParam注解的value属性值,如果为空则使用Spring封装好的ParameterNameDiscoverer,去读取参数的名称
     * <p>
     * 对于参数名称的获取，如果编译成Class文件时带上了参数名称信息，则可以正常获取，否则只有类似arg0,arg1这样的信息
     * <p>
     * Spring获取参数名称至少有如下几种方式：
     * 1、通过jdk8提供的getParameters方法
     * 2、通过ASM直接解析Class文件，从局部变量表中读取
     *
     * @param method
     * @param type
     * @param startIndex
     * @return
     */
    public static String getParameterName(Method method, Class type, int startIndex) {

        String paramName = null;
        Annotation[][] annotationArrays = method.getParameterAnnotations();

        for (int index = startIndex; index < annotationArrays.length; index++) {
            Annotation[] annotations = annotationArrays[index];
            for (Annotation a : annotations) {
                if (a.getClass().isAssignableFrom(type)) {
                    CacheParam cacheParam = CacheParam.class.cast(a);
                    paramName = cacheParam.value();

                    if (paramName == null || "".equals(paramName)) {
                        paramName = parameterNameDiscoverer.getParameterNames(method)[index];
                    }

                    break;
                }
            }
        }

        return paramName;
    }


    public static String getParameterName(Method method, Class type) {
        return getParameterName(method, type, 0);
    }

    public static Map<String, Object> getAllParamters(Method method, Class type, Object[] args) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        Annotation[][] annotationArrays = method.getParameterAnnotations();
        for (int index = 0; index < annotationArrays.length; index++) {
            Annotation[] annotations = annotationArrays[index];
            for (Annotation a : annotations) {

                if (type.isAssignableFrom(a.getClass())) {

                    CacheParam cacheParam = CacheParam.class.cast(a);
                    String paramName = cacheParam.value();
                    if (paramMap.containsKey(paramName)) {
                        throw new IllegalStateException("method:" + method.getName() + "参数列表中的@CacheParam定义的属性值重复");
                    }

                    if (paramName == null || "".equals(paramName)) {
                        paramName = parameterNameDiscoverer.getParameterNames(method)[index];
                    }
                    paramMap.put(paramName, args[index]);
                    break;
                }
            }
        }
        return paramMap;
    }


    public static <T> T getTargetBean(ApplicationContext ac, Class<T> type) {
        return ac.getBean(type);
    }


    public static boolean isPlaceholder(String value) {
        value = StringUtils.trimAllWhitespace(value);
        return value != null && value.startsWith("{") && value.endsWith("}");
    }

    public static String getCacheKey(String key) {
        return key.replaceAll("[{}]", "");
    }

    public static <T> T checkAndCastKeyValue(String key, Class<T> targetType, Map<String, Object> paramValues, boolean isRequired) {

        String keyTemp = key;

        if (CacheUtil.isPlaceholder(keyTemp)) {
            keyTemp = CacheUtil.getCacheKey(keyTemp);
        }

        Object keyObj = paramValues.get(keyTemp);

        if (keyObj == null) {
            if (isRequired) {
                throw new IllegalStateException("无@CacheParam(" + key + ")对应的参数");
            } else {
                return  null;
            }
        }

        if (!keyObj.getClass().isAssignableFrom(targetType)) {
            throw new IllegalStateException("方法中@CacheParam(" + key + ")对应的参数值必须为"+targetType+"类型");
        }

        return targetType.cast(keyObj);
    }
}
