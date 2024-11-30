package cn.argentoaskia.callback.extendpoints.globallifecycle;

import cn.argentoaskia.bean.User3;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("global")
public class User3GlobalComponentScanLifeCycleCallback extends User3
implements DisposableBean, InitializingBean {
    public void defaultInit(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用default-init-method属性进行init...");
    }

    public void defaultDestroy(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用default-destroy-method属性进行destroy...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用DisposableBean接口进行destroy...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用InitializingBean接口进行init...");
    }

    @PostConstruct
    public void init2(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用@PostConstruct接口进行init...");
    }

    @PreDestroy
    public void destroy2(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用@PreDestroy进行destroy...");
    }
}
