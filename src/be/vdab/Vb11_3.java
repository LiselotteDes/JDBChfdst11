package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
Datum en tijd functies.
Voorbeeld: Een lijst van werknemers die in de huidige maand jarig zijn.
Een werknemer komt voor in de lijst als de maand van zijn geboortedatum {fn month(geboorte)}
gelijk is aan de maand van de systeemdatum {fn month(fn curdate()})}
*/
public class Vb11_3 {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_JARIGEN_VAN_DE_MAAND =
            "select geboorte, voornaam, familienaam from werknemers " +
            "where {fn month(geboorte)} = {fn month({fn curdate()})} " +
            "order by {fn dayofmonth(geboorte)}";
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery(SELECT_JARIGEN_VAN_DE_MAAND)) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getDate("geboorte") + " " +
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
