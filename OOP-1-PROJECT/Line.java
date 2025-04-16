public class Line extends Figure {
    private int x2;
    private int y2;

    public Line(int xAxis, int yAxis, String fill, int x2, int y2) {
        super(xAxis, yAxis, fill);
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public String drawFigure() {
        return "<line x1='" + getXAxis() + "' y1='" + getYAxis() + "' x2='" + x2 + "' y2='" + y2 + "' stroke='" + getColor() + "' />";
    }
}