package svgcreator.app;

/**
 * Functional interface representing an action to be performed from the menu.
 */
@FunctionalInterface
public interface MenuAction {
    /**
     * Executes the menu action.
     * @return true if the application should continue running, false if it should exit.
     */
    boolean execute();
}