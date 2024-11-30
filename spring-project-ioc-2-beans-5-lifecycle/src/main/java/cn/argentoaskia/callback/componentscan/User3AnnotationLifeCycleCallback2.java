package cn.argentoaskia.callback.componentscan;


import cn.argentoaskia.bean.User3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Date;

@Component("user3_3_1")
public class User3AnnotationLifeCycleCallback2 extends User3 {


    @Autowired
    public User3AnnotationLifeCycleCallback2(@Value("20") Integer id,
                 @Value("Susan") String name,
                 @Value("北京市") String address,
                 @Value("99") int age,
                 @Autowired Date birthday,
                 @Autowired LocalDateTime upload) {
        super(id, name, address, age, birthday, upload);
    }

    @PostConstruct
    public void init(){
        System.out.println("3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，" +
                "在这种方式上方法签名必须是：public void [随便的方法名]();");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }

}
