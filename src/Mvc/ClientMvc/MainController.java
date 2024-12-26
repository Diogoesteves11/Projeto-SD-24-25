package Mvc.ClientMvc;

import Client.*;
import connectionProtocol.Connection;

import java.io.IOException;
import java.net.Socket;

import connectionProtocol.Package;

public class MainController {
    private final Client client = new Client();
    private final MainMenuView mainMenuView = new MainMenuView();
    private final LoginView loginMenuView = new LoginView();
    private final SignInView signInView = new SignInView();
    private static int port = 1;

    public void start() throws Exception {
        mainMenuView.displayMenu(this);
        port++;
    }


    public void login() throws Exception {
        int port = 12345;
        Socket socket = new Socket("localhost", port);
        Connection connection = new Connection(socket);

        String[] credentials = loginMenuView.displayLogin();

        Client client = this.client.authenticate(credentials[0], credentials[1], connection);

        if (client == null) {
            loginMenuView.showMessage("Failed to Login");
        } else {
            loginMenuView.showMessage("Login Successful");

            if (client instanceof User) {
                new ClientMenuView().displayClientMenu();
            } else if (client instanceof Admin) {
                new AdminMenuView().displayAdminMenu();
            } else {
                loginMenuView.showMessage("Unknown user type");
            }
        }

        connection.close();
    }

    public void signIn() throws Exception {
        int port = 12345;
        Socket socket = new Socket("localhost", port);
        Connection connection = new Connection(socket);

        String[] data = signInView.displaySignIn();
        boolean success = this.client.register(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]), connection);
        if (success) {
            signInView.showMessage("\n -> Registred in Server! <-\n");
        } else {
            signInView.showMessage("\n -> Username already exists! <- \n");
        }

        connection.close();
    }
}
