package _05_BillsPayment_System.entities.billing_details;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bank_accounts")
public class BankAccount extends BaseBillingDetail{

    @Column(nullable = false)
    private String name;

    @Column(name = "SWIFT_code", nullable = false)
    private String swiftCode;

    public BankAccount() {
    }

    public BankAccount(String name, String swiftCode) {
        this.name = name;
        this.swiftCode = swiftCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
