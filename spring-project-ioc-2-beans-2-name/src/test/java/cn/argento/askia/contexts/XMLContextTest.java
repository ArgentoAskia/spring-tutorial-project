package cn.argento.askia.contexts;

import cn.argento.askia.bean.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.AliasFor;

import java.util.Arrays;

public class XMLContextTest {


    private ClassPathXmlApplicationContext context;

    @Before
    public void init(){
        context = new ClassPathXmlApplicationContext("spring-context.xml");
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
        final Object user1 = context.getBean("user1");
        final Object user_one = context.getBean("user_one");
        final Object userThree = context.getBean("UserThree");
        System.out.println("通过id = user1获取的bean = " + user1);
        System.out.println("通过name = user_one获取的bean = " + user_one);
        System.out.println("通过name = userThree获取的bean = " + userThree);
    }


    @Test
    public void testAnonymityBeanId(){
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.print("容器内所有可用的Id(注意getBeanDefinitionNames()无法获取name属性)：[");
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            if (i > 0){
                System.out.print(", ");
            }
            System.out.print(beanDefinitionNames[i] + "(id)");
        }
        System.out.println("]");

        final Object bean = context.getBean("cn.argento.askia.bean.User#0");
        System.out.println("通过[cn.argento.askia.bean.User#0]这个id来获取User的匿名Bean：" + bean);
        final Object bean0 = context.getBean("cn.argento.askia.bean.Good#0");
        final Object bean1 = context.getBean("cn.argento.askia.bean.Good#1");
        final Object bean2 = context.getBean("cn.argento.askia.bean.Good#2");
        System.out.println("通过[cn.argento.askia.bean.Good#0]这个id来获取Good的匿名Bean：" + bean0);
        System.out.println("通过[cn.argento.askia.bean.Good#1]这个id来获取Good的匿名Bean：" + bean1);
        System.out.println("通过[cn.argento.askia.bean.Good#2]这个id来获取Good的匿名Bean：" + bean2);

        // 如果不指定#X,则获取的会是？(随机在#0 #1 #2中获取，未知的)
        final Object bean3 = context.getBean("cn.argento.askia.bean.Good");
        System.out.println("不添加#X后缀，获取到的bean是：" + bean3);
        System.out.println("cn.argento.askia.bean.Good#0 =" + (bean3 == bean0));
        System.out.println("cn.argento.askia.bean.Good#1 =" + (bean3 == bean1));
        System.out.println("cn.argento.askia.bean.Good#2 =" + (bean3 == bean2));
    }


    @Test
    public void testAlias(){
        final Object good = context.getBean("good");
        final Object good0 = context.getBean("cn.argento.askia.bean.Good#0");
        final Object user = context.getBean("user");
        final Object user1 = context.getBean("user1");
        System.out.println(good);
        System.out.println(good0);
        System.out.println(user);
        System.out.println(user1);
    }
}
