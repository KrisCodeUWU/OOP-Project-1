package svgcreator;

import svgcreator.app.SvgApplication;
import svgcreator.persistence.SvgFilePersistenceService;
import svgcreator.persistence.SvgPersistenceService;
import svgcreator.ui.ConsoleUI;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The main entry point for the SVG Creator application.
 * This class is responsible for initializing the core components of the application,
 * such as the UI, persistence service, and the main application logic,
 * and then starting the application's execution flow.
 */
public class Main {

    /**
     * The default file name (and path, if relative) for the SVG output file.
     */
    private static final String SVG_FILE_PATH = "output.svg";

    /**
     * The main method that serves as the entry point for the Java application.
     * It sets up the necessary services (UI, persistence) and launches the
     * {@link SvgApplication}.
     * <p>
     * The {@link ConsoleUI} is created within a try-with-resources block to ensure
     * its underlying resources (like the {@link java.util.Scanner}) are properly closed
     * upon completion or in case of an error.
     * </p>
     * Any unhandled exceptions during the application's setup or run phase are
     * caught, and an error message is printed to the standard error stream.
     *
     * @param args Command-line arguments passed to the application (not used by this application).
     */
    public static void main(String[] args) {
        Path path = Paths.get(SVG_FILE_PATH);

        // Use try-with-resources for ConsoleUI as it implements AutoCloseable
        // This ensures that the Scanner within ConsoleUI is closed automatically.
        try (ConsoleUI ui = new ConsoleUI()) {
            // Initialize the persistence service implementation
            SvgPersistenceService persistence = new SvgFilePersistenceService();

            // Create the main application instance with its dependencies
            SvgApplication application = new SvgApplication(ui, persistence, path);

            // Start the application's main loop
            application.run();

        } catch (Exception e) {
            // Catch any unexpected exceptions that might occur during setup or execution
            // and print an error message and stack trace for debugging.
            System.err.println("An unexpected error occurred in the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}