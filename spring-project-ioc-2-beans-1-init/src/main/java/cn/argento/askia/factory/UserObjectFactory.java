package cn.argento.askia.factory;

import cn.argento.askia.bean.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

public class UserObjectFactory {

    public User createUser(){
        User user = new User();
        user.setAddress("对象工厂创建")
                .setAge(25)
                .setBirthday(new Date())
                .setId(3)
                .setName("张三-对象工厂创建")
                .setUpload(LocalDateTime.now());
        return user;
    }
}
