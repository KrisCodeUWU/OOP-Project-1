public class CircleFactory implements FigureFactory {
    private int xAxis, yAxis, radius;
    private String fill;

    public CircleFactory(int xAxis, int yAxis, String fill, int radius) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.fill = fill;
        this.radius = radius;
    }

    @Override
    public Figure createFigure() {
        return new Circle(xAxis, yAxis, fill, radius);
    }
}
