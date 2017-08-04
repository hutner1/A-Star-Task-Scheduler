package scheduler.basicmilestone;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

//Using the jgrapht library data structures
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
/**
 * This class calculates all the vertex bottom levels
 * for the topological sort to work using Kahn's algorithm
 * NOT REQUIRED FOR BASIC MILESTONE
 * 
 */
public class NodeWeightCalculator {

	public static void calculate(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph) {
		/**
		 * Stack to store nodes that are not preceded by another node
		 * Use of ArrayDeque for faster element access than traditional linked list
		 * Source: https://stackoverflow.com/questions/6163166/why-is-arraydeque-better-than-linkedlist
		 */
		//Put all vertex nodes in the nodeStack for processing
		Deque<Vertex> nodeStack = new ArrayDeque<Vertex>();
		for (Vertex v: graph.vertexSet()) {
			if (graph.outDegreeOf(v)==0) {
				nodeStack.add(v);
				v.setBottomLevel(v.getWeight());
			}
		}
		
		Vertex source;
		ArrayList<DefaultWeightedEdge> completedNodes = new ArrayList<DefaultWeightedEdge>();
		while(nodeStack.size()>0) {
			Vertex v = nodeStack.pop(); //node to process
			for (DefaultWeightedEdge e: graph.edgesOf(v)) {
				//Calculate bottom levels for edges
				source = graph.getEdgeSource(e);
				source.setBottomLevel(Math.max(v.getBottomLevel()+source.getWeight(),source.getBottomLevel()));
				
				completedNodes.add(graph.removeEdge(source, v));
				
				//Add source vertices back into the queue
				if(graph.outDegreeOf(source)==0) {
					nodeStack.add(source);
				}
			}
		}
		
		for (DefaultWeightedEdge e : completedNodes) {
			graph.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e),e);
		}
		
	}




}