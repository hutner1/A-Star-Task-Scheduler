package scheduler.graphstructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A Directed Graph with edge weights
 * 
 * This graph caches the incoming and outgoing edges of all vertices
 * after the graph's vertices and edges are finalised, when
 * incomingEdgesOf() and outgoingEdgesOf() are called to any vertex.
 */
public class DefaultDirectedWeightedGraph {
	// NODES
	private ArrayList<Vertex> _vertices;
	// EDGES
	private ArrayList<DefaultWeightedEdge> _edges;
	// Store incoming and outgoing edges of each task vertex 
	private HashMap<Vertex, ArrayList<DefaultWeightedEdge>> _incomingEdges;
	private HashMap<Vertex, ArrayList<DefaultWeightedEdge>> _outgoingEdges;
	private HashMap<Vertex, ArrayList<Vertex>> _parents;
	private HashMap<Vertex, ArrayList<Vertex>> _children;

	/**
	 * Constructor for digraph
	 */
	public DefaultDirectedWeightedGraph(){
		_vertices = new ArrayList<>();
		_edges = new ArrayList<>();
		_incomingEdges = new HashMap<>();
		_outgoingEdges = new HashMap<>();
		_parents = new HashMap<>();
		_children = new HashMap<>();
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
				Collections.sort(edges);
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
				Collections.sort(edges);
				_outgoingEdges.put(vertex, edges);
		}
				return _outgoingEdges.get(vertex);
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
	
	/**
	 * Get parents vertices
	 * @param vertex
	 * @return parents vertices
	 */
	public ArrayList<Vertex> getDirectParents(Vertex vertex){
		ArrayList<Vertex> parents = new ArrayList<>();
		for(DefaultWeightedEdge e : incomingEdgesOf(vertex)){
			parents.add(getEdgeSource(e));
		}
		return parents;
	}

	/**
	 * Get children vertices
	 * @param vertex
	 * @return children vertices
	 */
	public ArrayList<Vertex> getDirectChildren(Vertex vertex){
		ArrayList<Vertex> children = new ArrayList<>();
		for(DefaultWeightedEdge e : outgoingEdgesOf(vertex)){
			children.add(getEdgeTarget(e));
		}
		return children;
	}
	
	/**
	 * Returns root nodes of the digraph
	 * @return root nodes of the digraph
	 */
	public ArrayList<Vertex> returnRootVertices(){
		ArrayList<Vertex> rootVertices = new ArrayList<>();
		for (Vertex v: vertexSet()) {
			if (inDegreeOf(v)==0) {
				rootVertices.add(v);
			}
		}
		return rootVertices;
	}
	
	public String getVertexString(Vertex v) {
		StringBuilder infoString = new StringBuilder();

		infoString.append(v.getWeight());
		for (DefaultWeightedEdge e : incomingEdgesOf(v)) {
			infoString.append(e.sourceString());
		}
		
		infoString.append("-");
		
		for (DefaultWeightedEdge e : outgoingEdgesOf(v)) {
			infoString.append(e.destString());
		}

		return infoString.toString();
	}



	/**
	 * Creates an artificial edge between two vertices
	 * @param parent
	 * @param child
	 */
	public void addChild(Vertex parent, Vertex child) {

		ArrayList<Vertex> childrenList;

		if (_children.get(parent) == null) {
			childrenList = createChildren(parent);
			_children.put(parent, childrenList);
		} else {
			childrenList = _children.get(parent);
		}

		childrenList.add(child);

		ArrayList<Vertex> parentList;

		if (_parents.get(child) == null) {
			parentList = createParents(child);
			_parents.put(child, parentList);
		} else {
			parentList = _parents.get(child);
		}

		parentList.add(parent);
	}

	private ArrayList<Vertex> createParents(Vertex v) {
		ArrayList<Vertex> parentList = new ArrayList<Vertex>();
		for (DefaultWeightedEdge e : incomingEdgesOf(v)) {
			parentList.add(e.getSource());
		}
		return parentList;
	}

	private ArrayList<Vertex> createChildren(Vertex v) {
		ArrayList<Vertex> childrenList = new ArrayList<Vertex>();
		for (DefaultWeightedEdge e : outgoingEdgesOf(v)) {
			childrenList.add(e.getDest());
		}
		return childrenList;
	}

	public List<Vertex> getChildren(Vertex v) {
		if (_children.get(v) == null) {
			_children.put(v, createChildren(v));
		} 
		return _children.get(v);
	}


	public List<Vertex> getParents(Vertex v) {
		if (_parents.get(v) == null) {
			_parents.put(v, createParents(v));
		} 
		return _parents.get(v);
	}
}
