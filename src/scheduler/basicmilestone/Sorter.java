package scheduler.basicmilestone;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

//Using the jgrapht library data structures
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
/**
 * This class creates a topological sort of a graph of 
 * events to be put in a schedule
 */
public class Sorter {

	private static List<Vertex> tSort = new ArrayList<Vertex>();

	/**
	 * Stack to store nodes that are not preceded by another node
	 * Use of ArrayDeque for faster element access than traditional linked list
	 * Source: https://stackoverflow.com/questions/6163166/why-is-arraydeque-better-than-linkedlist
	 */

	private static Deque<Vertex> nodeStack = new ArrayDeque<Vertex>();

	public static List<Vertex> generateSort(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph){
		//Loop through the graph to find nodes that are not preceded by another node
		for (Vertex v: graph.vertexSet()) {
			if (graph.inDegreeOf(v)==0) {
				nodeStack.add(v);
			}
		}

		while(nodeStack.size()>0) {
			Vertex v = nodeStack.pop(); //get the vertex to process
			tSort.add(v);


			//Remove all connecting edges to other nodes from the current vertex
			for (DefaultWeightedEdge dwe: graph.edgesOf(v)) {
				graph.removeEdge(dwe);

				//Add nodes that are not preceded by another node
				Vertex target = graph.getEdgeTarget(dwe);
				if (graph.inDegreeOf(target)==0) {
					nodeStack.add(target);
				}
			}

		}
		return tSort;
	}

}