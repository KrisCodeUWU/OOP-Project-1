package svgcreator.shapes;

/**
 * Represents a Rectangle shape in an SVG context.
 * This class extends the abstract {@link Figure} class and provides
 * specific implementations for drawing a rectangle and accessing its dimensions.
 */
public class Rectangle extends Figure {
    private int width, height;

    /**
     * Constructs a {@code Rectangle} object with specified coordinates, dimensions, and fill color.
     *
     * @param xAxis  The x-coordinate of the rectangle's top-left corner.
     * @param yAxis  The y-coordinate of the rectangle's top-left corner.
     * @param fill   The fill color of the rectangle (e.g., "blue", "#0000FF").
     * @param width  The width of the rectangle. Should be a non-negative value.
     * @param height The height of the rectangle. Should be a non-negative value.
     */
    public Rectangle(int xAxis, int yAxis, String fill, int width, int height) {
        super(xAxis, yAxis, fill); // Call the constructor of the parent Figure class
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height cannot be negative.");
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Generates the SVG string representation for this rectangle.
     * The format is {@code <rect x='...' y='...' width='...' height='...' fill='...' />}.
     *
     * @return A string representing the SVG element for this rectangle.
     */
    @Override
    public String drawFigure() {
        return "<rect x='" + getXAxis() + "' y='" + getYAxis() + "' width='" + width + "' height='" + height + "' fill='" + getColor() + "' />";
    }

    /**
     * Gets the width of this rectangle.
     * @return The width of the rectangle.
     */

    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this rectangle.
     * @return The height of the rectangle.
     */
    public int getHeight() {
        return height;
    }


     /**
      * Sets the width of this rectangle.
      *
      * @param width The new width for the rectangle. Must be a non-negative value.
      */
     public void setWidth(int width) {
        if (width < 0) {
             throw new IllegalArgumentException("Width cannot be negative.");
         }
         this.width = width;
     }

     /**
      * Sets the height of this rectangle.
      *
      * @param height The new height for the rectangle. Must be a non-negative value.
      */
     public void setHeight(int height) {
         if (height < 0) {
             throw new IllegalArgumentException("Height cannot be negative.");
         }
         this.height = height;
     }
}