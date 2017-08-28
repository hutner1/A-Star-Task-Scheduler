package visualization.gantt;

import java.util.Date;

import org.jfree.data.gantt.Task;

/**
 * A custom task that uses integer numbers instead of dates
 *
 */
@SuppressWarnings("serial")
public class TaskNumeric extends Task {
    private String _desc;
	private long _start;
	private long _end;

	/**
	 * This class documents the numeric format of a task 
	 * @param description
	 * @param start
	 * @param end
	 */
	public TaskNumeric(String description, long start, long end) {
        super(description, new Date(start), new Date(end));
        _desc = description;
        _start = start;
        _end = end;
    }

    /**
     * This class returns the duration of a task
     * @param description
     * @param start
     * @param duration
     * @return
     */
    public static TaskNumeric duration(String description, long start, long duration) {
        return new TaskNumeric(description, start, start + duration);
    }
    

    /**
     * Getter method for desc
     * @return
     */
    public String getDesc() {
    	return _desc;
    }
    
    /**
     * Getter method for start time
     * @return
     */
    public long getStartTime() {
    	return _start;
    }
    
    /**
     * Getter method for end time
     * @return
     */
    public long getEndTime() {
    	return _end;
    }
}