import java.net.Socket;

public class ClientSocketThread extends SocketThread {

    private boolean isAuthorized;

    public ClientSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    boolean isAuthorized (){
        return isAuthorized;
    }

    void messageFormatError(){
        sendMessageObject(MessageObject.getMsgFormatError());
        close();
    }

    void authError() {
        sendMessageObject(MessageObject.getAuthError());
        close();
    }

    void authorizeAccept() {
        this.isAuthorized = true;
        sendMessageObject(MessageObject.getAuthAccept());
    }

}