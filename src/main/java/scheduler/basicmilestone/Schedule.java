package scheduler.basicmilestone;

import java.util.HashMap;

/**
 * This class stores all the vertices in the graph
 * and the solutions of the topological sort in a hash map
 */
public class Schedule {
	
	private HashMap<Vertex,VertexInfo> vertexMap;
	
	/**
	 * Constructor that initialises an EMPTY hashmap for the schedule
	 * <Vertex,VertexInfo>
	 */
	public Schedule() {
		vertexMap = new HashMap<>();
	}
	
	/**
	 * Get VertexInfo for the vertex
	 * @param v Vertex
	 * @return VertexInfo for the Vertex (solution)
	 */
	public VertexInfo getVertexInfo(Vertex v) {
		return vertexMap.get(v);
	}
	
	/**
	 * Add a vertex into the hash map of vertices together with the solution
	 * @param v Vertex
	 * @param vi Vertex info
	 */
	public void addVertexToMap(Vertex v, VertexInfo vi) {
		vertexMap.put(v, vi);
	}
}