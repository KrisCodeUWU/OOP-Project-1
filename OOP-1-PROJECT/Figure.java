public abstract class Figure {
    private int xAxis, yAxis;
    private String color; // Renamed from fill to color

    public Figure(int xAxis, int yAxis, String color) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = color;
    }

    public int getXAxis() {
        return xAxis;
    }

    public int getYAxis() {
        return yAxis;
    }

    public String getColor() { // Renamed from getFill
        return color;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public abstract String drawFigure();
}
