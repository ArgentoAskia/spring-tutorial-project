package cn.argento.askia.context;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class XmlContextTest {

    @Test
    public void test(){
        ApplicationContext xmlContext =
                new ClassPathXmlApplicationContext("spring-context.xml");
        // 获取容器内所有Bean的名字
        final String[] beanDefinitionNames = xmlContext.getBeanDefinitionNames();
        System.out.println("容器内所有对象名：" + Arrays.toString(beanDefinitionNames));
        //  获取Bean数量
        System.out.println("容器内对象数量：" + xmlContext.getBeanDefinitionCount());
        System.out.println();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            // 获取Bean对象并输出
            System.out.println(xmlContext.getBean(beanDefinitionNames[i]));
            System.out.println();
        }

    }
}
