package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
Een datum of tijd letterlijk schrijven in een SQL statement.
Voorbeeld: een lijst van werknemers die vanaf 2001 in dienst kwamen;
*/
public class Vb11_1 {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_VANAF_2001 = 
            "select indienst, voornaam, familienaam" + 
            " from werknemers where indienst >= {d '2001-1-1'} order by indienst";
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery(SELECT_VANAF_2001)) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getDate("indienst") + " " +
                                        resultSet.getString("voornaam") + " " +
                                        resultSet.getString("familienaam"));
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
