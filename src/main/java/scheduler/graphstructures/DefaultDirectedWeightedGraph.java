package scheduler.graphstructures;

import java.util.ArrayList;

/**
 * A Directed Graph with edge weights 
 */
public class DefaultDirectedWeightedGraph {
	// NODES
	private ArrayList<Vertex> _vertices;
	// EDGES
	private ArrayList<DefaultWeightedEdge> _edges;

	/**
	 * Constructor for digraph
	 */
	public DefaultDirectedWeightedGraph(){
		_vertices = new ArrayList<>();
		_edges = new ArrayList<>();
	}
	
	/**
	 * Add node to digraph
	 */
	public void addVertex(Vertex vertex){
		_vertices.add(vertex);
	}
	
	/**
	 * Add edge to digraph
	 */
	public DefaultWeightedEdge addEdge(Vertex source, Vertex dest, int weight){
		DefaultWeightedEdge edge = new DefaultWeightedEdge(source, dest, weight);
		_edges.add(edge);
		return edge;
	}
	
	/**
	 * Return the set of nodes in the graph 
	 */
	public ArrayList<Vertex> vertexSet(){
		return _vertices;
	}
	
	/**
	 * Return the set of edge in the graph 
	 */
	public ArrayList<DefaultWeightedEdge> edgeSet(){
		return _edges;
	}
	
	/**
	 * Get all edges connected to the vertex
	 * @param vertex vertex to get edges for
	 * @return all edges connected to the vertex
	 */
	public ArrayList<DefaultWeightedEdge> edgesOf(Vertex vertex){
		ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
		for(DefaultWeightedEdge edge : _edges){
			if(edge.contains(vertex)){
				edges.add(edge);
			}
		}
		return edges;
	}
	
	/** 
	 * Get all edges pointing to the vertex
	 * @param vertex vertex to get incoming edges for
	 * @return all edges pointing to the vertex // TODO 
	 */
	public ArrayList<DefaultWeightedEdge> incomingEdgesOf(Vertex vertex){
		ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
		for(DefaultWeightedEdge edge : _edges){
			if(edge.getDest().equals(vertex)){
				edges.add(edge);
			}
		}
		return edges;
	}
	// MAKE IF EFFICIENT initialise them first // TODO
	/**
	 * Get all edges going out from the vertex
	 * @param vertex vertex to get outgoing edges for
	 * @return all edges going out from the vertex // TODO
	 */
	public ArrayList<DefaultWeightedEdge> outgoingEdgesOf(Vertex vertex){
		ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
		for(DefaultWeightedEdge edge : _edges){
			if(edge.getSource().equals(vertex)){
				edges.add(edge);
			}
		}
		return edges;
	}

	/**
	 * Remove the edge from the graph
	 * @param edge edge to remove
	 */
	public void removeEdge(DefaultWeightedEdge edge) {
		_edges.remove(edge);		
	}

	/**
	 * Returns the number of incoming edges
	 * @param vertex
	 * @return number of incoming edges
	 */
	public int inDegreeOf(Vertex vertex) {
		ArrayList<DefaultWeightedEdge> edges = incomingEdgesOf(vertex);
		return edges.size();
	}

	/**
	 * Get the edge of the edge
	 * @param edge
	 * @return destination node of edge
	 */
	public Vertex getEdgeSource(DefaultWeightedEdge edge) {
		return edge.getSource();
	}
	
	/**
	 * Get the source of the edge
	 * @param edge
	 * @return source node of edge
	 */
	public Vertex getEdgeTarget(DefaultWeightedEdge edge) {
		return edge.getDest();
	}

	/**
	 * Get the weight of the weighted edge
	 * @param edge
	 * @return weight on an edge
	 */
	public int getEdgeWeight(DefaultWeightedEdge edge) {
		return edge.getWeight();
	}
}
