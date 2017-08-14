package scheduler.topologicalsort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;

/**
 * This class creates a topological sort of a graph of 
 * events to be put in a schedule using Kahn's algorithm
 */
public class Sorter {

	private DefaultDirectedWeightedGraph _graph;
	
	public Sorter(DefaultDirectedWeightedGraph graph) {
		_graph = graph;
	}
	
	/**
	 * Takes the list of vertices in the digraph and returns returns a topological sorted order of the vertices 
	 * @return
	 */
	public List<Vertex> generateSort(){
		
		/**
		 * Stack to store nodes that are not preceded by another node
		 * Use of ArrayDeque for faster element access than traditional linked list
		 * Source: https://stackoverflow.com/questions/6163166/why-is-arraydeque-better-than-linkedlist
		 */

		//This data structure stores the nodes to be processed
		Deque<Vertex> nodeStack = new ArrayDeque<Vertex>();
		List<Vertex> tSort = new ArrayList<Vertex>();
		
		//Loop through the graph to find nodes that are not preceded by another node
		for (Vertex v: _graph.vertexSet()) {
			if (_graph.inDegreeOf(v)==0) {
				nodeStack.add(v); //add the vertices that have no incoming arcs to process
			}
		}

		while(nodeStack.size()>0) { // process nodes until all nodes are processed
			Vertex v = nodeStack.pop(); //get the vertex to process
			tSort.add(v);


			//Remove all connecting edges to other nodes from the current vertex
			for (DefaultWeightedEdge dwe: _graph.edgesOf(v)) {
				_graph.removeEdge(dwe); // remove all nodes that may overlap in the digraph

				//Add nodes that become 'leaves'
				Vertex target = _graph.getEdgeTarget(dwe);
				if (_graph.inDegreeOf(target)==0) {
					nodeStack.add(target);
				}
			}

		}
		return tSort;
	}

}