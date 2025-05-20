package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.ui.ConsoleUI;

/**
 * Represents the action of displaying all figures currently loaded or created
 * in the drawing.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for retrieving all figures from the {@link Drawing} and
 * displaying them to the user via the {@link ConsoleUI}.
 */
public class ShowAllFiguresAction implements MenuAction {

    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code ShowAllFiguresAction} instance.
     *
     * @param ui The console user interface used for displaying the figures to the user.
     * @param drawing The {@link Drawing} model from which the figures will be retrieved.
     */
    public ShowAllFiguresAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the action to display all figures.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Retrieves a list of all figures from the {@link Drawing}.</li>
     *   <li>Calls the {@link ConsoleUI} to display these figures under a specific header.</li>
     * </ol>
     *
     * @return {@code true} to indicate that the application should continue running.
     *         This action does not terminate the application.
     */
    @Override
    public boolean execute() {
        // Retrieve all figures from the drawing and pass them to the UI for display.
        // The UI is responsible for formatting the figures for console output.
        ui.displayFigures("--- All Figures in Drawing ---", drawing.getAllFigures());
        return true; // Always continue running after this action
    }
}