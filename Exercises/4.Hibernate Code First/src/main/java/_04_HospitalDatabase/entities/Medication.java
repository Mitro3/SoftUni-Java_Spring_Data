package _04_HospitalDatabase.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "medications", schema = "hospital_database")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "medications_patients",
            joinColumns = @JoinColumn(name = "medication_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id")
    )
    private Set<Patient> patients;
}
