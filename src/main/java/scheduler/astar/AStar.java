package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class AStar {
	private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _graph;
	private int _numberOfProcessors;

	public AStar(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph, int numberOfProcessors) {
		_graph = graph;
	}
	
	public void execute() {
		
		List<Solution> initialSolutions = new ArrayList<Solution>();
		
		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) {
				Solution s = new Solution(_numberOfProcessors);
				ProcessInfo p = new ProcessInfo(v,0);
				s.addProcess(p, 1);
				initialSolutions.add(s);
			}
		}
		
		Solution bestInitialSolution; 
		int maxTime = Integer.MAX_VALUE;
		
		for (Solution s : initialSolutions) {
			if (s.getTime() < maxTime) {
				bestInitialSolution = s;
				maxTime = s.getTime();
			}
		}
	
		
	}
	
	
	
}
