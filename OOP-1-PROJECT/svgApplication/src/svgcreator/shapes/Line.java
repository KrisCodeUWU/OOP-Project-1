package svgcreator.shapes;

public class Line extends Figure {
    private int x2;
    private int y2;

    public Line(int xAxis, int yAxis, String color, int x2, int y2) { // Use color consistently
        super(xAxis, yAxis, color);
        this.x2 = x2;
        this.y2 = y2;
    }

    // Existing Getters...
    public int getX2() { return x2; }
    public int getY2() { return y2; }

    // **** ADDED SETTERS ****
    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
    // **** END ADDED SETTERS ****

    @Override
    public String drawFigure() {
        // Use getColor() for consistency, SVG attribute is 'stroke'
        return "<line x1='" + getXAxis() + "' y1='" + getYAxis() + "' x2='" + x2 + "' y2='" + y2 + "' stroke='" + getColor() + "' />";
    }

    // Add implementations for abstract methods if svgcreator.shapes.Figure requires them
    // (If svgcreator.shapes.Figure's getters/setters are concrete, these might not be needed)
    // @Override public int getXAxis() { return super.getXAxis(); }
    // @Override public int getYAxis() { return super.getYAxis(); }
    // @Override public String getColor() { return super.getColor(); }
    // @Override public void setXAxis(int x) { super.setXAxis(x); }
    // @Override public void setYAxis(int y) { super.setYAxis(y); }
}