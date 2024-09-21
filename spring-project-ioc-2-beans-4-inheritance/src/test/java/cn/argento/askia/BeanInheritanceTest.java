package cn.argento.askia;

import cn.argento.askia.beans.DerivedTestBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanInheritanceTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        applicationContext.start();
    }

    @After
    public void after(){
        applicationContext.stop();
        applicationContext.close();
    }


    @Test
    public void testAbstractBeans(){
        DerivedTestBean inheritsWithDifferentClass = applicationContext.getBean("inheritsWithDifferentClass", DerivedTestBean.class);
        DerivedTestBean inheritsWithDifferentClass2 = applicationContext.getBean("inheritsWithDifferentClass2", DerivedTestBean.class);
        System.out.println(inheritsWithDifferentClass);
        System.out.println(inheritsWithDifferentClass2);
        final Object inheritsWithAnotherClass = applicationContext.getBean("inheritsWithAnotherClass");
        final Object inheritedTestBean3 = applicationContext.getBean("inheritedTestBean3");
        System.out.println(inheritsWithAnotherClass);
        System.out.println(inheritedTestBean3);
    }
}
