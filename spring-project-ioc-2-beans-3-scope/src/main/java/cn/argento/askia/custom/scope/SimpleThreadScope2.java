package cn.argento.askia.custom.scope;

import org.springframework.beans.factory.config.Scope;
import org.springframework.context.support.SimpleThreadScope;

public class SimpleThreadScope2 extends SimpleThreadScope implements Scope {

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        System.out.println("registerDestructionCallback ==> name:" + name);
        System.out.println("registerDestructionCallback ==> run call back：");
        callback.run();
        super.registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        System.out.println("resolveContextualObject ==> key:" + key);
        return super.resolveContextualObject(key);
    }

    @Override
    public String getConversationId() {
        System.out.println(super.getConversationId());
        return super.getConversationId();
    }
}
