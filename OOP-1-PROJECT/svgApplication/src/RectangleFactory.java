public class RectangleFactory implements FigureFactory {
    private int xAxis, yAxis, width, height;
    private String fill;

    public RectangleFactory(int xAxis, int yAxis, String fill, int width, int height) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.fill = fill;
        this.width = width;
        this.height = height;
    }

    @Override
    public Figure createFigure() {
        return new Rectangle(xAxis, yAxis, fill, width, height);
    }
}
