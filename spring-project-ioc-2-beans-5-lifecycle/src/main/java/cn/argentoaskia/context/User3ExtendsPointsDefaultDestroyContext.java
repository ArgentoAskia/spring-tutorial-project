package cn.argentoaskia.context;

import cn.argentoaskia.callback.extendpoints.defaultdestroy.User3AutoCloseXMLLifeCycleCallback;
import cn.argentoaskia.callback.extendpoints.defaultdestroy.User3ShutdownMethodLifeCycleCallback;
import org.springframework.context.annotation.Bean;

public class User3ExtendsPointsDefaultDestroyContext {

    @Bean
    public User3AutoCloseXMLLifeCycleCallback autoCloseXMLLifeCycleCallback(){
        return new User3AutoCloseXMLLifeCycleCallback();
    }

    @Bean
    public User3ShutdownMethodLifeCycleCallback user3ShutdownMethodLifeCycleCallback(){
        return new User3ShutdownMethodLifeCycleCallback();
    }
}
