## Bean Definition Inheritance

> 本节内容对应官方文档：

本章节介绍`Bean Definition Inheritance`，即说明如何实现`Bean`定义上的继承，注意此小节内容基本只针对`XML`形式的`Spring`配置。

在`Spring`中，所有`Bean`对象的相关配置性的信息（如构造器参数、属性值、初始化方法、静态方法名等）最终都会被`Spring`通过`BeanDefinition`类来进行存储。如果一个`Bean`对象的所属类是另外一个`Bean`对象的所属类的子类，则这个子类`Bean`会继承父类`Bean`的所有`BeanDefinition`信息，并且子类`Bean`可以重写其中的一些信息。

```java
# 官方原文
A child bean definition inherits scope, constructor argument values, property values, and method overrides from the parent, with the option to add new values. Any scope, initialization method, destroy method, or static factory method settings that you specify override the corresponding parent settings.

The remaining settings are always taken from the child definition: depends on, autowire mode, dependency check, singleton, and lazy init.
```

当你使用`XML`的配置方式的时候，你可以在子`Bean`对象的`Bean`标签中指定`parent`属性为父`Bean`对象的`id`，来实现`BeanDefinition`继承：

```xml
<!-- inherit type one -->
<!-- 加不加abstract = true 无关紧要-->
<bean id="inheritedTestBean" class="cn.argento.askia.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass" class="cn.argento.askia.beans.DerivedTestBean" parent="inheritedTestBean">
    <!-- override method -->
    <property name="age" value="25"/>
    <property name="address" value="California"/>
</bean>
```

下面是`DerivedTestBean`和`TestBean`的代码：

```java
public class TestBean {
    private String name;
    private int age;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestBean{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
    // 省略无参构造器和全参数构造器代码
}
```

```java
public class DerivedTestBean extends TestBean{
    private String address;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DerivedTestBean{");
        sb.append(super.toString()).append(',');
        sb.append("address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
    // 省略无参构造器和全参数构造器代码
}
```

可以看到为了方便演示这里我是用了继承，但实际上`Bean Definition Inheritance`并不要求`Bean`类需要存在继承关系，官方文档中说明，只要`DerivedTestBean`类中的属性完全覆盖`TestBean`类即可，所以我们稍微更改下`DerivedTestBean`类：

```java
public class DerivedTestBean2 {
    private String address;
    // 下面的属性和TestBean的一样，Spring会关联上DerivedTestBean2和TestBean的
    // 你可以通过指定DerivedTestBean2的这两个属性来覆盖TestBean的同名属性
    // 如果不指定的话，DerivedTestBean2这两个属性会使用TestBean的来替代
    private String name;
    private int age;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("cn.argento.askia.beans.DerivedTestBean2{");
        sb.append("address='").append(address).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
    // 省略无参构造器和全参数构造器代码
}
```

```xml
<!-- 加不加abstract = true 都能实现Bean Definition Inheritance-->
<!-- 但实际上这里因为指定了类名，所以加上abstract="true"将无法实例化inheritedTestBean3，不加则可以实例化 -->
<bean id="inheritedTestBean3" class="cn.argento.askia.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithAnotherClass" class="cn.argento.askia.beans.DerivedTestBean2" parent="inheritedTestBean3">
    <property name="address" value="California"/>
    <property name="age" value="25"/>
</bean>
```

如果你希望定义更加自由的父类模板，可以指定`abstract`属性为`true`，这样你就不需要指定类名，而且你可以定义任何的通用属性：

```xml
<!-- inherit type two -->
<!-- template for inherit bean -->
<!-- 必须要加上abstract="true"，因为没有指定类名 -->
<bean id="inheritedTestBean2" abstract="true">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass2" class="cn.argento.askia.beans.DerivedTestBean" parent="inheritedTestBean2">
    <!-- override method -->
    <property name="age" value="25"/>
    <property name="address" value="California"/>
</bean>
```

但是需要注意，`Spring`在初始化`IoC`容器的时候会忽略掉所有的`abstract="true"`的`Bean`，所以试图获取这些`Bean`都会抛出异常，如果你希望定义的父`Bean`也能够实例化的话，则不要加上`abstract="true"`

```xml
<!-- 加不加abstract = true 都能实现Bean Definition Inheritance-->
<!-- 但实际上这里因为指定了类名，所以加上abstract="true"将无法实例化inheritedTestBean3，不加则可以实例化 -->
<!-- 可以实例化inheritedTestBean3 -->
<bean id="inheritedTestBean3" class="cn.argento.askia.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<!-- 不可以实例化inheritedTestBean3 -->
<bean id="inheritedTestBean3" 
      class="cn.argento.askia.beans.TestBean" 
      abstract = "true">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithAnotherClass" class="cn.argento.askia.beans.DerivedTestBean2" parent="inheritedTestBean3">
    <property name="address" value="California"/>
    <property name="age" value="25"/>
</bean>
```

