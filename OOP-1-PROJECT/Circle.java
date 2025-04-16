public class Circle extends Figure {
    private int radius;

    public Circle(int xAxis, int yAxis, int radius, String fill) {
        super(xAxis, yAxis, fill);
        this.radius = radius;
    }

    @Override
    public String drawFigure() {
        return "<circle cx='" + getXAxis() + "' cy='" + getYAxis() + "' r='" + radius + "' fill='" + getColor() + "' />";
    }

    public int getRadius() {
        return radius;
    }
}