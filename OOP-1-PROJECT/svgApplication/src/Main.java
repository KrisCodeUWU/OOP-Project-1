import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Define menu options using a Map
        Map<Integer, String> menu = new LinkedHashMap<>();
        menu.put(1, "Create Circle");
        menu.put(2, "Create Rectangle");
        menu.put(3, "Create Line");
        menu.put(4, "Exit");

        while (true) {
            // Display the menu
            System.out.println("\nSVG Shape Creator Menu:");
            for (Map.Entry<Integer, String> entry : menu.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }

            // Get user input
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            // Handle the choice
            if (menu.containsKey(choice)) {
                System.out.println("You selected: " + menu.get(choice));

                switch (choice) {
                    case 1 -> System.out.println("Creating a Circle...");
                    case 2 -> System.out.println("Creating a Rectangle...");
                    case 3 -> System.out.println("Creating a Line...");
                    case 4 -> {
                        System.out.println("Exiting program...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
