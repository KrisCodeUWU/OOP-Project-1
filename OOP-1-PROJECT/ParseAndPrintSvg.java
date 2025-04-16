import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseAndPrintSvg {

    // Helper function to extract attribute value using regex (handles single quotes)
    private static String getAttributeValue(String text, String attributeName) {
        // Pattern to find attribute='value'
        // It looks for the attribute name, followed by '=', then captures content within single quotes
        Pattern pattern = Pattern.compile(attributeName + "='([^']*)'");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1); // Return the captured group (the value)
        }
        return null; // Return null if attribute not found
    }

    public static void main(String[] args) {
        // Use the correct path to your SVG file
        String filePath = "E:/programing/TU/Java/OOP-1-PROJECT/output.svg"; // Corrected path
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            System.err.println("Error: File not found at " + filePath);
            return;
        }

        try {
            // 1. Read the entire file content
            String svgContent = Files.readString(path);

            // 2. Find indices for inner content
            int startIndex = svgContent.indexOf('>');
            int endIndex = svgContent.lastIndexOf("</svg>");

            if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
                System.err.println("Error: Could not find valid <svg>...</svg> tags or content.");
                return;
            }
            startIndex += 1; // Move past the '>'

            // Extract the inner content
            String innerContent = svgContent.substring(startIndex, endIndex).trim();

            // 3. Split into lines
            String[] shapeLines = innerContent.split("\\r?\\n"); // Split by newline characters

            System.out.println("> print"); // Print the header as requested

            int figureCount = 0;
            // 4. Process each line
            for (String line : shapeLines) {
                line = line.trim(); // Remove leading/trailing whitespace
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                figureCount++;
                String output = figureCount + ". ";

                // 5. Identify shape and extract attributes
                if (line.startsWith("<circle")) {
                    String cx = getAttributeValue(line, "cx");
                    String cy = getAttributeValue(line, "cy");
                    String r = getAttributeValue(line, "r");
                    String fill = getAttributeValue(line, "fill");
                    // Format: circle x y radius color
                    output += "circle " + cx + " " + cy + " " + r + " " + fill;

                } else if (line.startsWith("<rect")) { // Assuming rectangle might be added later
                    String x = getAttributeValue(line, "x");
                    String y = getAttributeValue(line, "y");
                    String width = getAttributeValue(line, "width");
                    String height = getAttributeValue(line, "height");
                    String fill = getAttributeValue(line, "fill");
                    // Format: rectangle x y width height color
                    output += "rectangle " + x + " " + y + " " + width + " " + height + " " + fill;

                } else if (line.startsWith("<line")) {
                    String x1 = getAttributeValue(line, "x1");
                    String y1 = getAttributeValue(line, "y1");
                    String x2 = getAttributeValue(line, "x2");
                    String y2 = getAttributeValue(line, "y2");
                    String stroke = getAttributeValue(line, "stroke"); // Lines use 'stroke'
                    // Format: line x1 y1 x2 y2 color
                    output += "line " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + stroke;

                } else {
                    // Skip lines that don't match known shapes or are comments etc.
                    figureCount--; // Decrement count as this wasn't a recognized figure
                    continue;
                }

                // 6. Print formatted output
                System.out.println(output);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}