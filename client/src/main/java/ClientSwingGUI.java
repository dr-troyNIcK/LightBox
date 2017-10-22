import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientSwingGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

    private static final int POS_X_FRAME = 1200;
    private static final int POS_Y_FRAME = 150;
    private static final int WIDTH_FRAME = 400;
    private static final int HEIGHT_FRAME = 600;
    private static final String TITLE = "LightBox";

    private final JPanel northPanel = new JPanel(new GridLayout(2, 1));
    private final JPanel panelAddress = new JPanel(new GridLayout(1, 2));
    private final JTextField fieldIP = new JTextField("127.0.0.1");
    private final JTextField fieldPort = new JTextField("8189");
    private final JPanel panelAuthorization = new JPanel(new GridLayout(1, 3));
    private final JTextField fieldLogin = new JTextField("root");
    private final JPasswordField fieldPassword = new JPasswordField("root");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel southPanel = new JPanel(new GridLayout(1, 3));
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnDel = new JButton("Del");
    private final JButton btnCopy = new JButton("Copy");

    private final JFileChooser addChooser = new JFileChooser();
    private final JFileChooser copyChooser = new JFileChooser();

    private final JList<String> serverFilesList = new JList<>();
    private final JScrollPane scrollPaneServerFilesList = new JScrollPane(serverFilesList);

    private String[] list = {"fildddddddddddddddddddddddddddddddddddddddddddddddddddde.txt", "file.txt", "file.txt", "file.txt", "file.txt"};

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
        southPanel.add(btnAdd, BorderLayout.WEST);
        southPanel.add(btnDel, BorderLayout.CENTER);
        southPanel.add(btnCopy, BorderLayout.EAST);

        btnAdd.addActionListener(this);
        btnDel.addActionListener(this);
        btnCopy.addActionListener(this);


        add(scrollPaneServerFilesList, BorderLayout.CENTER);

        serverFilesList.setListData(list);

        setVisible(true);

    }


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

    }

    void disconnect() {

    }

    void addFile() {
        int chooserAnswer = addChooser.showDialog(this, "Add");
        if (chooserAnswer == JFileChooser.APPROVE_OPTION) System.out.println("добавить файл");
    }

    void delFile() {

    }

    void copyFile() {
        int chooserAnswer = copyChooser.showDialog(this, "Copy");
        if (chooserAnswer == JFileChooser.APPROVE_OPTION) System.out.println("скопировать файл");
    }

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

}