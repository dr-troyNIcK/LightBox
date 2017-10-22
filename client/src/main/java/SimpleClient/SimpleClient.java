package SimpleClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    Scanner scanner = new Scanner(System.in);

    File f = new File("D:\\documents\\GeekUniversity\\!EducationalProjects\\light_box\\client\\src\\vasilyev\\alexey\\client\\1.txt");

    public SimpleClient() {
        try (
                Socket socket = new Socket("127.0.0.1", 8189);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                FileInputStream fin = new FileInputStream(f)
        ) {
            System.out.println("клиент подключился к серверу");
            int i;
            while (true) {
                i = fin.read();
                if (i == -1) break;
                out.write(i);
            }
            System.out.println("файл принят");
//            while (true) {
//                System.out.print("введите запрос: ");
//                String request = scanner.nextLine();
//                out.writeUTF(request);
//                out.flush();
//                if (request.equals("выход")) break;
//                System.out.println(in.readUTF()); //answer
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
