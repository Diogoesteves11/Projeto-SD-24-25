package Mvc.ClientMvc;

import java.util.Scanner;

public class AdminMenuView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayAdminMenu() {
        while (true) {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. View Users");
            System.out.println("2. Manage Users");
            System.out.println("3. Generate Reports");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewUsers();
                case "2" -> manageUsers();
                case "3" -> generateReports();
                case "4" -> {
                    System.out.println("Logging out...");
                    return; // Sai do menu de admin
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewUsers() {
        System.out.println("Viewing all users...");
        // Lógica para visualizar usuários
    }

    private void manageUsers() {
        System.out.println("Managing users...");
        // Lógica para gerenciar usuários
    }

    private void generateReports() {
        System.out.println("Generating reports...");
        // Lógica para gerar relatórios
    }
}
