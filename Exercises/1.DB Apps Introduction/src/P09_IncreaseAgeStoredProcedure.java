import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class P09_IncreaseAgeStoredProcedure {

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);


        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] minionsIds = Arrays.stream(input.split("\s+")).mapToInt(Integer::parseInt).toArray();

        for (int id = 0; id < minionsIds.length; id++) {
            CallableStatement increaseMinionAgeByGivenId = connection.prepareCall("CALL usp_get_older(?)");
            increaseMinionAgeByGivenId.setInt(1, minionsIds[id]);
            increaseMinionAgeByGivenId.execute();

            PreparedStatement getUpdatedMinionInfo =
                    connection.prepareStatement("SELECT name, age FROM minions WHERE id = ?");
            getUpdatedMinionInfo.setInt(1 , minionsIds[id]);
            ResultSet resultSet = getUpdatedMinionInfo.executeQuery();
            resultSet.next();

            String minionName = resultSet.getString("name");
            int minionAge = resultSet.getInt("age");
            System.out.println(minionName + " " + minionAge);
        }
    }
}
