package astar;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class AStar {
	private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _graph;
	public AStar(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph) {
		_graph = graph;
	}
	
	
}
