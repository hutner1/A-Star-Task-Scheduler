package scheduler.astar;

import java.util.HashMap;

import scheduler.basicmilestone.Vertex;

public class Solution {
	private HashMap<Integer, Processor> _processors;

	public Solution(int numberOfProcessors) {
		_processors = new HashMap<Integer, Processor>();

		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}

	}

	public int getTime() {
		int maximumTime = 0;

		for (Processor p : _processors.values()) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}

		return maximumTime;
	}

	public void addProcess(Vertex v, int processorNumber) {
		_processors.get(processorNumber).addProcess(v);

	}

	public boolean scheduled(Vertex v) {

		for (Processor p : _processors.values()) {
			if (p.scheduled(v)) {
				return true;
			}
		}
		return false;
	}

}

