import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P03_GetMinionNames {

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");

        villainStatement.setInt(1, villainId);

        ResultSet villainSet = villainStatement.executeQuery();

        if (!villainSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            return;
        }

        String villainName = villainSet.getString("name");
        System.out.println("Villain: " + villainName);

        PreparedStatement minionStatement = connection.prepareStatement("SELECT name, age\n" +
                "FROM minions AS m\n" +
                "JOIN minions_villains AS mv ON mv.minion_id = m.id\n" +
                "WHERE mv.villain_id = ?;");

        minionStatement.setInt(1, villainId);

        ResultSet minionSet = minionStatement.executeQuery();

        for (int i = 0; minionSet.next(); i++) {
            String name = minionSet.getString("name");
            int age = minionSet.getInt("age");

            System.out.printf("%d. %s %d%n", i, name, age);

        }

        connection.close();
    }
}
