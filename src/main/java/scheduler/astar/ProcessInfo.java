package scheduler.astar;

import scheduler.graphstructures.Vertex;

/**
 * Task's start/end time on a processor in a schedule
 */
public class ProcessInfo {

	private int _startTime;
	private Vertex _vertex;
	
	/**
	 * Construct ProcessInfo to store vertex start time.
	 * To be used by Processor class to store task vertices that ran on the processor
	 * 
	 * @param v
	 * @param st
	 */
	public ProcessInfo(Vertex v, int st) {
		_vertex = v;
		_startTime = st;
	}
	
	/**
	 * Returns start time of task vertex  
	 * @return start time of task vertex 
	 */
	public int startTime() {
		return _startTime;
	}
	
	/**
	 * Returns end time of a task by adding the task's weight to the start time
	 * @return end time of task vertex
	 */
	public int endTime() {
		return _startTime + _vertex.getWeight();
	}

	/**
	 * Get the Vertex associated with the ProcessInfo
	 * @return Vertex instance associated with the ProcessInfo
	 */
	public Vertex getVertex() {
		return _vertex;
	}
	
	/**
	 * Get the name of a task vertex
	 * @return name of a task vertex
	 */
	public String getTaskName() {
		return _vertex.getName();
	}
}
