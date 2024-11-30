package cn.argentoaskia.callback.extendpoints.defaultdestroy;

import cn.argentoaskia.bean.User3;

public class User3ShutdownMethodLifeCycleCallback extends User3 {

    public void shutdown(){
        System.out.println("4.1.通过指定destroy-method属性为：(inferred)，可以实现自动关闭，而不需要写具体方法名。");
        System.out.println("4.2.这种方式会让spring默认情况下调用close方法或者shutdown方法");
        System.out.println();
    }
}
