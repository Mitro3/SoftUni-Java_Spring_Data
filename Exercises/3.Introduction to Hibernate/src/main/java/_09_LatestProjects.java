
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

public class _09_LatestProjects {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Project> resultList = entityManager.createQuery("SELECT p FROM Project p" +
                        " WHERE p.endDate = null" +
                        " ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList()
                .stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());

        resultList.forEach(p -> System.out.println(p.toString()));

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
