package svgcreator.persistence;

import svgcreator.shapes.Figure;

import java.nio.file.Path;
import java.util.List;

/**
 * Defines an interface for persistence services that handle loading and saving
 * {@link Figure} objects. Implementations of this interface will provide
 * specific mechanisms for storing and retrieving figure data, such as
 * reading from and writing to SVG files.
 */
public interface SvgPersistenceService {

    /**
     * Loads a list of {@link Figure} objects from a persistent storage
     * identified by the given file path.
     *
     * @param filePath The {@link Path} to the data source (e.g., an SVG file)
     *                 from which to load the figures.
     * @return A {@link List} of {@link Figure} objects loaded from the data source.
     *         If the data source does not exist or contains no figures,
     *         an empty list should be returned.
     */
    List<Figure> loadFigures(Path filePath);

    /**
     * Saves a list of {@link Figure} objects to a persistent storage
     * identified by the given file path.
     * This method typically overwrites the existing data at the specified path.
     *
     * @param figures The {@link List} of {@link Figure} objects to be saved.
     * @param filePath The {@link Path} to the data destination (e.g., an SVG file)
     *                 where the figures will be saved.
     */
    void saveFigures(List<Figure> figures, Path filePath); // Overwrites
}