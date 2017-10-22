package SimpleServer;

import java.io.*;
import java.net.Socket;

public class ClientHandler {

    Socket socket;
    SimpleServer server;

    public ClientHandler(Socket socket, SimpleServer server) {
        this.socket = socket;
        this.server = server;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                ) {
                    String userName = "Alex";
                    File dir = new File("D:\\documents\\GeekUniversity\\!EducationalProjects\\light_box\\server\\src\\vasilyev\\alexey\\server\\" + userName);
                    dir.mkdir();
                    String fName = "1.txt";
                    File f = new File(dir, fName);
                    FileOutputStream fout = new FileOutputStream(f);
                    int i;
                    while (true) {
                        i = in.read();
                        if (i == -1) break;
                        fout.write(i);
                    }
                    System.out.println("файл принят");
//                    while (true) {
//                        String request = in.readUTF();
//                        System.out.println("запрос клиента: " + request);
//                        if (request.equals("выход")) break;
//                        out.writeUTF("сервер ответил"); //answer
//                        out.flush();
//                        System.out.println("ответ клиенту отправлен");
//                    }
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
        }).start();

    }

}
