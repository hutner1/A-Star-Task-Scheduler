package scheduler.astar;

import java.util.HashMap;

import scheduler.graphstructures.Vertex;

public interface Schedule {
	public int getLastFinishTime();
	
	public void addProcess(Vertex v, int processorNumber);
	
	public int maxCostFunction();
	
	public boolean isCompleteSchedule();
		
	public HashMap<Integer, Processor> getProcess();
	
	public int getUpperBound();
}
