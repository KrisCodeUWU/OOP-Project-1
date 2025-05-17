package svgcreator.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles all console-based user interactions for the SVG Creator application.
 * This class is responsible for displaying menus, messages, errors, and figures,
 * as well as for prompting the user for input and parsing that input.
 * It implements {@link AutoCloseable} to ensure the underlying {@link Scanner} is closed properly.
 */
public class ConsoleUI implements AutoCloseable {

    private final Scanner scanner;

    /**
     * Constructs a new {@code ConsoleUI} instance.
     * Initializes an internal {@link Scanner} to read from {@code System.in}.
     */
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    // --- Display Methods ---

    /**
     * Displays the main application menu to the console.
     *
     * @param menu A {@link Map} where keys are option numbers and values are menu item descriptions.
     *             The menu items are printed in the order provided by the map's iterator
     *             (e.g., {@link java.util.LinkedHashMap} preserves insertion order).
     */
    public void displayMenu(Map<Integer, String> menu) {
        System.out.println("\n--- SVG Shape Creator Menu ---");
        for (Map.Entry<Integer, String> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    /**
     * Displays a general message to the console.
     *
     * @param message The message string to be displayed.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the console's standard error stream.
     * The message is prefixed with "ERROR: ".
     *
     * @param error The error message string to be displayed.
     */
    public void showError(String error) {
        System.err.println("ERROR: " + error);
    }

    /**
     * Displays a list of formatted figure strings to the console, preceded by a header.
     * If the list is null or empty, a "No figures to display." message is shown.
     *
     * @param header             The header string to display before the list of figures.
     * @param formattedFigures A {@link List} of strings, where each string represents a figure
     *                           already formatted for display.
     */
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

    /**
     * Prompts the user to choose an option from the menu and reads their integer choice.
     * Continuously prompts until a valid integer is entered.
     *
     * @return The integer choice entered by the user.
     */
    public int getUserChoice() {
        int choice; // No need to initialize to -1 as the loop guarantees assignment
        while (true) {
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the rest of the line including the newline character
                return choice;
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid token to prevent an infinite loop
            }
        }
    }

    /**
     * A record to hold data for creating a shape.
     * This is a compact way to return multiple values from input methods.
     *
     * @param x      The x-coordinate (e.g., center x for circle, top-left x for rectangle, x1 for line).
     * @param y      The y-coordinate (e.g., center y for circle, top-left y for rectangle, y1 for line).
     * @param color  The color of the shape (fill or stroke).
     * @param param1 Primary dimension (e.g., radius for circle, width for rectangle, x2 for line).
     * @param param2 Secondary dimension (e.g., height for rectangle, y2 for line; unused for circle).
     */
    public static record ShapeData(int x, int y, String color, int param1, int param2) {}

    /**
     * A record to hold data for translating a figure.
     *
     * @param index The 1-based index of the figure to translate, or 0 to translate all figures.
     * @param dx    The horizontal translation amount.
     * @param dy    The vertical translation amount.
     */
    public static record TranslateData(int index, int dx, int dy) {}

    /**
     * A record to hold data for defining a boundary region.
     *
     * @param type   The type of boundary ("rectangle" or "circle").
     * @param x      The x-coordinate of the boundary's reference point.
     * @param y      The y-coordinate of the boundary's reference point.
     * @param param1 Primary dimension of the boundary (width for rectangle, radius for circle).
     * @param param2 Secondary dimension of the boundary (height for rectangle; unused for circle).
     */
    public static record BoundaryData(String type, int x, int y, int param1, int param2) {}

    /**
     * Prompts the user for details required to create a circle (position, radius, color).
     *
     * @return A {@link ShapeData} object containing the circle's details, or {@code null}
     *         if input is invalid (e.g., non-numeric input, negative radius).
     */
    public ShapeData getCircleDetails() {
        showMessage("--- Create Circle ---"); // Corrected class name
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

    /**
     * Prompts the user for details required to create a rectangle (position, dimensions, color).
     *
     * @return A {@link ShapeData} object containing the rectangle's details, or {@code null}
     *         if input is invalid (e.g., non-numeric input, negative dimensions).
     */
    public ShapeData getRectangleDetails() {
        showMessage("--- Create Rectangle ---"); // Corrected class name
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

    /**
     * Prompts the user for details required to create a line (start/end points, color).
     *
     * @return A {@link ShapeData} object containing the line's details, or {@code null}
     *         if input is invalid (e.g., non-numeric input).
     *         For {@link ShapeData}, {@code x} is x1, {@code y} is y1,
     *         {@code param1} is x2, and {@code param2} is y2.
     */
    public ShapeData getLineDetails() {
        showMessage("--- Create Line ---"); // Corrected class name
        try {
            System.out.print("Enter x1: "); int x1 = scanner.nextInt();
            System.out.print("Enter y1: "); int y1 = scanner.nextInt();
            System.out.print("Enter x2: "); int x2 = scanner.nextInt();
            System.out.print("Enter y2: "); int y2 = scanner.nextInt();
            System.out.print("Enter stroke color: "); String color = scanner.next();
            scanner.nextLine(); // Consume newline
            return new ShapeData(x1, y1, color, x2, y2);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    /**
     * Prompts the user to enter the 1-based index of a figure to delete.
     * Validates that the index is within the allowed range [0, maxIndex].
     * An input of 0 typically signifies cancellation.
     *
     * @param maxIndex The maximum valid index for deletion (usually the number of figures).
     * @return The 1-based index entered by the user, or 0 for cancellation.
     */
    public int getDeleteIndex(int maxIndex) {
        int index;
        while (true) {
            System.out.print("Enter the number of the figure to delete (1-" + maxIndex + "), or 0 to cancel: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (index >= 0 && index <= maxIndex) {
                    return index;
                } else {
                    showError("Invalid index. Must be between 0 and " + maxIndex + ".");
                }
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid token
            }
        }
    }

    /**
     * Prompts the user for parameters to translate a figure (or all figures).
     * This includes the target figure's 1-based index (or 0 for all) and the dx/dy translation values.
     *
     * @param maxIndex The maximum valid index for a single figure translation.
     * @return A {@link TranslateData} object containing the translation parameters,
     *         or {@code null} if input for dx/dy is invalid.
     */
    public TranslateData getTranslateParams(int maxIndex) {
        int targetIndex;
        while (true) {
            System.out.print("Enter the number of the figure to translate (1-" + maxIndex + "), or 0 to translate all: ");
            if (scanner.hasNextInt()) {
                targetIndex = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (targetIndex >= 0 && targetIndex <= maxIndex) {
                    break; // Valid index
                } else {
                    showError("Invalid index. Must be between 0 and " + maxIndex + ".");
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

    /**
     * Prompts the user for details of a boundary region (type: rectangle or circle, and its dimensions).
     *
     * @return A {@link BoundaryData} object containing the boundary details, or {@code null}
     *         if input is invalid (e.g., invalid type, non-numeric input, negative dimensions).
     */
    public BoundaryData getBoundaryDetails() {
        String type;
        while (true) {
            System.out.print("Enter boundary type (rectangle or circle): ");
            type = scanner.next().toLowerCase();
            scanner.nextLine(); // Consume the rest of the line including newline
            if (type.equals("rectangle") || type.equals("circle")) {
                break;
            } else {
                showError("Invalid type. Please enter 'rectangle' or 'circle'.");
            }
        }

        try {
            if (type.equals("rectangle")) {
                System.out.print("Enter boundary rectangle x y width height: ");
                int bx = scanner.nextInt(); int by = scanner.nextInt();
                int bw = scanner.nextInt(); int bh = scanner.nextInt();
                if (bw < 0 || bh < 0) { showError("Width and height cannot be negative."); scanner.nextLine(); return null; }
                scanner.nextLine(); // Consume newline
                return new BoundaryData(type, bx, by, bw, bh);
            } else { // circle
                System.out.print("Enter boundary circle x y radius: ");
                int bx = scanner.nextInt(); int by = scanner.nextInt();
                int br = scanner.nextInt();
                if (br < 0) { showError("Radius cannot be negative."); scanner.nextLine(); return null; }
                scanner.nextLine(); // Consume newline
                return new BoundaryData(type, bx, by, br, 0); // param2 unused for circle
            }
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    /**
     * Handles an {@link InputMismatchException} by showing an error message
     * and consuming the rest of the invalid input line from the scanner.
     */
    private void handleInputMismatch() {
        showError("Invalid numeric input. Please enter numbers where expected.");
        scanner.nextLine(); // Consume the rest of the invalid line to clear the buffer
    }

    /**
     * Closes the underlying {@link Scanner} object.
     * This method is called automatically when the {@code ConsoleUI} is used
     * in a try-with-resources statement, due to implementing {@link AutoCloseable}.
     */
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
            System.out.println("ConsoleUI: Scanner closed.");
        }
    }
}