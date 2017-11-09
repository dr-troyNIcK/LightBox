import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Server implements ServerSocketThreadListener, SocketThreadListener {

    private final ServerListener eventListener;
    private final AuthorizeManager authorizeManager;
    private ServerSocketThread serverSocketThread;

    private final DateFormat dataFormat = new SimpleDateFormat("hh:mm:ss: ");

    private final Vector<SocketThread> clients = new Vector<>();

    public Server(ServerListener eventListener, AuthorizeManager authorizeManager) {
        this.eventListener = eventListener;
        this.authorizeManager = authorizeManager;
    }

    public void startListening(String name, int port, int timeout) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            putLog("Server is already starting");
            return;
        }
        serverSocketThread = new ServerSocketThread(this, name, port, timeout);
        authorizeManager.connect();
    }

    public void stopListening() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            putLog("Server is already stopped");
            return;
        }
        serverSocketThread.interrupt();
        authorizeManager.disconnect();
    }

    private synchronized void putLog(String msg) {
        msg = dataFormat.format(System.currentTimeMillis()) + "[" + Thread.currentThread().getName() + "]: " + msg;
        eventListener.onServerLog(msg, this);
    }

    //ServerSocketThreadListener
    @Override
    public void onStartServerSocketThread(ServerSocketThread thread) {
        putLog("Server started");
    }

    @Override
    public void onStopServerSocketThread(ServerSocketThread thread) {
        putLog("Server is stopped");
    }

    @Override
    public void onReadyServerSocketThread(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("Server socket is ready");
    }

    @Override
    public void onTimeOutAccept(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("Timeout");
    }

    @Override
    public void onAcceptedSocket(ServerSocketThread thread, ServerSocket serverSocket, Socket socket) {
        putLog("Client connected: " + socket);
        String threadName = "Socket thread: " + socket.getInetAddress() + ":" + socket.getPort();
        new SocketThread(this, threadName, socket);
    }

    @Override
    public void onExceptionServerSocketThread(ServerSocketThread thread, Exception e) {
        putLog("Exception [" + e.getClass().getName() + "]: " + e.getMessage());
    }

    //SocketThreadListener
    @Override
    public synchronized void onStartSocketThread(SocketThread socketThread) {
        putLog("Socket started");
    }

    @Override
    public synchronized void onStopSocketThread(SocketThread socketThread) {
        clients.remove(socketThread);
        putLog("Socket stopped");
    }

    @Override
    public synchronized void onReadySocketThread(SocketThread socketThread, Socket socket) {
        clients.add(socketThread);
        putLog("Socket is ready");

    }

    @Override
    public synchronized void onReceiveObject(SocketThread socketThread, Socket socket, Object object) {
        socketThread.sendObject(object);
    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e) {
        putLog("Exception [" + e.getClass().getName() + "]: " + e.getMessage());
    }

}