package cn.argento.askia.bean;

import cn.argentoaskia.callback.extendpoints.multicallback.User3MultipleLifecycleMechanisms;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExtendsPointsMultiCallbackLifecycleTest {

    private ClassPathXmlApplicationContext applicationContext;


    @After
    public void destroy(){
        System.out.println("准备关闭容器");
        applicationContext.close();
        System.out.println("容器已关闭");
    }
    @Test
    public void test(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-multicallbacks.xml");
        final User3MultipleLifecycleMechanisms bean = applicationContext.getBean(User3MultipleLifecycleMechanisms.class);
        System.out.println(bean);
        System.out.println("准备关闭容器");
        applicationContext.close();
        System.out.println("容器已关闭");
    }
}
