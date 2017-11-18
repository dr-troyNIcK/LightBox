import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketThread extends Thread {

    private final SocketThreadListener eventListener;
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public SocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(name);
        this.eventListener = eventListener;
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        eventListener.onStartSocketThread(this);
        try {
            //если поменять местами следующие 2 строки, то objectInputStream не создастся
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            eventListener.onReadySocketThread(this, socket);
            while (!isInterrupted()) {
                try {
                    MessageObject messageObject = (MessageObject) objectInputStream.readObject();
                    eventListener.onReceiveMessageObject(this, socket, messageObject);
                } catch (ClassNotFoundException e) {
                    eventListener.onExceptionSocketThread(this, socket, e);
                    continue;
                }
            }
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this, socket, e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                eventListener.onExceptionSocketThread(this, socket, e);
            }
            eventListener.onStopSocketThread(this);
        }
    }

    public synchronized void sendMessageObject(MessageObject messageObject) {
        try {
            objectOutputStream.writeObject(messageObject);
            objectOutputStream.flush();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this, socket, e);
            close();
        }
    }

    public synchronized void close() {
        interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this, socket, e);
        }
    }

}