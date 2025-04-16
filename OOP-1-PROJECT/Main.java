import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException; // Corrected import location
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Assuming Figure, Circle, Rectangle, Line,
// CircleFactory, RectangleFactory, LineFactory classes exist

public class Main {

    private static final String SVG_FILE_PATH = "E:/programing/TU/Java/OOP-1-PROJECT/output.svg"; // Use a constant

    // Helper function to extract attribute value using regex (handles single quotes)
    private static String getAttributeValue(String text, String attributeName) {
        // Pattern to find attribute='value'
        Pattern pattern = Pattern.compile(attributeName + "='([^']*)'");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1); // Return the captured group (the value)
        }
        // Removed the warning print from here as it clutters output during showAll
        // System.err.println("Warning: Attribute '" + attributeName + "' not found in line: " + text);
        return null; // Return null if attribute not found
    }

    public static void main(String[] args) {
        Path path = Paths.get(SVG_FILE_PATH);
        List<Figure> shapes = new ArrayList<>(); // Shapes created in this session

        // Use try-with-resources for the main scanner
        try (Scanner scanner = new Scanner(System.in)) {
            Map<Integer, String> menu = createMenu();

            boolean running = true;
            while (running) {
                displayMenu(menu);
                int choice = getUserChoice(scanner);

                if (menu.containsKey(choice)) {
                    System.out.println("You selected: " + menu.get(choice));

                    switch (choice) {
                        case 1 -> handleCreateCircle(scanner, shapes);
                        case 2 -> handleCreateRectangle(scanner, shapes);
                        case 3 -> handleCreateLine(scanner, shapes);
                        case 4 -> {
                            handleSaveShapes(shapes, SVG_FILE_PATH, path); // Save remaining session shapes first
                            running = false; // Set flag to exit loop
                        }
                        // **** UPDATED CASE 5 ****
                        case 5 -> handleDeleteFigure(scanner, shapes, SVG_FILE_PATH, path);
                        case 6 -> System.out.println("Translate figure - Not yet implemented."); // Placeholder
                        case 7 -> handleShowAll(SVG_FILE_PATH, path, shapes); // Pass the shapes list
                        case 8 -> System.out.println("Show Within - Not yet implemented."); // Placeholder
                        default -> System.out.println("Invalid option selected.");
                    }
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        } // Scanner is automatically closed here
        System.out.println("Exiting application.");
    }

    // --- Menu Handling ---
    // **** NEW HELPER METHOD ****
// Loads figures from the SVG file into a List of Figure objects
    private static List<Figure> loadFiguresFromFile(Path path) {
        List<Figure> loadedFigures = new ArrayList<>();
        if (!Files.exists(path)) {
            return loadedFigures; // Return empty list if file doesn't exist
        }

        try {
            String svgContent = Files.readString(path);
            int startIndex = svgContent.indexOf('>');
            int endIndex = svgContent.lastIndexOf("</svg>");

            if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
                System.err.println("Warning: Invalid or empty SVG structure in file. Cannot load figures.");
                return loadedFigures; // Return empty list on error
            }
            startIndex += 1;
            String innerContent = svgContent.substring(startIndex, endIndex).trim();

            if (!innerContent.isEmpty()) {
                String[] shapeLines = innerContent.split("\\r?\\n");

                for (String line : shapeLines) {
                    line = line.trim();
                    if (line.isEmpty() || !line.startsWith("<")) {
                        continue;
                    }

                    Figure figure = null;
                    try {
                        if (line.startsWith("<circle")) {
                            String cx = getAttributeValue(line, "cx");
                            String cy = getAttributeValue(line, "cy");
                            String r = getAttributeValue(line, "r");
                            String fill = getAttributeValue(line, "fill");
                            if (cx != null && cy != null && r != null && fill != null) {
                                // Use the Circle constructor directly
                                figure = new Circle(Integer.parseInt(cx), Integer.parseInt(cy), Integer.parseInt(r), fill);
                            }
                        } else if (line.startsWith("<rect")) {
                            String x = getAttributeValue(line, "x");
                            String y = getAttributeValue(line, "y");
                            String width = getAttributeValue(line, "width");
                            String height = getAttributeValue(line, "height");
                            String fill = getAttributeValue(line, "fill");
                            if (x != null && y != null && width != null && height != null && fill != null) {
                                // Use the Rectangle constructor directly
                                figure = new Rectangle(Integer.parseInt(x), Integer.parseInt(y), fill, Integer.parseInt(width), Integer.parseInt(height));
                            }
                        } else if (line.startsWith("<line")) {
                            String x1 = getAttributeValue(line, "x1");
                            String y1 = getAttributeValue(line, "y1");
                            String x2 = getAttributeValue(line, "x2");
                            String y2 = getAttributeValue(line, "y2");
                            String stroke = getAttributeValue(line, "stroke");
                            if (x1 != null && y1 != null && x2 != null && y2 != null && stroke != null) {
                                // Use the Line constructor directly
                                figure = new Line(Integer.parseInt(x1), Integer.parseInt(y1), stroke, Integer.parseInt(x2), Integer.parseInt(y2));
                            }
                        }

                        if (figure != null) {
                            loadedFigures.add(figure);
                        } else if (line.startsWith("<")) { // Only warn if it looked like a tag
                            System.err.println("Warning: Could not parse line from file: " + line);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("Warning: Invalid number format in line from file: " + line + " - " + nfe.getMessage());
                    } catch (Exception e) { // Catch other potential parsing errors
                        System.err.println("Warning: Error parsing line from file: " + line + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for loading figures: " + e.getMessage());
        }
        return loadedFigures;
    }
    // **** NEW HELPER METHOD ****
// Overwrites the SVG file with the provided list of figures
    private static void overwriteSvgFile(List<Figure> figures, Path path) {
        try (PrintWriter writer = new PrintWriter(path.toFile())) {
            // Write SVG header
            writer.println("<svg xmlns='http://www.w3.org/2000/svg' width='500' height='500'>");

            // Write each figure
            for (Figure figure : figures) {
                writer.println("  " + figure.drawFigure()); // Add indentation
            }

            // Write SVG footer
            writer.println("</svg>");
            System.out.println("SVG file successfully overwritten at: " + path.toString());

        } catch (IOException e) {
            System.err.println("Error overwriting SVG file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // **** NEW METHOD to handle Deletion ****
    private static void handleDeleteFigure(Scanner scanner, List<Figure> currentSessionShapes, String filePath, Path path) {
        System.out.println("--- Delete Figure ---");

        // 1. Combine saved and current shapes
        List<Figure> loadedFigures = loadFiguresFromFile(path);
        List<Figure> allFigures = new ArrayList<>(loadedFigures); // Start with loaded figures
        allFigures.addAll(currentSessionShapes); // Add current session figures

        if (allFigures.isEmpty()) {
            System.out.println("No figures available to delete (file is empty/missing and no shapes in current session).");
            return;
        }

        // 2. Display combined list with indices
        System.out.println("Available figures:");
        for (int i = 0; i < allFigures.size(); i++) {
            Figure shape = allFigures.get(i);
            String output = (i + 1) + ". "; // 1-based index for user
            // Use instanceof to get details directly from objects
            if (shape instanceof Circle circle) {
                output += "circle " + circle.getXAxis() + " " + circle.getYAxis() + " " + circle.getRadius() + " " + circle.getColor();
            } else if (shape instanceof Rectangle rect) {
                output += "rectangle " + rect.getXAxis() + " " + rect.getYAxis() + " " + rect.getWidth() + " " + rect.getHeight() + " " + rect.getColor();
            } else if (shape instanceof Line line) {
                output += "line " + line.getXAxis() + " " + line.getYAxis() + " " + line.getX2() + " " + line.getY2() + " " + line.getColor();
            } else {
                output += "<Unknown shape type>";
            }
            System.out.println(output);
        }

        // 3. Get user input for index
        int indexToDelete = -1;
        while (true) {
            System.out.print("Enter the number of the figure to delete (or 0 to cancel): ");
            if (scanner.hasNextInt()) {
                indexToDelete = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (indexToDelete == 0) {
                    System.out.println("Deletion cancelled.");
                    return; // Exit deletion process
                }
                if (indexToDelete >= 1 && indexToDelete <= allFigures.size()) {
                    break; // Valid index entered
                } else {
                    System.out.println("Invalid index. Please enter a number between 1 and " + allFigures.size() + ", or 0 to cancel.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
            }
        }

        // 4. Remove the shape (adjusting for 0-based list index)
        int listIndex = indexToDelete - 1;
        Figure removedFigure = allFigures.remove(listIndex);
        System.out.println("Removed figure #" + indexToDelete + ": " + removedFigure.getClass().getSimpleName()); // Simple feedback

        // 5. Overwrite the SVG file with the modified list
        overwriteSvgFile(allFigures, path);

        // 6. Clear the current session shapes list
        currentSessionShapes.clear();
        System.out.println("Current session shape list cleared as file has been updated.");
    }
    private static Map<Integer, String> createMenu() {
        Map<Integer, String> menu = new LinkedHashMap<>(); // Use LinkedHashMap to maintain order
        menu.put(1, "Create Circle");
        menu.put(2, "Create Rectangle");
        menu.put(3, "Create Line");
        menu.put(4, "Save and Exit");
        menu.put(5, "Delete figure");
        menu.put(6, "Translate figure");
        menu.put(7, "Show all figures (saved & current)"); // Updated label
        menu.put(8, "Show Within");
        return menu;
    }

    private static void displayMenu(Map<Integer, String> menu) {
        System.out.println("\n--- SVG Shape Creator Menu ---");
        for (Map.Entry<Integer, String> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
        System.out.print("Choose an option: ");
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Choose an option: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left-over
        return choice;
    }


    // --- Shape Creation Handlers (Keep these as they are) ---

    private static void handleCreateCircle(Scanner scanner, List<Figure> shapes) {
        try {
            System.out.print("Enter x position: ");
            int x = scanner.nextInt();
            System.out.print("Enter y position: ");
            int y = scanner.nextInt();
            System.out.print("Enter radius: ");
            int r = scanner.nextInt();
            System.out.print("Enter fill color: ");
            String fill = scanner.next();
            scanner.nextLine(); // Consume newline

            CircleFactory circleFactory = new CircleFactory(x, y, r, fill);
            Figure circle = circleFactory.createFigure();
            shapes.add(circle);
            System.out.println("Circle added to current session.");
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter numbers for coordinates and radius.");
            scanner.nextLine(); // Consume the rest of the invalid line
        }
    }

    private static void handleCreateRectangle(Scanner scanner, List<Figure> shapes) {
        try {
            System.out.print("Enter x position: ");
            int x = scanner.nextInt();
            System.out.print("Enter y position: ");
            int y = scanner.nextInt();
            System.out.print("Enter width: ");
            int width = scanner.nextInt();
            System.out.print("Enter height: ");
            int height = scanner.nextInt();
            System.out.print("Enter fill color: ");
            String fill = scanner.next();
            scanner.nextLine(); // Consume newline

            // Assuming RectangleFactory and Rectangle exist similar to Circle/Line
            RectangleFactory rectFactory = new RectangleFactory(x, y, fill, width, height);
            Figure rect = rectFactory.createFigure();
            shapes.add(rect);
            System.out.println("Rectangle added to current session.");
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter numbers for coordinates and dimensions.");
            scanner.nextLine(); // Consume the rest of the invalid line
        }
    }

    private static void handleCreateLine(Scanner scanner, List<Figure> shapes) {
        try {
            System.out.print("Enter x1: ");
            int x1 = scanner.nextInt();
            System.out.print("Enter y1: ");
            int y1 = scanner.nextInt();
            System.out.print("Enter x2: ");
            int x2 = scanner.nextInt();
            System.out.print("Enter y2: ");
            int y2 = scanner.nextInt();
            System.out.print("Enter stroke color: ");
            String stroke = scanner.next();
            scanner.nextLine(); // Consume newline

            // Assuming LineFactory exists similar to CircleFactory
            LineFactory lineFactory = new LineFactory(x1, y1, stroke, x2, y2);
            Figure line = lineFactory.createFigure();
            shapes.add(line);
            System.out.println("Line added to current session.");
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter numbers for coordinates.");
            scanner.nextLine(); // Consume the rest of the invalid line
        }
    }

    // --- File Operations Handlers ---

    // handleSaveShapes remains the same
    private static void handleSaveShapes(List<Figure> shapesToSave, String filePath, Path path) {
        if (shapesToSave.isEmpty()) {
            System.out.println("No new shapes created in this session to save.");
            // return; // Keep commented if exit should proceed even without saving new shapes
        } else {
            System.out.println("Saving shapes added in this session to SVG file...");
        }

        File svgFile = path.toFile();
        StringBuilder svgContent = new StringBuilder();

        try {
            if (svgFile.exists() && svgFile.length() > 0) {
                svgContent.append(Files.readString(path));
            } else {
                System.out.println("Creating new SVG file structure.");
                svgContent.append("<svg xmlns='http://www.w3.org/2000/svg' width='500' height='500'>\n");
                svgContent.append("</svg>\n");
            }

            int insertIndex = svgContent.lastIndexOf("</svg>");
            if (insertIndex != -1) {
                if (!shapesToSave.isEmpty()) { // Only add if there are shapes
                    StringBuilder newShapesSvg = new StringBuilder();
                    for (Figure shape : shapesToSave) {
                        newShapesSvg.append("  ").append(shape.drawFigure()).append("\n");
                    }
                    svgContent.insert(insertIndex, newShapesSvg);

                    try (PrintWriter writer = new PrintWriter(svgFile)) {
                        writer.write(svgContent.toString());
                    }
                    System.out.println("Shapes saved to " + filePath);
                    shapesToSave.clear(); // Clear the list after successful save
                }
            } else {
                System.err.println("Error: Could not find closing </svg> tag in the file. Cannot save.");
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // **** MODIFIED handleShowAll ****
    private static void handleShowAll(String filePath, Path path, List<Figure> unsavedShapes) {
        System.out.println("--- Figures (Saved & Current Session) ---");
        int figureCount = 0; // Initialize count

        // --- Part 1: Read and print shapes from the file ---
        if (Files.exists(path)) {
            try {
                String svgContent = Files.readString(path);
                int startIndex = svgContent.indexOf('>');
                int endIndex = svgContent.lastIndexOf("</svg>");

                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    startIndex += 1;
                    String innerContent = svgContent.substring(startIndex, endIndex).trim();

                    if (!innerContent.isEmpty()) {
                        String[] shapeLines = innerContent.split("\\r?\\n");
                        System.out.println("> print (from file)"); // Indicate source

                        for (String line : shapeLines) {
                            line = line.trim();
                            if (line.isEmpty() || !line.startsWith("<")) {
                                continue;
                            }

                            figureCount++; // Increment count for each potential shape line from file
                            String output = figureCount + ". ";
                            boolean shapeRecognized = false;

                            // Identify shape and extract attributes from file line
                            if (line.startsWith("<circle")) {
                                String cx = getAttributeValue(line, "cx");
                                String cy = getAttributeValue(line, "cy");
                                String r = getAttributeValue(line, "r");
                                String fill = getAttributeValue(line, "fill");
                                if (cx != null && cy != null && r != null && fill != null) {
                                    output += "circle " + cx + " " + cy + " " + r + " " + fill;
                                    shapeRecognized = true;
                                }
                            } else if (line.startsWith("<rect")) {
                                String x = getAttributeValue(line, "x");
                                String y = getAttributeValue(line, "y");
                                String width = getAttributeValue(line, "width");
                                String height = getAttributeValue(line, "height");
                                String fill = getAttributeValue(line, "fill");
                                if (x != null && y != null && width != null && height != null && fill != null) {
                                    output += "rectangle " + x + " " + y + " " + width + " " + height + " " + fill;
                                    shapeRecognized = true;
                                }
                            } else if (line.startsWith("<line")) {
                                String x1 = getAttributeValue(line, "x1");
                                String y1 = getAttributeValue(line, "y1");
                                String x2 = getAttributeValue(line, "x2");
                                String y2 = getAttributeValue(line, "y2");
                                String stroke = getAttributeValue(line, "stroke");
                                if (x1 != null && y1 != null && x2 != null && y2 != null && stroke != null) {
                                    output += "line " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + stroke;
                                    shapeRecognized = true;
                                }
                            }

                            if (shapeRecognized) {
                                System.out.println(output);
                            } else {
                                // Malformed line in file, print placeholder but keep count
                                System.out.println(output + "<Malformed line in file: " + line + ">");
                            }
                        }
                    }
                } else {
                    System.out.println("File exists but has invalid SVG structure or is empty.");
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                // Continue to show unsaved shapes even if file reading fails
            }
        } else {
            System.out.println("SVG file not found or not created yet.");
        }

        // --- Part 2: Print shapes from the current session (in memory) ---
        if (!unsavedShapes.isEmpty()) {
            System.out.println("> print (current session - unsaved)"); // Indicate source
            for (Figure shape : unsavedShapes) {
                figureCount++; // Continue numbering
                String output = figureCount + ". ";
                // Use instanceof to get details directly from objects
                if (shape instanceof Circle circle) { // Java 16+ pattern matching
                    output += "circle " + circle.getXAxis() + " " + circle.getYAxis() + " " + circle.getRadius() + " " + circle.getColor();
                } else if (shape instanceof Rectangle rect) { // Assuming Rectangle class exists
                    output += "rectangle " + rect.getXAxis() + " " + rect.getYAxis() + " " + rect.getWidth() + " " + rect.getHeight() + " " + rect.getColor();
                } else if (shape instanceof Line line) {
                    // Access methods specific to Line or common Figure methods
                    // Note: Line constructor uses fill for color, but SVG uses stroke. Assuming getColor() returns the intended color.
                    output += "line " + line.getXAxis() + " " + line.getYAxis() + " " + line.getX2() + " " + line.getY2() + " " + line.getColor();
                } else {
                    output += "<Unknown shape type in memory>";
                }
                System.out.println(output);
            }
        }

        // --- Final check: If no shapes were printed at all ---
        if (figureCount == 0) {
            System.out.println("No shapes found in the file or in the current session.");
        }
    }
}