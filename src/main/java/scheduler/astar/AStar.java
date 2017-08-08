package scheduler.astar;

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
		Solution solution = new Solution(_numberOfProcessors);
	}
	
}
