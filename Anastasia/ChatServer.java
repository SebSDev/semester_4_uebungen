package de.thro.inf.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(10014);
    }

    public void message(Socket socket, List<Socket> sockets) throws IOException {
        InputStream in = socket.getInputStream();
        Scanner scan = new Scanner(in);
        String s = scan.nextLine();
        while(!s.equals("exit")){
            for(Socket soc : sockets){
                if(!soc.equals(socket)){
                    OutputStream out = soc.getOutputStream();
                    out.write(("Client " + soc.toString() + ": " + s + "\r\n").getBytes());
                    out.flush();
                }
            }
            System.out.println("Text: " + s);
            s = scan.nextLine();
        }
    }


    public static void main(String[] args) throws IOException {
        final ChatServer chatServer = new ChatServer();
        List<Socket> list = new LinkedList<>();
        while(true){
            Socket socket = chatServer.serverSocket.accept();
            list.add(socket);
            Thread t = new Thread(() -> {
                try {
                    chatServer.message(socket, list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();

        }
    }
}
