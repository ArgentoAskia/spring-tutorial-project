package cn.argentoaskia.callback;

import cn.argentoaskia.bean.User3;
import org.springframework.stereotype.Component;

public class User3XMLLifeCycleCallback extends User3 {

    public void init(){
        System.out.println("2.采用XML：init-method属性的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，" +
                "在这种方式上方法签名必须是：public void [随便的方法名]();");
    }

    public void destroy(){
        System.out.println("2.采用XML：destroy-method属性的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }
}
