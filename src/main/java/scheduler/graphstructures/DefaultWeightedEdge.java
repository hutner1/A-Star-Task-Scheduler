package scheduler.graphstructures;

/**
 * An edge with weight 
 */
public class DefaultWeightedEdge implements Comparable<DefaultWeightedEdge>{
	private int _edgeWeight;
	private Vertex _sourceNode;
	private Vertex _destNode;

	/**
	 * Constructor for weighted edge
	 * @param source 
	 * @param dest 
	 * @param weight 
	 */
	public DefaultWeightedEdge(Vertex source, Vertex dest, int weight){
		_edgeWeight = weight;
		_sourceNode = source;
		_destNode = dest;
	}

	/**
	 * @return source edge
	 */
	public Vertex getSource(){
		return _sourceNode;
	}

	/**
	 * @return destination edge
	 */	
	public Vertex getDest(){
		return _destNode;
	}

	/**
	 * @return weight of an edge
	 */
	public int getWeight(){
		return _edgeWeight;
	}

	/**
	 * Check if vertex is part of an edge
	 * @param vertex node
	 * @return whether it is part of the edge
	 */
	public boolean contains(Vertex vertex){
		if(_sourceNode.equals(vertex)||_destNode.equals(vertex)){
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(DefaultWeightedEdge o) {

		if (o._sourceNode.getName().equals(_sourceNode.getName())) {
			return o._destNode.getName().compareTo(_destNode.getName());
		} else {
			return (o._sourceNode.getName().compareTo(_sourceNode.getName()));
		}
	}
	
	public String sourceString() {
		StringBuilder string = new StringBuilder();
		string.append(_edgeWeight);
		string.append(_sourceNode.getName());
		
		return string.toString();
	}
	
	public String destString() {
		StringBuilder string = new StringBuilder();
		string.append(_edgeWeight);
		string.append(_destNode.getName());
		
		return string.toString();
	}
}
