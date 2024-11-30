package cn.argentoaskia.callback;

import cn.argentoaskia.bean.User3;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;



public class User3LifeCycleInterfaceCallback extends User3 implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }
}
