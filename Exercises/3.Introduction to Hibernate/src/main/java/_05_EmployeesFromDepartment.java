import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class _05_EmployeesFromDepartment {

    private static final String DEPARTMENT_NAME = "Research and Development";

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Employee> resultList = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :departmentName " +
                        "ORDER BY e.salary ASC, e.id ASC",
                        Employee.class)
                .setParameter("departmentName", DEPARTMENT_NAME)
                .getResultList();

        resultList.forEach(e -> System.out.printf("%s %s from %s - $%.2f%n",
                e.getFirstName(), e.getLastName(), DEPARTMENT_NAME, e.getSalary()));

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
