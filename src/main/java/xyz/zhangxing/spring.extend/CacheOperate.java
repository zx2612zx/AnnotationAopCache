package xyz.zhangxing.spring.extend;

/**
 * 使用注解方式操作缓存需要自己实现该接口的方法
 * 因为缓存组件有很多种，操作的Api也不一样
 */
public interface CacheOperate {

    public Object  get(String key,String configFile);

    public  Object  update(String key,Object value,String configFile);

    public  void del(String key,String configFile);

    public boolean existCache(String key,String configFile);

}
