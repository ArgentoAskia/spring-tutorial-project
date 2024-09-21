package cn.argento.askia.beans;

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

    public DerivedTestBean() {
    }

    public DerivedTestBean(String name, int age, String address) {
        super(name, age);
        this.address = address;
    }

    public DerivedTestBean(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public DerivedTestBean setAddress(String address) {
        this.address = address;
        return this;
    }
}
