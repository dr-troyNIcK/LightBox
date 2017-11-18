import java.net.Socket;

class ClientSocketThread extends SocketThread {

    private boolean isAuthorized;
    private String login;

    ClientSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    boolean isAuthorized (){
        return isAuthorized;
    }

    String getLogin() {
        return login;
    }

    void setLogin(String login) {
        this.login = login;
    }

    void messageFormatError(){
        sendMessageObject(MessageObject.getMsgFormatError());
        close();
    }

    void authError(String authError) {
        sendMessageObject(MessageObject.getAuthError(authError));
        close();
    }

    void authorizeAccept() {
        this.isAuthorized = true;
        sendMessageObject(MessageObject.getAuthAccept());
    }

}