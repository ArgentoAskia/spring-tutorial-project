package cn.argento.askia.beans.awares;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class ContextBeanFactoryAwareBean implements BeanFactoryAware {
    // BeanFactory用来为第三方框架做兼容用
    // 参考https://docs.spring.io/spring-framework/reference/core/beans/beanfactory.html
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  实际的BeanFactory类型：" + beanFactory.getClass());
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#toString：" + beanFactory.toString());
        // 判断Bean的类型
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isPrototype(\"now\")：" + beanFactory.isPrototype("now"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isSingleton(\"localhost\")：" + beanFactory.isSingleton("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isTypeMatch(\"localhost\", InetAddress.class)：" + beanFactory.isTypeMatch("localhost", InetAddress.class));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getAliases(\"localhost\")：" + Arrays.toString(beanFactory.getAliases("localhost")));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getType(\"localhost\")：" + beanFactory.getType("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getBean(\"localhost\")：" + beanFactory.getBean("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#containsBean(\"localhost\")：" + beanFactory.containsBean("localhost"));

    }
}
