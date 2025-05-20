package svgcreator.app;

import svgcreator.shapes.Figure;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for managing a collection of figures in the drawing.
 * Handles adding, removing, retrieving, and transforming figures,
 * as well as loading from and saving to a persistent store.
 */
public interface Drawing {
    /**
     * Adds a figure to the drawing.
     * @param figure The figure to add.
     */
    void addFigure(Figure figure);

    /**
     * Removes a figure from the drawing based on its 1-based display index.
     * @param displayIndex The 1-based index of the figure to remove.
     * @return true if a figure was removed, false otherwise.
     */
    boolean removeFigure(int displayIndex);

    /**
     * Retrieves a figure from the drawing based on its 1-based display index.
     * @param displayIndex The 1-based index of the figure.
     * @return The Figure at the specified index, or null if the index is invalid.
     */
    Figure getFigure(int displayIndex);

    /**
     * Gets a list of all figures currently in the drawing.
     * The returned list is a copy and modifications to it will not affect the drawing.
     * @return A list of all figures.
     */
    List<Figure> getAllFigures();

    /**
     * Translates all figures in the drawing by the given deltas.
     * @param dx The horizontal translation amount.
     * @param dy The vertical translation amount.
     */
    void translateAllFigures(int dx, int dy);

    /**
     * Translates a single figure in the drawing by the given deltas.
     * @param displayIndex The 1-based index of the figure to translate.
     * @param dx The horizontal translation amount.
     * @param dy The vertical translation amount.
     * @return true if the figure was found and translated, false otherwise.
     */
    boolean translateSingleFigure(int displayIndex, int dx, int dy);

    /**
     * Retrieves a list of figures that are completely within the specified boundary.
     * @param boundaryType The type of boundary ("rectangle" or "circle").
     * @param bx The x-coordinate of the boundary's reference point.
     * @param by The y-coordinate of the boundary's reference point.
     * @param bParam1 Primary dimension of the boundary (width or radius).
     * @param bParam2 Secondary dimension of the boundary (height, or unused for circle).
     * @return A list of figures within the boundary.
     */
    List<Figure> getFiguresWithinBoundary(String boundaryType, int bx, int by, int bParam1, int bParam2);

    /**
     * Loads figures from the associated file path into the drawing.
     * Any existing figures in the drawing are cleared first.
     */
    void loadFromFile();

    /**
     * Saves all figures currently in the drawing to the associated file path.
     */
    void saveToFile();

    /**
     * Gets the current number of figures in the drawing.
     * @return The count of figures.
     */
    int getFigureCount();
}