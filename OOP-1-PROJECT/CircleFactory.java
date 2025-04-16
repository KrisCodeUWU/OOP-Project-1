public class CircleFactory implements FigureFactory {
    private int xAxis, yAxis, radius;
    private String fill;

    public CircleFactory(int xAxis, int yAxis, int radius, String fill) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.radius = radius;
        this.fill = fill;
    }

    @Override
    public Figure createFigure() {
        return new Circle(xAxis, yAxis, radius, fill);
    }
}