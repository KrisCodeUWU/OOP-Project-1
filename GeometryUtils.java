package svgcreator.utils;

import svgcreator.shapes.Circle;
import svgcreator.shapes.Figure;
import svgcreator.shapes.Line;
import svgcreator.shapes.Rectangle;

/**
 * A utility class providing static methods for common geometric calculations
 * and operations related to {@link Figure} objects.
 * This class cannot be instantiated.
 */
public class GeometryUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GeometryUtils() {
        // This class is not meant to be instantiated.
    }

    /**
     * Calculates the Euclidean distance between two points (x1, y1) and (x2, y2).
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The distance between the two points.
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Translates a given {@link Figure} by specified delta values (dx, dy).
     * Modifies the figure's coordinates in place. If the figure is a {@link Line},
     * both its start (x1, y1) and end (x2, y2) points are translated.
     * If the figure is {@code null}, the method does nothing.
     *
     * @param figure The {@link Figure} to translate. Can be {@code null}.
     * @param dx     The amount to translate along the x-axis.
     * @param dy     The amount to translate along the y-axis.
     */
    public static void translateFigure(Figure figure, int dx, int dy) {
        if (figure == null) {
            return;
        }
        figure.setXAxis(figure.getXAxis() + dx);
        figure.setYAxis(figure.getYAxis() + dy);

        // If the figure is a Line, its second point (x2, y2) also needs to be translated.
        if (figure instanceof Line line) {
            line.setX2(line.getX2() + dx);
            line.setY2(line.getY2() + dy);
        }
    }

    /**
     * Checks if a given {@link Figure} is completely contained within a specified boundary.
     * The boundary can be a rectangle or a circle.
     *
     * @param figure       The {@link Figure} to check.
     * @param boundaryType A string indicating the type of boundary ("rectangle" or "circle").
     *                     Case-insensitive.
     * @param bx           The x-coordinate of the boundary's reference point
     *                     (top-left for rectangle, center for circle).
     * @param by           The y-coordinate of the boundary's reference point
     *                     (top-left for rectangle, center for circle).
     * @param bParam1      Primary dimension of the boundary:
     *                     <ul>
     *                       <li>If boundaryType is "rectangle", this is the width of the boundary rectangle.</li>
     *                       <li>If boundaryType is "circle", this is the radius of the boundary circle.</li>
     *                     </ul>
     * @param bParam2      Secondary dimension of the boundary:
     *                     <ul>
     *                       <li>If boundaryType is "rectangle", this is the height of the boundary rectangle.</li>
     *                       <li>If boundaryType is "circle", this parameter is unused.</li>
     *                     </ul>
     * @return {@code true} if the figure is completely within the specified boundary,
     *         {@code false} otherwise, or if the figure or boundary type is unrecognized.
     */
    public static boolean isFigureWithinBoundary(Figure figure, String boundaryType, int bx, int by, int bParam1, int bParam2) {
        if (figure == null || boundaryType == null) {
            return false;
        }

        if (boundaryType.equalsIgnoreCase("rectangle")) {
            int bWidth = bParam1;
            int bHeight = bParam2;
            if (figure instanceof Circle c) {
                // Check if the circle's bounding box is within the rectangle
                return (c.getXAxis() - c.getRadius() >= bx) && // Circle's leftmost point >= rectangle's left edge
                        (c.getXAxis() + c.getRadius() <= bx + bWidth) && // Circle's rightmost point <= rectangle's right edge
                        (c.getYAxis() - c.getRadius() >= by) && // Circle's topmost point >= rectangle's top edge
                        (c.getYAxis() + c.getRadius() <= by + bHeight);   // Circle's bottommost point <= rectangle's bottom edge
            } else if (figure instanceof Rectangle r) {
                // Check if the rectangle is within the boundary rectangle
                return (r.getXAxis() >= bx) &&
                        (r.getXAxis() + r.getWidth() <= bx + bWidth) &&
                        (r.getYAxis() >= by) &&
                        (r.getYAxis() + r.getHeight() <= by + bHeight);
            } else if (figure instanceof Line l) {
                // Check if both endpoints of the line are within the boundary rectangle
                boolean p1Inside = (l.getXAxis() >= bx) && (l.getXAxis() <= bx + bWidth) &&
                        (l.getYAxis() >= by) && (l.getYAxis() <= by + bHeight);
                boolean p2Inside = (l.getX2() >= bx) && (l.getX2() <= bx + bWidth) &&
                        (l.getY2() >= by) && (l.getY2() <= by + bHeight);
                return p1Inside && p2Inside;
            }
        } else if (boundaryType.equalsIgnoreCase("circle")) {
            int bRadius = bParam1;
            if (figure instanceof Circle c) {
                // Distance between centers + circle's radius must be <= boundary circle's radius
                return distance(bx, by, c.getXAxis(), c.getYAxis()) + c.getRadius() <= bRadius;
            } else if (figure instanceof Rectangle r) {
                // Check if all four corners of the rectangle are within the boundary circle
                boolean topLeftIn = distance(bx, by, r.getXAxis(), r.getYAxis()) <= bRadius;
                boolean topRightIn = distance(bx, by, r.getXAxis() + r.getWidth(), r.getYAxis()) <= bRadius;
                boolean bottomLeftIn = distance(bx, by, r.getXAxis(), r.getYAxis() + r.getHeight()) <= bRadius;
                boolean bottomRightIn = distance(bx, by, r.getXAxis() + r.getWidth(), r.getYAxis() + r.getHeight()) <= bRadius;
                return topLeftIn && topRightIn && bottomLeftIn && bottomRightIn;
            } else if (figure instanceof Line l) {
                // Check if both endpoints of the line are within the boundary circle
                boolean p1Inside = distance(bx, by, l.getXAxis(), l.getYAxis()) <= bRadius;
                boolean p2Inside = distance(bx, by, l.getX2(), l.getY2()) <= bRadius;
                return p1Inside && p2Inside;
            }
        }
        return false; // Unknown figure type or boundary type
    }
}