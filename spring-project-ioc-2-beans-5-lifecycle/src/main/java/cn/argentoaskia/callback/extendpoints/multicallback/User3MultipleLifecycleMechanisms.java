package cn.argentoaskia.callback.extendpoints.multicallback;

import cn.argentoaskia.bean.User3;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

// 当存在多个声明周期
public class User3MultipleLifecycleMechanisms extends User3 implements InitializingBean, DisposableBean {
    public void initialization(){
        System.out.println("3.init-method方式初始化在最后才被调用，init-method优先级最低");
    }

    public void shutdown(){
        System.out.println("3.destroy-method方式销毁在最后被调用，destroy-method优先级最低");
    }

    @PostConstruct
    public void init(){
        System.out.println("1.@PostConstruct方式初始化在最先被调用，优先级最高");
    }

    @PreDestroy
    public void destroy2(){
        System.out.println("1.@PreDestroy方式销毁在最开始被调用，优先级最高");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("2.接口方式初始化在中间被调用，优先级居中");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("2.接口方式销毁在中间被调用，优先级居中");
    }
}
