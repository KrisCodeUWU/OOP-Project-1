package svgcreator.shapes;

/**
 * Represents a Circle shape in an SVG context.
 * This class extends the abstract {@link Figure} class and provides
 * specific implementations for drawing a circle and accessing its radius.
 */
public class Circle extends Figure {
    private int radius;

    /**
     * Constructs a {@code Circle} object with specified coordinates, radius, and fill color.
     *
     * @param xAxis  The x-coordinate of the circle's center.
     * @param yAxis  The y-coordinate of the circle's center.
     * @param radius The radius of the circle. Must be a non-negative value.
     * @param fill   The fill color of the circle (e.g., "red", "#FF0000").
     */
    public Circle(int xAxis, int yAxis, int radius, String fill) {
        super(xAxis, yAxis, fill); // Call the constructor of the parent Figure class
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative.");
        }
        this.radius = radius;
    }

    /**
     * Generates the SVG string representation for this circle.
     * The format is {@code <circle cx='...' cy='...' r='...' fill='...' />}.
     *
     * @return A string representing the SVG element for this circle.
     */
    @Override
    public String drawFigure() {
        return "<circle cx='" + getXAxis() + "' cy='" + getYAxis() + "' r='" + radius + "' fill='" + getColor() + "' />";
    }

    /**
     * Gets the radius of this circle.
     *
     * @return The radius of the circle.
     */
    public int getRadius() {
        return radius;
    }
}