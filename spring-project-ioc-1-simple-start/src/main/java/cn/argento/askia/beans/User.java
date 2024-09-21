package cn.argento.askia.beans;

import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private Integer id;
    private String name;
    private String address;
    private int age;
    private Date birthday;
    private LocalDateTime upload;

    public User() {
        this.id = -1;
        this.name = "uname";
        this.address = "none";
        this.age = 0;
        this.birthday = null;
        this.upload = LocalDateTime.of(1970, 1, 1, 0, 0,0);
    }


    public User(Integer id, String name, String address, int age, Date birthday, LocalDateTime upload) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.birthday = birthday;
        this.upload = upload;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", age=").append(age);
        sb.append(", birthday=").append(birthday);
        sb.append(", upload=").append(upload);
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public User setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public LocalDateTime getUpload() {
        return upload;
    }

    public User setUpload(LocalDateTime upload) {
        this.upload = upload;
        return this;
    }
}

