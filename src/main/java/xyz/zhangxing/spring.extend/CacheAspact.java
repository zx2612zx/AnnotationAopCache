package xyz.zhangxing.spring.extend;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 使用注解的方式获取缓存
 * 可以动态指定某个缓存的key
 * 可以动态指定是否要清空某个缓存
 * <p>
 * 动态指定的实现方式类似于SpringMVC中的RequestMapping和PathVariable
 */
@Component
@Aspect
public class CacheAspact {

    @Resource
    private ApplicationContext ac;

    @Around("@annotation(xyz.zhangxing.spring.extend.CacheManage)")
    public Object aspact(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature s = joinPoint.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method method = ms.getMethod();
        Map<String, Object> paramValues = CacheUtil.getAllParamters(method, CacheParam.class, joinPoint.getArgs());

        CacheManage cacheManage = method.getAnnotation(CacheManage.class);

        CacheOperate cacheOperate = CacheUtil.getTargetBean(ac, CacheOperate.class);


        String key = CacheUtil.checkAndCastKeyValue(cacheManage.key(), String.class, paramValues, true);
        String configFile = cacheManage.configFile();

        Boolean clearCacheFlag = CacheUtil.checkAndCastKeyValue(cacheManage.clearCacheFlag(), Boolean.class, paramValues, false);
        if (clearCacheFlag == null) {
            clearCacheFlag = cacheManage.isClearCache();
        }

        Object result = null;

        //如果需要清除缓存,或者缓存不存在
        if (clearCacheFlag||!cacheOperate.existCache(key,configFile)) {
            result= joinPoint.proceed();
            cacheOperate.update(key,result,configFile);
        }else{
            result= cacheOperate.get(key,configFile);
        }
        return result;
    }
}
