package cn.argento.askia.context;

import cn.argento.askia.custom.SpringCustomScopeJavaConfigContext;
import cn.argento.askia.custom.scope.SimpleThreadScope2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CustomScopeSpringContextTest {

    private ClassPathXmlApplicationContext classPathXmlApplicationContext;
    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Before
    public void before(){
        classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:/spring-custom-scope.xml");
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringCustomScopeJavaConfigContext.class);
        // 除了在SpringCustomScopeJavaConfigContext中进行声明式创建，还可以手动调用ApplicationContext的API来实现注册，不过这种注册方式比较少用！
        // 代码如下：
        // annotationConfigApplicationContext.getBeanFactory().registerScope("thread", new SimpleThreadScope2());
    }

    @After
    public void after(){
        classPathXmlApplicationContext.close();
        annotationConfigApplicationContext.close();
    }

    /**
     * xml容器形式实现自定义Scope
     * @throws InterruptedException -
     */
    @Test
    public void testDynamicScopeBean() throws InterruptedException {
        final Object[] now30 = new Object[1];
        final Object[] now31 = new Object[1];
        final Object[] now32 = new Object[1];
        final Object[] now33 = new Object[1];
        // now3对象对不同的thread会有不同的值，在同一个thread中，其值是相同的
        new Thread("Thread1"){
            @Override
            public void run() {
                // now3对象对不同的thread会有不同的值，在同一个thread中，其值是相同的
                now30[0] = classPathXmlApplicationContext.getBean("now3");
                System.out.println(now30[0]);
                now31[0] = classPathXmlApplicationContext.getBean("now3");
                System.out.println(now31[0]);
                System.out.println(now30[0] == now31[0]);
            }
        }.start();
        // 延迟两秒，再次在新的线程中获取now3，结果Thread2中的时间和Thread1中的时间不一样！
        // 证明我们的scope是面向不同Thread的!
        Thread.sleep(2000);
        new Thread("Thread2"){
            @Override
            public void run() {
                now32[0] = classPathXmlApplicationContext.getBean("now3");
                System.out.println(now32[0]);
                now33[0] = classPathXmlApplicationContext.getBean("now3");
                System.out.println(now33[0]);
                System.out.println(now33[0] == now32[0]);
            }
        }.start();
        Thread.sleep(5000);
        System.out.println(now33[0] == now31[0]);
    }

    /**
     * Java-config类容器形式实现自定义Scope
     * @throws InterruptedException -
     */
    @Test
    public void testDynamicScopeBean2() throws InterruptedException {
        new Thread("thread3"){
            @Override
            public void run() {
                final Object now3 = annotationConfigApplicationContext.getBean("now3");
                System.out.println(now3);
                final Object now31 = annotationConfigApplicationContext.getBean("now3");
                System.out.println(now3);
                System.out.println(now3 == now31);
            }
        }.start();
        Thread.sleep(3000);
        new Thread("thread4"){
            @Override
            public void run() {
                final Object now3 = annotationConfigApplicationContext.getBean("now3");
                System.out.println(now3);
                final Object now31 = annotationConfigApplicationContext.getBean("now3");
                System.out.println(now3);
                System.out.println(now3 == now31);
            }
        }.start();
    }
}
