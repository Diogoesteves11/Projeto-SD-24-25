package utils;

import client.Client;

import java.util.HashMap;
import java.util.Map;

public class Database implements Login{
    private final Map<String, Client> clientMap;

    public Database(){
        clientMap = new HashMap<>();
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

        if(!this.clientMap.containsKey(username)){
            this.clientMap.put(username, client);
            return true;
        }

        return false;
    }

}