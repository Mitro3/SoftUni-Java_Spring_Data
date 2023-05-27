
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _03_CheckEmployeeExists {

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String[] nameToSearch = scanner.nextLine().split("\\s+");

        Long employeeCount = entityManager
                .createQuery("SELECT COUNT(e) FROM Employee e " +
                        "WHERE e.firstName = :first_name AND e.lastName = :last_name", Long.class)
                .setParameter("first_name", nameToSearch[0])
                .setParameter("last_name", nameToSearch[1])
                .getSingleResult();

        String employeePresence = employeeCount > 0 ? "YES" : "NO";
        System.out.println(employeePresence);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
