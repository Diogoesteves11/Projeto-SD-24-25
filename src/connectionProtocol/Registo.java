package connectionProtocol;
import Client.*;

import java.io.*;
import java.time.LocalDate;

public class Registo extends Package{
    private final Client cliente;
    public Registo(String clientKey, String password, String name, String email, String birth_date, Boolean admin)
    {
        super(clientKey, 0);
        LocalDate date = LocalDate.parse(birth_date);
        if(admin){
            this.cliente = new Admin(clientKey, password, name, email,date);
        } else {
            this.cliente = new User(clientKey, password, name, email,date);
        }

    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }

    public Registo(String clientKey, Client client) {
        super(clientKey, 0);  // Tipo 0 para Registro
        this.cliente = new Client(client);
    }

    public Client getClient(){
        return new Client(this.cliente);
    }

    public static Registo convertBytesToPackage(byte[] bytes) {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            return Registo.deserialize(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert bytes to Package", e);
        }
    }

    public byte[] convertPackageToBytes() {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             DataOutputStream dataOut = new DataOutputStream(byteOut)) {
            this.serialize(dataOut);
            return byteOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Package to bytes", e);
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        this.cliente.serialize(out);
    }

    public static Registo deserialize(DataInputStream in) throws IOException {
        System.out.println("Iniciando a desserialização...");
        Package pkg = Package.deserialize(in);
        Client client = Client.deserialize(in);
        return new Registo(pkg.getClientKey(), client);
    }
}