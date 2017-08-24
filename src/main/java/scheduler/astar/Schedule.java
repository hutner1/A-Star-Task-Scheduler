package scheduler.astar;

import java.util.HashMap;

import scheduler.graphstructures.Vertex;

/**
 * Interface to be implemented by Solution class and test classes. 
 */
public interface Schedule {

	/**
	 * The greatest last end time on all the processors
	 */
	public int getLastFinishTime();

	/**
	 * Adds a process to a processor at its earliest possible start time
	 * @param v task vertex
	 * @param processorNumber processor to allocate task on
	 */	
	public void addProcess(Vertex v, int processorNumber);

	/**
	 * Helper function that calls all three parts of the proposed cost function
	 * and returns the maximum of those three values.
	 * 
	 * Parts
	 * 1. maximum of start time and bottom level of any task in schedule
	 * 2. idle time plus computation load
	 * 3. maximum of earliest start time and bottom level of any free tasks
	 * 
	 * @return the cost function 
	 */
	public int maxCostFunction();
	

	/**
	 * Checks if all tasks have been scheduled on the solution
	 */
	public boolean isCompleteSchedule();
		
	
	public HashMap<Integer, Processor> getProcess();
	
	public int getUpperBound();
}
