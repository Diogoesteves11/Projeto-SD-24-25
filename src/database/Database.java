package database;

import Client.Client;
import connectionProtocol.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Database implements Login{
    private final Map<String, Client> clientMap;
    private int totalClients;
    ReentrantLock lock = new ReentrantLock();

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
        this.lock.lock();
        try{
            if(this.clientMap.containsKey(username)){
                return this.clientMap.get(username).getPassword().equals(password);
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean register(Client client){
        this.lock.lock();
        try{
            if(client == null) return false;
            String username = client.getUsername();

            if(!this.clientMap.containsKey(username)) {
                this.clientMap.put(username, client);
                this.totalClients++;
                return true;
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public Client getClient(String username){
        this.lock.lock();
        try{
            return this.clientMap.get(username);
        } finally {
            this.lock.unlock();
        }
    }

    public void updateClientData(Package p) {
        this.lock.lock();
        String clientKey = p.getClientKey();
        //Client client = p.getClientData();
        try {
            if (!this.clientMap.containsKey(clientKey)) {
                throw new IllegalArgumentException("Client key does not exist: " + clientKey);
            }
            //this.clientMap.replace(clientKey, new Client(client));
        } finally {
            this.lock.unlock();
        }
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