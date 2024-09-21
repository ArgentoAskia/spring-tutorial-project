package cn.argento.askia;

import cn.argento.askia.bean.Outer;
import cn.argento.askia.bean.User;
import cn.argento.askia.factory.UserObjectFactory;
import cn.argento.askia.factory.UserStaticFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;


public class SpringJavaConfigContext {

    // 基于构造器注入
    @Bean
    public User createUserByConstructor(){
        User user = new User();
        user.setAddress("China");
        user.setAge(40);
        user.setBirthday(new Date());
        user.setId(1);
        user.setName("Askia");
        user.setUpload(LocalDateTime.now());
        return user;
    }

    // 基于静态工厂
    @Bean
    public User createUserByStaticMethod(){
        return UserStaticFactory.createDefaultUser();
    }


    // 基于对象工厂
    @Bean
    public User createUserByObjectMethod(){
        UserObjectFactory factory = new UserObjectFactory();
        return factory.createUser();
    }


    // 以下展示内部类的创建
    @Bean
    public Outer.StaticInner createStaticInnerClassObject(){
        return new Outer.StaticInner();
    }

    @Bean
    public Outer.Inner createInnerClassObject(){
        return new Outer().new Inner();
    }
}
