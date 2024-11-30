package cn.argento.askia.beans.awares;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ContextBeanClassLoaderAwareBean implements BeanClassLoaderAware {

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println(LocalDateTime.now() + " #BeanClassLoaderAware ===>>>   加载Bean所使用的ClassLoader：" + classLoader);
    }
}
