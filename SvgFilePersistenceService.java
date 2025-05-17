package svgcreator.persistence;

import svgcreator.shapes.Circle;
import svgcreator.shapes.Figure;
import svgcreator.shapes.Line;
import svgcreator.shapes.Rectangle;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements the {@link SvgPersistenceService} interface to provide
 * functionality for loading and saving {@link Figure} objects to and from an SVG file.
 * This service handles the parsing of SVG content to create Figure objects and
 * the generation of SVG content from a list of Figure objects.
 */
public class SvgFilePersistenceService implements SvgPersistenceService {

    /**
     * Extracts the value of a specified attribute from an SVG element string.
     * This method uses regular expressions to find attributes in the format {@code attributeName='value'}.
     *
     * @param text The string containing the SVG element (e.g., a single line from an SVG file).
     * @param attributeName The name of the attribute whose value is to be extracted.
     * @return The value of the attribute if found, or {@code null} if the attribute is not present
     *         or does not have a value enclosed in single quotes.
     */
    private String getAttributeValue(String text, String attributeName) {
        // Pattern to find attribute='value'
        // It looks for the attribute name, followed by '=', then captures content within single quotes
        Pattern pattern = Pattern.compile(attributeName + "='([^']*)'");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Loads a list of {@link Figure} objects from the specified SVG file.
     * If the file does not exist, an empty list is returned.
     * The method parses SVG elements like {@code <circle>}, {@code <rect>}, and {@code <line>}
     * from the file content.
     *
     * @param filePath The {@link Path} to the SVG file to load figures from.
     * @return A {@link List} of {@link Figure} objects parsed from the file.
     *         Returns an empty list if the file doesn't exist, is empty, or contains no valid figures.
     *         Error messages are printed to {@code System.err} if issues occur during parsing.
     */
    @Override
    public List<Figure> loadFigures(Path filePath) {
        List<Figure> loadedFigures = new ArrayList<>();
        if (!Files.exists(filePath)) {
            // System.out.println("Info: SVG file not found at " + filePath + ". Starting with no figures."); // Optional info message
            return loadedFigures; // Return empty list if file doesn't exist
        }

        try {
            String svgContent = Files.readString(filePath);
            // Find indices for inner content, skipping the main <svg ...> tag itself
            int startIndex = svgContent.indexOf('>');
            int endIndex = svgContent.lastIndexOf("</svg>");

            if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
                System.err.println("Warning: Invalid or empty SVG structure in file: " + filePath + ". Cannot load figures.");
                return loadedFigures;
            }
            startIndex += 1; // Move past the '>' of the opening <svg> tag

            String innerContent = svgContent.substring(startIndex, endIndex).trim();

            if (!innerContent.isEmpty()) {
                String[] shapeLines = innerContent.split("\\r?\\n"); // Split by newline characters
                for (String line : shapeLines) {
                    parseAndAddFigure(line.trim(), loadedFigures);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for loading figures: " + filePath + " - " + e.getMessage());
        }
        return loadedFigures;
    }

    /**
     * Parses a single line of SVG content, attempts to create a {@link Figure} object from it,
     * and adds it to the provided list of figures.
     * This method supports parsing {@code <circle>}, {@code <rect>}, and {@code <line>} elements.
     *
     * @param line The SVG string line to parse (e.g., {@code "<circle cx='50' cy='50' r='40' fill='red' />"}).
     * @param figures The list to which the parsed {@link Figure} object will be added.
     *                Warnings are printed to {@code System.err} if parsing fails for a line.
     */
    private void parseAndAddFigure(String line, List<Figure> figures) {
        if (line.isEmpty() || !line.startsWith("<")) {
            return; // Skip empty lines or lines not starting with an opening tag
        }

        Figure figure = null;
        try {
            if (line.startsWith("<circle")) {
                String cx = getAttributeValue(line, "cx");
                String cy = getAttributeValue(line, "cy");
                String r = getAttributeValue(line, "r");
                String fill = getAttributeValue(line, "fill");
                if (cx != null && cy != null && r != null && fill != null) {
                    figure = new Circle(Integer.parseInt(cx), Integer.parseInt(cy), Integer.parseInt(r), fill);
                }
            } else if (line.startsWith("<rect")) {
                String x = getAttributeValue(line, "x");
                String y = getAttributeValue(line, "y");
                String w = getAttributeValue(line, "width");
                String h = getAttributeValue(line, "height");
                String fill = getAttributeValue(line, "fill");
                if (x != null && y != null && w != null && h != null && fill != null) {
                    figure = new Rectangle(Integer.parseInt(x), Integer.parseInt(y), fill, Integer.parseInt(w), Integer.parseInt(h));
                }
            } else if (line.startsWith("<line")) {
                String x1 = getAttributeValue(line, "x1");
                String y1 = getAttributeValue(line, "y1");
                String x2 = getAttributeValue(line, "x2");
                String y2 = getAttributeValue(line, "y2");
                String stroke = getAttributeValue(line, "stroke"); // Lines use 'stroke' for color
                if (x1 != null && y1 != null && x2 != null && y2 != null && stroke != null) {
                    figure = new Line(Integer.parseInt(x1), Integer.parseInt(y1), stroke, Integer.parseInt(x2), Integer.parseInt(y2));
                }
            }

            if (figure != null) {
                figures.add(figure);
            } else if (line.startsWith("<")) { // Only warn if it looked like an SVG tag but wasn't recognized
                System.err.println("Warning: Could not parse or missing attributes in line from file: " + line);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Warning: Invalid number format in line from file: " + line + " - " + nfe.getMessage());
        } catch (Exception e) { // Catch any other unexpected errors during parsing of a line
            System.err.println("Warning: Error parsing line from file: " + line + " - " + e.getMessage());
        }
    }


    /**
     * Saves a list of {@link Figure} objects to the specified SVG file.
     * This method OVERWRITES the existing file if it exists, or creates a new file.
     * Each figure in the list is converted to its SVG string representation using its
     * {@code drawFigure()} method.
     *
     * @param figures The {@link List} of {@link Figure} objects to save.
     *                If {@code null} or empty, an empty SVG structure will be written.
     * @param filePath The {@link Path} to the SVG file where the figures will be saved.
     *                 Error messages are printed to {@code System.err} if an {@link IOException} occurs.
     */
    @Override
    public void saveFigures(List<Figure> figures, Path filePath) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) { // Use Files.newBufferedWriter for better charset handling
            writer.println("<svg xmlns='http://www.w3.org/2000/svg' width='500' height='500'>"); // Standard SVG header
            if (figures != null) {
                for (Figure figure : figures) {
                    if (figure != null) { // Add a null check for robustness
                        writer.println("  " + figure.drawFigure()); // Indent for readability
                    }
                }
            }
            writer.println("</svg>");
            // Success message is typically handled by the calling layer (e.g., SvgApplication)
        } catch (IOException e) {
            System.err.println("Error overwriting SVG file: " + filePath + " - " + e.getMessage());
            // Consider re-throwing as a custom runtime exception or returning a boolean status
            // e.g., throw new PersistenceException("Failed to save SVG file", e);
        }
    }
}