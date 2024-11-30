package cn.argento.askia.beans.awares;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component("beanNameAware")
public class ContextBeanNameAwareBean implements BeanNameAware {
    @Override
    public void setBeanName(String name) {
        System.out.println(LocalDateTime.now() + " #BeanNameAware ===>>>  注册ContextBeanNameAwareBean，bean’s name = " + name);
    }
}
