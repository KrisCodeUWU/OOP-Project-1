package svgcreator.shapes;

/**
 * Represents an abstract geometric figure, serving as a base class for specific shapes
 * like {@link Circle}, {@link Rectangle}, and {@link Line}.
 * It defines common properties such as position (x and y coordinates) and color,
 * as well as an abstract method for generating an SVG string representation.
 */
public abstract class Figure {
    private int xAxis, yAxis;
    private String color; // Common color property (e.g., fill for Circle/Rectangle, stroke for Line)

    /**
     * Constructs a {@code Figure} with specified coordinates and color.
     *
     * @param xAxis The x-coordinate of the figure's primary reference point (e.g., center for circle, top-left for rectangle).
     * @param yAxis The y-coordinate of the figure's primary reference point.
     * @param color The color of the figure (e.g., "red", "#00FF00").
     */
    public Figure(int xAxis, int yAxis, String color) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = color;
    }

    /**
     * Gets the x-coordinate of this figure's primary reference point.
     *
     * @return The x-coordinate.
     */
    public int getXAxis() {
        return xAxis;
    }

    /**
     * Gets the y-coordinate of this figure's primary reference point.
     *
     * @return The y-coordinate.
     */
    public int getYAxis() {
        return yAxis;
    }

    /**
     * Gets the color of this figure.
     *
     * @return The color string (e.g., "blue", "#FFFFFF").
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the x-coordinate of this figure's primary reference point.
     *
     * @param xAxis The new x-coordinate.
     */
    public void setXAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * Sets the y-coordinate of this figure's primary reference point.
     *
     * @param yAxis The new y-coordinate.
     */
    public void setYAxis(int yAxis) {
        this.yAxis = yAxis;
    }
    /**
     * Abstract method to generate the SVG string representation for this specific figure.
     * Concrete subclasses (e.g., {@link Circle}, {@link Rectangle}) must implement this
     * method to define how they are drawn in SVG format.
     *
     * @return A string representing the SVG element for this figure.
     */
    public abstract String drawFigure();
}
