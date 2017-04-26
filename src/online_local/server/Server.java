package online_local.server;

import online_local.server.GS_Game_online;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ray on 2017/1/28.
 */


public class Server {

    private ServerSocket serverSocket;
    Socket[] sockets;
    List<Socket> users = new LinkedList<Socket>();
    Socket socket;

    public Server(int port) {
        try {
            sockets = new Socket[2];
            serverSocket = new ServerSocket(port);
            while (true) {
                socket = serverSocket.accept();
                if (!users.contains(socket))
                    users.add(socket);
                if (users.size() == 2){
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(users.get(0).getOutputStream()));
                    out.println("/1");
                    out = new PrintWriter(new OutputStreamWriter(users.get(1).getOutputStream()));
                    out.println("/2");
                    sockets [0] = users.get(0);
                    sockets [1] = users.get(1);
                }
                if (sockets[0] != null && sockets[1] != null)
                    new GS_Game_online(sockets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(12345);
    }

}
