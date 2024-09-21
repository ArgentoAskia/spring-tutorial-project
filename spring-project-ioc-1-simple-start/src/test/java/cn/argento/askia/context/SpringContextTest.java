package cn.argento.askia.context;

import cn.argento.askia.beans.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextTest {


    @Test
    public void testGetBean(){
        // 1. 创建IoC容器
        // ClassPathXmlApplicationContext类有一个构造器，需要传入容器的xml配置文件来指导创建的对象并放入容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        // 2. 通过调用getBean(String beanId)方法来获取对象，返回的是Object类型
        final Object user = applicationContext.getBean("user");
        // 3. 通过调用getBean(String beanId, Class<T> beanType)方法来获取对象，返回的是具体的type类型。
        final User user1 = applicationContext.getBean("user", User.class);
        System.out.println(user);
        System.out.println(user1);
    }
}
