package cn.argento.askia.context;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Date;

public class SpringJavaConfigContext {


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Date date(){
        return new Date();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Date date2(){
        return new Date();
    }

}
