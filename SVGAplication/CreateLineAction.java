package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.factories.LineFactory;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;

/**
 * Represents the action of creating a new line figure.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for prompting the user for line details, creating a line
 * using a {@link LineFactory}, and adding it to the {@link Drawing}.
 */
public class CreateLineAction implements MenuAction {

    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code CreateLineAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., getting line details).
     * @param drawing The {@link Drawing} model to which the newly created line
     *                will be added.
     */
    public CreateLineAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the line creation process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Prompts the user for line details (start/end points, color) via the {@link ConsoleUI}.</li>
     *   <li>If valid details are provided:
     *     <ul>
     *       <li>Creates a {@link LineFactory} with the obtained details.</li>
     *       <li>Uses the factory to create a {@link Figure} (specifically, a Line).</li>
     *       <li>Adds the created line to the {@link Drawing}.</li>
     *       <li>Displays a confirmation message to the user.</li>
     *     </ul>
     *   </li>
     * </ol>
     *
     * @return {@code true} to indicate that the application should continue running.
     *         This action does not terminate the application.
     */
    @Override
    public boolean execute() {
        ConsoleUI.ShapeData data = ui.getLineDetails();
        if (data != null) {
            // data.x() is x1, data.y() is y1, data.param1() is x2, data.param2() is y2 for a line
            LineFactory factory = new LineFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure line = factory.createFigure();
            drawing.addFigure(line);
            ui.showMessage("Line added to drawing.");
        }
        return true; // Always continue running after this action
    }
}