package cn.argento.askia.factory;

import cn.argento.askia.bean.User;

import java.time.LocalDateTime;
import java.util.Date;

public class UserStaticFactory {

    public static User createDefaultUser(){
        User user = new User();
        user.setAddress("china")
                .setAge(25)
                .setBirthday(new Date())
                .setId(3)
                .setName("张三")
                .setUpload(LocalDateTime.now());
        return user;
    }
}
