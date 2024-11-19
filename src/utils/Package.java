package utils;

import client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Package{
    private String clientKey;
    private Client clientData;

    public Package(String clientKey, Client clientData){
        this.clientKey = clientKey;
        this.clientData = new Client(clientData);
    }

    public String getClientKey(){
        return this.clientKey;
    }

    public Client getClientData(){
        return new Client(this.clientData);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.clientKey);
        this.clientData.serialize(out);
    }

    public static Package deserialize(DataInputStream in) throws IOException {
        String clientKey = in.readUTF();
        Client client = Client.deserialize(in);
        return new Package(clientKey, client);
    }
}