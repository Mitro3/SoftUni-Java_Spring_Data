
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _11_FindEmployeesByFirstName {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String namePattern = scanner.nextLine();

        entityManager.createQuery("SELECT e FROM Employee e" +
                " WHERE e.firstName LIKE :pattern", Employee.class)
                .setParameter("pattern", namePattern + "%")
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                        e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary()));

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
