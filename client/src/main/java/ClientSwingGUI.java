import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSwingGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int POS_X_FRAME = 450;
    private static final int POS_Y_FRAME = 50;
    private static final int WIDTH_FRAME = 400;
    private static final int HEIGHT_FRAME = 600;
    private static final String TITLE = "LightBox";

    private final JPanel northPanel = new JPanel(new GridLayout(2, 1));
    private final JPanel panelAddress = new JPanel(new GridLayout(1, 2));
    private final JTextField fieldIP = new JTextField("127.0.0.1");
    private final JTextField fieldPort = new JTextField("8189");
    private final JPanel panelAuthorization = new JPanel(new GridLayout(1, 3));
    private final JTextField fieldLogin = new JTextField("login1");
    private final JPasswordField fieldPassword = new JPasswordField("pass1");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel southPanel = new JPanel(new GridLayout(1, 3));
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnDel = new JButton("Del");
    private final JButton btnCopy = new JButton("Copy");

    private final JPanel centralPanel = new JPanel(new GridLayout(2, 1));

    private final DefaultListModel<String> serverFilesListModel = new DefaultListModel<>();
    private final JList<String> serverFilesList = new JList<>(serverFilesListModel);
    private final JScrollPane scrollPaneServerFilesList = new JScrollPane(serverFilesList);

    private final JTextArea log = new JTextArea();
    private final JScrollPane scrollPaneLog = new JScrollPane(log);

    private final JFileChooser addChooser = new JFileChooser();
    private final JFileChooser copyChooser = new JFileChooser();


//    private String selectedFile;
//    private int selectedFileIndex;

    private SocketThread socketThread;

    private ClientSwingGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                disconnect();
            }
        });

        setTitle(TITLE);
        setLocation(POS_X_FRAME, POS_Y_FRAME);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);

        add(northPanel, BorderLayout.NORTH);
        northPanel.add(panelAddress);
        northPanel.add(panelAuthorization);
        panelAddress.add(fieldIP);
        panelAddress.add(fieldPort);
        panelAuthorization.add(fieldLogin);
        panelAuthorization.add(fieldPassword);
        panelAuthorization.add(btnLogin);

        fieldIP.addActionListener(this);
        fieldPort.addActionListener(this);

        fieldLogin.addActionListener(this);
        fieldPassword.addActionListener(this);
        btnLogin.addActionListener(this);

        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(btnAdd);
        southPanel.add(btnDel);
        southPanel.add(btnCopy);
        southPanel.setVisible(false);

        btnAdd.addActionListener(this);
        btnDel.addActionListener(this);
        btnCopy.addActionListener(this);

        add(centralPanel, BorderLayout.CENTER);
        centralPanel.add(scrollPaneServerFilesList);
        centralPanel.add(scrollPaneLog);

        //scrollPaneLog.setVisible(false);

        log.setEditable(false);
        log.setLineWrap(true);

        serverFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        serverFilesList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                selectedFile = serverFilesList.getSelectedValue();
//                selectedFileIndex = serverFilesList.getSelectedIndex();
//                System.out.println(selectedFile + " || " + selectedFileIndex);
//            }
//        });

        serverFilesListModel.addElement("file1.txt");
        serverFilesListModel.addElement("file2.txt");
        serverFilesListModel.addElement("file3.txt");
        serverFilesListModel.addElement("file4.txt");
        serverFilesListModel.addElement("file5.txt");

        setVisible(true);

    }

    //ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        if (object == fieldIP ||
                object == fieldPort ||
                object == fieldLogin ||
                object == fieldPassword ||
                object == btnLogin) connect();
        else if (object == btnAdd) addFile();
        else if (object == btnDel) delFile();
        else if (object == btnCopy) copyFile();
        else throw new RuntimeException("Unknown activity: " + object);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientSwingGUI();
            }
        });
    }

    void connect() {
        try {
            Socket socket = new Socket(fieldIP.getText(), Integer.parseInt(fieldPort.getText()));
            socketThread = new SocketThread(this, this.getClass().getName() + ": " + fieldLogin.getText(), socket);
        } catch (IOException e) {
            e.printStackTrace();
            log.append("Exception: " + e.getMessage() + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        }

    }

    void disconnect() {
        if (socketThread != null)
            socketThread.close();
    }

    void addFile() {
        int chooserAnswer = addChooser.showDialog(this, "Add");
        if (chooserAnswer == JFileChooser.APPROVE_OPTION) System.out.println("добавить файл");
        //отправляем объект через socket thread socketThread.sentObject(object);
        byte[] bytes = new byte[100];
        try (FileInputStream fileInputStream = new FileInputStream("1.txt")){
            int i;
            int count = 0;
            while (true) {
                i = fileInputStream.read();
                if (i == -1) break;
                bytes[count++] = (byte) i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        socketThread.sendMessageObject(MessageObject.getAddFileObject("1.txt", 12, bytes));
    }

    void delFile() {
        //serverFilesListModel.remove(serverFilesList.getSelectedIndex());
        //serverFilesListModel.removeElement(serverFilesList.getSelectedValue());
        //отправляем объект через socket thread socketThread.sentObject(object);
        //Object message = MessageObject.getAuthAnswer("login1");
        //socketThread.sendObject(message);
    }

    void copyFile() {
        int chooserAnswer = copyChooser.showDialog(this, "Copy");
        if (chooserAnswer == JFileChooser.APPROVE_OPTION) System.out.println("скопировать файл");
        //отправляем объект через socket thread socketThread.sentObject(object);
    }

    //Thread.UncaughtExceptionHandler
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String msg;
        if (stackTraceElements.length == 0) msg = "Empty stackTraceElements";
        else msg = e.getClass().getName() + ": " + e.getMessage() + "\n" + stackTraceElements[0];
        JOptionPane.showMessageDialog(this, msg, "Exception: ", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    //SocketThreadListener
    @Override
    public void onStartSocketThread(SocketThread socketThread) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Socket started\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void onStopSocketThread(SocketThread socketThread) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Socket stopped\n");
                log.setCaretPosition(log.getDocument().getLength());
                northPanel.setVisible(true);
                southPanel.setVisible(false);
            }
        });
    }

    @Override
    public void onReadySocketThread(SocketThread socketThread, Socket socket) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Socket is ready\n");
                log.setCaretPosition(log.getDocument().getLength());
                setTitle(fieldLogin.getText());
                northPanel.setVisible(false);
                southPanel.setVisible(true);

                String login = fieldLogin.getText();
                String password = String.valueOf(fieldPassword.getPassword());
                socketThread.sendMessageObject(MessageObject.getAuthRequest(login, password));
            }
        });
    }

    @Override
    public void onReceiveMessageObject(SocketThread socketThread, Socket socket, MessageObject messageObject) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(messageObject + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e) {
        e.printStackTrace();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Exception: " + e.getMessage() + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}