package cn.argentoaskia.callback;

import cn.argentoaskia.bean.User3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Date;

public class User3AnnotationLifeCycleCallback extends User3 {



    public User3AnnotationLifeCycleCallback(){
        super();
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
