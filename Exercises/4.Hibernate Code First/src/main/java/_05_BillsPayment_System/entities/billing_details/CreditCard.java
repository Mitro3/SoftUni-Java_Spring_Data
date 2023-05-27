package _05_BillsPayment_System.entities.billing_details;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BaseBillingDetail{

    private String type;

    @Column(name = "expiration_month", nullable = false)
    private byte expirationMonth;

    @Column(name = "expiration_year", nullable = false)
    private short expirationYear;

    public CreditCard() {}


    public CreditCard(String type, byte expirationMonth, short expirationYear) {
        this.type = type;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(byte expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public short getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(short expirationYear) {
        this.expirationYear = expirationYear;
    }
}
