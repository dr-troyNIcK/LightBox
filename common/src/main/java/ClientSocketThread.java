import java.net.Socket;

class ClientSocketThread extends SocketThread {

    private boolean isAuthorized;
    private String login;

    ClientSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    boolean isAuthorized() {
        return isAuthorized;
    }

    String getLogin() {
        return login;
    }

    void messageFormatError() {
        sendMessageObject(MessageObject.getMsgFormatError());
        close();
    }

    void authError(String authError) {
        sendMessageObject(MessageObject.getAuthError(authError));
        close();
    }

    void authorizeAccept(String login) {
        this.isAuthorized = true;
        this.login = login;
        sendMessageObject(MessageObject.getAuthAccept());
    }

    void filesList(String[] filesList) {
        sendMessageObject(MessageObject.getFilesListObject(filesList));
    }

}