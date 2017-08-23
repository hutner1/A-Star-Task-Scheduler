package visualization;

import java.util.HashMap;

import scheduler.astar.Processor;
import scheduler.astar.Schedule;
import scheduler.graphstructures.Vertex;

public class MockSolution implements Schedule{
	
	private HashMap<Integer, Processor> _processors;
	
	public MockSolution() {
		_processors = new HashMap<Integer, Processor>();
		
		Processor p0 = new Processor();
		
		
	}
	@Override
	public int getLastFinishTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addProcess(Vertex v, int processorNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int maxCostFunction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCompleteSchedule() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HashMap<Integer, Processor> getProcess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUpperBound() {
		// TODO Auto-generated method stub
		return 0;
	}

}
