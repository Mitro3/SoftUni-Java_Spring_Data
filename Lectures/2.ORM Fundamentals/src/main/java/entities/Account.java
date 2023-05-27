package entities;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;

import java.time.LocalDate;

@Entity(name = "accounts")
public class Account {
    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "signInDate")
    private LocalDate signInDate;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nickname")
    private String nickname;

    public Account() {};

    public Account(String name, LocalDate signInDate, Integer age) {
        this.name = name;
        this.signInDate = signInDate;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSignInDate() {
        return signInDate;
    }

    public void setSignInDate(LocalDate signInDate) {
        this.signInDate = signInDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
