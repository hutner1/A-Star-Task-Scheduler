package scheduler.astar;

import scheduler.basicmilestone.Vertex;

/**
 * Task's start/end time on a processor in a schedule
 */
public class ProcessInfo {

	private int _startTime;
	private Vertex _vertex;
	
	public ProcessInfo(Vertex v, int st) {
		_vertex = v;
		_startTime = st;
	}
	
	public int startTime() {
		return _startTime;
	}
	
	public int endTime() {
		return _startTime + _vertex.getWeight();
	}

	public Vertex getVertex() {
		return _vertex;
	}
	
	
}
