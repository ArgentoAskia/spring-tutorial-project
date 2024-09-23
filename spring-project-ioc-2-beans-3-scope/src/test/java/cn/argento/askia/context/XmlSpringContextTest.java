package cn.argento.askia.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class XmlSpringContextTest {
    private ClassPathXmlApplicationContext classPathXmlApplicationContext;
    private Random random;

    @Before
    public void before(){
        random = new Random();
        classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
    }

    @Test
    public void testSingletonBean() throws InterruptedException {
        final String[] beanDefinitionNames = classPathXmlApplicationContext.getBeanDefinitionNames();
        System.out.println("Bean names:" + Arrays.toString(beanDefinitionNames));
        System.out.println("What is SingletonBean：");
        LocalDateTime now2 = classPathXmlApplicationContext.getBean("now2", LocalDateTime.class);
        Thread.sleep(1000 + random.nextInt(2000));
        LocalDateTime now3 = classPathXmlApplicationContext.getBean("now2", LocalDateTime.class);
        System.out.println("now2 == now3: " + (now2 == now3));
        System.out.println("now2 =" + now2);
        System.out.println("now3 =" + now3);
    }

    @Test
    public void testPrototypeBean() throws InterruptedException {
        System.out.println("What is PrototypeBean：");
        LocalDateTime now2 = classPathXmlApplicationContext.getBean("now", LocalDateTime.class);
        Thread.sleep(1000 + random.nextInt(2000));
        LocalDateTime now3 = classPathXmlApplicationContext.getBean("now", LocalDateTime.class);
        System.out.println("now2 == now3: " + (now2 == now3));
        System.out.println("now2 =" + now2);
        System.out.println("now3 =" + now3);
    }

}
