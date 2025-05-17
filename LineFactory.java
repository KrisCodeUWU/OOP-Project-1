package svgcreator.factories;
import svgcreator.shapes.Line;
import svgcreator.shapes.Figure;

/**
 * A factory class responsible for creating {@link Line} objects.
 * This class implements the {@link FigureFactory} interface, providing a specific
 * way to instantiate lines with given parameters.
 */
public class LineFactory implements FigureFactory {
    private int xAxis, yAxis, x2, y2;
    private String stroke;

    /**
     * Constructs a {@code LineFactory} with the specified parameters for the line to be created.
     *
     * @param xAxis The x-coordinate of the line's starting point (x1).
     * @param yAxis The y-coordinate of the line's starting point (y1).
     * @param stroke The stroke color of the line.
     * @param x2 The x-coordinate of the line's ending point.
     * @param y2 The y-coordinate of the line's ending point.
     */
    public LineFactory(int xAxis, int yAxis, String stroke, int x2, int y2) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.stroke = stroke;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Creates and returns a new {@link Line} instance.
     * The line is created using the parameters provided to this factory's constructor.
     *
     * @return A new {@link Line} object, cast as a {@link Figure}.
     */
    @Override
    public Figure createFigure() {
        return new Line(xAxis, yAxis, stroke, x2, y2);
    }
}