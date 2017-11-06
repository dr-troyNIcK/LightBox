import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketThread extends Thread {

    private final SocketThreadListener eventListener;
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

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
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            eventListener.onReadySocketThread(this, socket);
            while (!isInterrupted()) {
                try {
                    Object object = objectInputStream.readObject();
                    eventListener.onReceiveObject(this, socket, object);
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

    //кидаемся сериализованными объектами
    public synchronized void sentObject(Object object) {
        try {
            objectOutputStream.writeObject(object);
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