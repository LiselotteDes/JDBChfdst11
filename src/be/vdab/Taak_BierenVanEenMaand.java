package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Taak_BierenVanEenMaand {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_BIEREN_VAN_MAAND = 
            "select verkochtsinds, naam from bieren" +
            " where {fn month(verkochtsinds)} = ?" +
            " order by verkochtsinds";
    public static void main(String[] args) {
        System.out.print("Maandnummer (getal tussen 1 en 12): ");
        try (Scanner scanner = new Scanner(System.in)) {
            int maand = scanner.nextInt();
            while ( !(maand >= 1 && maand <= 12) ){
                System.out.println("Geef een getal tussen 1 en 12");
                maand = scanner.nextInt();
            }
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement statement = connection.prepareStatement(SELECT_BIEREN_VAN_MAAND)) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                statement.setInt(1, maand);
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Aantal geretourneerde rijen weergeven
                    resultSet.last();
                    System.out.println("Aantal resultaten: " + resultSet.getRow());
                    // Cursor terug vooraan plaatsen om te kunnen itereren over de rijen
                    resultSet.beforeFirst();
                    while (resultSet.next()){
                        System.out.println(resultSet.getDate("verkochtsinds") + " " +
                                resultSet.getString("naam"));
                    }
                }
                connection.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
