## Bean Lifecycle Callbacks

在`Spring`中，`IoC Context`（`IoC`容器）管理着我们所需要的所有`Bean`，这些`Bean`自我们在`XML`或`JavaConfig`中定义，再到使用这些，最后销毁，`spring`对于这个过程有一个完成的描述，这个过程中`spring`实际上做了很多工作，包括：

1. 定义`Bean`：在`XML`配置文件中通过`<bean>`标签或者使用注解`@Component`、`@Service`、`@Repository`、`@Controller`、`@Bean`等方式定义`Bean`。
2. 注册`Bean`：当`Spring IoC`容器启动时，它会读取配置文件或注解，将定义的`Bean`注册到容器中。
3. `Bean`实例化：`Spring`容器根据配置信息创建`Bean`的实例。
4. 依赖注入：`Spring`容器注入配置的依赖项，如通过构造器注入、字段注入或设置方法注入。
5. 使用`Bean`：此时，`Bean`已经准备好被应用程序使用了，可以从`Spring`容器中获取并注入到需要使用它的地方
6. `Bean`销毁：`Spring`容器在关闭时会销毁单例`Bean`。

上面的`6`个步骤是所有注册到`IoC`容器中的`Bean`必须经历的过程，这`6`个过程像是描述着一个`Bean`的出生到死亡，因此这样的过程也被成为作为`Bean`生命周期（`Bean Lifecycle`）。

而在`Bean`的生命周期过程中，`Bean`的一些特殊节点可能也会引起我们的注意，比如在初始化`bean`的时候，我们希望能够干预其初始化过程，定制其初始化的方式等，这些功能，`spring`也提供了一些相应的接口来辅助实现，而这些监听`Bean`生命周期的接口，被称为生命周期回调（`Lifecycle callback`）。

`spring`会在`bean`完成依赖注入的时候调用这些生命周期接口，这些接口有三类，分别是：

- `Aware`类接口：也叫感知类接口，感知类接口有很多，但和`bean`生命周期相关的只有三个：`BeanNameAware`、`BeanFactoryAware`、`BeanClassLoaderAware`
- `BeanPostProcessor`接口：`Bean`后置处理器，用于处理`Bean`初始化前和初始化后阶段
- `Initialization Callbacks`：初始化回调，这些回调会在`Bean`初始化的过程中进行调用
- `Destruction Callbacks`：销毁回调，这些回调会在`Bean`被销毁的过程中进行调用

其中后面的`Initialization Callbacks`和`Destruction Callbacks`可以被归类为一类。在加上生命周期回调之后，一个更加详细的`bean`生命周期过程如下：

1. 定义`Bean`：在`XML`配置文件中通过`<bean>`标签或者使用注解`@Component`、`@Service`、`@Repository`、`@Controller`、`@Bean`等方式定义`Bean`。
2. 注册`Bean`：当`Spring IoC`容器启动时，它会读取配置文件或注解，将定义的`Bean`注册到容器中。
3. `Bean`实例化：`Spring`容器根据配置信息创建`Bean`的实例。
4. 依赖注入：`Spring`容器注入配置的依赖项，如通过构造器注入、字段注入或设置方法注入。
5. `Aware`接口回调：如果`Bean`实现了`BeanNameAware`、`BeanFactoryAware`、`BeanClassLoaderAware`，则`Spring`容器将调用它们的`setBeanName`、`setBeanClassLoader`、`setBeanFactory`等方法。
6. `BeanPostProcessor`前置处理：调用`BeanPostProcessor#postProcessBeforeInitialization()`
7. `Initialization Callbacks`
   1. 如果`Bean`中存在`@PostConstruct`标记的方法，则调用该方法
   2. 如果`Bean`实现了`InitializingBean`接口，`Spring`容器将调用其`afterPropertiesSet`方法。
   3. 如果`Bean`定义了`init-method`，`Spring`容器将调用该方法。
8. `BeanPostProcessor`后置处理：调用`BeanPostProcessor#postProcessAfterInitialization()`
9. 使用`Bean`：此时，`Bean`已经准备好被应用程序使用了，可以从`Spring`容器中获取并注入到需要使用它的地方
10. `Destruction Callbacks`
    1. 如果`Bean`定义了`destroy-method`，`Spring`容器将调用该方法。
    2. 当容器关闭时，如果`Bean`实现了`DisposableBean`接口，`Spring`容器将调用其`destroy`方法。
    3. 如果`Bean`中存在`@PreDestroy`标记的方法，则调用该方法
11. `Bean`销毁：`Spring`容器在关闭时会销毁单例`Bean`。

当看到完整的生命周期时（这也是`spring`的面试题之一），你可能会感觉无可适从和莫名其妙，没关系，随着深入的学习你的这种感觉会灰飞云散。我们会在后续的学习中讨论`Aware`类接口和`BeanPostProcessor`接口这两类周期，今天我们主要讨论`Initialization Callbacks`和`Destruction Callbacks`。

### Initialization and Destruction Callbacks（XML）

你可能注意到在上面第七步`Initialization Callbacks`中，有三个小点，没错，`spring`为`Initialization Callbacks`提供了三种方式来实现`Initialization Callbacks`：

1. 使用`@PostConstruct`标记方法
2. 实现`InitializingBean`接口，并重写`afterPropertiesSet()`
3. 使用`bean`标签的`init-method`属性指定初始化方法

而`Destruction Callbacks`同理：

1. 使用`@PreDestroy`标记方法
2. 实现`DisposableBean`接口，并重写`destroy()`
3. 使用`bean`标签的`destroy-method`属性指定初始化方法

我们定义了一个公共`Bean`叫`User3`：

```java
public class User3 {
    private Integer id;
    private String name;
    private String address;
    private int age;
    private Date birthday;
    private LocalDateTime upload;

    public User3() {
        this.id = -1;
        this.name = "uname";
        this.address = "none";
        this.age = 0;
        this.birthday = null;
        this.upload = LocalDateTime.of(1970, 1, 1, 0, 0,0);
        System.out.println("初始化User3, 使用NoArgsConstructor");
    }

    public User3(Integer id,
                 String name,
                 String address,
                 int age,
                 Date birthday,
                 LocalDateTime upload) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.birthday = birthday;
        this.upload = upload;
        System.out.println("初始化User3, 使用AllArgsConstructor");
    }
    // getter 、setter and toString()
}
```

我们分别编写三个子`Bean`继承自`User3`，并应用上面三种初始化方式：

1. 使用`@PostConstruct`来初始化，使用方式就是在初始化方法（该方法随意，但不能有参数且返回值必须是`void`）上标记`@PostConstruct`注解，`@PreDestroy`同理。

```java
// 1.使用@PostConstruct来实现初始化
public class User3AnnotationLifeCycleCallback extends User3 {

    public User3AnnotationLifeCycleCallback(){
        super();
    }
    
    @PostConstruct
    public void init(){
        System.out.println("3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，" +
                "在这种方式上方法签名必须是：public void [随便的方法名]();");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }
}
```

2. 使用`InitializingBean`接口来初始化，实现该接口并重写`afterPropertiesSet()`方法，`DisposableBean`接口同理

```java
// 2.使用InitializingBean接口和DisposableBean接口来初始化
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

```

3. 使用`init-method`属性，在`Bean`中定义一个方法，方法名随意但必须无参，然后再`XML`配置文件的`bean`标签中指定`init-method`属性。`destroy-method`属性同理

```java
// 3.使用init-method属性来初始化
public class User3XMLLifeCycleCallback extends User3 {

    public void init(){
        System.out.println("2.采用XML：init-method属性的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，" +
                "在这种方式上方法签名必须是：public void [随便的方法名]();");
    }

    public void destroy(){
        System.out.println("2.采用XML：destroy-method属性的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }
}
```

```xml
<bean id="user3_2" class="cn.argentoaskia.callback.User3XMLLifeCycleCallback"
    init-method="init" destroy-method="destroy"/>
```

然后，我们在`XML`配置文件中定义上面三个`Bean`：

```xml
<bean id="date" class="java.util.Date"/>
<bean id="localDateTime" class="java.time.LocalDateTime" factory-method="now"/>
<!-- 1.使用InitializingBean接口和DisposableBean接口来初始化 -->
<bean id="user3_1" class="cn.argentoaskia.callback.User3LifeCycleInterfaceCallback">
    <property name="address" value="广州市"/>
    <property name="age" value="30"/>
    <property name="birthday" ref="date"/>
    <property name="id" value="1"/>
    <property name="name" value="Askia"/>
    <property name="upload" ref="localDateTime"/>
</bean>

<!-- 2.采用XML-init-method属性和destroy-method属性管理bean的初始化和销毁 -->
<bean id="user3_2" class="cn.argentoaskia.callback.User3XMLLifeCycleCallback"
init-method="init" destroy-method="destroy">
    <property name="address" value="江门市"/>
    <property name="age" value="23"/>
    <property name="birthday" ref="date"/>
    <property name="id" value="2"/>
    <property name="name" value="Askia"/>
    <property name="upload" ref="localDateTime"/>
</bean>

<!-- 3.采用@PostConstruct、@PreDestroy实现初始化-->
<!-- 必须要有context:annotation-config, 否则@PostConstruct、@PreDestroy注解不生效-->
<context:annotation-config/>
<bean id="user3_3" class="cn.argentoaskia.callback.User3AnnotationLifeCycleCallback"></bean>
```

我们进行测试，测试代码如下：

```java
public class XMLBeanLifeCycleTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Before
    public void init(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        applicationContext = new ClassPathXmlApplicationContext("/spring-context.xml");
        System.out.println();
    }

    @After
    public void destroy(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        applicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    @Test
    public void testAllLifeCycle(){
        User3 user3_1 = applicationContext.getBean("user3_1", User3.class);
        User3 user3_2 = applicationContext.getBean("user3_2", User3.class);
        User3 user3_3 = applicationContext.getBean("user3_3", User3.class);
        System.out.println("## 输出bean");
        System.out.println("user3_1: " + user3_1);
        System.out.println("user3_2: " + user3_2);
        System.out.println("user3_3: " + user3_3);
        System.out.println();
    }
}

```

其结果如下：

```bash
## 依赖注入完成之后，开始进行生命周期回调

初始化User3, 使用NoArgsConstructor
1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...

初始化User3, 使用NoArgsConstructor
2.采用XML：init-method属性的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();

初始化User3, 使用NoArgsConstructor
3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();

## 输出bean
user3_1: User{id=1, name='Askia', address='广州市', age=30, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_2: User{id=2, name='Askia', address='江门市', age=23, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_3: User{id=20, name='Susan', address='北京市', age=99, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}

## 准备关闭容器

3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
2.采用XML：destroy-method属性的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...

## 容器已关闭
```

可以看到，上面三种方式都是可以工作的，为了更加直观地显示，运行结果部分使用了`##`分割。

另外需要特别注意，能够使用`@PostConstruct`、`@PreDestroy`的前提是容器内已经注册了下面三个名字的`Bean`：

- `org.springframework.context.annotation.internalConfigurationAnnotationProcessor`（实际类型：`org.springframework.context.annotation.ConfigurationClassPostProcessor`）
- `org.springframework.context.annotation.internalAutowiredAnnotationProcessor`（实际类型：`org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor`）
- `org.springframework.context.annotation.internalCommonAnnotationProcessor`（实际类型：`org.springframework.context.annotation.CommonAnnotationBeanPostProcessor`）

这三个`Bean`专门用来处理注解形式的内容（实际上都是`BeanPostProcessor`的子类），如`@Component`、`@Configuration`、`@PostConstruct`、`@PreDestroy`等，要想注入这三个`Bean`，你可以直接写他们的`Bean`标签：

```xml
<!-- 处理@PostConstruct、@PreDestroy时，需要使用CommonAnnotationBeanPostProcessor -->
<bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"/>

<!-- 处理@Configuration时，需要使用ConfigurationClassPostProcessor -->
<bean class="org.springframework.context.annotation.ConfigurationClassPostProcessor"></bean>

<!-- 处理@Autowired时，需要使用AutowiredAnnotationBeanPostProcessor -->
<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"></bean>

<!-- 测试过程中，理论上只需要注册CommonAnnotationBeanPostProcessor即可 -->
<!-- 但低版本spring可能需要都注册 -->
```

或者使用`spring`提供的整合标签：

```xml
<context:annotation-config/>
```

如果你不注册上面三个`Bean`，则你会发现`@PostConstruct`、`@PreDestroy`会失效，不会有任何关于`@PostConstruct`、`@PreDestroy`的输出：

```bash
## 依赖注入完成之后，开始进行生命周期回调

初始化User3, 使用NoArgsConstructor
1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...

初始化User3, 使用NoArgsConstructor
2.采用XML：init-method属性的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();

初始化User3, 使用NoArgsConstructor

## 输出bean
user3_1: User{id=1, name='Askia', address='广州市', age=30, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_2: User{id=2, name='Askia', address='江门市', age=23, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_3: User{id=20, name='Susan', address='北京市', age=99, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}

## 准备关闭容器

2.采用XML：destroy-method属性的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...

## 容器已关闭
```

### Initialization and Destruction Callbacks（JavaConfig）

使用`JavaConfig`的方式也能实现，和`XML`方式基本相同，在`@Bean`中，有两个属性：

```java
String initMethod() default "";
String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
```

对应`bean`标签的`init-method`属性和`destroy-method`属性。我们编写`JavaConfig`形式的配置来实现回调：

```java
public class User3JavaConfigContext {

    @Bean
    public Date date(){
        return new Date();
    }

    @Bean
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }

    // 1.使用InitializingBean接口和DisposableBean接口来初始化
    @Bean
    public User3LifeCycleInterfaceCallback user3_1(@Value("广州市") String address,
                                                   @Value("30") int age,
                                                   @Autowired Date birthday,
                                                   @Value("1") int id,
                                                   @Value("Askia") String name,
                                                   @Autowired LocalDateTime upload){
        final User3LifeCycleInterfaceCallback user3LifeCycleInterfaceCallback = new User3LifeCycleInterfaceCallback();
        user3LifeCycleInterfaceCallback.setAddress(address)
                .setAge(age)
                .setBirthday(birthday)
                .setId(id)
                .setName(name)
                .setUpload(upload);
        return user3LifeCycleInterfaceCallback;
    }

    // 2.采用XML-init-method属性和destroy-method属性管理bean的初始化和销毁
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public User3XMLLifeCycleCallback user3_2(@Value("江门市") String address,
                                             @Value("23") int age,
                                             @Autowired Date birthday,
                                             @Value("2") int id,
                                             @Value("Askia") String name,
                                             @Autowired LocalDateTime upload){
        final User3XMLLifeCycleCallback user3XMLLifeCycleCallback = new User3XMLLifeCycleCallback();
        user3XMLLifeCycleCallback.setAddress(address)
                .setAge(age)
                .setBirthday(birthday)
                .setId(id)
                .setName(name)
                .setUpload(upload);
        return user3XMLLifeCycleCallback;
    }
    
    // 3.采用@PostConstruct、@PreDestroy实现初始化
    @Bean
    public User3AnnotationLifeCycleCallback user3_3(){
        return new User3AnnotationLifeCycleCallback();
    }
}
```

我们进行测试，测试代码如下：

```java
public class AnnotationBeanLifeCycleTest {

    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        applicationContext = new AnnotationConfigApplicationContext(User3JavaConfigContext.class);
        System.out.println();
    }

    @After
    public void destroy(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        applicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    @Test
    public void testAllLifeCycle(){
        User3 user3_1 = applicationContext.getBean("user3_1", User3.class);
        User3 user3_2 = applicationContext.getBean("user3_2", User3.class);
        User3 user3_3 = applicationContext.getBean("user3_3", User3.class);
        System.out.println("## 输出bean");
        System.out.println("user3_1: " + user3_1);
        System.out.println("user3_2: " + user3_2);
        System.out.println("user3_3: " + user3_3);
        System.out.println();
    }
}
```

可以看到输出是相同的：

```bash
## 依赖注入完成之后，开始进行生命周期回调

初始化User3, 使用NoArgsConstructor
1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...

初始化User3, 使用NoArgsConstructor
2.采用XML：init-method属性的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();

初始化User3, 使用NoArgsConstructor
3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();

## 输出bean
user3_1: User{id=1, name='Askia', address='广州市', age=30, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_2: User{id=2, name='Askia', address='江门市', age=23, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}
user3_3: User{id=20, name='Susan', address='北京市', age=99, birthday=Thu Nov 28 16:32:07 CST 2024, upload=2024-11-28T16:32:07.766}

## 准备关闭容器

3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
2.采用XML：destroy-method属性的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...

## 容器已关闭
```

另外由于`AnnotationConfigApplicationContext`本身就会注册`CommonAnnotationBeanPostProcessor`、`ConfigurationClassPostProcessor`、`AutowiredAnnotationBeanPostProcessor`所以不用担心`@PostConstruct`和`@PreDestroy`不生效问题。

### Initialization and Destruction Callbacks（Component Scan）

自`spring 2.5`引入组件扫描方式（`Component Scan`）之后，我们则可以在标记了`@Component`或者`@Component`的衍生注解（比如：`@Service`、`@Controller`、`@Configuration`等）的类上执行`Initialization and Destruction Callbacks`。

在组件扫描（`Component Scan`）中，`@PostConstruct`注解、`@PreDestroy`注解和`InitializingBean`接口、`DisposableBean`接口这两种方式仍然生效，而基于`init-method`属性和`destroy-method`属性的方式则无法复现。

我们创建两个`User3`的子类，并给他们标记上`@Compoment`注解：

```java
@Component("user3_3_1")
public class User3AnnotationLifeCycleCallback2 extends User3 {


    @Autowired
    public User3AnnotationLifeCycleCallback2(@Value("20") Integer id,
                 @Value("Susan") String name,
                 @Value("北京市") String address,
                 @Value("99") int age,
                 @Autowired Date birthday,
                 @Autowired LocalDateTime upload) {
        super(id, name, address, age, birthday, upload);
    }

    @PostConstruct
    public void init(){
        System.out.println("3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，" +
                "在这种方式上方法签名必须是：public void [随便的方法名]();");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }

}
```

```java
@Component("user3_3_2")
public class User3LifeCycleInterfaceCallback2 extends User3 implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...");
    }
}
```

然后由于组件扫描（`Component Scan`）既能在`xml`配置（`2.5`版本特性）中实现，又能在`JavaConfig`（`3.1`版本特性）中实现，所以我们分别创建`xml Ioc`容器和`JavaConfig IoC`容器：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 创建User3需要的依赖，以方便@Component的类进行注入-->
    <bean id="date" class="java.util.Date"/>
    <bean id="localDateTime" class="java.time.LocalDateTime" factory-method="now"/>

    <!-- 开启组件扫描需要下面两个标签，其中第一个我们之前讲过，会注册三个BeanPostProcessors来实现注解驱动 -->
    <context:annotation-config/>
    <!-- 第二个标签代表要在哪个包中寻找类，会遍历该包及其所有子包下标记@Component及其衍生注解的类，我们会在component-scan一节具体说明 -->
    <context:component-scan base-package="cn.argentoaskia.callback.componentscan"/>
</beans>
```

```java
// spring 3.1引入了@ComponentScan，以实现JavaConfig配置下的组件扫描
// 其配置方法和<context:component-scan>标签基本没有区别
// 同样会我们会在component-scan一节具体说明
@ComponentScan(basePackages = "cn.argentoaskia.callback.componentscan")
public class User3ComponentScanContext {

    @Bean
    public Date date(){
        return new Date();
    }

    @Bean
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }

}
```

然后我们分别测试两个容器是否能正常检测出`@PostConstruct`注解、`@PreDestroy`注解和`InitializingBean`接口、`DisposableBean`接口，我们编写下面的测试代码：

```java
public class ComponentScanLifeCycleTest {

    private ClassPathXmlApplicationContext xmlApplicationContext;
    private AnnotationConfigApplicationContext annotationApplicationContext;


    public void initXml(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        xmlApplicationContext = new ClassPathXmlApplicationContext("/spring-context-component-scan.xml");
        System.out.println();
    }

    public void initAnnotation(){
        System.out.println("## 依赖注入完成之后，开始进行生命周期回调");
        System.out.println();
        annotationApplicationContext = new AnnotationConfigApplicationContext(User3ComponentScanContext.class);
        System.out.println();
    }


    public void destroyXml(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(xmlApplicationContext.getBeanDefinitionNames()));
        xmlApplicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    public void destroyAnnotation(){
        System.out.println("## 准备关闭容器");
        System.out.println();
        System.out.println(Arrays.toString(annotationApplicationContext.getBeanDefinitionNames()));
        annotationApplicationContext.close();
        System.out.println();
        System.out.println("## 容器已关闭");
    }

    @Test
    public void testXmlComponentScanLifeCycle(){
        initXml();
        final Object user3_3_1 = xmlApplicationContext.getBean("user3_3_1");
        final Object user3_3_2 = xmlApplicationContext.getBean("user3_3_2");
        System.out.println("## 输出bean");
        System.out.println("user3_3_1: " + user3_3_1);
        System.out.println("user3_3_2: " + user3_3_2);
        System.out.println();
        destroyXml();
    }

    @Test
    public void testAnnotationComponentScanLifeCycle(){
        initAnnotation();
        final Object user3_3_1 = annotationApplicationContext.getBean("user3_3_1");
        final Object user3_3_2 = annotationApplicationContext.getBean("user3_3_2");
        System.out.println("## 输出bean");
        System.out.println("user3_3_1: " + user3_3_1);
        System.out.println("user3_3_2: " + user3_3_2);
        System.out.println();
        destroyAnnotation();
    }

}
```

可以看到无论是`JavaConfig`容器，还是`Xml`容器，他们的输出结果都是一样的：

```bash
## 依赖注入完成之后，开始进行生命周期回调

初始化User3, 使用AllArgsConstructor
3.采用@PostConstruct注解的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...，在这种方式上方法签名必须是：public void [随便的方法名]();
初始化User3, 使用NoArgsConstructor
1.通过实现接口InitializingBean的方式来进行User3对象的生命周期初始化Callback，一般用于初始化外部资源等...

## 输出bean
user3_3_1: User{id=20, name='Susan', address='北京市', age=99, birthday=Thu Nov 28 19:39:23 CST 2024, upload=2024-11-28T19:39:23.772}
user3_3_2: User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}

## 准备关闭容器

1.通过实现接口DisposableBean的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...
3.采用@PreDestroy注解的方式来进行User3对象的生命周期销毁Callback，一般用于销毁外部资源等...

## 容器已关闭
```

这样证明了组件扫描方式下不管是哪一类容器，都能实现`@PostConstruct`注解、`@PreDestroy`注解和`InitializingBean`接口、`DisposableBean`接口。

### extends points

最后我们说明一下`Initialization and Destruction Callbacks`的一些扩展点，以结束本篇章的学习。

首先`spring`的`Xml`配置中支持为所有该配置的`Bean`统一设置初始化和销毁方法（注意`JavaConfig`不支持统一设置初始化和销毁方法，该特性是`Xml`配置独有），需要在`beans`标签中设置`default-init-method`和`default-destroy-method`属性。这种设置方式对组件扫面和`Xml`配置都起作用，属于全局初始化方法和全局销毁方法。

后期如果`Xml`配置中的`bean`想要覆盖这种全局设置，可以额外指定`init-method`和`destroy-method`，这样`bean`会使用`init-method`和`destroy-method`而非`default-init-method`和`default-destroy-method`。

但是需要注意，由于组件扫描方式没有`init-method`和`destroy-method`，并且`@PostConstruct`注解、`@PreDestroy`注解和`InitializingBean`接口、`DisposableBean`接口均无法实现覆盖`default-init-method`和`default-destroy-method`属性，也就是说当你同时设置了`default-init-method`和`default-destroy-method`属性，`@PostConstruct`注解和`@PreDestroy`注解，`InitializingBean`接口和`DisposableBean`接口时，他们都会生效，并且调用有先后顺序，具体顺序我们接下来会讲到。

为了证实上面的点，我们创建了两`User3`的，其中一个用于演示`init-method`和`destroy-method`覆盖`default-init-method`和`default-destroy-method`，另外一个用来演示`@PostConstruct`注解、`@PreDestroy`注解和`InitializingBean`接口、`DisposableBean`接口无法覆盖`default-init-method`和`default-destroy-method`属性

```java
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
```

```java
@Component("global")
public class User3GlobalComponentScanLifeCycleCallback extends User3
implements DisposableBean, InitializingBean {
    public void defaultInit(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用default-init-method属性进行init...");
    }

    public void defaultDestroy(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用default-destroy-method属性进行destroy...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用DisposableBean接口进行destroy...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用InitializingBean接口进行init...");
    }

    @PostConstruct
    public void init2(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用@PostConstruct接口进行init...");
    }

    @PreDestroy
    public void destroy2(){
        System.out.println("User3GlobalComponentScanLifeCycleCallback: 使用@PreDestroy进行destroy...");
    }
}
```

然后我们编写`xml`配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd"
default-init-method="defaultInit" default-destroy-method="defaultDestroy">
    <!--
       0.可以指定default-init-method和default-destroy-method来实现统一管理初始化和销毁方法
         这种全局的方式对无论是注解方式的bean还是XML方式的定义都起作用，属于全局初始化方法和全局销毁方法
         后期bean标签中可以使用init-method和destroy-method覆盖这个方法,但是注解的形式却不能做到覆盖
   -->
    <bean id="user3GlobalLifeCycleCallback"
          class="cn.argentoaskia.callback.extendpoints.globallifecycle.User3GlobalLifeCycleCallback"/>


    <!-- 演示：init-method能够覆盖default-init-method和destroy-method覆盖default-destroy-method -->
    <bean id="user3GlobalLifeCycleCallbackOverride"
          class="cn.argentoaskia.callback.extendpoints.globallifecycle.User3GlobalLifeCycleCallback"
    init-method="init" destroy-method="destroy"/>

    <!-- 演示：注解扫描无法覆盖default-init-method和default-destroy-method,都会执行-->
    <context:annotation-config/>
    <context:component-scan base-package="cn.argentoaskia.callback.extendpoints.globallifecycle"/>
</beans>
```

测试代码：

```java
public class ExtendsPointsGlobalLifecycleTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Test
    public void print(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-global-lifecycle.xml");
        System.out.println();
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println();
        System.out.println("## beans：");
        for (String name : beanDefinitionNames) {
            System.out.println(name + " = " + applicationContext.getBean(name));
        }
        System.out.println();
        applicationContext.close();
    }
}
```

其输出结果如下，我们使用`##`辅以说明：

```bash
## 此处调用的是default-init-method，创建user3GlobalLifeCycleCallback
## 可以看到使用了全局的init-method
初始化User3, 使用NoArgsConstructor
User3GlobalLifeCycleCallback: 使用default-init-method属性进行init...

## 此处调用的是init-method，创建user3GlobalLifeCycleCallbackOverride对象
## 可以看到init-method属性成功覆盖了default-init-method属性
初始化User3, 使用NoArgsConstructor
User3GlobalLifeCycleCallback-override: 指定init-method属性为init方法，覆盖defaultInit进行初始化...

## 此时是组件扫描方式创建global对象
## 可以看到@PostConstruct和InitializingBean接口无法覆盖default-init-method，他们都会执行
初始化User3, 使用NoArgsConstructor
User3GlobalComponentScanLifeCycleCallback: 使用@PostConstruct进行init...
User3GlobalComponentScanLifeCycleCallback: 使用InitializingBean接口进行init...
User3GlobalComponentScanLifeCycleCallback: 使用default-init-method属性进行init...

## 打印IoC中的所有Bean Names
[user3GlobalLifeCycleCallback, user3GlobalLifeCycleCallbackOverride, org.springframework.context.annotation.internalConfigurationAnnotationProcessor, org.springframework.context.annotation.internalAutowiredAnnotationProcessor, org.springframework.context.annotation.internalCommonAnnotationProcessor, org.springframework.context.event.internalEventListenerProcessor, org.springframework.context.event.internalEventListenerFactory, global]

## beans：
user3GlobalLifeCycleCallback = User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}
user3GlobalLifeCycleCallbackOverride = User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}
global = User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}

## 此时是销毁组件扫面方式创建的global对象
## 可以看到@PreDestroy和DisposableBean接口无法覆盖default-destroy-method，他们都会执行
User3GlobalComponentScanLifeCycleCallback: 使用@PreDestroy进行destroy...
User3GlobalComponentScanLifeCycleCallback: 使用DisposableBean接口进行destroy...
User3GlobalComponentScanLifeCycleCallback: 使用default-destroy-method属性进行destroy...

## 此处销毁user3GlobalLifeCycleCallbackOverride对象，可以看到destroy-method成功覆盖default-destroy-method
User3GlobalLifeCycleCallback-override: 指定destroy-method属性为destroy方法，覆盖defaultDestroy进行初始化...

User3GlobalLifeCycleCallback: 使用default-destroy-method属性进行destroy...
```

然后我们便来讨论如果一个`Bean`上既有`@PostConstruct`、`@PreDestroy`，又有`InitializingBean`接口、`DisposableBean`接口还有`destroy-method`属性、`init-method`属性时，他们的调用顺序。

先说答案，无论是`Initialization Callbacks`还是`Destruction Callbacks`，则其顺序都是：

1. 先执行`@PostConstruct`、`@PreDestroy`
2. 然后是`InitializingBean`接口、`DisposableBean`接口
3. 最后才是`init-method`属性、`destroy-method`属性

这个顺序在`JavaConfig`配置、`Xml`配置、`Xml`配置下定义了全局初始化方法和全局销毁方法的组件扫描均有效

我们选择`Xml`配置来证明，`JavaConfig`配置和`Xml`配置的组件扫描方式同理。

我们先定义一个类实现上面三种生命周期回调：

```java
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
```

编写`Xml`配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 让@PostConstructor注解生效 -->
    <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"/>
    
    <bean class="cn.argentoaskia.callback.extendpoints.multicallback.User3MultipleLifecycleMechanisms"
    init-method="initialization" destroy-method="shutdown"/>
</beans>
```

测试代码：

```java
public class ExtendsPointsMultiCallbackLifecycleTest {

    private ClassPathXmlApplicationContext applicationContext;


    @After
    public void destroy(){
        System.out.println("准备关闭容器");
        applicationContext.close();
        System.out.println("容器已关闭");
    }
    @Test
    public void test(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-multicallbacks.xml");
        final User3MultipleLifecycleMechanisms bean = applicationContext.getBean(User3MultipleLifecycleMechanisms.class);
        System.out.println(bean);
        System.out.println("准备关闭容器");
        applicationContext.close();
        System.out.println("容器已关闭");
    }
}
```

输出：

```bash
初始化User3, 使用NoArgsConstructor
## 先调用@PostConstruct
1.@PostConstruct方式初始化在最先被调用，优先级最高
## 然后是InitializingBean接口的afterPropertiesSet()
2.接口方式初始化在中间被调用，优先级居中
## 最后是init-method或default-init-method
3.init-method方式初始化在最后才被调用，init-method优先级最低

User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}
准备关闭容器
## 先调用@PreDestroy
1.@PreDestroy方式销毁在最开始被调用，优先级最高
## 然后是DisposableBean接口的destroy()
2.接口方式销毁在中间被调用，优先级居中
## 最后是destroy-method属性
3.destroy-method方式销毁在最后被调用，destroy-method优先级最低
容器已关闭
```

最后`destroy-method`属性和`default-destroy-method`属性可以指定一个特殊的值：`(inferred)`，指定该值之后，则默认会使用`bean`中的`close()`方法或者`shutdown()`方法作为`destroy-method`属性和`default-destroy-method`属性的值。

另外`spring`会判断`Bean`是否实现了`closeable`接口或者`AutoCloseable`接口，这两个接口提供了`close()`，如果实现了这两个接口的其中一个，则`spring`就可以自动将`close()`作为`destroy-method`属性的值，同样也符合`JDK`的接口特性。（`Xml`配置特有）

从`spring 3`版本之后，`JavaConfig`配置中的`@Bean`注解的`destroyMethod()`默认就是`"(inferred)"`，也就是说只要`Bean`中存在`close()`方法或者`shutdown()`方法，他们都会被当作`destroyMethod()`

```java
String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
public static final String INFER_METHOD = "(inferred)";
```

我们定义两个`User3`子类，其中一个继承`closeable`接口，另外一个定义了`shutdown()`：

```java
public class User3AutoCloseXMLLifeCycleCallback extends User3 implements Closeable {

    @Override
    public void close() {
        System.out.println("spring默认情况下会判断Bean是否实现了AutoCloseable接口或者Closeable接口");
        System.out.println("因此只要实现了这两个接口，则会将close方法作为destroy-method的值...");
    }
}
```

```java
public class User3ShutdownMethodLifeCycleCallback extends User3 {

    public void shutdown(){
        System.out.println("4.1.通过指定destroy-method属性为：(inferred)，可以实现自动关闭，而不需要写具体方法名。");
        System.out.println("4.2.这种方式会让spring默认情况下调用close方法或者shutdown方法");
        System.out.println();
    }
}
```

然后我们分别创建`JavaConfig`容器和`Xml`配置容器：

```java
public class User3ExtendsPointsDefaultDestroyContext {

    // 无需指定destroyMethod()，其默认值就是"(inferred)"
    @Bean
    public User3AutoCloseXMLLifeCycleCallback autoCloseXMLLifeCycleCallback(){
        return new User3AutoCloseXMLLifeCycleCallback();
    }

    @Bean
    public User3ShutdownMethodLifeCycleCallback user3ShutdownMethodLifeCycleCallback(){
        return new User3ShutdownMethodLifeCycleCallback();
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">


    <!-- 4. AutoCloseable | Closeable的写法 -->
    <!-- 此处不需要指定"(inferred)"，因为spring会判断bean是否实现了closeable或者AutoCloseable，如果实现了则自动绑定destroy-method="close", 当然你强制指定destroy-method="(inferred)也是可以的-->
    <bean id="autoCloseXMLLifeCycleCallback" class="cn.argentoaskia.callback.extendpoints.defaultdestroy.User3AutoCloseXMLLifeCycleCallback"/>
    
    <!-- 指定destroy-method="(inferred)" -->
    <bean id="shutdownMethodLifeCycleCallback"
class="cn.argentoaskia.callback.extendpoints.defaultdestroy.User3ShutdownMethodLifeCycleCallback"
    destroy-method="(inferred)"/>

</beans>
```

测试代码：

```java
public class ExtendsPointsDefaultDestroyTest {
    private ClassPathXmlApplicationContext applicationContext;

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Test
    public void print(){
        applicationContext = new ClassPathXmlApplicationContext("/spring-context-extends-points-default-destroy.xml");
        System.out.println();
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println();
        System.out.println("## beans：");
        for (String name : beanDefinitionNames) {
            System.out.println(name + " = " + applicationContext.getBean(name));
        }
        System.out.println();
        applicationContext.close();
    }

    @Test
    public void print2(){
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(User3ExtendsPointsDefaultDestroyContext.class);
        System.out.println();
        final String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println();
        System.out.println("## beans：");
        for (String name : beanDefinitionNames) {
            System.out.println(name + " = " + annotationConfigApplicationContext.getBean(name));
        }
        System.out.println();
        annotationConfigApplicationContext.close();
    }
}
```

两个输出的结果都是：

```bash
初始化User3, 使用NoArgsConstructor
初始化User3, 使用NoArgsConstructor

[autoCloseXMLLifeCycleCallback, shutdownMethodLifeCycleCallback]

## beans：
autoCloseXMLLifeCycleCallback = User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}
shutdownMethodLifeCycleCallback = User{id=-1, name='uname', address='none', age=0, birthday=null, upload=1970-01-01T00:00}

## 成功调用shutdown()
4.1.通过指定destroy-method属性为：(inferred)，可以实现自动关闭，而不需要写具体方法名。
4.2.这种方式会让spring默认情况下调用close方法或者shutdown方法

## 成功调用close()
spring默认情况下会判断Bean是否实现了AutoCloseable接口或者Closeable接口
因此只要实现了这两个接口，则会将close方法作为destroy-method的值...
```

### 参考文献

- [Initialization Callbacks](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-lifecycle-initializingbean)
- [Destruction Callbacks](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-lifecycle-disposablebean)
- [Default Initialization and Destroy Methods](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-lifecycle-default-init-destroy-methods)
- [Combining Lifecycle Mechanisms](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-lifecycle-combined-effects)

