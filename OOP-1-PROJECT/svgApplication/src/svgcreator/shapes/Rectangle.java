package svgcreator.shapes;

public class Rectangle extends Figure {
    private int width, height;

    public Rectangle(int xAxis, int yAxis, String fill, int width, int height) {
        super(xAxis, yAxis, fill);
        this.width = width;
        this.height = height;
    }

    @Override
    public String drawFigure() {
        return "<rect x='" + getXAxis() + "' y='" + getYAxis() + "' width='" + width + "' height='" + height + "' fill='" + getColor() + "' />";
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
