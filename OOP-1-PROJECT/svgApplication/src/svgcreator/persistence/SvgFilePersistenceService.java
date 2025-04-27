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

public class SvgFilePersistenceService implements SvgPersistenceService {

    // Moved from svgcreator.Main
    private String getAttributeValue(String text, String attributeName) {
        Pattern pattern = Pattern.compile(attributeName + "='([^']*)'");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    @Override
    public List<Figure> loadFigures(Path filePath) {
        List<Figure> loadedFigures = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return loadedFigures; // Return empty list if file doesn't exist
        }

        try {
            String svgContent = Files.readString(filePath);
            int startIndex = svgContent.indexOf('>');
            int endIndex = svgContent.lastIndexOf("</svg>");

            if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
                System.err.println("Warning: Invalid or empty SVG structure in file. Cannot load figures.");
                return loadedFigures;
            }
            startIndex += 1;
            String innerContent = svgContent.substring(startIndex, endIndex).trim();

            if (!innerContent.isEmpty()) {
                String[] shapeLines = innerContent.split("\\r?\\n");
                for (String line : shapeLines) {
                    parseAndAddFigure(line, loadedFigures);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for loading figures: " + e.getMessage());
        }
        return loadedFigures;
    }

    private void parseAndAddFigure(String line, List<Figure> figures) {
        line = line.trim();
        if (line.isEmpty() || !line.startsWith("<")) {
            return;
        }

        Figure figure = null;
        try {
            if (line.startsWith("<circle")) {
                String cx = getAttributeValue(line, "cx"); String cy = getAttributeValue(line, "cy");
                String r = getAttributeValue(line, "r"); String fill = getAttributeValue(line, "fill");
                if (cx != null && cy != null && r != null && fill != null) {
                    // Consider using Factory if it has complex logic
                    figure = new Circle(Integer.parseInt(cx), Integer.parseInt(cy), Integer.parseInt(r), fill);
                }
            } else if (line.startsWith("<rect")) {
                String x = getAttributeValue(line, "x"); String y = getAttributeValue(line, "y");
                String w = getAttributeValue(line, "width"); String h = getAttributeValue(line, "height");
                String fill = getAttributeValue(line, "fill");
                if (x != null && y != null && w != null && h != null && fill != null) {
                    figure = new Rectangle(Integer.parseInt(x), Integer.parseInt(y), fill, Integer.parseInt(w), Integer.parseInt(h));
                }
            } else if (line.startsWith("<line")) {
                String x1 = getAttributeValue(line, "x1"); String y1 = getAttributeValue(line, "y1");
                String x2 = getAttributeValue(line, "x2"); String y2 = getAttributeValue(line, "y2");
                String stroke = getAttributeValue(line, "stroke");
                if (x1 != null && y1 != null && x2 != null && y2 != null && stroke != null) {
                    figure = new Line(Integer.parseInt(x1), Integer.parseInt(y1), stroke, Integer.parseInt(x2), Integer.parseInt(y2));
                }
            }

            if (figure != null) {
                figures.add(figure);
            } else if (line.startsWith("<")) {
                System.err.println("Warning: Could not parse line from file: " + line);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Warning: Invalid number format in line from file: " + line + " - " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Warning: Error parsing line from file: " + line + " - " + e.getMessage());
        }
    }


    @Override
    public void saveFigures(List<Figure> figures, Path filePath) {
        // This method now OVERWRITES the file completely
        try (PrintWriter writer = new PrintWriter(filePath.toFile())) {
            writer.println("<svg xmlns='http://www.w3.org/2000/svg' width='500' height='500'>");
            if (figures != null) {
                for (Figure figure : figures) {
                    writer.println("  " + figure.drawFigure());
                }
            }
            writer.println("</svg>");
            // Removed success message from here, let controller handle UI message
        } catch (IOException e) {
            System.err.println("Error overwriting SVG file: " + e.getMessage());
            // Consider throwing a custom exception or returning boolean status
        }
    }
}