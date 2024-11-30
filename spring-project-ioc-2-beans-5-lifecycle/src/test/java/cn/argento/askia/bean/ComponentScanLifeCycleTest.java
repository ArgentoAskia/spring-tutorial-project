package cn.argento.askia.bean;

import cn.argentoaskia.context.User3ComponentScanContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class ComponentScanLifeCycleTest {

    private ClassPathXmlApplicationContext xmlApplicationContext;
    private AnnotationConfigApplicationContext annotationApplicationContext;


    public void initXml(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        xmlApplicationContext = new ClassPathXmlApplicationContext("/spring-context-component-scan.xml");
        System.out.println();
    }

    public void initAnnotation(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        annotationApplicationContext = new AnnotationConfigApplicationContext(User3ComponentScanContext.class);
        System.out.println();
    }


    public void destroyXml(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(xmlApplicationContext.getBeanDefinitionNames()));
        xmlApplicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    public void destroyAnnotation(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(annotationApplicationContext.getBeanDefinitionNames()));
        annotationApplicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    @Test
    public void testXmlComponentScanLifeCycle(){
        initXml();
        final Object user3_3_1 = xmlApplicationContext.getBean("user3_3_1");
        final Object user3_3_2 = xmlApplicationContext.getBean("user3_3_2");
        System.out.println("## 输出bean");
        System.out.println("user3_3_1: " + user3_3_1);
        System.out.println("user3_3_2: " + user3_3_2);
        System.out.println();
        destroyXml();
    }

    @Test
    public void testAnnotationComponentScanLifeCycle(){
        initAnnotation();
        final Object user3_3_1 = annotationApplicationContext.getBean("user3_3_1");
        final Object user3_3_2 = annotationApplicationContext.getBean("user3_3_2");
        System.out.println("## 输出bean");
        System.out.println("user3_3_1: " + user3_3_1);
        System.out.println("user3_3_2: " + user3_3_2);
        System.out.println();
        destroyAnnotation();
    }

}
