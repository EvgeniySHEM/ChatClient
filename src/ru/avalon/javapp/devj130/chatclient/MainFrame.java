package ru.avalon.javapp.devj130.chatclient;

import ru.avalon.javapp.devj130.chatlibrary.ClientMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final JTextArea chatArea;
    private final JTextArea messageArea;
    private final ConnectDialog connectDlg;
    private ServerThread serverThread;

    public MainFrame() {
        super();

        setBounds(300, 200, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        messageArea = new JTextArea();

        JPanel bottomPane = new JPanel(new BorderLayout());
        bottomPane.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        JButton sendBtn = new JButton("Send");
        bottomPane.add(sendBtn, BorderLayout.EAST);
        sendBtn.addActionListener(e -> {
            String msg = messageArea.getText();
            if(!msg.isBlank()) {
                serverThread.sendMsg(msg);
                messageArea.setText("");
            }
        });

        JSplitPane sp = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(chatArea),
                bottomPane
        );
        add(sp, BorderLayout.CENTER);

        connectDlg = new ConnectDialog(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                sp.setDividerLocation(0.7);
                connectToServer();
            }
        });
    }

    private void connectToServer() {
        setTitle("Not connected");
        while (serverThread == null) {
            connectDlg.setVisible(true);
            if (!connectDlg.isConnectPressed()) {
                setVisible(false);
                System.exit(0);
            }
            try {
                serverThread = new ServerThread(this, connectDlg.getHost(), connectDlg.getLogin());
                serverThread.start();
                setTitle("Connected to " + connectDlg.getHost());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Error connecting to the server",
                        JOptionPane.ERROR_MESSAGE);
                serverThread = null;
            }
        }
    }

    public void massegeReceived(ClientMessage cliMsg) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(cliMsg.getUser() + ", " + cliMsg.getTime() + ":\n" +
                    cliMsg.getMsg() + "\n\n");
        });
    }

    public void connectionIsLost(String error) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(getContentPane(), error, "Error", JOptionPane.ERROR_MESSAGE);
            serverThread = null;
            connectToServer();
        });
    }
    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

}
