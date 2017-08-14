package scheduler.topologicalsort;

import java.util.List;

import scheduler.graphstructures.Vertex;

/**
 * This class makes a valid schedule based on a topological sort
 * implemented in the Sorter class.
 */
public class ScheduleGenerator {
	
	/**
	 * Takes the TOPOLOGICALLY sorted tasks and generate an RANDOM schedule atm 
	 * @param tSort The sorted tasks (vertex) 
	 * @return the solution (schedule)
	 */
	public static Schedule makeSolution(List<Vertex> tSort) {
		Schedule solution = new Schedule();
		// start time used for the first task
		int start = 0;
		
		//Assign all vertices on one processor
		for (int i = 0; i < tSort.size(); i++) {
			assignToProcessor(tSort.get(i), 1, start, solution); //TODO currently all tasks are assigned to 1
			// start time changed for next task
			start += tSort.get(i).getWeight();
		}
		return solution;
	}

	/**
	 * Set the processor no of VertexInfo for a Vertex, 
	 * currently everything is assigned to processor 1
	 * @param vertex Vertex
	 * @param processorNo Processor No
	 * @param start Start time
	 * @param sol Schedule
	 */
	private static void assignToProcessor(Vertex vertex, int processorNo, int start, Schedule sol) {
		VertexInfo vInfo = new VertexInfo(start, processorNo);
		sol.addVertexToMap(vertex, vInfo);
		
	}
}