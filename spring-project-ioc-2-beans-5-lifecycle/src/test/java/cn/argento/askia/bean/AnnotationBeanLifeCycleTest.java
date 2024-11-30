package cn.argento.askia.bean;

import cn.argentoaskia.bean.User3;
import cn.argentoaskia.context.User3JavaConfigContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class AnnotationBeanLifeCycleTest {

    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        applicationContext = new AnnotationConfigApplicationContext(User3JavaConfigContext.class);
        System.out.println();
    }

    @After
    public void destroy(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        applicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    @Test
    public void testAllLifeCycle(){
        User3 user3_1 = applicationContext.getBean("user3_1", User3.class);
        User3 user3_2 = applicationContext.getBean("user3_2", User3.class);
        User3 user3_3 = applicationContext.getBean("user3_3", User3.class);
        System.out.println("## 输出bean");
        System.out.println("user3_1: " + user3_1);
        System.out.println("user3_2: " + user3_2);
        System.out.println("user3_3: " + user3_3);
        System.out.println();
    }
}
