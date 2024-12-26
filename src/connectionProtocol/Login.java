package connectionProtocol;

public class Login extends Package{
    private String password;

    public Login(String clientKey, String password)
    {
        super(clientKey, 1);
        this.password = password;
    }
}