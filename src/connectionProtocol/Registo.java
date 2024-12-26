package connectionProtocol;
import Client.Client;

public class Registo extends Package{
    private Client cliente;
    public Registo(String clientKey, Client clientData)
    {
        super(clientKey, 0);
        this.cliente = clientData;
    }
}