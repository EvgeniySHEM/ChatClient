package ru.avalon.javapp.devj130.chatclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectDialog extends JDialog {
    private final JTextField host;
    private final JTextField login;

    private boolean connectPressed;

    public ConnectDialog(MainFrame owner) {
        super(owner, "Provide connection properties", true);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel("Server");
        host = new JTextField(19);
        lbl.setLabelFor(host);
        lbl.setDisplayedMnemonic(KeyEvent.VK_S);
        p.add(lbl);
        p.add(host);
        centerPanel.add(p);

        p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lbl = new JLabel("Login");
        login = new JTextField(20);
        lbl.setLabelFor(login);
        lbl.setDisplayedMnemonic(KeyEvent.VK_L);
        p.add(lbl);
        p.add(login);
        centerPanel.add(p);

        add(centerPanel, BorderLayout.CENTER);

        p = new JPanel();

        JButton btn = new JButton("Connect");
        btn.setMnemonic(KeyEvent.VK_C);
        btn.addActionListener(e -> buttonPressed(true));
        p.add(btn);

        btn = new JButton("Exit");
        btn.setMnemonic(KeyEvent.VK_X);
        btn.addActionListener(e -> buttonPressed(false));
        p.add(btn);

        add(p, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                pack();
                setLocationRelativeTo(getOwner());
                connectPressed = false;
                host.requestFocus();
            }
        });
    }

    public String getHost() {
        return host.getText();
    }

    public String getLogin() {
        return login.getText();
    }

    public boolean isConnectPressed() {
        return connectPressed;
    }

    private void buttonPressed(boolean connectPressed) {
        this.connectPressed = connectPressed;
        setVisible(false);
    }
}
