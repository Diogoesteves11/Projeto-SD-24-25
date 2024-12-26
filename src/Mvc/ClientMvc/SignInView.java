package Mvc.ClientMvc;

import java.util.Scanner;

public class SignInView {
    public String[] displaySignIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Escolha um username: ");
        String username = scanner.nextLine();
        System.out.print("Escolha uma senha: ");
        String password = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Birth Date (yyyy-mm-dd): ");
        String birthDate = scanner.nextLine();

        System.out.print("Are you admin? (true/false): ");
        boolean isAdmin = scanner.nextBoolean();

        return new String[] { username, password,name,email,birthDate,String.valueOf(isAdmin) };
    }
    public void showMessage(String message) {
        System.out.println(message);
    }
}
