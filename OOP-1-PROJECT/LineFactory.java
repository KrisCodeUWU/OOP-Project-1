public class LineFactory implements FigureFactory {
    private int xAxis, yAxis, x2, y2;
    private String stroke;

    public LineFactory(int xAxis, int yAxis, String stroke, int x2, int y2) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.stroke = stroke;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public Figure createFigure() {
        return new Line(xAxis, yAxis, stroke, x2, y2);
    }
}
