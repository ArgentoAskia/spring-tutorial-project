package cn.argento.askia.beans;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Configuration
@ComponentScan("cn.argento.askia.beans")
public class SpringBeanAwareContext {

    @Bean
    @Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
    public LocalDateTime now(){
        return LocalDateTime.now();
    }

    @Bean({"localhost","127.0.0.1"})
    public InetAddress inetAddress() throws UnknownHostException {
        return Inet4Address.getLocalHost();
    }
}