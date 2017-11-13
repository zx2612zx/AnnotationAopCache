package xyz.zhangxing.spring.extend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:spring.conf.xml");
       TestAspact ta= ac.getBean("test",TestAspact.class);
        System.out.println(ta.test("第一次的值","key",false));
        System.out.println(ta.test("第二次的值","key",false));
        System.out.println(ta.test("第三次的值","key",true));
        System.out.println(ta.test("第四次的值","key",false));
    }
}
