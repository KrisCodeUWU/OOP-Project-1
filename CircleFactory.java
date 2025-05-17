package svgcreator.factories;
import svgcreator.shapes.Figure;
import svgcreator.shapes.Circle;

/**
 * A factory class responsible for creating {@link Circle} objects.
 * This class implements the {@link FigureFactory} interface, providing a specific
 * way to instantiate circles with given parameters.
 */
public class CircleFactory implements FigureFactory {
    private int xAxis, yAxis, radius;
    private String fill;

    /**
     * Constructs a {@code CircleFactory} with the specified parameters for the circle to be created.
     *
     * @param xAxis The x-coordinate of the circle's center.
     * @param yAxis The y-coordinate of the circle's center.
     * @param radius The radius of the circle.
     * @param fill The fill color of the circle.
     */
    public CircleFactory(int xAxis, int yAxis, int radius, String fill) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.radius = radius;
        this.fill = fill;
    }

    /**
     * Creates and returns a new {@link Circle} instance.
     * The circle is created using the parameters provided to this factory's constructor.
     *
     * @return A new {@link Circle} object, cast as a {@link Figure}.
     */
    @Override
    public Figure createFigure() {
        return new Circle(xAxis, yAxis, radius, fill);
    }
}