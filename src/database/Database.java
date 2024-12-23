package database;

import Order.Order;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Database implements Login{
    private final Map<String, byte[]> clientMap;
    private final Map<String,byte[]> ordersMap; // username->pedidos
    private int totalClients;
    ReentrantLock lock = new ReentrantLock();

    public Database(){
        clientMap = new HashMap<>();
        ordersMap = new HashMap<>();
        this.totalClients = 0;
    }

    public Database(int totalClients, Map<String, byte[]> clientMap,Map<String,byte[]> ordersMap){
        this.totalClients = totalClients;
        this.clientMap = new HashMap<>(clientMap);
        this.ordersMap = new HashMap<>(ordersMap);
    }

    @Override
    public boolean authenticate(String username, String password) {
        this.lock.lock();
        try{
            if(this.clientMap.containsKey(username)){
                byte[] clientData = this.clientMap.get(username);
                String storePassword = extractPassword(clientData);
                return storePassword.equals(password);
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean register(String username, byte[] clientData){
        this.lock.lock();
        try{
            if(clientData == null) return false;
            

            if(!this.clientMap.containsKey(username)) {
                this.clientMap.put(username, clientData);
                this.totalClients++;
                return true;
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public byte[] getClient(String username){
        this.lock.lock();
        try{
            return this.clientMap.get(username);
        } finally {
            this.lock.unlock();
        }
    }

    public void updateClientData(String username,byte[] newClientData) {
        this.lock.lock();
        try {
            if (!this.clientMap.containsKey(username)) {
                throw new IllegalArgumentException("Client key does not exist: " + username);
            }
            this.clientMap.replace(username, newClientData);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean addOrder(String username,Order order) {
        this.lock.lock();
        try {
            
            if(order==null) return false;

            byte[] orderData = serialize(order);
            this.ordersMap.put(username,orderData);
            return true;
        } finally {
        this.lock.unlock();
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        this.lock.lock();
        try {
            out.writeInt(this.totalClients);

            for(Map.Entry<String,byte[]> entry : clientMap.entrySet()) {
                out.writeUTF(entry.getKey());
                byte[] data = entry.getValue();
                out.writeInt(data.length);
                out.write(data);

            }

        } finally {
            this.lock.unlock();
        }
    }

    public static Database deserialize(DataInputStream in) throws IOException {
        int totalClients = in.readInt();
        Map<String, byte[]> tmp_map = new HashMap<>();
        
        for (int i = 0; i < totalClients; i++) {
            String username = in.readUTF();
            int length = in.readInt();
            byte[] clientData = new byte[length];
            in.readFully(clientData);
            tmp_map.put(username, clientData);
        }

        return new Database(totalClients, tmp_map);
    }

    private String extractPassword(byte[] clientData) {
        //assumindo que a password é username:password
        String decoded = new String(clientData); // cast para string
        String[] parts = decoded.split(":");
        return parts.length > 1 ? parts[1] : ""; //retornar a password
    }

    void put(String key, byte[] value) {
        lock.lock();
        try {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key or value cannot be null");
            }
            this.clientMap.put(key, value);
        } finally {
            lock.unlock();
        }
    }

}