package scheduler.dfsbranchandbound;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.Vertex;

/**
 * This class generates an optimal solution using Depth First Search Branch & Bound algorithm
 *
 */
public class SolutionGenerator {

	DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _digraph;
	int _noOfProcessors; 
	int _currentMinCost;
	HashMap<Integer, ArrayList<Vertex>> _processorList;
	HashMap<Vertex,int[]> _vertexScheduleInfo;
	
	/**
	 * Takes in digraph information to generate optimum solution for
	 * @param digraph
	 * @param noOfProcessors
	 */
	public SolutionGenerator(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> digraph, int noOfProcessors){
		_digraph = digraph;
		_noOfProcessors = noOfProcessors;
		_processorList = new HashMap<>();
		// create a list for tasks for each processor
		for(int i = 0; i<noOfProcessors; i++){
			_processorList.put(i, new ArrayList<>());
		}
		_vertexScheduleInfo = new HashMap<>();
	} 
	
	/**
	 * Does Depth First Search Branch & Bound and returns the optimal solution
	 * @return optimal schedule
	 */
	public Schedule generateSolution(){
		ArrayList<Vertex> rootVertices =  returnRootVertices();
		
		
		return null; // schedule is placeholder
	}
	
	/**
	 * Returns root nodes of the digraph
	 * @return root nodes of the digraph
	 */
	private ArrayList<Vertex> returnRootVertices(){
		ArrayList<Vertex> rootVertices = new ArrayList<>();
		for (Vertex v: _digraph.vertexSet()) {
			if (_digraph.inDegreeOf(v)==0) {
				rootVertices.add(v);
			}
		}
		return rootVertices;
	}
	
}
