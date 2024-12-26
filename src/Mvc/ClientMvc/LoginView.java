package Mvc.ClientMvc;

import java.util.Scanner;

public class LoginView {
    public String[] displayLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return new String[] { username, password };
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
