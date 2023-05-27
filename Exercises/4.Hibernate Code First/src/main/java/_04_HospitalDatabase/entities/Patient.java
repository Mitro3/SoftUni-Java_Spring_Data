package _04_HospitalDatabase.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patients", schema = "hospital_database")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    private String email;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Lob
    private byte[] picture;

    @Column(name = "has_medical_insured")
    private boolean hasMedicalInsured;

    @ManyToMany(mappedBy = "patients")
    private Set<Visitation> visitations;

    @ManyToMany(mappedBy = "patients")
    private Set<Diagnose> diagnoses;

    @ManyToMany(mappedBy = "patients")
    private Set<Medication> medications;

    public Patient() {}

    public Patient(String firstName, String lastName, String address, String email,
                   LocalDate dateOfBirth, byte[] picture, boolean hasMedicalInsured) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAddress(address);
        this.setEmail(email);
        this.setDateOfBirth(dateOfBirth);
        this.setPicture(picture);
        this.setHasMedicalInsured(hasMedicalInsured);

        this.visitations = new HashSet<>();
        this.diagnoses = new HashSet<>();
        this.medications = new HashSet<>();
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    private void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getPicture() {
        return picture;
    }

    private void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isHasMedicalInsured() {
        return hasMedicalInsured;
    }

    private void setHasMedicalInsured(boolean hasMedicalInsured) {
        this.hasMedicalInsured = hasMedicalInsured;
    }

    public Set<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(Set<Visitation> visitations) {
        this.visitations = visitations;
    }

    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public Set<Medication> getMedications() {
        return medications;
    }

    public void setMedications(Set<Medication> medications) {
        this.medications = medications;
    }
}
