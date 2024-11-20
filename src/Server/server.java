package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import connectionProtocol.Connection;
import database.Database;
import connectionProtocol.Package;

public class Server{
    private final int maxClients;
    private int currentClients;
    private ServerSocket socket;
    private static final int ThreadCount = 10;
    private static final int PORT = 12345;

    public Server(int maxClients) throws IOException {
        this.maxClients = maxClients;
        this.currentClients = 0;
        this.socket = new ServerSocket(PORT);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        Server server = new Server(10);
        System.out.println("Servidor iniciado na porta " + PORT);
        ThreadPool threadPool = new ThreadPool(ThreadCount);

        try{
            while(true){
                Socket s = server.socket.accept();
                Connection new_connection = new Connection(s);
                System.out.println("Novo cliente conectado: " + s.getInetAddress());

                Package received = Package.convertBytesToPackage(new_connection.read());

                threadPool.submit(received);
            }
        } finally{
            threadPool.shutdown();
        }

    }




}