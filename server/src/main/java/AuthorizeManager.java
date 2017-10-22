public interface AuthorizeManager {
    void connect();
    void disconnect();
    boolean checkLogin(String login);
}