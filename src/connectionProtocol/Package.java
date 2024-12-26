package connectionProtocol;

import Client.Client;

import java.io.*;

public class Package{
    private String clientKey;
    private Client clientData;
    private int tipo;

    public Package(String clientKey, int tipo){
        this.clientKey = clientKey;
        this.tipo = tipo;
    }

    public int getTipo(){
        return this.tipo;
    }

    public String getClientKey(){
        return this.clientKey;
    }

    public Client getClientData(){
        return new Client(this.clientData);
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
        this.clientData.serialize(out);
    }

    public static Package deserialize(DataInputStream in) throws IOException {
        String clientKey = in.readUTF();
        int tipo = in.readInt();
        return new Package(clientKey,tipo);
    }
}