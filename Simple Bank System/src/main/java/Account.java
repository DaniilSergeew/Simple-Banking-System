import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Account {

    private final String number;
    private final String pin;
    static Scanner scanner = new Scanner(System.in);

    Account(int count_of_card, Connection con) {
        this.number = Service.generateNumber(count_of_card);
        this.pin = Service.generatePin();
        Service.insertCard(this.number, this.pin, con);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(this.number);
        System.out.println("Your card PIN:");
        System.out.println(this.pin);
    }

    private Account(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }


    public static Account getAccount(Connection con) {

        System.out.println("Enter your card number:");
        String number = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        try (final PreparedStatement statement = con.prepareStatement("SELECT number, pin FROM card WHERE number=? AND pin=?")) {

            statement.setString(1, number);
            statement.setString(2, pin);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Account(number, pin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance(Connection con) {
        try (PreparedStatement statement = con.prepareStatement(Constants.SQL_GET_BALANCE)) {

            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("balance");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}