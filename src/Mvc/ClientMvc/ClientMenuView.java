package Mvc.ClientMvc;

import java.util.Scanner;

public class ClientMenuView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayClientMenu() {
        while (true) {
            System.out.println("\n==== Client Menu ====");
            System.out.println("1. View Account Details");
            System.out.println("2. Make an Order");
            System.out.println("3. View Order History");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewAccountDetails();
                case "2" -> makeTransaction();
                case "3" -> viewTransactionHistory();
                case "4" -> {
                    System.out.println("Logging out...");
                    return; // Sai do menu do cliente
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewAccountDetails() {
        System.out.println("Viewing account details...");
        // Lógica para visualizar detalhes da conta
    }

    private void makeTransaction() {
        System.out.println("Making a transaction...");
        // Lógica para realizar uma transação
    }

    private void viewTransactionHistory() {
        System.out.println("Viewing transaction history...");
        // Lógica para visualizar histórico de transações
    }
}
