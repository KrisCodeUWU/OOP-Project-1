package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;
import java.util.List;

/**
 * Represents the action of displaying all figures that are completely contained
 * within a user-specified boundary (either a rectangle or a circle).
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for prompting the user for boundary details, querying the
 * {@link Drawing} for matching figures, and displaying the results via the
 * {@link ConsoleUI}.
 */
public class ShowFiguresWithinAction implements MenuAction {
    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code ShowFiguresWithinAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., getting boundary details, displaying results).
     * @param drawing The {@link Drawing} model used to retrieve all figures and
     *                to query for figures within the specified boundary.
     */
    public ShowFiguresWithinAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the process of showing figures within a specified boundary.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Prompts the user for boundary details (type, position, dimensions) via the {@link ConsoleUI}.</li>
     *   <li>If the boundary data is invalid or the user cancels, an error message is shown, and the action ends.</li>
     *   <li>Checks if there are any figures in the {@link Drawing}. If not, a message is shown, and the action ends.</li>
     *   <li>Calls the {@link Drawing#getFiguresWithinBoundary(String, int, int, int, int)} method
     *       to get a list of figures that are completely within the specified boundary.</li>
     *   <li>Constructs a header string that describes the boundary parameters.</li>
     *   <li>Calls the {@link ConsoleUI#displayFigures(String, List)} method to show the found figures (or an empty list).</li>
     *   <li>If no figures were found within the boundary, an additional message is displayed to inform the user.</li>
     * </ol>
     *
     * @return {@code true} to indicate that the application should continue running.
     *         This action does not terminate the application.
     */
    @Override
    public boolean execute() {
        ConsoleUI.BoundaryData boundary = ui.getBoundaryDetails();
        if (boundary == null) {
            ui.showError("Show Within cancelled due to invalid input.");
            return true; // Continue running
        }

        if (drawing.getFigureCount() == 0) {
            ui.showMessage("No figures available to check.");
            return true; // Continue running
        }

        List<Figure> figuresWithin = drawing.getFiguresWithinBoundary(
                boundary.type(), boundary.x(), boundary.y(), boundary.param1(), boundary.param2()
        );

        // Construct a descriptive header for the output
        String header = "\n> Figures within " + boundary.type() + " " +
                boundary.x() + " " + boundary.y() + " " + boundary.param1() +
                (boundary.type().equalsIgnoreCase("rectangle") ? " " + boundary.param2() : "");
        // The ConsoleUI will handle formatting and displaying the list of figures
        ui.displayFigures(header, figuresWithin);

        if (figuresWithin.isEmpty()) {
            ui.showMessage("No figures found completely within the specified region.");
        }
        return true; // Always continue running after this action
    }
}