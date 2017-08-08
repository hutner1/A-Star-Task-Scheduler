package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	private List<Processor> _processors;
	
	public Solution(int numberOfProcessors) {
		_processors = new ArrayList<Processor>();
		
		for (int i = 0; i < numberOfProcessors; i++) {
			_processors.add(new Processor());
		}
	}
	
	public int getTime() {
		int maximumTime = 0;
		
		for (Processor p : _processors) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}
		
		return maximumTime;
	}
}
