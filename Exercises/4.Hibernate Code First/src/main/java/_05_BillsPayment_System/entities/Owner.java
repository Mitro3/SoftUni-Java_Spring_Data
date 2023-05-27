package _05_BillsPayment_System.entities;

import _05_BillsPayment_System.entities.billing_details.BaseBillingDetail;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", targetEntity = BaseBillingDetail.class)
    private Set<BaseBillingDetail> billingDetail;

    public Owner() {
    }

    public Owner(String firstName, String lastName, String email, String password, Set<BaseBillingDetail> billingDetail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.billingDetail = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<BaseBillingDetail> getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(Set<BaseBillingDetail> billingDetail) {
        this.billingDetail = billingDetail;
    }
}
