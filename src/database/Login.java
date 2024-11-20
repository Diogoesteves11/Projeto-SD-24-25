package database;

import client.Client;

public interface Login{
    boolean authenticate(String username, String password);
    boolean register(Client client);
}

