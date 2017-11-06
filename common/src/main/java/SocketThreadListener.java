import java.net.Socket;

public interface SocketThreadListener {
    void onStartSocketThread(SocketThread socketThread);
    void onStopSocketThread(SocketThread socketThread);

    void onReadySocketThread(SocketThread socketThread, Socket socket);
    void onReceiveObject(SocketThread socketThread, Socket socket, Object object);

    void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e);
}