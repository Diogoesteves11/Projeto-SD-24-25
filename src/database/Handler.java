package database;

import Client.Client;

public interface Handler{
    boolean authenticate(String username, String password);
    boolean register(Client client);
}

