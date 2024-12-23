package database;

public interface Login{
    boolean authenticate(String username, String password);
    boolean register(String username, byte[] clientData);
}

