## Bean Awares

> 本节内容参考：
>
> - https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-aware
>
> - https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#aware-list

什么是`Aware`？`spring doc`中对其有如下描述：

`Aware`是一个标记性超级接口，表明一个`Bean`有资格被`Spring`容器通过回调式方法通知特定的框架对象，它是`spring`生命周期回调的一种。`Aware`为`spring 3.1`引入的特性，其旨在帮助开发者感知`spring`的工作状态。

```java
public interface Aware {

}
```

`Aware`实际的方法签名由各个子接口决定，但通常应该只包含一个接受单个参数且返回类型为`void`的方法。其子`Aware`接口如下：<img src="README/Aware-17329798251912.png" alt="Aware" style="zoom:200%;" />

其中和`Bean`相关的只有三个，分别是：`BeanClassLoaderAware`、`BeanFactoryAware`、`BeanNameAware`。在`lifecycle`一节中，我们说明了`spring bean`的生命周期，而和`Bean`有关的`Aware`会在会在执行`Init-method`和之前执行，步骤如下：

1. 依赖注入：`Spring`容器注入配置的依赖项，如通过构造器注入、字段注入或设置方法注入。
2. `Aware`接口回调：如果`Bean`实现了`BeanNameAware`、`BeanFactoryAware`、`BeanClassLoaderAware`，则`Spring`容器将调用它们的`setBeanName`、`setBeanClassLoader`、`setBeanFactory`等方法。
3. `BeanPostProcessor`前置处理：调用`BeanPostProcessor#postProcessBeforeInitialization()`

4. `Initialization Callbacks`
   1. 如果`Bean`中存在`@PostConstruct`标记的方法，则调用该方法
   2. 如果`Bean`实现了`InitializingBean`接口，`Spring`容器将调用其`afterPropertiesSet`方法。
   3. 如果`Bean`定义了`init-method`，`Spring`容器将调用该方法。
5. `BeanPostProcessor`后置处理：调用`BeanPostProcessor#postProcessAfterInitialization()`

另外请注意，仅仅实现`Aware`接口本身并不提供任何默认功能。相反，必须显式进行处理，例如在 `org.springframework.beans.factory.config.BeanPostProcessor` 中。可以参考 `org.springframework.context.support.ApplicationContextAwareProcessor` 来了解如何处理特定的`*Aware`接口回调。

最后，由于注册`Aware`相关的`Bean`在`xml`配置、`JavaConfig`配置和组件扫描中的方式基本相同，所以我们选择一种注册方式来讲解即可。

### BeanNameAware

实现了`BeanNameAware`接口的`Bean`会获取到自身在`IoC`容器中的名字（或者`id`），如果`Bean`在执行的过程中需要处理`beanName`，则可以实现该接口，例如我们定义了一个`Bean`：

```java
@Component("beanNameAware")
public class ContextBeanNameAwareBean implements BeanNameAware {
    @Override
    public void setBeanName(String name) {
        System.out.println(LocalDateTime.now() + " #BeanNameAware ===>>>  注册ContextBeanNameAwareBean，bean’s name = " + name);
    }
}
```

则当我们启动容器的时候，会输出：

```bash
2024-11-30T23:29:33.940 #BeanNameAware ===>>>  注册ContextBeanNameAwareBean，bean’s name = beanNameAware
```

即`setBeanName(String name)`中的参数`name`就是`@Component`的`value`值或者`Bean`标签的`id`属性或者`@Bean`注解的`value`属性的第一个成员值。如上，我们更改`@Component`的值：

```java
@Component("newBeanNameAware")
public class ContextBeanNameAwareBean implements BeanNameAware {
    @Override
    public void setBeanName(String name) {
        System.out.println(LocalDateTime.now() + " #BeanNameAware ===>>>  注册ContextBeanNameAwareBean，bean’s name = " + name);
    }
}
```

则输出：

```bash
2024-11-30T23:29:33.940 #BeanNameAware ===>>>  注册ContextBeanNameAwareBean，bean’s name = newBeanNameAware
```

### BeanClassLoaderAware

`BeanClassLoaderAware`会返回容器初始化`Bean`时采用的`ClassLoader`，使用该`Aware`，则可以在加载类的时候使用`ClassLoader`的`API`，常用于加载外部资源。

```java
@Component
public class ContextBeanClassLoaderAwareBean implements BeanClassLoaderAware {

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println(LocalDateTime.now() + " #BeanClassLoaderAware ===>>>   加载Bean所使用的ClassLoader：" + classLoader);
    }
}
```

则我们启动容器的时候，输出：

```bash
2024-11-30T23:29:33.918 #BeanClassLoaderAware ===>>>   加载Bean所使用的ClassLoader：sun.misc.Launcher$AppClassLoader@18b4aac2
```

可以看到，实际上使用的是`AppClassLoader`来加载类。具体`ClassLoader`的使用可以参考`Java`的类加载相关内容。

### BeanFactoryAware

> 关于BeanFactory API：https://docs.spring.io/spring-framework/reference/core/beans/beanfactory.html

什么是`BeanFactory`？

我们知道，在`Spring`，`IoC`容器的抽象接口是`ApplicationContext`，一个`ApplicationContext`子类就代表一类`IoC`容器，比如常见的`ClassPathXmlApplicationContext`（使用`ClassPath`中的`Xml`文件作为配置的`IoC`容器）`AnnotationConfigApplicationContext`（使用`Java`类作为配置的`IoC`容器）。

我们会在后续介绍`Context`的`API`的时候介绍具体`Spring`提供了多少类`IoC`容器，这里只需要有一个`IoC`容器的抽象接口概念即可，而我们在介绍`bean lifecycle`一节中也提到过，一个`IoC`容器的初始化，会涉及到大量的步骤，比如解析`Xml`配置文件或者`JavaConfig`类，初始化所有的`Bean`对象和进行依赖注入，回调各类生命周期接口等等。

而`spring`的设计精妙之处在于，它将这些步骤单独抽象出来，每个步骤都有一个顶层接口进行负责，而其中负责初始化所有`Bean`对象和进行依赖注入的，就是`BeanFactory`。

`BeanFactory`提供了`Spring IoC`（控制反转）中最基础的功能，即`Bean`初始化和依赖注入，其主要用于与`Spring`的其他部分以及相关第三方框架的集成，`BeanFactory`还有一个更高层次的接口`DefaultListableBeanFactory`，以方便一些框架的定制需要。

`BeanFactory`及相关接口（例如`BeanFactoryAware`、`InitializingBean`、`DisposableBean`）是其他框架组件的重要集成点。这种设计方式使你不需要任何注解甚至反射，允许容器与其组件之间进行非常高效的交互。

有关`BeanFactory`的`API`我们同样在介绍`ApplicationContext`的时候进行说明，这里同样只说明如何使用`BeanFactoryAware`：

`JavaConfig`容器类代码如下：

```java
@Configuration
@ComponentScan("cn.argento.askia.beans")
public class SpringBeanAwareContext {

    @Bean
    @Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
    public LocalDateTime now(){
        return LocalDateTime.now();
    }

    @Bean({"localhost","127.0.0.1"})
    public InetAddress inetAddress() throws UnknownHostException {
        return Inet4Address.getLocalHost();
    }
}
```

`BeanFactoryAware`实现类如下：

```java
@Component
public class ContextBeanFactoryAwareBean implements BeanFactoryAware {
    // BeanFactory用来为第三方框架做兼容用
    // 参考https://docs.spring.io/spring-framework/reference/core/beans/beanfactory.html
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  实际的BeanFactory类型：" + beanFactory.getClass());
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#toString：" + beanFactory.toString());
        // 判断Bean的类型
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isPrototype(\"now\")：" + beanFactory.isPrototype("now"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isSingleton(\"localhost\")：" + beanFactory.isSingleton("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#isTypeMatch(\"localhost\", InetAddress.class)：" + beanFactory.isTypeMatch("localhost", InetAddress.class));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getAliases(\"localhost\")：" + Arrays.toString(beanFactory.getAliases("localhost")));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getType(\"localhost\")：" + beanFactory.getType("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#getBean(\"localhost\")：" + beanFactory.getBean("localhost"));
        System.out.println(LocalDateTime.now() + " #BeanFactoryAware ===>>>  BeanFactory#containsBean(\"localhost\")：" + beanFactory.containsBean("localhost"));

    }
}
```

启动容器后，输出：

```bash
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  实际的BeanFactory类型：class org.springframework.beans.factory.support.DefaultListableBeanFactory
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  BeanFactory#toString：org.springframework.beans.factory.support.DefaultListableBeanFactory@192d3247: defining beans [org.springframework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,org.springframework.context.event.internalEventListenerProcessor,org.springframework.context.event.internalEventListenerFactory,springBeanAwareContext,contextBeanClassLoaderAwareBean,contextBeanFactoryAwareBean,beanNameAware,now,localhost]; root of factory hierarchy
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  BeanFactory#isPrototype("now")：true
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  BeanFactory#isSingleton("localhost")：true
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  BeanFactory#isTypeMatch("localhost", InetAddress.class)：true
2024-11-30T23:29:33.919 #BeanFactoryAware ===>>>  BeanFactory#getAliases("localhost")：[127.0.0.1]
2024-11-30T23:29:33.920 #BeanFactoryAware ===>>>  BeanFactory#getType("localhost")：class java.net.InetAddress
2024-11-30T23:29:33.920 #BeanFactoryAware ===>>>  BeanFactory#getBean("localhost")：LAPTOP-047TRRV3/192.168.56.1
2024-11-30T23:29:33.939 #BeanFactoryAware ===>>>  BeanFactory#containsBean("localhost")：true
```

