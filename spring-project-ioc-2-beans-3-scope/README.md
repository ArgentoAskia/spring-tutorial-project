## Bean Scope

本小节主要介绍`spring IoC`容器中`Bean`的两种作用范围（`Scope`）：单例（`singleton`）和原型（俗称多例，`prototype`）。

你可能听过所谓的单例（`singleton`），在设计模式中，单例模式非常出名，我们此处说的单例的概念实际上也来源自此，通常来说，单例即一个类有且只有对应的一个实例（对象）。

`spring IoC`容器内的所有`Bean`对象都是单例的，每一个类在`IoC`中只有唯一的实例对象和类对应。使用单例模式可以避免频繁地`new`对象，虽然`JVM`会进行垃圾回收，但具体什么时候进行回收是不确定的，因此频繁地`new`对象可能会让`JVM`长时间占用大量内存。

下图来自官网，说明了单例模式是如何工作的：![image-20241018185626030](README/image-20241018185626030.png)

但单例也并不是万金油的，如果你的`Bean`对象要表示一种状态，或者可能会随时改变，如表示时间的类`Date`、`LocalDate`的对象，这些对象需要根据当前时间来进行创建时，就不能用单例。再或者是一些可能存在枚举的量，如表示抽象工厂模式中使用到的例子：一个杯子，他有红色和蓝色款式、圆口和方口款式，这个时候根据需求，他会有`4`种组合方式，在单例中，您必须为此创建`4`个对应的类。因此，在面对这种涉及排列组合或者需要频繁更变状态的类的对象的时候，单例并不可取！

好在`spring`提供了原型，这种模式和直接`new`一样，在需要对象的时候会`new`一个新的，但是需要注意，原型模式下的Bean对象并不会存储在`IoC`容器中，也就是说`IoC`只会存储原型模式`Bean`的类信息而不存储创建出来的`Bean`对象，因此原则上除非确实需要表达一种状态，否则应尽量减少使用原型模式。

下图来自官网，说明了原型模式是如何工作的：![image-20241018185701296](README/image-20241018185701296.png)

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

> 此处文档参考：[beans-factory-scopes-other](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html#beans-factory-scopes-other)

`Spring`框架提供了`6`种`Scopes`，除了上述两种基础`Scopes`之外，还有四种`Scopes`在`Web`版本的`IoC`容器中可用：

- `request`：将单例`bean`定义作用于单个`HTTP`请求周期中，也就是说，每个`HTTP`请求都有自己的`bean`实例，该实例是在单例`bean`定义的后面创建的
- `session`：将单例`bean`定义作用于一个`session`周期中
- `application`：将单例`bean`定义作用于整个`ServletContext`周期中
- `websocket`：将单例`bean`定义作用于整个`WebSocket`周期中

这四个`Scopes`我们会在介绍`spring-mvc`章节中进行介绍，感兴趣可以点击[这里]()

## 自定义Scope

> 此处文档参考：[beans-factory-scopes-custom](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html#beans-factory-scopes-custom)

`Spring`允许你自行定义新的`Scope`甚至重定义现有的`Scope`（虽然`Spring`官方不建议你重定义，并且内置的`singleton Scope`和`prototype Scope`无法被重定义）

想要自定义`Scope`，需要遵循下面的三个步骤：

1. 实现`org.springframework.beans.factory.config.Scope`接口并重写其中的核心方法
2. 将自定义的`Scope`注册到`IoC`容器中，以便`Spring`感知到该`Scope`的存在
3. 在`bean`的`Scope`标签或者属性中填写该`Scope`名称

在`Spring`的`context`包下，有一个类`org.springframework.context.support.SimpleThreadScope`，该类是`Spring`提供的自定义`Scope`的`Demo`，描述的是线程级别的`Scope`（即每个不同的线程拥有独立的单例`bean`实例），默认这个`Scope`并不会注册到`Spring`容器中，我们通过剖析该类来学习如何自定义`Scope`，在阅读该类的代码之前，我们来看看`Scope`接口的接口定义：

```java
public interface Scope {

    // 该接口方法主要负责对应Scope级别的bean的创建
    // 其中参数1是Bean的id
    // 参数2是一个ObjectFactory对象，是一个通用工厂对象，用于创建Bean对象，调用该对象的getObject()将返回该Bean的新实例（相当于原型模式）。
    // 接口的返回值就是代表要返回的Bean对象
	Object get(String name, ObjectFactory<?> objectFactory);

    // 移除相关id的Bean对象
	Object remove(String name);

    // 该注册了一个回调函数，该回调函数在范围中的指定对象被销毁时应该被调用（可选）:
    // 参数1即为bean的id
    // 参数2即为销毁的回调方法
	void registerDestructionCallback(String name, Runnable callback);
    
    // 该方法用于解析给定键的上下文对象(如果有的话)（可选）。例如，HttpServletRequest对象的关键字"request"。
	Object resolveContextualObject(String key);

    // 返回一个会话ID（Conversation Id），该ID旨在用于区分同一级别Scope内的不同的作用域，如在web的request级Scope中，不同的request应该具有不同的会话ID（Conversation Id），再如在线程级别的Scope中，不同的线程应该具有不同的会话ID（Conversation Id）
    // （可选）操作
	String getConversationId();

}
```

在实现该接口的时候，理论上只需要实现`getConversationId()`、`remove(String name)`、和`get(String name, ObjectFactory<?> objectFactory)`方法即可，剩余的方法中`registerDestructionCallback`提供了一种销毁对象的能力，当对`Bean`的生命周期结束时，会进行调用。

我们再来看看`SimpleThreadScope`是如何编写的，在阅读其代码之前，我们先要搞清楚线程级别的`Scope`到底是什么东西，或者说`Scope`接口到底提供的是一种什么能力！

`Scope`接口实际上提供的是一种"隔离"能力，即针对同一环境的不同作用域下获取同一个`ID`的`Bean`是否返回同一个对象的能力，比如，同在多线程环境下的不同线程，同在一条连接上的不同请求（`request`），同在一个服务端上的不同会话客户端（`Session`），同一个`tcp`服务器上的不同`Socket`连接等，这些就是所谓的同一环境不同作用域。这种"隔离"能力使得我们能够得到更加细粒度的`Bean`划分。如果读者能够完全理解`Scope`接口的这种能力，那也基本能够解决何时应该使用自定义`Scope`的问题。

从另外一个角度讲，`spring`内置的`singleton Scope`和`prototype scope`可以理解为`scope`接口能力的两种极端，`singleton Scope`是同一环境下不管作用域如何不同或者如何相同，我始终返回一个相同的对象（单例），而`prototype scope`是同一环境下不管作用域如何不同，只要客户端需要使用该对象，我始终创建一个新的实例返回给你客户端，而我们自定义的`Scope`，则刚好在这两种极端之间，即遵循同一环境下不同的作用域我们要返回不同的对象，相同的作用域我们要返回相同的对象。![image-20241018215035430](README/image-20241018215035430.png)

至于什么样的作用域被定义为相同，什么样的作用域被定义为不同，则有开发者自行定义，`Scope`的细粒度性质也体现在这一点上。另外自定义`Scope`的时候，开发者一般需要自行缓存或维护同一个作用域下的对象，自定义`Scope`的对象默认并不会被`IoC`容器所管理，当然也有解决方法，我们会在`DI`依赖注入一节进行介绍

我们在补充完了这一点理论之后，现在再次将目光放在`SimpleThreadScope`类上，我们的要求是，希望实现线程级别的隔离，这个线程级别的`scope`在不同的线程中获取同一`ID`的`bean`时返回不同的对象，而在同一线程下，返回相同对象，带着这样的要求，我们阅读`SimpleThreadScope`的代码，`SimpleThreadScope`代码中首先定义了一个`ThreadLocal`的字段，这里的`ThreadLocal`字段的作用是为不同的线程维护一个`Map`来记录数据，不同的线程都会有一个独立的`Map`结构，注意是独立的，感兴趣的可以自行深入了解`ThreadLocal`类。

```java
private final ThreadLocal<Map<String, Object>> threadScope =
			new NamedThreadLocal<Map<String, Object>>("SimpleThreadScope") {
				@Override
				protected Map<String, Object> initialValue() {
					return new HashMap<>();
				}
			};
```

然后核心在于`get`方法：

```java
@Override
public Object get(String name, ObjectFactory<?> objectFactory) {
   Map<String, Object> scope = this.threadScope.get();
   // NOTE: Do NOT modify the following to use Map::computeIfAbsent. For details,
   // see https://github.com/spring-projects/spring-framework/issues/25801.
   Object scopedObject = scope.get(name);
   if (scopedObject == null) {
      scopedObject = objectFactory.getObject();
      scope.put(name, scopedObject);
   }
   return scopedObject;
}
```

`get`方法首先从`ThreadLocal`中获取，由于`ThreadLocal`会判断当前线程是哪一个线程，所以会从当前运行的线程的`Map`中获取，如果事先不存在对象，则调用`objectFactory.getObject()`创建新对象并放入当前线程的`ThreadLocal`中进行缓存，由于不同线程具有独立的`Map`，所以就算`name`参数（即传递的`Bean`的`Id`）是相同的，只要是不同线程下，它们获取的对象就不同，这就基本能够实现需求了。

然后我们还要关注一个方法：

```java
@Override
public String getConversationId() {
   return Thread.currentThread().getName();
}
```

`getConversationId()`是一个可选操作，是否需要实现取决于底层的存储机制（即我们自定义的缓存机制，见上面的`ThreadLocal`字段），一般情况下，这个方法应该提供一种区分不同作用域的能力，所以我们一般会把能够区分不同作用域的代码写在这个方法，如上面的`Thread.currentThread().getName()`，因为不同的线程应该具有不同的线程名，因此我们直接返回每一个线程的线程名即可。

至此，实现`Scope`接口的核心，我们已说明完毕。

然后我们需要将`SimpleThreadScope`注册到`Spring`容器中以让其生效，注册的方法可以使用`ConfigurableBeanFactory`的`registerScope`方法：

```java
void registerScope(String scopeName, Scope scope);
// 参数1代表该scope的名称，即我们在xml的scope属性或者@Scope注解中要写的内容
// 参数2是具体的Scope实现对象
```

然而现实情况下我们很少直接接触`BeanFactory`接口，更多地是使用`ApplicationContext`容器接口（关于`BeanFactory`接口和`ApplicationContext`容器接口的相关内容我们会在`context`章节中进行介绍），好在`ApplicationContext`容器接口提供了一些方法来让我们直接获得`BeanFactory`对象，我们可以使用下面的`API`来获取`BeanFactory`：

```java
public final ConfigurableListableBeanFactory getBeanFactory();
// ConfigurableListableBeanFactory是ConfigurableBeanFactory的子接口

// 因此使用方法如下：
private AnnotationConfigApplicationContext applicationContext = ...;
ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();
factory.registerScope("thread", new SimpleThreadScope2());
```

除了直接调用容器`API`来注册，`Spring 2.0`还提供了一种很行的非侵入的声明式注册，我们只需要往容器内注册`org.springframework.beans.factory.config.CustomScopeConfigurer`对象，并往该对象设置`Scope`即可实现`Scope`注册，对应的`XML`形式`Bean`注册和`JavaConfig`形式的Bean注册参考如下：

```xml
<!-- 可以使用CustomScopeConfigurer类来实现配置, 配置类 -->
<!-- 声明式注册 -->
<bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
    <!-- 对应CustomScopeConfigurer的
		 public void setScopes(Map<String, Object> scopes)方法
	-->
    <property name="scopes">
        <map>
            <!-- 注意该Scope的名字为thread，后面要写这个名字 -->
            <entry key="thread">
                <bean class="cn.argento.askia.custom.scope.SimpleThreadScope2"/>
            </entry>
        </map>
    </property>
</bean>
```

```java
@Bean
public CustomScopeConfigurer threadScope(){
    final CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
    // 注意该Scope的名字为thread，后面要写这个名字
    customScopeConfigurer.addScope("thread", new SimpleThreadScope2());
    return customScopeConfigurer;
}
```

最后，我们在`Bean`定义时，添加上对应的`Scope`属性，相关的`XML`配置和`JavaConfig`配置如下：

```xml
<bean id="now3" class="java.time.LocalDateTime" factory-method="now" scope="thread"/>
```

```java
@Bean
@Scope("thread")
public LocalDateTime now3(){
    return LocalDateTime.now();
}
```

那么如何测试呢？我们新建两个线程，并且在两个线程中尝试获取相同`id`的`Bean`对象，测试代码如下：

```java
@Test
public void testDynamicScopeBean2() throws InterruptedException {
    // 创建第一个线程
    new Thread("thread3"){
        @Override
        public void run() {
            final Object now3 = annotationConfigApplicationContext.getBean("now3");
            System.out.println(now3);
            final Object now31 = annotationConfigApplicationContext.getBean("now3");
            System.out.println(now31);
            // 由于thread级别的Scope是基于单例Scope的基础上的，所以这里会输出true（因为是同一个对象，都是通过SimpleThreadScope2的get()方法获取到同一个对象）
            System.out.println(now3 == now31);
        }
    }.start();
    // 延迟几秒后，为了方面看出效果
    Thread.sleep(3000);
    // 创建第二个线程
    new Thread("thread4"){
        @Override
        public void run() {
            // 我们获取同样id的Bean，发现这个时候，得到的对象已经不是先前线程thread3中的now3了
            // 这里的now3是一个新的对象，大家看该对象的输出可以看出来
            final Object now3 = annotationConfigApplicationContext.getBean("now3");
            System.out.println(now3);
            final Object now31 = annotationConfigApplicationContext.getBean("now3");
            System.out.println(now3);
            System.out.println(now3 == now31);
        }
    }.start();
}
```

输出如下：

```java
2024-10-18T21:06:11.953
2024-10-18T21:06:11.953
true
2024-10-18T21:06:14.952
2024-10-18T21:06:14.952
true
```

可以看到，第一个线程获取的`ID`为`now3`的`Bean`和第二个线程获取的`now3`的`Bean`并不是同一个`Bean`对象，这就实现了线程级`Scope`上的`Bean`隔离。
