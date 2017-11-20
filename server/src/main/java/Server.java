import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
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
        new ClientSocketThread(this, threadName, socket);
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
        //добавить проверку на подключене под одним логином более одного клиента
        clients.add(socketThread);
        putLog("Socket is ready");

    }

    @Override
    public synchronized void onReceiveMessageObject(SocketThread socketThread, Socket socket, MessageObject messageObject) {
        ClientSocketThread clientSocketThread = (ClientSocketThread) socketThread;
        if (clientSocketThread.isAuthorized()) {
            handleAuthorizeClient(clientSocketThread, messageObject);
        } else {
            handleNonAuthorizeClient(clientSocketThread, messageObject);
        }
    }

    private void handleAuthorizeClient(ClientSocketThread client, MessageObject messageObject) {
        if (messageObject instanceof FileAddObject) {
            String fileName = ((FileAddObject) messageObject).getFileName();
            long fileSize = ((FileAddObject) messageObject).getFileSize();
            byte[] file = ((FileAddObject) messageObject).getFile();

            File dir = new File(client.getLogin());
            dir.mkdir();
            File fileForSave = new File(dir, fileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileForSave)) {
                fileOutputStream.write(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (messageObject instanceof FileDeleteObject) {
            String fileName = ((FileDeleteObject) messageObject).getFileName();
            File file = new File(client.getLogin(), fileName);
            System.out.println(file.delete());
        }
        if (messageObject instanceof FileCopyObject) {
            String fileName = ((FileCopyObject) messageObject).getFileName();
            File file = new File(client.getLogin(), fileName);
            long fileSize = file.length();
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.sendMessageObject(MessageObject.getFileAddObject(fileName, fileSize, bytes));
        }
        sendFileListToClient(client);
    }

    private void sendFileListToClient(ClientSocketThread client) {
        String clientRepositoryName = client.getLogin();
        File dir = new File(clientRepositoryName);
        String[] filesList = dir.list();
        client.filesList(filesList);
    }

    private void handleNonAuthorizeClient(ClientSocketThread client, MessageObject messageObject) {
        if (!(messageObject instanceof AuthRequestObject)) {
            client.messageFormatError();
            putLog("Error object message format: " + messageObject);
            return;
        }
        String login = ((AuthRequestObject) messageObject).getLogin();
        String password = ((AuthRequestObject) messageObject).getPassword();
        if (!authorizeManager.checkLogin(login, password)) {
            client.authError("Client is not registered");
            putLog(login + " is not registered");
            return;
        }
        client.authorizeAccept(login);
        putLog(login + " authorized");
    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e) {
        putLog("Exception [" + e.getClass().getName() + "]: " + e.getMessage());
    }

}