package cn.argento.askia.bean;

import cn.argentoaskia.context.User3ExtendsPointsDefaultDestroyContext;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class ExtendsPointsDefaultDestroyTest {
    private ClassPathXmlApplicationContext applicationContext;

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Test
    public void print(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-default-destroy.xml");
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

    @Test
    public void print2(){
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(User3ExtendsPointsDefaultDestroyContext.class);
        System.out.println();
        final String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println();
        System.out.println("## beans：");
        for (String name : beanDefinitionNames) {
            System.out.println(name + " = " + annotationConfigApplicationContext.getBean(name));
        }
        System.out.println();
        annotationConfigApplicationContext.close();
    }
}
