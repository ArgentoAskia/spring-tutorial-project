package cn.argento.askia.context;

import cn.argento.askia.SpringJavaConfigContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class AnnotationContextTest {

    @Test
    public void test(){
        ApplicationContext annotationContext =
                new AnnotationConfigApplicationContext(SpringJavaConfigContext.class);
        final String[] beanDefinitionNames = annotationContext.getBeanDefinitionNames();
        System.out.println("容器内所有对象名：" + Arrays.toString(beanDefinitionNames));
        System.out.println("容器内对象数量：" + annotationContext.getBeanDefinitionCount());
        System.out.println();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            System.out.println(annotationContext.getBean(beanDefinitionNames[i]));
            System.out.println();
        }
    }
}
