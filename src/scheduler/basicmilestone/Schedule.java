package scheduler.basicmilestone;

import java.util.HashMap;

/**
 * This class stores all the vertices in the graph
 * and the solutions of the topological sort in a hash map
 */
public class Schedule {
	
	private HashMap<Vertex,NodeInfo> nodeMap;
	
	/**
	 * Constructor that initialises an EMPTY hashmap for the schedule
	 * <Vertex,NodeInfo>
	 */
	public Schedule() {
		nodeMap = new HashMap<>();
	}
	
	/**
	 * Get NodeInfo for the vertex
	 * @param v Vertex
	 * @return NodeInfo for the Vertex (solution)
	 */
	public NodeInfo getVertexInfo(Vertex v) {
		return nodeMap.get(v);
	}
	
	/**
	 * Add a vertex into the hash map of nodes togehter with the solution
	 * @param v Vertex
	 * @param ni Node info
	 */
	public void addVertexToMap(Vertex v, NodeInfo ni) {
		nodeMap.put(v, ni);
	}
}