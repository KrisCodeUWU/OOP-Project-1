package svgcreator; // Make sure you have the package declaration if Main is in svgcreator

import svgcreator.app.SvgApplication;
import svgcreator.persistence.SvgFilePersistenceService; // <-- Added import
import svgcreator.persistence.SvgPersistenceService;   // <-- Added import
import svgcreator.ui.ConsoleUI;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final String SVG_FILE_PATH = "output.svg";
    public static void main(String[] args) {
        Path path = Paths.get(SVG_FILE_PATH);

        // Use try-with-resources for svgcreator.ui.ConsoleUI if it manages the Scanner
        try (ConsoleUI ui = new ConsoleUI()) {
            // Now the compiler knows where to find these types
            SvgPersistenceService persistence = new SvgFilePersistenceService();
            SvgApplication application = new SvgApplication(ui, persistence, path);
            application.run();
        } // ui.close() is called automatically, closing the scanner
        catch (Exception e) {
            // Catch potential exceptions during setup or run
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}