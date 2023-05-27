package _01_GringottsDatabase;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wizard_deposits", schema = "code_first")
public class WizardDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @Column(length = 1000)
    private String notes;

    private int age;

    @Column(name = "magic_wand_creator", length = 100)
    private String magicWandCreator;

    @Column(name = "magic_wand_size")
    private short magicWandSize;

    @Column(name = "deposit_group", length = 20)
    private String depositGroup;

    @Column(name = "deposit_start_date", nullable = false)
    private LocalDateTime depositStartDate;

    @Column(name = "deposit_amount", nullable = false)
    private double depositAmount;

    @Column(name = "deposit_interest", nullable = false)
    private double depositInterest;

    @Column(name = "deposit_charge", nullable = false)
    private double depositCharge;

    @Column(name = "deposit_expiration_date")
    private LocalDateTime depositExpDate;

    @Column(name = "is_deposit_expired")
    private boolean isDepositExpired;

    public WizardDeposit() {};
}
