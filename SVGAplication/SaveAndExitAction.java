package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.ui.ConsoleUI;

/**
 * Represents the action of saving all figures in the drawing to a file
 * and then signaling the application to exit.
 * This class implements the {@link MenuAction} interface.
 */
public class SaveAndExitAction implements MenuAction {
    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code SaveAndExitAction} instance.
     *
     * @param ui The console user interface used for displaying messages to the user.
     * @param drawing The {@link Drawing} model whose current state will be saved
     *                to the persistent store.
     */
    public SaveAndExitAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the save and exit process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Instructs the {@link Drawing} to save its current state to the persistent store.</li>
     *   <li>Displays a confirmation message to the user via the {@link ConsoleUI} indicating
     *       that all figures have been saved.</li>
     *   <li>Returns {@code false} to signal the main application loop to terminate.</li>
     * </ol>
     *
     * @return {@code false} to indicate that the application should stop running.
     */
    @Override
    public boolean execute() {
        drawing.saveToFile();
        ui.showMessage("All figures saved.");
        return false; // Signal to exit the application
    }
}