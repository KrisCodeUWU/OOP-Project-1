package svgcreator.app;

// --- ADD ALL THESE IMPORTS ---
import svgcreator.ui.ConsoleUI; // For using the UI
import svgcreator.persistence.SvgPersistenceService; // For using the persistence service
import svgcreator.shapes.Figure; // Base shape class
import svgcreator.shapes.Circle; // Specific shape (already there, but good to be explicit)
import svgcreator.shapes.Rectangle; // Specific shape
import svgcreator.shapes.Line; // Specific shape
import svgcreator.factories.CircleFactory; // Factory for circles
import svgcreator.factories.RectangleFactory; // Factory for rectangles
import svgcreator.factories.LineFactory; // Factory for lines
import svgcreator.utils.GeometryUtils; // For geometry calculations

// Keep existing Java utility imports
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
// --- END OF IMPORTS TO ADD ---

public class SvgApplication {

    private final ConsoleUI ui;
    private final SvgPersistenceService persistenceService;
    private final Path svgFilePath;
    private final List<Figure> currentSessionShapes;
    private final Map<Integer, String> menu;

    public SvgApplication(ConsoleUI ui, SvgPersistenceService persistenceService, Path svgFilePath) {
        this.ui = ui;
        this.persistenceService = persistenceService;
        this.svgFilePath = svgFilePath;
        this.currentSessionShapes = new ArrayList<>();
        this.menu = createMenu();
    }

    private Map<Integer, String> createMenu() {
        Map<Integer, String> menuMap = new LinkedHashMap<>();
        menuMap.put(1, "Create svgcreator.shapes.Circle");
        menuMap.put(2, "Create svgcreator.shapes.Rectangle");
        menuMap.put(3, "Create svgcreator.shapes.Line");
        menuMap.put(4, "Save and Exit");
        menuMap.put(5, "Delete figure");
        menuMap.put(6, "Translate figure");
        menuMap.put(7, "Show all figures");
        menuMap.put(8, "Show Within");
        return menuMap;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ui.displayMenu(menu);
            int choice = ui.getUserChoice();

            if (menu.containsKey(choice)) {
                ui.showMessage("You selected: " + menu.get(choice));
                running = processChoice(choice); // processChoice returns false if exiting
            } else {
                ui.showError("Invalid option. Please try again.");
            }
        }
        ui.showMessage("Exiting application.");
    }

    private boolean processChoice(int choice) {
        switch (choice) {
            case 1 -> createCircle();
            case 2 -> createRectangle();
            case 3 -> createLine();
            case 4 -> { saveAndExit(); return false; } // Signal exit
            case 5 -> deleteFigure();
            case 6 -> translateFigure();
            case 7 -> showAllFigures();
            case 8 -> showFiguresWithin();
            default -> ui.showError("Invalid option selected (should not happen).");
        }
        return true; // Continue running
    }

    // --- Action Handlers ---

    private void createCircle() {
        ConsoleUI.ShapeData data = ui.getCircleDetails();
        if (data != null) {
            // Use Factory
            CircleFactory factory = new CircleFactory(data.x(), data.y(), data.param1(), data.color());
            Figure circle = factory.createFigure();
            currentSessionShapes.add(circle);
            ui.showMessage("svgcreator.shapes.Circle added to current session.");
        }
    }

    private void createRectangle() {
        ConsoleUI.ShapeData data = ui.getRectangleDetails();
        if (data != null) {
            RectangleFactory factory = new RectangleFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure rect = factory.createFigure();
            currentSessionShapes.add(rect);
            ui.showMessage("svgcreator.shapes.Rectangle added to current session.");
        }
    }

    private void createLine() {
        ConsoleUI.ShapeData data = ui.getLineDetails();
        if (data != null) {
            // Note: data uses param1=x2, param2=y2
            LineFactory factory = new LineFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure line = factory.createFigure();
            currentSessionShapes.add(line);
            ui.showMessage("svgcreator.shapes.Line added to current session.");
        }
    }

    private List<Figure> getAllFiguresCombined() {
        List<Figure> loaded = persistenceService.loadFigures(svgFilePath);
        // Combine carefully - avoid adding duplicates if session shapes might already be loaded
        // For simplicity now, just combine. A more robust solution might track session shapes better.
        List<Figure> combined = new ArrayList<>(loaded);
        combined.addAll(currentSessionShapes);
        return combined;
    }

    private List<String> formatFiguresForDisplay(List<Figure> figures) {
        List<String> formatted = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            formatted.add(formatSingleFigure(figures.get(i), i + 1));
        }
        return formatted;
    }

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


    private void showAllFigures() {
        List<Figure> combinedFigures = getAllFiguresCombined();
        List<String> formatted = formatFiguresForDisplay(combinedFigures);
        ui.displayFigures("--- All Figures (Saved & Current) ---", formatted);
    }

    private void deleteFigure() {
        List<Figure> allFigures = getAllFiguresCombined();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to delete.");
            return;
        }

        ui.displayFigures("--- Delete svgcreator.shapes.Figure --- \nAvailable figures:", formatFiguresForDisplay(allFigures));
        int indexToDelete = ui.getDeleteIndex(allFigures.size());

        if (indexToDelete == 0) {
            ui.showMessage("Deletion cancelled.");
            return;
        }

        int listIndex = indexToDelete - 1;
        Figure removed = allFigures.remove(listIndex);
        ui.showMessage("Removed figure #" + indexToDelete + ": " + removed.getClass().getSimpleName());

        // Overwrite file and clear session
        persistenceService.saveFigures(allFigures, svgFilePath);
        currentSessionShapes.clear(); // Reset session state
        ui.showMessage("File updated. Session cleared.");
    }

    private void translateFigure() {
        List<Figure> allFigures = getAllFiguresCombined();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to translate.");
            return;
        }

        ui.displayFigures("--- Translate svgcreator.shapes.Figure(s) --- \nAvailable figures:", formatFiguresForDisplay(allFigures));
        ConsoleUI.TranslateData data = ui.getTranslateParams(allFigures.size());

        if (data == null) { // Input failed
            ui.showError("Translation cancelled due to invalid input.");
            return;
        }

        if (data.index() == 0) { // Translate all
            ui.showMessage("Translating all figures by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            for (Figure figure : allFigures) {
                GeometryUtils.translateFigure(figure, data.dx(), data.dy());
            }
            ui.showMessage("Translated all figures vertical=" + data.dy() + " horizontal=" + data.dx()); // Updated message
        } else { // Translate one
            int listIndex = data.index() - 1;
            Figure figureToTranslate = allFigures.get(listIndex);
            ui.showMessage("Translating figure #" + data.index() + " by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            GeometryUtils.translateFigure(figureToTranslate, data.dx(), data.dy());
            ui.showMessage("Translation complete for figure #" + data.index() + ".");
        }

        // Overwrite file and clear session
        persistenceService.saveFigures(allFigures, svgFilePath);
        currentSessionShapes.clear(); // Reset session state
        ui.showMessage("File updated. Session cleared.");
    }

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

    private void saveAndExit() {
        // Overwrite the file with the final combined state
        List<Figure> finalFigures = getAllFiguresCombined();
        persistenceService.saveFigures(finalFigures, svgFilePath);
        currentSessionShapes.clear(); // Good practice
        ui.showMessage("All figures saved to " + svgFilePath);
    }
}