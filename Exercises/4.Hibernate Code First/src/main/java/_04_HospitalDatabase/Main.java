package _04_HospitalDatabase;

import _04_HospitalDatabase.entities.Patient;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("HospitalDatabase");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();
//        byte[] picture = new byte[1];
//        picture[0] = 1;
//
//        Patient patient = new Patient("Sasho", "Mitroshanov", "Lagera", "mitro6anov3@abv.bg",
//                LocalDate.now(), picture, true);
//
//        entityManager.persist(patient);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
