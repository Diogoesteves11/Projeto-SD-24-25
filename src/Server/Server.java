package Server;

import Client.Client;
import connectionProtocol.Connection;
import connectionProtocol.Package;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
    System.out.println("Instantiating ThreadPool...");
    ThreadPool threadPool = new ThreadPool(Server.ThreadCount);

    System.out.println("\n------------ SERVER LOG -------------\n");

    try {
        while (true) {
            Socket s = server.socket.accept();
            Connection new_connection = new Connection(s);
            System.out.println("Novo cliente conectado: " + s.getInetAddress());

            byte[] receivedBytes = new_connection.read();
            Package receivedPackage = Package.convertBytesToPackage(receivedBytes);
            String clientKey = receivedPackage.getClientKey();

            threadPool.submit(receivedPackage);

            Package pkg = threadPool.getProcessedPackage(clientKey);
            String clientKey1 = pkg.getClientKey();
            Client clientData = pkg.getClientData();
            System.out.println("Chave do cliente: " + clientKey1);
            System.out.println("  Username: " + clientData.getUsername());
            System.out.println("  Email: " + clientData.getEmail());
            System.out.println("  Data de nascimento: " + clientData.getBirth_date());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}