package utils;

import client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Database implements Login{
    private final Map<String, Client> clientMap;
    private int totalClients;

    public Database(){
        clientMap = new HashMap<String, Client>();
        this.totalClients = 0;
    }

    public Database(int totalClients, Map<String, Client> clientMap){
        this.totalClients = totalClients;
        this.clientMap = new HashMap<String, Client>(clientMap);
    }

    @Override
    public boolean authenticate(String username, String password) {
        if(this.clientMap.containsKey(username)){
            return this.clientMap.get(username).getPassword().equals(password);
        }
        return false;
    }

    public boolean register(Client client){
        if(client == null) return false;
        String username = client.getUsername();

        if(!this.clientMap.containsKey(username)) {
            this.clientMap.put(username, client);
            this.totalClients++;
            return true;
        }
        return false;
    }

    public Client getClient(String username){
        return this.clientMap.get(username);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.totalClients);

        for (Map.Entry<String, Client> entry : clientMap.entrySet()) {
            out.writeUTF(entry.getKey());
            entry.getValue().serialize(out);
        }
    }

    public static Database deserialize(DataInputStream in) throws IOException {
        int totalClients = in.readInt();

        Map<String, Client> tmp_map = new HashMap<>();
        for (int i = 0; i < totalClients; i++) {
            String username = in.readUTF();
            Client client = Client.deserialize(in);
            tmp_map.put(username, client);
        }

        return new Database(totalClients, tmp_map);
    }
}