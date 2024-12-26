package Mvc.ClientMvc;

import java.io.IOException;
import java.util.Scanner;

public class MainMenuView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu(MainController controller) throws Exception {
        while (true) {
            System.out.println("Welcome! Choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Sign In");
            System.out.println("3. Exit");
            System.out.print("-> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> controller.login();
                case "2" -> controller.signIn();
                case "3" -> {
                    System.out.println("See you soon!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! ");
            }
        }
    }
}