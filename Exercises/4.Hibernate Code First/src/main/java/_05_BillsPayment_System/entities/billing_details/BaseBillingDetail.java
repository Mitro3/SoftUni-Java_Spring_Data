package _05_BillsPayment_System.entities.billing_details;

import _05_BillsPayment_System.entities.Owner;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseBillingDetail {
    @Id
    @Column(unique = true)
    private int number;

    @ManyToOne
    private Owner owner;

    protected BaseBillingDetail() {
    }
}
