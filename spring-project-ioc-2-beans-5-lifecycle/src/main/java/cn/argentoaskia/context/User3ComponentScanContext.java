package cn.argentoaskia.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.Date;

@ComponentScan(basePackages = "cn.argentoaskia.callback.componentscan")
public class User3ComponentScanContext {

    @Bean
    public Date date(){
        return new Date();
    }

    @Bean
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }

}
