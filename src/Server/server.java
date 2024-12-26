package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import database.Database;
import Client.Client;
import connectionProtocol.Connection;
import connectionProtocol.Package;

public class Server{
    private final int maxClients;
    private ServerSocket socket;
    private static final int ThreadCount = 10;
    private static final int PORT = 12345;

    public Server(int maxClients) throws IOException {
        this.maxClients = maxClients;
        this.socket = new ServerSocket(PORT);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        Server server = new Server(10);
        System.out.println("Servidor iniciado na porta " + PORT);
        ThreadPool threadPool = new ThreadPool(ThreadCount);

        System.out.println("------------ SERVER LOG ------------");

        try {
            while (true) {
                Socket s = server.socket.accept();
                System.out.println("Novo cliente conectado: " + s.getInetAddress());

                threadPool.submit(new Package(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}