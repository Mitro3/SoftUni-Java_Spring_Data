package _04_HospitalDatabase.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "diagnoses", schema = "hospital_database")
public class Diagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String comments;

    @ManyToMany
    @JoinTable(
            name = "diagnoses_patients",
            joinColumns = @JoinColumn(name = "diagnose_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id")
    )
    private Set<Patient> patients;
}
