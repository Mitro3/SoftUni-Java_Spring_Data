import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class P08_IncreaseMinionsAge {
    private static final String INCREASE_MINION_AGE_FOR_GIVEN_ID = "UPDATE minions SET age = age + 1 WHERE id = ?;";
    private static final String CHANGE_NAME_TO_LOWER_CASE = "UPDATE minions SET name = LOWER(name) WHERE id = ?";
    private static final String GET_ALL_MINIONS = "SELECT name, age FROM minions";

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] minionsIds = Arrays.stream(input.split("\s+")).mapToInt(Integer::parseInt).toArray();

        for (int id = 0; id < minionsIds.length; id++) {
            PreparedStatement increaseMinionAgeForGivenID =
                    connection.prepareStatement(INCREASE_MINION_AGE_FOR_GIVEN_ID);
            increaseMinionAgeForGivenID.setInt(1, minionsIds[id]);
            increaseMinionAgeForGivenID.executeUpdate();

            PreparedStatement changeNameToLowerCase =
                    connection.prepareStatement(CHANGE_NAME_TO_LOWER_CASE);
            changeNameToLowerCase.setInt(1, minionsIds[id]);
            changeNameToLowerCase.executeUpdate();
        }

        PreparedStatement getAllMinion = connection.prepareStatement(GET_ALL_MINIONS);
        ResultSet minionSet = getAllMinion.executeQuery();

        while (minionSet.next()) {
            String minionName = minionSet.getString("name");
            int minionAge = minionSet.getInt("age");
            System.out.println(minionName + " " + minionAge);
        }

    }
}
