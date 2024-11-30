package cn.argento.askia;

import cn.argento.askia.beans.SpringBeanAwareContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBeanAwareTest {

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Before
    public void init(){
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringBeanAwareContext.class);
    }

    @After
    public void destroy(){
        annotationConfigApplicationContext.close();
    }


    @Test
    public void testBeanAwares(){
    }
}
