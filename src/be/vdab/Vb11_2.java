package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
/*
Een datum als parameter.
Voorbeeld: een lijst van werknemers die vanaf een ingetikte datum in dienst kwamen.
*/
public class Vb11_2 {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_VANAF_DATUM = 
            "select indienst, voornaam, familienaam from werknemers" +
            " where indienst >= ? order by indienst";
    public static void main(String[] args) {
        System.out.print("Datum vanaf (dd/mm/yyyy): ");
        try (Scanner scanner = new Scanner(System.in)) {
            /*
            Een DateTimeFormatter object helpt een String te converteren naar een LocalDate.
            De constructor heeft een String parameter met de datumopmaak.
            d betekent dag, M betekent maand, y betekent jaar.
            */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y");
            /*
            De parse method converteert een String naar een LocalDate.
            De eerste parameter is de te converteren String.
            De tweede parameter is een DateTimeFormatter object met de datumopmaak.
            Als de conversie mislukt, werpt Java een DateTimeParseException, die je onder in de source opvangt.
            */
            LocalDate datum = LocalDate.parse(scanner.nextLine(), formatter);
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement statement = connection.prepareStatement(SELECT_VANAF_DATUM)) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                /*
                De method setDate verwacht een java.sql.Date.
                Je converteert de LocalDate naar dit type met de static method valueOf. 
                De parameter is een LocalDate.
                De returnwaarde is een java.sql.Date.
                */
                statement.setDate(1, java.sql.Date.valueOf(datum));
                try (ResultSet resultSet = statement.executeQuery()) {
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
        } catch (DateTimeException ex) {
            System.out.println("Verkeerde datum");
        }
    }
    
}
