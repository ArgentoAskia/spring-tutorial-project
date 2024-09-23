## Bean Scope

本小节主要介绍`spring IoC`容器中`Bean`的两种作用范围（`Scope`）：单例（`singleton`）和原型（俗称多例，`prototype`）。

你可能听过所谓的单例（`singleton`），在设计模式中，单例模式非常出名，我们此处说的单例的概念实际上也来源自此，通常来说，单例即一个类有且只有对应的一个实例（对象）。

`spring IoC`容器内的所有`Bean`对象都是单例的，每一个类在`IoC`中只有唯一的实例对象和类对应。使用单例模式可以避免频繁地`new`对象，虽然`JVM`会进行垃圾回收，但具体什么时候进行回收是不确定的，因此频繁地`new`对象可能会让`JVM`长时间占用大量内存。

但单例也并不是万金油的，如果你的`Bean`对象要表示一种状态，或者可能会随时改变，如表示时间的类`Date`、`LocalDate`的对象，这些对象需要根据当前时间来进行创建时，就不能用单例。再或者是一些可能存在枚举的量，如表示抽象工厂模式中使用到的例子：一个杯子，他有红色和蓝色款式、圆口和方口款式，这个时候根据需求，他会有`4`种组合方式，在单例中，您必须为此创建`4`个对应的类。因此，在面对这种涉及排列组合或者需要频繁更变状态的类的对象的时候，单例并不可取！

好在`spring`提供了原型，这种模式和直接`new`一样，在需要对象的时候会`new`一个新的，但是需要注意，原型模式下的Bean对象并不会存储在`IoC`容器中，也就是说`IoC`只会存储原型模式`Bean`的类信息而不存储创建出来的`Bean`对象，因此原则上除非确实需要表达一种状态，否则应尽量减少使用原型模式。

本小节首先介绍如何配置`singleton Bean`和` prototype Bean`，基于`XML`配置和`Java-Config`配置，然后我们再了解一下除了`singleton`和`prototype`之外的其他领域的`Scope`，如`web`领域中，`spring`额外定义了`request`、`session`、`application`、`websocket`这4种作用域，最后我们来说下如何基于`spring`提供的接口，实现自定义`scope`。

文章部分例子和说明描述，参考自官方文档[Bean Scopes](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html)一节，其中：

- `XML`配置和`Java-config`配置小节参考了文档：[singleton bean](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html#beans-factory-scopes-singleton)、[prototype bean](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html#beans-factory-scopes-prototype)
- 

## XML配置

在`bean`标签中，有一个`scope`属性，该属性有两个值：`singleton（默认值）`和`prototype`，代表单例和原型，我们在`bean`标签中指定这个属性即可：

```xml
<bean id="now" class="java.time.LocalDateTime" factory-method="now" scope="prototype"/>
<bean id="now2" class="java.time.LocalDateTime" factory-method="now" scope="singleton"/>
```

`singleton`的`bean`和`prototype`的`bean`的处理是不一样的，`singleton`的`bean`在我们初始化`IoC`容器（即我们具体`new`出`IoC`容器类的对象的时候）的时候就已经创建完成并被存入容器。`prototype`的`bean`在容器初始化的时候并不会被创建，初始化`IoC`容器的时候只会存储原型`Bean`的信息，当我们调用`getBean()`方法的时候，才会创建出原型`Bean`的实例。

因此单例`Bean`无论怎么获取都会是同一个`Bean`对象，但是原型`Bean`每次调用`getBean()`拿到的都是新的`Bean`对象，我们通过下面的两组测试代码来说明：

```java
// 单例Bean对象的测试代码：
// 第一次获取单例Bean对象
LocalDateTime now2 = classPathXmlApplicationContext.getBean("now2", LocalDateTime.class);
// 过了一段时间之后
Thread.sleep(1000 + random.nextInt(2000));
// 再次获取
LocalDateTime now3 = classPathXmlApplicationContext.getBean("now2", LocalDateTime.class);
System.out.println("now2 == now3: " + (now2 == now3));
System.out.println("now2 =" + now2);
System.out.println("now3 =" + now3);

// 结果：
// now2 == now3: true
// now2 =2024-08-04T11:33:09.200
// now3 =2024-08-04T11:33:09.200
// 这说明了获取到的是同一个对象
```

```java
// 原型Bean对象的测试代码
// 第一次获取原型Bean对象
LocalDateTime now2 = classPathXmlApplicationContext.getBean("now", LocalDateTime.class);
// 过了一段时间之后
Thread.sleep(1000 + random.nextInt(2000));
// 再次获取
LocalDateTime now3 = classPathXmlApplicationContext.getBean("now", LocalDateTime.class);
System.out.println("now2 == now3: " + (now2 == now3));
System.out.println("now2 =" + now2);
System.out.println("now3 =" + now3);


// 结果：
// now2 == now3: false
// now2 =2024-08-04T11:42:34.066
// now3 =2024-08-04T11:42:36.246
// 这说明了获取到的是不同的对象，间接说明了只有调用getBean()才会创建对象，并且对象是新对象，原型对象不会被IoC容器缓存
```

> 由于原型`Bean`的特点，在进行依赖注入的时候，会存在一些问题，典型的就是一个单例的`Bean`需要依赖一个原型的`Bean`。由于单例的`Bean`在初始化的时候只会初始化一次，所以在容器初始化的时候会事先创建一个依赖的原型`Bean`，需要注意的是这个原型的`Bean`是匿名的，并且不符合原型`Bean`的特征，换句话说当我们需要使用单例`Bean`的时候，其依赖的原型`Bean`不会随之改变而是在初始化的时候就已经固定，这实际上不符合原型`Bean`的特点，我们将会在依赖注入章节中对单例`Bean`依赖原型`Bean`情况作具体介绍。
>
> 此处说明参考文档：[beans-factory-scopes-sing-prot-interaction](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html#beans-factory-scopes-sing-prot-interaction)

## Java-config配置

在`Java-config`配置方式中，可以使用注解`@Scope`来配合`@Bean`指定`Bean`的`Scope`属性，`@Scope`源代码如下：

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	@AliasFor("scopeName")
	String value() default "";

	@AliasFor("value")
	String scopeName() default "";
	
	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;

}
```

其中重要的属性是`scopeName`（或者`value`，`@AliasFor`注解能将两个属性进行合并统一处理，因此设置`value`属性等于设置了`scopeName`属性）

`scopeName`是一个字符串，你可以传递`singleton`和`prototype`字符串来指定这个`Bean`是单例还是原型`Bean`：

```java
@Bean("prototype")
@Bean("singleton")
```

`ConfigurableBeanFactory`接口提供了两个字符串常量，以方便开发者指定`@Scope`：

```java
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";

	String SCOPE_PROTOTYPE = "prototype";
	// 省略其他接口方法
}
// 可以使用常量来替代手写字符串
@Bean(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Bean(ConfigurableBeanFactory.SCOPE_SINGLETON)
```

具体的`Java-config`指定方式参考下面的代码：

```java
public class SpringJavaConfigContext {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Date date(){
        return new Date();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Date date2(){
        return new Date();
    }

}
```

测试方法与结果和`XML`方式基本无差。

```java
public class AnnotationSpringContextTest {
    private AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private Random random;

    @Before
    public void before(){
        random = new Random();
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringJavaConfigContext.class);
    }

    @Test
    public void testSingletonBean() throws InterruptedException {
        Date date2 = annotationConfigApplicationContext.getBean("date2", Date.class);
        Thread.sleep(1000 + random.nextInt(2000));
        Date date3 = annotationConfigApplicationContext.getBean("date2", Date.class);
        System.out.println("date2 == date3: " + (date2 == date3));
        System.out.println("date2 =" + date2);
        System.out.println("date3 =" + date3);
    }
// result:    
// date2 == date3: true
// date2 =Mon Aug 19 16:56:12 CST 2024
// date3 =Mon Aug 19 16:56:12 CST 2024

    @Test
    public void testPrototypeBean() throws InterruptedException {
        Date date2 = annotationConfigApplicationContext.getBean("date", Date.class);
        Thread.sleep(1000 + random.nextInt(2000));
        Date date3 = annotationConfigApplicationContext.getBean("date", Date.class);
        System.out.println("date2 == date3: " + (date2 == date3));
        System.out.println("date2 =" + date2);
        System.out.println("date3 =" + date3);
    }
// result:
// date2 == date3: false
// date2 =Mon Aug 19 17:09:21 CST 2024
// date3 =Mon Aug 19 17:09:23 CST 2024

}
```

## 其他类型Scope

// web开发

## 自定义Scope

// demo待优化，改进说明

// 何时需要自定义Scope，某一个状态需要自己的数据，比如说不同的线程携带不同的数据，request域携带自己的数据，Session域携带自己的数据，不同的GUI组件携带自己的数据等



