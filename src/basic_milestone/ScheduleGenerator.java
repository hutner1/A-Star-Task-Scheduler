package basic_milestone;

import java.util.List;

/**
 * This class makes a valid schedule based on a topological sort
 * implemented in the Sorter class.
 */
public class ScheduleGenerator {
	
	public Schedule makeSolution(List<Vertex> tSort) {
		Schedule solution = new Schedule();
		int start = 0;
		
		//Assign all vertices on one processor
		for (int i = 0; i < tSort.size(); i++) {
			assignToProcessor(tSort.get(i), 1, start, solution);
			start += tSort.get(i).getWeight();
		}
		return solution;
	}

	private void assignToProcessor(Vertex vertex, int processorNo, int start, Schedule sol) {
		NodeInfo nInfo = new NodeInfo(start, processorNo);
		sol.addVertexToMap(vertex, nInfo);
		
	}
}