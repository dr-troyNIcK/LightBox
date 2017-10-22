import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerSwingGUI extends JFrame implements ActionListener, ServerListener, Thread.UncaughtExceptionHandler {

    private static final int POS_X_FRAME = 50;
    private static final int POS_Y_FRAME = 50;
    private static final int WIDTH_FRAME = 600;
    private static final int HEIGHT_FRAME = 300;

    private static final String TITLE = "Server";
    private static final String START_LISTENING = "Star listening";
    private static final String STOP_LISTENING = "Stop listening";

    private final Server server = new Server(this, new SQLAuthorizeManager());

    private final JPanel northPanel = new JPanel(new GridLayout(1, 2));
    private final JButton btnStartListening = new JButton(START_LISTENING);
    private final JButton btnStopListening = new JButton(STOP_LISTENING);
    private final JTextArea log = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(log);

    private ServerSwingGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X_FRAME, POS_Y_FRAME, WIDTH_FRAME, HEIGHT_FRAME);
        setTitle(TITLE);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        northPanel.add(btnStartListening);
        northPanel.add(btnStopListening);

        log.setEditable(false);

        btnStartListening.addActionListener(this);
        btnStopListening.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerSwingGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnStartListening) {
            server.startListening(8189);
        } else if (source == btnStopListening) {
            server.stopListening();
        } else {
            throw new RuntimeException("Unknown source: " + source);
        }
    }

    @Override
    public void onServerLog(String msg, Server server) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
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