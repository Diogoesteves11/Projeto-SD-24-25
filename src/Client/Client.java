package Client;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Order.Order;
import connectionProtocol.Connection;
import connectionProtocol.Package;

public class Client {
    private String username;
    private String password;
    private String email;
    private LocalDate birth_date;

    public Client() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.birth_date = null;
    }

    public Client(String username, String password, String email, LocalDate birth_date) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birth_date = birth_date;
    }

    public Client(Client c) {
        this.username = c.getUsername();
        this.password = c.getPassword();
        this.email = c.getEmail();
        this.birth_date = c.getBirth_date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public static Client deserialize(DataInputStream in) {
        try {
            String username = in.readUTF();
            String password = in.readUTF();
            String email = in.readUTF();
            String birthDateString = in.readUTF();
            LocalDate birth_date = LocalDate.parse(birthDateString);
    
            return new Client(username,password,email, birth_date);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.getUsername());
        out.writeUTF(this.getPassword());
        out.writeUTF(this.getEmail());
        out.writeUTF(this.getBirth_date() != null ? this.getBirth_date().toString() : "");
    }

    public static void main(String[] args) {
        try {
            // Configurações de conexão
            int port = 12345;
            Socket socket = new Socket("localhost", port);
            Connection connection = new Connection(socket);
    
            // Dados do cliente
            String clientKey = "client1";
            Client clientData = new Client("user123", "password123", "user@example.com",LocalDate.of(1990, 1, 1));
    
            // Criação do pacote
            Package pkg = new Package(clientKey, clientData);
    
            // Conversão para bytes e envio
            byte[] data = pkg.convertPackageToBytes();
            connection.send(data);
            System.out.println("Pacote enviado: " + clientKey);
    
            connection.close();
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
      
}
