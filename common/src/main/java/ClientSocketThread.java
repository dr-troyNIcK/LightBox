import java.net.Socket;

public class ClientSocketThread extends SocketThread {

    private boolean isAuthorized;

    public ClientSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    boolean isAuthorized (){
        return isAuthorized;
    }

    void messageFormatError(MessageObject messageObject){
        sendMessageObject(messageObject.getMsgFormatError());
        close();
    }

    void authError(MessageObject messageObject, String login) {
        sendMessageObject(messageObject.getAuthError(login));
        close();
    }

    void authorizeAccept(MessageObject messageObject, String login) {
        this.isAuthorized = true;
        sendMessageObject(messageObject.getAuthAccept(login));
    }

}