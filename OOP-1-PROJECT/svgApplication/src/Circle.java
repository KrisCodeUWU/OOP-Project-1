public class Circle extends Figure {
    private int radius;

    public Circle(int xAxis, int yAxis, String fill, int radius) {
        super(xAxis, yAxis, fill);
        this.radius = radius;
    }


    @Override
    public String drawFigure() {
        return "<circle cx='" + getXAxis() + "' cy='" + getYAxis() + "' r='" + radius + "' fill='" + getColor() + "' />";
    }

}
