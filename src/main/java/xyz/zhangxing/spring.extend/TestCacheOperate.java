package xyz.zhangxing.spring.extend;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TestCacheOperate implements CacheOperate {

    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public Object get(String key, String configFile) {
        return map.get(key);
    }

    public Object update(String key, Object value, String configFile) {
        if (value == null) {
            return null;
        }
        return map.put(key, value);
    }

    public void del(String key, String configFile) {
        map.remove(key);
    }

    public boolean existCache(String key, String configFile) {
        return map.containsKey(key) ? get(key, configFile) != null : false;
    }
}
