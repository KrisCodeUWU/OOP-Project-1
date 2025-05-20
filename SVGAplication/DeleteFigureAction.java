package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;
import java.util.List;

/**
 * Represents the action of deleting a figure from the drawing.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for displaying available figures, prompting the user for which
 * figure to delete, removing it from the {@link Drawing}, and persisting
 * the changes.
 */
public class DeleteFigureAction implements MenuAction {
    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code DeleteFigureAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., displaying figures, getting the index of the figure to delete).
     * @param drawing The {@link Drawing} model from which the figure will be deleted
     *                and which will be responsible for saving the changes.
     */
    public DeleteFigureAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the figure deletion process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Retrieves all figures from the {@link Drawing}.</li>
     *   <li>If no figures are available, displays a message and returns.</li>
     *   <li>Displays the list of available figures to the user via the {@link ConsoleUI}.</li>
     *   <li>Prompts the user to enter the 1-based index of the figure to delete, or 0 to cancel.</li>
     *   <li>If the user cancels (enters 0), displays a cancellation message and returns.</li>
     *   <li>Attempts to retrieve the figure to be deleted (for display purposes) and then remove it from the {@link Drawing}.</li>
     *   <li>If removal is successful:
     *     <ul>
     *       <li>Displays a confirmation message including the type of the removed figure.</li>
     *       <li>Instructs the {@link Drawing} to save its current state to the persistent store.</li>
     *       <li>Displays a message confirming that the drawing was updated and saved.</li>
     *     </ul>
     *   </li>
     *   <li>If removal fails (e.g., due to an invalid index not caught by {@code ui.getDeleteIndex}),
     *       displays an error message.</li>
     * </ol>
     *
     * @return {@code true} to indicate that the application should continue running.
     *         This action does not terminate the application.
     */
    @Override
    public boolean execute() {
        List<Figure> allFigures = drawing.getAllFigures();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to delete.");
            return true; // Continue running
        }

        ui.displayFigures("--- Delete Figure --- \nAvailable figures:", allFigures);
        int indexToDelete = ui.getDeleteIndex(allFigures.size()); // ui.getDeleteIndex handles 1-based indexing and max size

        if (indexToDelete == 0) { // User chose to cancel
            ui.showMessage("Deletion cancelled.");
            return true; // Continue running
        }

        // Get the figure before removing it, for a more informative message
        Figure removedFigure = drawing.getFigure(indexToDelete);

        if (drawing.removeFigure(indexToDelete)) { // Drawing.removeFigure expects a 1-based index
            ui.showMessage("Removed figure #" + indexToDelete + ": " +
                    (removedFigure != null ? removedFigure.getClass().getSimpleName() : "Unknown type"));
            drawing.saveToFile(); // Persist the changes
            ui.showMessage("Drawing updated and saved.");
        } else {
            // This case should ideally not be reached if ui.getDeleteIndex and drawing.removeFigure are robust,
            // but it's good for defensive programming.
            ui.showError("Failed to remove figure #" + indexToDelete + ". The figure might have been already removed or the index was invalid.");
        }
        return true; // Always continue running after this action
    }
}