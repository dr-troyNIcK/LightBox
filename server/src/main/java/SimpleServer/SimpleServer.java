package SimpleServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

    Socket socket;

    public SimpleServer() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("сервер включен");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}