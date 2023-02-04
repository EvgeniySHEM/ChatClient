package ru.avalon.javapp.devj130.chatclient;

import ru.avalon.javapp.devj130.chatlibrary.ClientMessage;
import ru.avalon.javapp.devj130.chatlibrary.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread{
    private final MainFrame mainFrame;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public ServerThread(MainFrame mainFrame, String host, String user) throws IOException {
        this.mainFrame = mainFrame;
        socket = new Socket(host, Constants.SERVER_PORT);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream.flush();

        this.outputStream.writeUTF(user);
    }

    @Override
    public void run() {
        String error = null;
        while (error == null) {
            try {
                ClientMessage cliMsg = (ClientMessage) inputStream.readObject();
                mainFrame.massegeReceived(cliMsg);
            } catch (IOException e) {
                error = e.getMessage();
            } catch (ClassNotFoundException e) {
            }
        }

        mainFrame.connectionIsLost(error);
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public void sendMsg(String msg) {
        try {
            outputStream.writeUTF(msg);
            outputStream.flush();
        } catch (IOException e) {
        }
    }
}
