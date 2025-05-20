package svgcreator.shapes;

/**
 * Represents a Line shape in an SVG context.
 * This class extends the abstract {@link Figure} class and provides
 * specific implementations for drawing a line and accessing its start and end coordinates.
 */
public class Line extends Figure {
    private int x2;
    private int y2;

    /**
     * Constructs a {@code Line} object with specified start and end coordinates, and stroke color.
     * The {@code xAxis} and {@code yAxis} from the parent {@link Figure} class represent the
     * starting point (x1, y1) of the line.
     *
     * @param xAxis The x-coordinate of the line's starting point (x1).
     * @param yAxis The y-coordinate of the line's starting point (y1).
     * @param color The stroke color of the line (e.g., "black", "#0000FF").
     * @param x2    The x-coordinate of the line's ending point.
     * @param y2    The y-coordinate of the line's ending point.
     */
    public Line(int xAxis, int yAxis, String color, int x2, int y2) { // Use color consistently
        super(xAxis, yAxis, color);
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Gets the x-coordinate of the line's ending point.
     *
     * @return The x-coordinate of the end point (x2).
     */
    public int getX2() { return x2; }

    /**
     * Gets the y-coordinate of the line's ending point.
     *
     * @return The y-coordinate of the end point (y2).
     */
    public int getY2() { return y2; }

    /**
     * Sets the x-coordinate of the line's ending point.
     *
     * @param x2 The new x-coordinate for the end point.
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Sets the y-coordinate of the line's ending point.
     *
     * @param y2 The new y-coordinate for the end point.
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * Generates the SVG string representation for this line.
     * The format is {@code <line x1='...' y1='...' x2='...' y2='...' stroke='...' />}.
     * The color from the parent class is used as the 'stroke' attribute.
     *
     * @return A string representing the SVG element for this line.
     */
    @Override
    public String drawFigure() {
        // Use getColor() for consistency, SVG attribute is 'stroke'
        return "<line x1='" + getXAxis() + "' y1='" + getYAxis() + "' x2='" + x2 + "' y2='" + y2 + "' stroke='" + getColor() + "' />";
    }
}