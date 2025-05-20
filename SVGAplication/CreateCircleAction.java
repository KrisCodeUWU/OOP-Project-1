package svgcreator.app.actions;

import svgcreator.app.Drawing;
import svgcreator.app.MenuAction;
import svgcreator.factories.CircleFactory;
import svgcreator.shapes.Figure;
import svgcreator.ui.ConsoleUI;

/**
 * Represents the action of creating a new circle figure.
 * This class implements the {@link MenuAction} interface and encapsulates
 * the logic for prompting the user for circle details, creating a circle
 * using a {@link CircleFactory}, and adding it to the {@link Drawing}.
 */
public class CreateCircleAction implements MenuAction {
    private final ConsoleUI ui;
    private final Drawing drawing;

    /**
     * Constructs a {@code CreateCircleAction} instance.
     *
     * @param ui The console user interface used for interacting with the user
     *           (e.g., getting circle details).
     * @param drawing The {@link Drawing} model to which the newly created circle
     *                will be added.
     */
    public CreateCircleAction(ConsoleUI ui, Drawing drawing) {
        this.ui = ui;
        this.drawing = drawing;
    }

    /**
     * Executes the circle creation process.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Prompts the user for circle details (position, radius, color) via the {@link ConsoleUI}.</li>
     *   <li>If valid details are provided:
     *     <ul>
     *       <li>Creates a {@link CircleFactory} with the obtained details.</li>
     *       <li>Uses the factory to create a {@link Figure} (specifically, a Circle).</li>
     *       <li>Adds the created circle to the {@link Drawing}.</li>
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
        ConsoleUI.ShapeData data = ui.getCircleDetails();
        if (data != null) {
            // data.param1() is the radius for a circle
            CircleFactory factory = new CircleFactory(data.x(), data.y(), data.param1(), data.color());
            Figure circle = factory.createFigure();
            drawing.addFigure(circle);
            ui.showMessage("Circle added to drawing.");
        }
        return true; // Always continue running after this action
    }
}