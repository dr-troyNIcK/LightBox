import java.net.Socket;

public interface SocketThreadListener {
    void onStartSocketThread(SocketThread socketThread);
    void onStopSocketThread(SocketThread socketThread);

    void onReadySocketThread(SocketThread socketThread, Socket socket);
    void onReceiveMessageObject(SocketThread socketThread, Socket socket, MessageObject messageObject);

    void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e);
}