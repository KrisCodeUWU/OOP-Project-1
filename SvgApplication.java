package svgcreator.app;

// --- IMPORTS ---
import svgcreator.ui.ConsoleUI;
import svgcreator.persistence.SvgPersistenceService;
import svgcreator.shapes.Figure;
import svgcreator.shapes.Circle;
import svgcreator.shapes.Rectangle;
import svgcreator.shapes.Line;
import svgcreator.factories.CircleFactory;
import svgcreator.factories.RectangleFactory;
import svgcreator.factories.LineFactory;
import svgcreator.utils.GeometryUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// --- END OF IMPORTS ---

/**
 * Main application class for the SVG Shape Creator.
 * It orchestrates the user interface, shape creation, persistence, and other operations.
 * This class uses a map-based approach for handling menu actions.
 */
public class SvgApplication {

    /**
     * Functional interface representing an action to be performed from the menu.
     */
    @FunctionalInterface
    interface MenuAction {
        /**
         * Executes the menu action.
         * @return true if the application should continue running, false if it should exit.
         */
        boolean execute();
    }

    private final ConsoleUI ui;
    private final SvgPersistenceService persistenceService;
    private final Path svgFilePath;
    private final List<Figure> currentSessionShapes;
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
        this.persistenceService = persistenceService;
        this.svgFilePath = svgFilePath;
        this.currentSessionShapes = new ArrayList<>();
        this.menu = createMenu(); // Create the display text map
        this.menuActions = createMenuActions(); // Create the action map
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
        menuMap.put(4, "Save and Exit");
        menuMap.put(5, "Delete figure");
        menuMap.put(6, "Translate figure");
        menuMap.put(7, "Show all figures");
        menuMap.put(8, "Show Within");
        return menuMap;
    }

    /**
     * Creates and returns the map linking menu choices to their corresponding actions.
     *
     * @return A map where keys are option numbers and values are {@link MenuAction} implementations.
     */
    private Map<Integer, MenuAction> createMenuActions() {
        Map<Integer, MenuAction> actions = new HashMap<>();
        actions.put(1, this::createCircleAction);
        actions.put(2, this::createRectangleAction);
        actions.put(3, this::createLineAction);
        actions.put(4, this::saveAndExitAction);
        actions.put(5, this::deleteFigureAction);
        actions.put(6, this::translateFigureAction);
        actions.put(7, this::showAllFiguresAction);
        actions.put(8, this::showFiguresWithinAction);
        return actions;
    }

    // --- Wrapper methods to match MenuAction signature ---

    /** Wrapper for {@link #createCircle()} to fit the {@link MenuAction} interface. */
    private boolean createCircleAction() { createCircle(); return true; }
    /** Wrapper for {@link #createRectangle()} to fit the {@link MenuAction} interface. */
    private boolean createRectangleAction() { createRectangle(); return true; }
    /** Wrapper for {@link #createLine()} to fit the {@link MenuAction} interface. */
    private boolean createLineAction() { createLine(); return true; }
    /** Wrapper for {@link #deleteFigure()} to fit the {@link MenuAction} interface. */
    private boolean deleteFigureAction() { deleteFigure(); return true; }
    /** Wrapper for {@link #translateFigure()} to fit the {@link MenuAction} interface. */
    private boolean translateFigureAction() { translateFigure(); return true; }
    /** Wrapper for {@link #showAllFigures()} to fit the {@link MenuAction} interface. */
    private boolean showAllFiguresAction() { showAllFigures(); return true; }
    /** Wrapper for {@link #showFiguresWithin()} to fit the {@link MenuAction} interface. */
    private boolean showFiguresWithinAction() { showFiguresWithin(); return true; }

    /**
     * Wrapper for {@link #saveAndExit()} to fit the {@link MenuAction} interface.
     * Signals the application to stop.
     * @return false to indicate the application should exit.
     */
    private boolean saveAndExitAction() {
        saveAndExit();
        return false;
    }
    // --- End of Wrapper Methods ---

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
                ui.showMessage("You selected: " + menu.get(choice));
                running = action.execute();
            } else {
                ui.showError("Invalid option. Please try again.");
            }
        }
        ui.showMessage("Exiting application.");
    }

    // --- Original Action Handlers ---

    /**
     * Handles the creation of a new circle. Prompts the user for details
     * and adds the created circle to the current session.
     */
    private void createCircle() {
        ConsoleUI.ShapeData data = ui.getCircleDetails();
        if (data != null) {
            CircleFactory factory = new CircleFactory(data.x(), data.y(), data.param1(), data.color());
            Figure circle = factory.createFigure();
            currentSessionShapes.add(circle);
            ui.showMessage("Circle added to current session.");
        }
    }

    /**
     * Handles the creation of a new rectangle. Prompts the user for details
     * and adds the created rectangle to the current session.
     */
    private void createRectangle() {
        ConsoleUI.ShapeData data = ui.getRectangleDetails();
        if (data != null) {
            RectangleFactory factory = new RectangleFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure rect = factory.createFigure();
            currentSessionShapes.add(rect);
            ui.showMessage("Rectangle added to current session.");
        }
    }

    /**
     * Handles the creation of a new line. Prompts the user for details
     * and adds the created line to the current session.
     */
    private void createLine() {
        ConsoleUI.ShapeData data = ui.getLineDetails();
        if (data != null) {
            LineFactory factory = new LineFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure line = factory.createFigure();
            currentSessionShapes.add(line);
            ui.showMessage("Line added to current session.");
        }
    }

    /**
     * Retrieves all figures, combining those loaded from the SVG file and those
     * currently in the session.
     *
     * @return A list containing all figures.
     */
    private List<Figure> getAllFiguresCombined() {
        List<Figure> loaded = persistenceService.loadFigures(svgFilePath);
        List<Figure> combined = new ArrayList<>(loaded);
        combined.addAll(currentSessionShapes);
        return combined;
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

    /**
     * Displays all figures (both from file and current session) to the console.
     */
    private void showAllFigures() {
        List<Figure> combinedFigures = getAllFiguresCombined();
        List<String> formatted = formatFiguresForDisplay(combinedFigures);
        ui.displayFigures("--- All Figures (Saved & Current) ---", formatted);
    }

    /**
     * Handles the deletion of a figure. Displays available figures, prompts the user
     * for which one to delete, removes it, and updates the SVG file.
     */
    private void deleteFigure() {
        List<Figure> allFigures = getAllFiguresCombined();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to delete.");
            return;
        }

        ui.displayFigures("--- Delete Figure --- \nAvailable figures:", formatFiguresForDisplay(allFigures));
        int indexToDelete = ui.getDeleteIndex(allFigures.size());

        if (indexToDelete == 0) {
            ui.showMessage("Deletion cancelled.");
            return;
        }

        int listIndex = indexToDelete - 1;
        Figure removed = allFigures.remove(listIndex);
        ui.showMessage("Removed figure #" + indexToDelete + ": " + removed.getClass().getSimpleName());

        persistenceService.saveFigures(allFigures, svgFilePath);
        currentSessionShapes.clear(); // Reset session as its state is now in the file
        ui.showMessage("File updated. Session cleared.");
    }

    /**
     * Handles the translation of a figure or all figures. Prompts the user for
     * parameters, performs the translation, and updates the SVG file.
     */
    private void translateFigure() {
        List<Figure> allFigures = getAllFiguresCombined();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to translate.");
            return;
        }

        ui.displayFigures("--- Translate Figure(s) --- \nAvailable figures:", formatFiguresForDisplay(allFigures));
        ConsoleUI.TranslateData data = ui.getTranslateParams(allFigures.size());

        if (data == null) {
            ui.showError("Translation cancelled due to invalid input.");
            return;
        }

        if (data.index() == 0) { // Translate all
            ui.showMessage("Translating all figures by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            for (Figure figure : allFigures) {
                GeometryUtils.translateFigure(figure, data.dx(), data.dy());
            }
            ui.showMessage("Translated all figures vertical=" + data.dy() + " horizontal=" + data.dx());
        } else { // Translate one
            int listIndex = data.index() - 1;
            Figure figureToTranslate = allFigures.get(listIndex);
            ui.showMessage("Translating figure #" + data.index() + " by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            GeometryUtils.translateFigure(figureToTranslate, data.dx(), data.dy());
            ui.showMessage("Translation complete for figure #" + data.index() + ".");
        }

        persistenceService.saveFigures(allFigures, svgFilePath);
        currentSessionShapes.clear(); // Reset session
        ui.showMessage("File updated. Session cleared.");
    }

    /**
     * Handles displaying figures that are within a specified boundary (circle or rectangle).
     * Prompts the user for boundary details and shows the matching figures.
     */
    private void showFiguresWithin() {
        ConsoleUI.BoundaryData boundary = ui.getBoundaryDetails();
        if (boundary == null) {
            ui.showError("Show Within cancelled due to invalid input.");
            return;
        }

        List<Figure> allFigures = getAllFiguresCombined();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to check.");
            return;
        }

        List<Figure> figuresWithin = new ArrayList<>();
        for (Figure figure : allFigures) {
            if (GeometryUtils.isFigureWithinBoundary(figure, boundary.type(), boundary.x(), boundary.y(), boundary.param1(), boundary.param2())) {
                figuresWithin.add(figure);
            }
        }

        String header = "\n> within " + boundary.type() + " " + boundary.x() + " " + boundary.y() + " " + boundary.param1() + (boundary.type().equals("rectangle") ? " " + boundary.param2() : "");
        ui.displayFigures(header, formatFiguresForDisplay(figuresWithin));
        if (figuresWithin.isEmpty()) {
            ui.showMessage("No figures found completely within the specified region.");
        }
    }

    /**
     * Saves all current and previously loaded figures to the SVG file and clears the session.
     * This method is called when the user chooses to exit the application.
     */
    private void saveAndExit() {
        List<Figure> finalFigures = getAllFiguresCombined();
        persistenceService.saveFigures(finalFigures, svgFilePath);
        currentSessionShapes.clear();
        ui.showMessage("All figures saved to " + svgFilePath);
    }
}