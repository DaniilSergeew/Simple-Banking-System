import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class Main {
    public static void main(String[] args) {
        String command = null;
        String argument = null;

        try {
            command = args[0];
            argument = args[1];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        if (command.equals("-fileName")) {
            String url = Constants.SQL_URL + argument;

            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(url);

            try (final Connection con = dataSource.getConnection()) {
                Bank bank = new Bank(con);
                bank.startMenu();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid command.");
        }
    }
}