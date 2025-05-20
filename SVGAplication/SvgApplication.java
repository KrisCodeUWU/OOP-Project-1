package svgcreator.app;

import svgcreator.app.actions.*; // Import all action classes
import svgcreator.persistence.SvgPersistenceService;
import svgcreator.ui.ConsoleUI;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main application class for the SVG Shape Creator.
 * It orchestrates the user interface, drawing management, and menu actions.
 * This class uses a map-based approach for handling menu actions,
 * delegating specific actions to dedicated classes.
 */
public class SvgApplication {

    private final ConsoleUI ui;
    private final Drawing drawing; // Manages figures and persistence

    private final Map<Integer, String> menu; // For display text
    private final Map<Integer, MenuAction> menuActions; // For executing actions

    /**
     * Constructs an SvgApplication instance.
     *
     * @param ui The console user interface handler.
     * @param persistenceService The service responsible for loading and saving figures.
     * @param svgFilePath The path to the SVG file used for persistence.
     */
    public SvgApplication(ConsoleUI ui, SvgPersistenceService persistenceService, Path svgFilePath) {
        this.ui = ui;
        this.drawing = new SvgDrawing(persistenceService, svgFilePath);
        this.drawing.loadFromFile(); // Load initial figures

        this.menu = createMenu();
        this.menuActions = createMenuActions();
    }

    /**
     * Creates and returns the map of menu options for display.
     * Uses a LinkedHashMap to maintain insertion order.
     *
     * @return A map where keys are option numbers and values are menu item descriptions.
     */
    private Map<Integer, String> createMenu() {
        Map<Integer, String> menuMap = new LinkedHashMap<>();
        menuMap.put(1, "Create Circle");
        menuMap.put(2, "Create Rectangle");
        menuMap.put(3, "Create Line");
        menuMap.put(4, "Delete figure");
        menuMap.put(5, "Show Figures Within Region");
        menuMap.put(6, "Translate figure(s)");
        menuMap.put(7, "Show all figures");
        menuMap.put(8, "Save and Exit");
        return menuMap;
    }

    /**
     * Creates and returns the map linking menu choices to their corresponding actions.
     * Each action is an instance of a class implementing {@link MenuAction}.
     *
     * @return A map where keys are option numbers and values are {@link MenuAction} implementations.
     */
    private Map<Integer, MenuAction> createMenuActions() {
        Map<Integer, MenuAction> actions = new HashMap<>();
        actions.put(1, new CreateCircleAction(ui, drawing));
        actions.put(2, new CreateRectangleAction(ui, drawing));
        actions.put(3, new CreateLineAction(ui, drawing));
        actions.put(4, new DeleteFigureAction(ui, drawing));
        actions.put(5, new ShowFiguresWithinAction(ui, drawing));
        actions.put(6, new TranslateFigureAction(ui, drawing));
        actions.put(7, new ShowAllFiguresAction(ui, drawing));
        actions.put(8, new SaveAndExitAction(ui, drawing));
        return actions;
    }

    /**
     * Runs the main application loop, displaying the menu and processing user choices
     * until the user chooses to exit.
     */
    public void run() {
        boolean running = true;
        while (running) {
            ui.displayMenu(menu);
            int choice = ui.getUserChoice();

            MenuAction action = menuActions.get(choice);

            if (action != null) {
                running = action.execute();
            } else {
                ui.showError("Invalid option. Please try again.");
            }
        }
        ui.showMessage("Exiting application.");
    }
}