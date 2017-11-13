package xyz.zhangxing.spring.extend;

import org.springframework.stereotype.Component;

@Component("test")
public class TestAspact {

    @CacheManage(key = "{name}",clearCacheFlag = "{clearFlag}")
    public String  test(String value,@CacheParam("name") String name,@CacheParam("clearFlag") boolean clearFlag) {
        return value;
    }
}
