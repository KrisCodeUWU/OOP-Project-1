package svgcreator.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI implements AutoCloseable { // Implement AutoCloseable for scanner

    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in); // Create scanner internally
    }

    // --- Display Methods ---

    public void displayMenu(Map<Integer, String> menu) {
        System.out.println("\n--- SVG Shape Creator Menu ---");
        for (Map.Entry<Integer, String> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.err.println("ERROR: " + error);
    }

    public void displayFigures(String header, List<String> formattedFigures) {
        System.out.println("\n" + header);
        if (formattedFigures == null || formattedFigures.isEmpty()) {
            System.out.println("No figures to display.");
        } else {
            for (String figureString : formattedFigures) {
                System.out.println(figureString);
            }
        }
    }

    // --- Input Methods ---

    public int getUserChoice() {
        int choice = -1;
        while (true) {
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return choice;
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid token
            }
        }
    }

    // Simple data classes for returning multiple values from input methods
    public static record ShapeData(int x, int y, String color, int param1, int param2) {} // param1=r/w, param2=h
    public static record TranslateData(int index, int dx, int dy) {}
    public static record BoundaryData(String type, int x, int y, int param1, int param2) {} // param1=r/w, param2=h

    public ShapeData getCircleDetails() {
        showMessage("--- Create svgcreator.shapes.Circle ---");
        try {
            System.out.print("Enter x position: "); int x = scanner.nextInt();
            System.out.print("Enter y position: "); int y = scanner.nextInt();
            System.out.print("Enter radius: "); int r = scanner.nextInt();
            if (r < 0) { showError("Radius cannot be negative."); scanner.nextLine(); return null; }
            System.out.print("Enter fill color: "); String color = scanner.next();
            scanner.nextLine(); // Consume newline
            return new ShapeData(x, y, color, r, 0); // param2 is unused for circle
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public ShapeData getRectangleDetails() {
        showMessage("--- Create svgcreator.shapes.Rectangle ---");
        try {
            System.out.print("Enter x position: "); int x = scanner.nextInt();
            System.out.print("Enter y position: "); int y = scanner.nextInt();
            System.out.print("Enter width: "); int w = scanner.nextInt();
            System.out.print("Enter height: "); int h = scanner.nextInt();
            if (w < 0 || h < 0) { showError("Width and height cannot be negative."); scanner.nextLine(); return null; }
            System.out.print("Enter fill color: "); String color = scanner.next();
            scanner.nextLine(); // Consume newline
            return new ShapeData(x, y, color, w, h);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public ShapeData getLineDetails() {
        showMessage("--- Create svgcreator.shapes.Line ---");
        try {
            System.out.print("Enter x1: "); int x1 = scanner.nextInt();
            System.out.print("Enter y1: "); int y1 = scanner.nextInt();
            System.out.print("Enter x2: "); int x2 = scanner.nextInt();
            System.out.print("Enter y2: "); int y2 = scanner.nextInt();
            System.out.print("Enter stroke color: "); String color = scanner.next();
            scanner.nextLine(); // Consume newline
            // Using param1=x2, param2=y2 for LineData structure
            return new ShapeData(x1, y1, color, x2, y2);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public int getDeleteIndex(int maxIndex) {
        int index = -1;
        while (true) {
            System.out.print("Enter the number of the figure to delete (1-" + maxIndex + "), or 0 to cancel: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (index >= 0 && index <= maxIndex) {
                    return index;
                } else {
                    showError("Invalid index.");
                }
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid token
            }
        }
    }

    public TranslateData getTranslateParams(int maxIndex) {
        int targetIndex = -1;
        while (true) {
            System.out.print("Enter the number of the figure to translate (1-" + maxIndex + "), or 0 to translate all: ");
            if (scanner.hasNextInt()) {
                targetIndex = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (targetIndex >= 0 && targetIndex <= maxIndex) {
                    break; // Valid index
                } else {
                    showError("Invalid index.");
                }
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid token
            }
        }

        try {
            System.out.print("Enter horizontal translation (dx): "); int dx = scanner.nextInt();
            System.out.print("Enter vertical translation (dy): "); int dy = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return new TranslateData(targetIndex, dx, dy);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null; // Indicate failure
        }
    }

    public BoundaryData getBoundaryDetails() {
        String type = "";
        while (true) {
            System.out.print("Enter boundary type (rectangle or circle): ");
            type = scanner.next().toLowerCase();
            if (type.equals("rectangle") || type.equals("circle")) {
                break;
            } else {
                showError("Invalid type. Please enter 'rectangle' or 'circle'.");
                scanner.nextLine(); // Consume rest of line if needed
            }
        }

        try {
            if (type.equals("rectangle")) {
                System.out.print("Enter boundary rectangle x y width height: ");
                int bx = scanner.nextInt(); int by = scanner.nextInt();
                int bw = scanner.nextInt(); int bh = scanner.nextInt();
                if (bw < 0 || bh < 0) { showError("Width and height cannot be negative."); scanner.nextLine(); return null; }
                scanner.nextLine();
                return new BoundaryData(type, bx, by, bw, bh);
            } else { // circle
                System.out.print("Enter boundary circle x y radius: ");
                int bx = scanner.nextInt(); int by = scanner.nextInt();
                int br = scanner.nextInt();
                if (br < 0) { showError("Radius cannot be negative."); scanner.nextLine(); return null; }
                scanner.nextLine();
                return new BoundaryData(type, bx, by, br, 0); // param2 unused
            }
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    private void handleInputMismatch() {
        showError("Invalid numeric input.");
        scanner.nextLine(); // Consume the rest of the invalid line
    }

    @Override
    public void close() { // Method from AutoCloseable
        scanner.close();
        System.out.println("Scanner closed.");
    }
}