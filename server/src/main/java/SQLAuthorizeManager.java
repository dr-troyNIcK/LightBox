import java.sql.*;

public class SQLAuthorizeManager implements AuthorizeManager {

    private Connection connection;
    private Statement statement;

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkLogin(String login) {
        String request = "SELECT * FROM main WHERE Login = '" + login + "'";
        try (ResultSet rs = statement.executeQuery(request)){
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}