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

    try {
        while (true) {
            Socket s = server.socket.accept();
            Connection new_connection = new Connection(s);
            System.out.println("Novo cliente conectado: " + s.getInetAddress());

            // Leitura do pacote recebido
            byte[] receivedBytes = new_connection.read();
            Package receivedPackage = Package.convertBytesToPackage(receivedBytes);
            
            // Processamento e exibição dos dados do pacote
            String clientKey = receivedPackage.getClientKey();
            Client clientData = receivedPackage.getClientData();

            System.out.println("Chave do cliente: " + clientKey);
            System.out.println("Dados do cliente:");
            System.out.println("  Username: " + clientData.getUsername());
            System.out.println("  Email: " + clientData.getEmail());
            System.out.println("  Data de nascimento: " + clientData.getBirth_date());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}