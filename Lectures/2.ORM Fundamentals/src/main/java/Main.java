import entities.Account;
import entities.Student;
import entities.User;
import orm.Connector;
import orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Connector.createConnection("root", "1234", "soft_uni");
        Connection connection = Connector.getConnection();

        EntityManager<User> userManager = new EntityManager<>(connection);
//        User firstUser = new User("First", 28, LocalDate.now());
//        firstUser.setId(1L);
//        userManager.persist(firstUser);
//
//        User secondUser = new User("Second", 16, LocalDate.now());
//        userManager.persist(secondUser);
//
//        firstUser.setAge(12);
//        userManager.persist(firstUser);

//
//        EntityManager<Student> studentManager = new EntityManager<>(connection);
//        Student student = new Student("Pesho");
//        studentManager.persist(student);

//        User firstUser = userManager.findFirst(User.class);
//
//        System.out.println(firstUser.getId() + " " + firstUser.getUsername());
//
//        userManager
//                .find(User.class, "`age` > 18 AND registration_date > '2022-06-06'")
//                .forEach(u -> System.out.println(u.toString()));
//
//        EntityManager<Account> accountManager = new EntityManager<>(connection);
////        accountManager.doCreate(Account.class);
//        accountManager.doAlter(Account.class);

        User user = new User();
        user.setId(1L);
        userManager.doDelete(user);

        System.out.println();
    }
}
