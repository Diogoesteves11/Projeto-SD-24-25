package connectionProtocol;

import Client.Client;

import java.io.*;
import java.net.Socket;

public class Package {
    private String clientKey;
    private int tipo;
    private Connection connection;
    private Socket clientSocket;

    public Package(String clientKey, int tipo) {
        this.clientKey = clientKey;
        this.tipo = tipo;
    }

    public boolean isInitialConnection() {
        return this.clientSocket != null;
    }

    // Construtor para pacotes baseados em Socket
    public Package(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    public void processConnection() throws IOException {
        if (this.clientSocket != null) {
            this.connection = new Connection(clientSocket);
            Package received = Package.convertBytesToPackage(this.connection.read());
            this.clientKey = received.getClientKey();
            this.tipo = received.getTipo();
        }
    }

    public void covertInticialConnection(){
        this.clientSocket = null;
    }

    public int getTipo() {
        return this.tipo;
    }

    public String getClientKey() {
        return this.clientKey;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }


    public static Package convertBytesToPackage(byte[] bytes) {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            return Package.deserialize(in);
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
        out.writeUTF(this.clientKey);
        out.writeInt(this.tipo);

    }


    public static Package deserialize(DataInputStream in) throws IOException {
        String clientKey = in.readUTF();
        int tipo = in.readInt();

        switch (tipo) {
            case 0: {
                Client c = Client.deserialize(in);
                return new Registo(clientKey, c);
            }
            case 1: {
                break;
            }
        }
        return null;
    }
}
