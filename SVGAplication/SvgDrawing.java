package svgcreator.app;

import svgcreator.persistence.SvgPersistenceService;
import svgcreator.shapes.Figure;
import svgcreator.utils.GeometryUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the {@link Drawing} interface.
 * Manages a list of {@link Figure} objects and uses an {@link SvgPersistenceService}
 * for loading and saving them to an SVG file.
 */
public class SvgDrawing implements Drawing {
    private final List<Figure> figures;
    private final SvgPersistenceService persistenceService;
    private final Path filePath;

    /**
     * Constructs an SvgDrawing instance.
     * @param persistenceService The service used for file operations.
     * @param filePath The path to the SVG file.
     */
    public SvgDrawing(SvgPersistenceService persistenceService, Path filePath) {
        this.figures = new ArrayList<>();
        this.persistenceService = persistenceService;
        this.filePath = filePath;
    }

    @Override
    public void addFigure(Figure figure) {
        if (figure != null) {
            this.figures.add(figure);
        }
    }

    @Override
    public boolean removeFigure(int displayIndex) {
        if (displayIndex > 0 && displayIndex <= this.figures.size()) {
            this.figures.remove(displayIndex - 1); // Convert 1-based to 0-based
            return true;
        }
        return false;
    }

    @Override
    public Figure getFigure(int displayIndex) {
        if (displayIndex > 0 && displayIndex <= this.figures.size()) {
            return this.figures.get(displayIndex - 1); // Convert 1-based to 0-based
        }
        return null;
    }

    @Override
    public List<Figure> getAllFigures() {
        return new ArrayList<>(this.figures); // Return a copy
    }

    @Override
    public void translateAllFigures(int dx, int dy) {
        for (Figure figure : this.figures) {
            GeometryUtils.translateFigure(figure, dx, dy);
        }
    }

    @Override
    public boolean translateSingleFigure(int displayIndex, int dx, int dy) {
        if (displayIndex > 0 && displayIndex <= this.figures.size()) {
            Figure figureToTranslate = this.figures.get(displayIndex - 1);
            GeometryUtils.translateFigure(figureToTranslate, dx, dy);
            return true;
        }
        return false;
    }

    @Override
    public List<Figure> getFiguresWithinBoundary(String boundaryType, int bx, int by, int bParam1, int bParam2) {
        List<Figure> figuresWithin = new ArrayList<>();
        for (Figure figure : this.figures) {
            if (GeometryUtils.isFigureWithinBoundary(figure, boundaryType, bx, by, bParam1, bParam2)) {
                figuresWithin.add(figure);
            }
        }
        return figuresWithin;
    }

    @Override
    public void loadFromFile() {
        this.figures.clear();
        this.figures.addAll(persistenceService.loadFigures(this.filePath));
        // System.out.println("Loaded " + this.figures.size() + " figures from " + this.filePath); // Optional debug
    }

    @Override
    public void saveToFile() {
        persistenceService.saveFigures(this.figures, this.filePath);
        // System.out.println("Saved " + this.figures.size() + " figures to " + this.filePath); // Optional debug
    }

    @Override
    public int getFigureCount() {
        return this.figures.size();
    }
}