package cn.argentoaskia.context;


import cn.argentoaskia.callback.User3AnnotationLifeCycleCallback;
import cn.argentoaskia.callback.User3LifeCycleInterfaceCallback;
import cn.argentoaskia.callback.User3XMLLifeCycleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

// 基于JavaConfig形式注册
//@Configuration
public class User3JavaConfigContext {

    @Bean
    public Date date(){
        return new Date();
    }

    @Bean
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }

    @Bean
    public User3LifeCycleInterfaceCallback user3_1(@Value("广州市") String address,
                                                   @Value("30") int age,
                                                   @Autowired Date birthday,
                                                   @Value("1") int id,
                                                   @Value("Askia") String name,
                                                   @Autowired LocalDateTime upload){
        final User3LifeCycleInterfaceCallback user3LifeCycleInterfaceCallback = new User3LifeCycleInterfaceCallback();
        user3LifeCycleInterfaceCallback.setAddress(address)
                .setAge(age)
                .setBirthday(birthday)
                .setId(id)
                .setName(name)
                .setUpload(upload);
        return user3LifeCycleInterfaceCallback;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public User3XMLLifeCycleCallback user3_2(@Value("江门市") String address,
                                             @Value("23") int age,
                                             @Autowired Date birthday,
                                             @Value("2") int id,
                                             @Value("Askia") String name,
                                             @Autowired LocalDateTime upload){
        final User3XMLLifeCycleCallback user3XMLLifeCycleCallback = new User3XMLLifeCycleCallback();
        user3XMLLifeCycleCallback.setAddress(address)
                .setAge(age)
                .setBirthday(birthday)
                .setId(id)
                .setName(name)
                .setUpload(upload);
        return user3XMLLifeCycleCallback;
    }
    @Bean
    public User3AnnotationLifeCycleCallback user3_3(){
        return new User3AnnotationLifeCycleCallback();
    }
}
