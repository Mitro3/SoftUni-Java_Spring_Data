import java.sql.*;
import java.util.Properties;

public class P02_GetVillainsName {


    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement statement = connection.prepareStatement("SELECT name, count(distinct mv.minion_id) AS minion_count " +
                "FROM minions_db.villains AS v\n" +
                "join minions_villains AS mv ON mv.villain_id = v.id\n" +
                "group by mv.villain_id\n" +
                "having minion_count > 15\n" +
                "order by minion_count DESC;");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String villainName = resultSet.getString("name");
            int minionCount = resultSet.getInt("minion_count");
            System.out.println(villainName + " " + minionCount);
        }

        connection.close();
    }
}
