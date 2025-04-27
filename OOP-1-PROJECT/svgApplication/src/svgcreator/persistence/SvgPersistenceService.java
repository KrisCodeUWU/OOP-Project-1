package svgcreator.persistence;

import svgcreator.shapes.Figure;

import java.nio.file.Path;
import java.util.List;

public interface SvgPersistenceService {
    List<Figure> loadFigures(Path filePath);
    void saveFigures(List<Figure> figures, Path filePath); // Overwrites
}