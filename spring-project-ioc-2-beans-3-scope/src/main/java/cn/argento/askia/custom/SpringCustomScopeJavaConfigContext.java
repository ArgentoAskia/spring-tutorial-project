package cn.argento.askia.custom;

import cn.argento.askia.custom.scope.SimpleThreadScope2;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

public class SpringCustomScopeJavaConfigContext {

    @Bean
    @Scope("thread")
    public LocalDateTime now3(){
        return LocalDateTime.now();
    }

    // 一种注册方式是将对象推入到容器内，Spring在启动的时候会获取容器内所有BeanFactoryPostProcessor接口的对象
    // 然后调用所有BeanFactoryPostProcessor接口的对象的postProcessBeanFactory()实现注册
    // 具体注册的代码可以参考CustomScopeConfigurer类，该类实现了BeanFactoryPostProcessor接口并重写了postProcessBeanFactory()方法
    @Bean
    public CustomScopeConfigurer threadScope(){
        final CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        customScopeConfigurer.addScope("thread", new SimpleThreadScope2());
        return customScopeConfigurer;
    }
}
