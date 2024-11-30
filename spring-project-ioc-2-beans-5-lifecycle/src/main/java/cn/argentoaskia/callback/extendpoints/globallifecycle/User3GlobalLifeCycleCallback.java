package cn.argentoaskia.callback.extendpoints.globallifecycle;

import cn.argentoaskia.bean.User3;

public class User3GlobalLifeCycleCallback extends User3 {
    public void defaultInit(){
        System.out.println("User3GlobalLifeCycleCallback: 使用default-init-method属性进行init...");
        System.out.println();
    }

    public void defaultDestroy(){
        System.out.println("User3GlobalLifeCycleCallback: 使用default-destroy-method属性进行destroy...");
        System.out.println();
    }


    public void init(){
        System.out.println("User3GlobalLifeCycleCallback-override: 指定init-method属性为init方法，覆盖defaultInit进行初始化...");
        System.out.println();
    }

    public void destroy(){
        System.out.println("User3GlobalLifeCycleCallback-override: 指定destroy-method属性为destroy方法，覆盖defaultDestroy进行初始化...");
        System.out.println();
    }
}
