package cn.argento.askia.beans;

public class DerivedTestBean2 {
    private String address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
