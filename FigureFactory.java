package svgcreator.factories;

import svgcreator.shapes.Figure;

/**
 * Defines a factory interface for creating {@link Figure} objects.
 * Implementations of this interface will provide concrete ways to instantiate
 * different types of figures. This promotes loose coupling by allowing the
 * application to work with this factory interface rather than concrete factory classes.
 */
public interface FigureFactory {

/**
 * Creates and returns a new {@link Figure} instance.
 * The specific type of figure created will depend on the concrete factory
 * implementation.
 *
 */
    Figure createFigure();
}
