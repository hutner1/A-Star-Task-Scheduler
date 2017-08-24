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
		p0.addProcess(new Vertex("aaaa", 5), 0);
		p0.addProcess(new Vertex("bbbb", 4), 5);
		p0.addProcess(new Vertex("cccc", 5) ,  10);
		_processors.put(1, p0);
		
		Processor p1 = new Processor();
		p1.addProcess(new Vertex("dddd", 1), 9);
		p1.addProcess(new Vertex("eeee", 2), 15);
		p1.addProcess(new Vertex("ffff", 2), 25);
		_processors.put(2, p1);

		
		Processor p2 = new Processor();
		p2.addProcess(new Vertex("gggg", 2), 27);
		_processors.put(3, p2);

		
		Processor p3 = new Processor();
		_processors.put(4, p3);

		
	}
	@Override
	public int getLastFinishTime() {
		return 0;
	}

	@Override
	public void addProcess(Vertex v, int processorNumber) {
		
	}

	@Override
	public int maxCostFunction() {
		return 0;
	}

	@Override
	public boolean isCompleteSchedule() {
		return false;
	}

	@Override
	public HashMap<Integer, Processor> getProcess() {
		// TODO Auto-generated method stub
		return _processors;
	}

	@Override
	public int getUpperBound() {
		return 30;
	}

}
