package cn.argento.askia.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.Random;

public class AnnotationSpringContextTest {
    private AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private Random random;

    @Before
    public void before(){
        random = new Random();
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringJavaConfigContext.class);
    }

    @Test
    public void testSingletonBean() throws InterruptedException {
        Date date2 = annotationConfigApplicationContext.getBean("date2", Date.class);
        Thread.sleep(1000 + random.nextInt(2000));
        Date date3 = annotationConfigApplicationContext.getBean("date2", Date.class);
        System.out.println("date2 == date3: " + (date2 == date3));
        System.out.println("date2 =" + date2);
        System.out.println("date3 =" + date3);
    }

    @Test
    public void testPrototypeBean() throws InterruptedException {
        Date date2 = annotationConfigApplicationContext.getBean("date", Date.class);
        Thread.sleep(1000 + random.nextInt(2000));
        Date date3 = annotationConfigApplicationContext.getBean("date", Date.class);
        System.out.println("date2 == date3: " + (date2 == date3));
        System.out.println("date2 =" + date2);
        System.out.println("date3 =" + date3);
    }

}
