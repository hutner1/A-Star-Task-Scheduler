package basic_milestone;

import java.util.HashMap;

/**
 * This class stores all the vertices in the graph
 * and the solutions of the topological sort in a hash map
 */
public class ScheduleInfo {
	
	private HashMap<Vertex,NodeInfo> nodeMap;
	
	//Constructor
	public ScheduleInfo() {
		nodeMap = new HashMap<>();
	}
	
	//Get all node information for the vertex
	public NodeInfo getVertexInfo(Vertex v) {
		return nodeMap.get(v);
	}
	
	//Add a vertex into the hash map of nodes
	public void addVertexToMap(Vertex v, NodeInfo ni) {
		nodeMap.put(v, ni);
	}
}