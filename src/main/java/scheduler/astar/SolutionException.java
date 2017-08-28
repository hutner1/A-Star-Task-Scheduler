package scheduler.astar;

@SuppressWarnings("serial")
/**
 * An Exception subclass that is thrown when a task is attempted to be scheduled
 * without having all of its prerequisites scheduled
 *
 */
public class SolutionException extends Exception {
}
