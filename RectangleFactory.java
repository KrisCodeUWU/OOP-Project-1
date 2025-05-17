package svgcreator.factories;
import svgcreator.shapes.Rectangle;
import svgcreator.shapes.Figure;

/**
 * A factory class responsible for creating {@link Rectangle} objects.
 * This class implements the {@link FigureFactory} interface, providing a specific
 * way to instantiate rectangles with given parameters.
 */
public class RectangleFactory implements FigureFactory {
    private int xAxis, yAxis, width, height;
    private String fill;

    /**
     * Constructs a {@code RectangleFactory} with the specified parameters for the rectangle to be created.
     *
     * @param xAxis The x-coordinate of the rectangle's top-left corner.
     * @param yAxis The y-coordinate of the rectangle's top-left corner.
     * @param fill The fill color of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RectangleFactory(int xAxis, int yAxis, String fill, int width, int height) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.fill = fill;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates and returns a new {@link Rectangle} instance.
     * The rectangle is created using the parameters provided to this factory's constructor.
     *
     * @return A new {@link Rectangle} object, cast as a {@link Figure}.
     */
    @Override
    public Figure createFigure() {
        return new Rectangle(xAxis, yAxis, fill, width, height);
    }
}