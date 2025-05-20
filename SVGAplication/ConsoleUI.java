package svgcreator.ui;

import svgcreator.shapes.Circle; // Required for instanceof and casting
import svgcreator.shapes.Figure; // Required for List<Figure> and instanceof
import svgcreator.shapes.Line;   // Required for instanceof and casting
import svgcreator.shapes.Rectangle; // Required for instanceof and casting

import java.util.ArrayList;
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

    /**
     * Displays a list of figures to the console, preceded by a header.
     * If the list is null or empty, a "No figures to display." message is shown.
     * This method now handles the formatting of figures.
     *
     * @param header  The header string to display before the list of figures.
     * @param figures A {@link List} of {@link Figure} objects to display.
     */
    public void displayFigures(String header, List<Figure> figures) {
        System.out.println("\n" + header);
        if (figures == null || figures.isEmpty()) {
            System.out.println("No figures to display.");
        } else {
            List<String> formattedFigures = formatFiguresForDisplay(figures);
            for (String figureString : formattedFigures) {
                System.out.println(figureString);
            }
        }
    }

    /**
     * Formats a list of {@link Figure} objects into a list of strings for display.
     * Each string includes an index and the figure's details.
     *
     * @param figures The list of figures to format.
     * @return A list of formatted strings representing the figures.
     */
    private List<String> formatFiguresForDisplay(List<Figure> figures) {
        List<String> formatted = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            formatted.add(formatSingleFigure(figures.get(i), i + 1));
        }
        return formatted;
    }

    /**
     * Formats a single {@link Figure} object into a displayable string.
     *
     * @param shape The figure to format.
     * @param index The display index for the figure.
     * @return A string representation of the figure for display.
     */
    private String formatSingleFigure(Figure shape, int index) {
        String output = index + ". ";
        if (shape instanceof Circle c) {
            output += "circle " + c.getXAxis() + " " + c.getYAxis() + " " + c.getRadius() + " " + c.getColor();
        } else if (shape instanceof Rectangle r) {
            output += "rectangle " + r.getXAxis() + " " + r.getYAxis() + " " + r.getWidth() + " " + r.getHeight() + " " + r.getColor();
        } else if (shape instanceof Line l) {
            output += "line " + l.getXAxis() + " " + l.getYAxis() + " " + l.getX2() + " " + l.getY2() + " " + l.getColor();
        } else {
            output += "<Unknown shape type>";
        }
        return output;
    }


    // --- Input Methods (remain the same) ---

    public int getUserChoice() {
        int choice;
        while (true) {
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public static record ShapeData(int x, int y, String color, int param1, int param2) {}
    public static record TranslateData(int index, int dx, int dy) {}
    public static record BoundaryData(String type, int x, int y, int param1, int param2) {}

    public ShapeData getCircleDetails() {
        showMessage("--- Create Circle ---");
        try {
            System.out.print("Enter x position: "); int x = scanner.nextInt();
            System.out.print("Enter y position: "); int y = scanner.nextInt();
            System.out.print("Enter radius: "); int r = scanner.nextInt();
            if (r < 0) { showError("Radius cannot be negative."); scanner.nextLine(); return null; }
            System.out.print("Enter fill color: "); String color = scanner.next();
            scanner.nextLine();
            return new ShapeData(x, y, color, r, 0);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public ShapeData getRectangleDetails() {
        showMessage("--- Create Rectangle ---");
        try {
            System.out.print("Enter x position: "); int x = scanner.nextInt();
            System.out.print("Enter y position: "); int y = scanner.nextInt();
            System.out.print("Enter width: "); int w = scanner.nextInt();
            System.out.print("Enter height: "); int h = scanner.nextInt();
            if (w < 0 || h < 0) { showError("Width and height cannot be negative."); scanner.nextLine(); return null; }
            System.out.print("Enter fill color: "); String color = scanner.next();
            scanner.nextLine();
            return new ShapeData(x, y, color, w, h);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public ShapeData getLineDetails() {
        showMessage("--- Create Line ---");
        try {
            System.out.print("Enter x1: "); int x1 = scanner.nextInt();
            System.out.print("Enter y1: "); int y1 = scanner.nextInt();
            System.out.print("Enter x2: "); int x2 = scanner.nextInt();
            System.out.print("Enter y2: "); int y2 = scanner.nextInt();
            System.out.print("Enter stroke color: "); String color = scanner.next();
            scanner.nextLine();
            return new ShapeData(x1, y1, color, x2, y2);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public int getDeleteIndex(int maxIndex) {
        int index;
        while (true) {
            System.out.print("Enter the number of the figure to delete (1-" + maxIndex + "), or 0 to cancel: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine();
                if (index >= 0 && index <= maxIndex) {
                    return index;
                } else {
                    showError("Invalid index. Must be between 0 and " + maxIndex + ".");
                }
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public TranslateData getTranslateParams(int maxIndex) {
        int targetIndex;
        while (true) {
            System.out.print("Enter the number of the figure to translate (1-" + maxIndex + "), or 0 to translate all: ");
            if (scanner.hasNextInt()) {
                targetIndex = scanner.nextInt();
                scanner.nextLine();
                if (targetIndex >= 0 && targetIndex <= maxIndex) {
                    break;
                } else {
                    showError("Invalid index. Must be between 0 and " + maxIndex + ".");
                }
            } else {
                showError("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        try {
            System.out.print("Enter horizontal translation (dx): "); int dx = scanner.nextInt();
            System.out.print("Enter vertical translation (dy): "); int dy = scanner.nextInt();
            scanner.nextLine();
            return new TranslateData(targetIndex, dx, dy);
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    public BoundaryData getBoundaryDetails() {
        String type;
        while (true) {
            System.out.print("Enter boundary type (rectangle or circle): ");
            type = scanner.next().toLowerCase();
            scanner.nextLine();
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
                scanner.nextLine();
                return new BoundaryData(type, bx, by, bw, bh);
            } else { // circle
                System.out.print("Enter boundary circle x y radius: ");
                int bx = scanner.nextInt(); int by = scanner.nextInt();
                int br = scanner.nextInt();
                if (br < 0) { showError("Radius cannot be negative."); scanner.nextLine(); return null; }
                scanner.nextLine();
                return new BoundaryData(type, bx, by, br, 0);
            }
        } catch (InputMismatchException e) {
            handleInputMismatch();
            return null;
        }
    }

    private void handleInputMismatch() {
        showError("Invalid numeric input. Please enter numbers where expected.");
        scanner.nextLine();
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
            System.out.println("ConsoleUI: Scanner closed.");
        }
    }
}