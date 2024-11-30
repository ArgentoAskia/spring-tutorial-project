package cn.argento.askia.bean;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class ExtendsPointsGlobalLifecycleTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Test
    public void print(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-global-lifecycle.xml");
        System.out.println();
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println();
        System.out.println("## beans：");
        for (String name : beanDefinitionNames) {
            System.out.println(name + " = " + applicationContext.getBean(name));
        }
        System.out.println();
        applicationContext.close();
    }
}
