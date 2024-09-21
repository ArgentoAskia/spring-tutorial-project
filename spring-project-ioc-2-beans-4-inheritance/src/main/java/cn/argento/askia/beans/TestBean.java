package cn.argento.askia.beans;

public class TestBean {
    private String name;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestBean{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }

    private int age;

    public TestBean() {
    }

    public TestBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public TestBean setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public TestBean setAge(int age) {
        this.age = age;
        return this;
    }
}
