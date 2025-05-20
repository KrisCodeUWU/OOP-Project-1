package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;
import java.util.List;

/**
 * Represents the action of translating one or all figures in the drawing.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for displaying available figures, prompting the user for translation
 * parameters (which figure to translate or all, and the dx/dy offsets),
 * performing the translation on the {@link Drawing}, and persisting the changes.
 */
public class TranslateFigureAction implements MenuAction {
    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code TranslateFigureAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., displaying figures, getting translation parameters).
     * @param drawing The {@link Drawing} model whose figures will be translated
     *                and which will be responsible for saving the changes.
     */
    public TranslateFigureAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the figure translation process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Retrieves all figures from the {@link Drawing}.</li>
     *   <li>If no figures are available, displays a message and returns.</li>
     *   <li>Displays the list of available figures to the user via the {@link ConsoleUI}.</li>
     *   <li>Prompts the user to enter translation parameters:
     *       <ul>
     *           <li>The 1-based index of the figure to translate, or 0 to translate all figures.</li>
     *           <li>The horizontal translation amount (dx).</li>
     *           <li>The vertical translation amount (dy).</li>
     *       </ul>
     *   </li>
     *   <li>If the translation data is invalid or the user cancels, an error message is shown, and the action ends.</li>
     *   <li>If the user chose to translate all figures (index 0):
     *       <ul>
     *           <li>Calls {@link Drawing#translateAllFigures(int, int)}.</li>
     *           <li>Displays a confirmation message.</li>
     *       </ul>
     *   </li>
     *   <li>If the user chose to translate a single figure:
     *       <ul>
     *           <li>Calls {@link Drawing#translateSingleFigure(int, int, int)}.</li>
     *           <li>If successful, displays a confirmation message.</li>
     *           <li>If unsuccessful (e.g., invalid index), displays an error message.</li>
     *       </ul>
     *   </li>
     *   <li>If any translation occurred, instructs the {@link Drawing} to save its current state to the persistent store
     *       and displays a message confirming the update.</li>
     * </ol>
     *
     * @return {@code true} to indicate that the application should continue running.
     *         This action does not terminate the application.
     */
    @Override
    public boolean execute() {
        List<Figure> allFigures = drawing.getAllFigures();
        if (allFigures.isEmpty()) {
            ui.showMessage("No figures available to translate.");
            return true; // Continue running
        }

        ui.displayFigures("--- Translate Figure(s) --- \nAvailable figures:", allFigures);
        ConsoleUI.TranslateData data = ui.getTranslateParams(allFigures.size());

        if (data == null) {
            ui.showError("Translation cancelled due to invalid input.");
            return true; // Continue running
        }

        boolean translated = false;
        if (data.index() == 0) { // User chose to translate all figures
            ui.showMessage("Translating all figures by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            drawing.translateAllFigures(data.dx(), data.dy());
            ui.showMessage("Translated all figures vertical=" + data.dy() + " horizontal=" + data.dx());
            translated = true;
        } else { // User chose to translate a single figure
            ui.showMessage("Translating figure #" + data.index() + " by dx=" + data.dx() + ", dy=" + data.dy() + "...");
            // Drawing.translateSingleFigure expects a 1-based index
            if (drawing.translateSingleFigure(data.index(), data.dx(), data.dy())) {
                ui.showMessage("Translation complete for figure #" + data.index() + ".");
                translated = true;
            } else {
                // This case should ideally not be reached if ui.getTranslateParams and drawing.translateSingleFigure are robust
                ui.showError("Failed to translate figure #" + data.index() + ". Invalid index.");
            }
        }

        if (translated) {
            drawing.saveToFile(); // Persist the changes if any translation occurred
            ui.showMessage("Drawing updated and saved.");
        }
        return true; // Always continue running after this action
    }
}