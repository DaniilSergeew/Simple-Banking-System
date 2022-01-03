public class Constants {
    public static final String BANK_IDENTIFICATION_NUMBER = "400000";

    public static final String SQL_URL = "jdbc:sqlite:";

    public static final String SQL_INSERT_CARD = "INSERT INTO card (number, pin) VALUES (?, ?)";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS card(" +
            "id      INTEGER," +
            "number  VARCHAR(16)," +
            "pin     VARCHAR(4)," +
            "balance INTEGER DEFAULT 0)";

    public static final String SQL_DELETE_ACCOUNT = "DELETE FROM card WHERE number=?";

    public static final String SQL_DECREASE_BALANCE = ("UPDATE card " +
            "SET balance = balance-?" +
            "WHERE number=?");

    public static final String SQL_ADD_BALANCE = ("UPDATE card " +
            "SET balance = balance+?" +
            "WHERE number=?");
    public static final String SQL_GET_BALANCE = "SELECT balance FROM card WHERE number=?";
    public static final String SQL_IS_NUMBER = "SELECT number FROM card WHERE number=?";
}