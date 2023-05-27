import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class _13_DeleteTowns {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String townName = scanner.nextLine();

        Town town = entityManager.createQuery("SELECT t FROM Town AS t WHERE t.name = :tName", Town.class)
                .setParameter("tName", townName)
                .getSingleResult();

        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address AS a WHERE a.town.name = :tName", Address.class)
                .setParameter("tName", townName)
                .getResultList();

        addresses.forEach(a -> {
            for (Employee employee : a.getEmployees()) {
                employee.setAddress(null);
            }

            a.setTown(null);
            entityManager.remove(a);
        });

        entityManager.remove(town);

        entityManager.getTransaction().commit();

        System.out.printf("%d address%s in %s deleted%n",
                addresses.size(),
                addresses.size() != 1 ? "es" : "",
                town.getName());

        entityManager.close();
    }
}
