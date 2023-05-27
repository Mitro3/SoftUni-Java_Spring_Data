import java.sql.*;
import java.util.Properties;

public class P07_PrintAllMinionNames {

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

       PreparedStatement getMinionsSize = connection.prepareStatement(
                "SELECT COUNT(id) AS count FROM minions");

        ResultSet minionsSet = getMinionsSize.executeQuery();
        minionsSet.next();

        int setSize = minionsSet.getInt("count");

        String[] minionsNames = new String[setSize];

        PreparedStatement getAllMinionsNames = connection.prepareStatement(
                "SELECT name FROM minions ORDER BY id");

        minionsSet = getAllMinionsNames.executeQuery();

        for (int i = 0; i < setSize; i++) {
            minionsSet.next();
            minionsNames[i] = minionsSet.getString("name");
        }

        for (int i = 0; i < setSize; i++) {
            setSize--;
            System.out.println(minionsNames[i]);
            if (i != setSize) {
                System.out.println(minionsNames[setSize]);
            }
        }
    }
}
