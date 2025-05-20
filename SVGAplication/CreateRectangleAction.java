package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.factories.RectangleFactory;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;

/**
 * Represents the action of creating a new rectangle figure.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for prompting the user for rectangle details, creating a rectangle
 * using a {@link RectangleFactory}, and adding it to the {@link Drawing}.
 */
public class CreateRectangleAction implements MenuAction {

    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code CreateRectangleAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., getting rectangle details).
     * @param drawing The {@link Drawing} model to which the newly created rectangle
     *                will be added.
     */
    public CreateRectangleAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the rectangle creation process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Prompts the user for rectangle details (position, width, height, color) via the {@link ConsoleUI}.</li>
     *   <li>If valid details are provided:
     *     <ul>
     *       <li>Creates a {@link RectangleFactory} with the obtained details.</li>
     *       <li>Uses the factory to create a {@link Figure} (specifically, a Rectangle).</li>
     *       <li>Adds the created rectangle to the {@link Drawing}.</li>
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
        ConsoleUI.ShapeData data = ui.getRectangleDetails();
        if (data != null) {
            // data.param1() is width, data.param2() is height for a rectangle
            RectangleFactory factory = new RectangleFactory(data.x(), data.y(), data.color(), data.param1(), data.param2());
            Figure rect = factory.createFigure();
            drawing.addFigure(rect);
            ui.showMessage("Rectangle added to drawing.");
        }
        return true; // Always continue running after this action
    }
}