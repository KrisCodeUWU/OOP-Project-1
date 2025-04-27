package svgcreator.shapes;

public abstract class Figure {
    private int xAxis, yAxis;
    private String color; // Renamed from fill to color

    public Figure(int xAxis, int yAxis, String color) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = color;
    }

    // Existing Getters...
    public int getXAxis() { return xAxis; }
    public int getYAxis() { return yAxis; }
    public String getColor() { return color; }

    // **** ADDED SETTERS ****
    public void setXAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public void setYAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public abstract String drawFigure();
}