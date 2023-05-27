
import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.beans.PropertyEditorSupport;
import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

public class _10_IncreaseSalaries {
    private static final String DEPARTMENTS_NAMES = "'Information Services', 'Marketing', 'Tool Design', 'Engineering'";

    private static final String UPDATE_SALARIES_IN_GIVEN_DEPARTMENTS_BY_12_PERCENT = "UPDATE Employee e" +
            " SET e.salary = e.salary * 1.12 WHERE e.department.id in (1, 2, 14,3)";
    private static final String SELECT_EMPLOYEES_WITH_UPDATED_BY_12_PERCENT_SALARIES = "SELECT e FROM Employee AS e" +
            " WHERE e.department.name IN (" + DEPARTMENTS_NAMES + ")";

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        int countUpdatedEmployees = 0;

        countUpdatedEmployees = entityManager.createQuery(UPDATE_SALARIES_IN_GIVEN_DEPARTMENTS_BY_12_PERCENT)
                .executeUpdate();

        if (countUpdatedEmployees != 0) {
            entityManager.createQuery(SELECT_EMPLOYEES_WITH_UPDATED_BY_12_PERCENT_SALARIES,
                            Employee.class)
                    .getResultList()
                    .forEach(e -> System.out.printf("%s %s ($%.2f)%n",
                            e.getFirstName(), e.getLastName(), e.getSalary()));
        } else {
            entityManager.getTransaction().rollback();
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
