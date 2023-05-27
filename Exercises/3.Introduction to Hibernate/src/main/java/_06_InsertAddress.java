import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class _06_InsertAddress {

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String lastName = scanner.nextLine();

        String addressText = "Vitoshka 15";
        Address newAddress = new Address();
        newAddress.setText(addressText);
        entityManager.persist(newAddress);

        Employee employee = entityManager
                .createQuery("SELECT e FROM Employee e " +
                                "WHERE e.lastName = :name",
                        Employee.class)
                .setParameter("name", lastName)
                .getSingleResult();

        employee.setAddress(newAddress);
        entityManager.persist(employee);

        // Second option for updating entity:
//        entityManager.createQuery("UPDATE Employee e" +
//                " SET e.address = :address" +
//                " WHERE e.lastName = :name")
//                .setParameter("address", newAddress)
//                .setParameter("name", lastName)
//                .executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
