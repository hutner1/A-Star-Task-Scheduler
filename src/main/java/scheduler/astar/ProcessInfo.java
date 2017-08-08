package scheduler.astar;

import scheduler.basicmilestone.Vertex;

public class ProcessInfo {

	private int _startTime;
	private Vertex _vertex;
	
	public ProcessInfo(Vertex v, int st) {
		_vertex = v;
		_startTime = st;
	}
	
	public int endTime() {
		return _startTime + _vertex.getWeight();
	}
	
}
