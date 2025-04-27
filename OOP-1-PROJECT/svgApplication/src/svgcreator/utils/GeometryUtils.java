package svgcreator.utils;

import svgcreator.shapes.Circle;
import svgcreator.shapes.Figure;
import svgcreator.shapes.Line;
import svgcreator.shapes.Rectangle;

public class GeometryUtils {

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void translateFigure(Figure figure, int dx, int dy) {
        if (figure == null) return;
        figure.setXAxis(figure.getXAxis() + dx);
        figure.setYAxis(figure.getYAxis() + dy);
        if (figure instanceof Line line) {
            line.setX2(line.getX2() + dx);
            line.setY2(line.getY2() + dy);
        }
    }

    public static boolean isFigureWithinBoundary(Figure figure, String boundaryType, int bx, int by, int bParam1, int bParam2) {
        if (boundaryType.equalsIgnoreCase("rectangle")) {
            int bWidth = bParam1; int bHeight = bParam2;
            if (figure instanceof Circle c) {
                return (c.getXAxis() - c.getRadius() >= bx) && (c.getXAxis() + c.getRadius() <= bx + bWidth) &&
                        (c.getYAxis() - c.getRadius() >= by) && (c.getYAxis() + c.getRadius() <= by + bHeight);
            } else if (figure instanceof Rectangle r) {
                return (r.getXAxis() >= bx) && (r.getXAxis() + r.getWidth() <= bx + bWidth) &&
                        (r.getYAxis() >= by) && (r.getYAxis() + r.getHeight() <= by + bHeight);
            } else if (figure instanceof Line l) {
                boolean p1 = (l.getXAxis() >= bx) && (l.getXAxis() <= bx + bWidth) && (l.getYAxis() >= by) && (l.getYAxis() <= by + bHeight);
                boolean p2 = (l.getX2() >= bx) && (l.getX2() <= bx + bWidth) && (l.getY2() >= by) && (l.getY2() <= by + bHeight);
                return p1 && p2;
            }
        } else if (boundaryType.equalsIgnoreCase("circle")) {
            int bRadius = bParam1;
            if (figure instanceof Circle c) {
                return distance(bx, by, c.getXAxis(), c.getYAxis()) + c.getRadius() <= bRadius;
            } else if (figure instanceof Rectangle r) {
                boolean tl = distance(bx, by, r.getXAxis(), r.getYAxis()) <= bRadius;
                boolean tr = distance(bx, by, r.getXAxis() + r.getWidth(), r.getYAxis()) <= bRadius;
                boolean bl = distance(bx, by, r.getXAxis(), r.getYAxis() + r.getHeight()) <= bRadius;
                boolean br = distance(bx, by, r.getXAxis() + r.getWidth(), r.getYAxis() + r.getHeight()) <= bRadius;
                return tl && tr && bl && br;
            } else if (figure instanceof Line l) {
                boolean p1 = distance(bx, by, l.getXAxis(), l.getYAxis()) <= bRadius;
                boolean p2 = distance(bx, by, l.getX2(), l.getY2()) <= bRadius;
                return p1 && p2;
            }
        }
        return false;
    }
}