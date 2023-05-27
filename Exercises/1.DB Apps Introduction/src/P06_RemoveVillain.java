import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P06_RemoveVillain {

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement selectVillain = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        selectVillain.setInt(1, villainId);

        ResultSet villainSet = selectVillain.executeQuery();

        if (!villainSet.next()) {
            System.out.println("No such villain was found");
            return;
        }

        String villainName = villainSet.getString("name");

        connection.setAutoCommit(false);

        PreparedStatement selectAllVillainMinions = connection.prepareStatement(
                "SELECT COUNT(minion_id) AS m_count FROM minions_villains WHERE villain_id = ?");
        selectAllVillainMinions.setInt(1, villainId);
        ResultSet minionsSet = selectAllVillainMinions.executeQuery();
        minionsSet.next();
        int countMinionsReleased = minionsSet.getInt("m_count");

        try {
            PreparedStatement deleteMinionsVillains = connection.prepareStatement(
                    "DELETE FROM minions_villains WHERE villain_id = ?");
            deleteMinionsVillains.setInt(1, villainId);
            deleteMinionsVillains.executeUpdate();

            PreparedStatement deleteVillain = connection.prepareStatement("DELETE FROM villains WHERE id = ?");
            deleteVillain.setInt(1, villainId);
            deleteVillain.executeUpdate();

            connection.commit();

            System.out.println(villainName + "  was deleted");
            System.out.println(countMinionsReleased + " minions released");

        } catch (SQLException e) {
            connection.rollback();
        }
    }
}
