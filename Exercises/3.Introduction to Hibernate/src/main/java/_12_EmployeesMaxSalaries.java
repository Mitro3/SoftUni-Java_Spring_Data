
import entities.Department;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class _12_EmployeesMaxSalaries {

    private static final String SELECT_QUERY = "SELECT e.department.name, MAX(e.salary) AS maxSalary " +
            "FROM Employee AS e " +
            "WHERE e.salary NOT BETWEEN 30000 AND 70000" +
            "GROUP BY e.department ";

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Object[]> resultList = entityManager.createQuery(SELECT_QUERY, Object[].class)
                .getResultList();

        for (Object[] objects : resultList) {
            System.out.printf("%s %.2f%n", objects[0], Double.valueOf(objects[1].toString()));
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
