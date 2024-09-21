package cn.argento.askia.contexts;

import cn.argento.askia.SpringJavaConfigBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationContextTest {


    private ApplicationContext context;

    @Before
    public void init(){
        context = new AnnotationConfigApplicationContext(SpringJavaConfigBean.class);
    }


    @Test
    public void testNameAttributes(){
        // getBeanDefinitionNames()只能获取id
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.print("容器内所有可用的Id(注意getBeanDefinitionNames()无法获取name属性)：[");
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            if (i > 0){
                System.out.print(", ");
            }
            System.out.print(beanDefinitionNames[i] + "(id)");
        }
        System.out.println("]");
    }

}
