import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Service {
    static Scanner scanner = new Scanner(System.in);

    public static void createNewDataBase(Connection con) {
        try (final Statement statement = con.createStatement()) {
            statement.executeUpdate(Constants.SQL_CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void exit(Connection con) {
        System.out.println("Bye!");
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static String generatePin() {
        int num = ThreadLocalRandom.current().nextInt(9999);
        return String.format("%04d", num);
    }

    public static int lunaDigit(char[] number) {
        int[] tmp = new int[number.length];
        for (int i = 0; i < number.length; i++) {
            tmp[i] = i % 2 == 0 ? Integer.parseInt(String.valueOf(number[i])) * 2 : Integer.parseInt(String.valueOf(number[i]));
        }
        int sum = 0;
        for (int i = 0; i < number.length; i++) {
            if (tmp[i] > 9) {
                tmp[i] -= 9;
            }
            sum += tmp[i];
        }
        return ((10 - sum % 10) % 10);
    }

    public static String generateNumber(int count_of_card) {
        StringBuilder stringBuilder = new StringBuilder(Constants.BANK_IDENTIFICATION_NUMBER);
        int add = String.valueOf(count_of_card).length();
        StringBuilder bin = new StringBuilder("000000000");
        bin.delete(bin.length() - add, bin.length());
        bin.append(count_of_card);
        stringBuilder.append(bin);
        stringBuilder.append(lunaDigit(stringBuilder.toString().toCharArray()));
        return stringBuilder.toString();
    }

    public static void insertCard(String number, String pin, Connection con) {
        try (final PreparedStatement preparedStatement = con.prepareStatement(Constants.SQL_INSERT_CARD)) {
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, pin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncome(Account account, Connection con) {
        try (final PreparedStatement preparedStatement = con.prepareStatement(Constants.SQL_ADD_BALANCE)) {
            System.out.println("Enter income:");
            int income = scanner.nextInt();
            preparedStatement.setInt(1, income);
            preparedStatement.setString(2, account.getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeAccount(Account account, Connection con) {
        try (final PreparedStatement preparedStatement = con.prepareStatement(Constants.SQL_DELETE_ACCOUNT)) {
            preparedStatement.setString(1, account.getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("The account has been closed!\n");
    }

    public static boolean isNumber(String number, Connection con) {
        try (PreparedStatement statement = con.prepareStatement(Constants.SQL_IS_NUMBER)) {
            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}