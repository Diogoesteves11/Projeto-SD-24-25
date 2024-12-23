package database;

import Client.Client;

public interface Login{
    boolean authenticate(String username, String password);
    boolean register(Client client);
}

