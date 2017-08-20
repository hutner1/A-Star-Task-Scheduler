package scheduler.graphstructures;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Directed Graph with edge weights 
 */
public class DefaultDirectedWeightedGraph {
	// NODES
	private ArrayList<Vertex> _vertices;
	// EDGES
	private ArrayList<DefaultWeightedEdge> _edges;
	// Store incoming and outgoing edges of each task vertex 
	private static HashMap<Vertex, ArrayList<DefaultWeightedEdge>> _incomingEdges;
	private static HashMap<Vertex, ArrayList<DefaultWeightedEdge>> _outgoingEdges;

	/**
	 * Constructor for digraph
	 */
	public DefaultDirectedWeightedGraph(){
		_vertices = new ArrayList<>();
		_edges = new ArrayList<>();
		_incomingEdges = new HashMap<>();
		_outgoingEdges = new HashMap<>();
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
		edges.addAll(incomingEdgesOf(vertex));
		edges.addAll(outgoingEdgesOf(vertex));
		return edges;
	}
	
	/** 
	 * Get all edges pointing to the vertex
	 * @param vertex vertex to get incoming edges for
	 * @return all edges pointing to the vertex // TODO 
	 */
	public ArrayList<DefaultWeightedEdge> incomingEdgesOf(Vertex vertex){
		if(_incomingEdges.get(vertex)==null){
			ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
			for(DefaultWeightedEdge edge : _edges){
				if(edge.getDest().equals(vertex)){
					edges.add(edge);
				}
			}
			_incomingEdges.put(vertex, edges);	
		}
		return _incomingEdges.get(vertex);
	}
	// MAKE IF EFFICIENT initialise them first // TODO
	/**
	 * Get all edges going out from the vertex
	 * @param vertex vertex to get outgoing edges for
	 * @return all edges going out from the vertex // TODO
	 */
	public ArrayList<DefaultWeightedEdge> outgoingEdgesOf(Vertex vertex){
		if(_outgoingEdges.get(vertex)==null){
			ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
			for(DefaultWeightedEdge edge : _edges){
				if(edge.getSource().equals(vertex)){
					edges.add(edge);
				}
			}
			_outgoingEdges.put(vertex, edges);
		}
		return _outgoingEdges.get(vertex);
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
