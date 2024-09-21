package cn.argento.askia;

import cn.argento.askia.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

public class SpringJavaConfigBean {


    @Bean
    public User user1(){
        return new User();
    }


    @Bean({"user_two", "userTwo"})
    public User user2(){
        return new User();
    }

    @Bean(name = {"user_three", "userThree"})
    public User user3(){
        return new User();
    }
}
