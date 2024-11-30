package cn.argentoaskia.callback.extendpoints.defaultdestroy;

import cn.argentoaskia.bean.User3;
import org.springframework.stereotype.Component;

import java.io.Closeable;

public class User3AutoCloseXMLLifeCycleCallback extends User3 implements Closeable {

    @Override
    public void close() {
        System.out.println("spring默认情况下会判断Bean是否实现了AutoCloseable接口或者Closeable接口");
        System.out.println("因此只要实现了这两个接口，则会将close方法作为destroy-method的值...");
    }
}
