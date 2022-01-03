import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

class Bank {
    private final Connection con;
    private static int count_of_card = 0;
    Scanner scanner = new Scanner(System.in);

    public Bank(Connection con) {
        this.con = con;
        Service.createNewDataBase(con);
    }

    public void startMenu() {

        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        switch (scanner.nextInt()) {
            case 0:
                Service.exit(con);
            case 1:
                Account acc = new Account(++count_of_card, con);
                startMenu();
                break;
            case 2:
                Account account = Account.getAccount(con);
                if (account != null) {
                    System.out.println("You have successfully logged in!");
                    AccountMenu(account, con);
                } else {
                    System.out.println("Wrong card number or PIN!");
                }
                startMenu();
                break;
            default:
                System.out.println("invalid command");
                startMenu();
                break;
        }
    }

    public void AccountMenu(Account account, Connection con) {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        switch (scanner.nextInt()) {
            case 0:
                Service.exit(con);
            case 1:
                System.out.println("Balance: " + account.getBalance(con) + "\n");
                AccountMenu(account, con);
            case 2:
                Service.addIncome(account, con);
                AccountMenu(account, con);
            case 3:
                doTransfer(account, con);
                AccountMenu(account, con);
            case 4:
                Service.closeAccount(account, con);
            case 5:
                System.out.println("You have successfully logged out!\n");
                startMenu();
        }
    }

    private void doTransfer(Account account, Connection con) {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String recipient = scanner.next();

        if (account.getNumber().equals(recipient)) {
            System.out.println("You can't transfer money to the same account!\n");
            AccountMenu(account, con);
        } else if (Long.parseLong(recipient) % 10 != Service.lunaDigit(Arrays.copyOfRange(recipient.toCharArray(), 0, 15))) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
            AccountMenu(account, con);
        } else if (!Service.isNumber(recipient, con)) {
            System.out.println("Such a card does not exist.\n");
            AccountMenu(account, con);
        } else {
            try (PreparedStatement send = con.prepareStatement(Constants.SQL_DECREASE_BALANCE);
                 PreparedStatement get = con.prepareStatement(Constants.SQL_ADD_BALANCE)) {
                System.out.println("Enter how much money you want to transfer:");
                int money = scanner.nextInt();
                if (account.getBalance(con) < money) {
                    System.out.println("Not enough money!\n");
                    AccountMenu(account, con);
                }

                send.setInt(1, money);
                send.setString(2, account.getNumber());
                send.executeUpdate();

                get.setInt(1, money);
                get.setString(2, recipient);
                get.executeUpdate();
                System.out.println("Income was added!\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}