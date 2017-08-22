package scheduler.graphstructures;
/**
 * This class is used to store information about a node
 * while it is processed in an algorithm.
 * 
 * Name and weight will be set when initializing the Vertex.
 * 
 * BottomLvl will be set later, it is the level from the leaf 
 * Visited is used by topological sort to indicate that the node is visited
 */
public class Vertex {
	private String _name;
	private int _weight;
	private int _bottomLvl;
	private boolean _visited;
	
	public Vertex(String name) {
		_name = name;
		_visited = false;
	}
	
	//---Getter Methods---
	
		/**
		 * Get the name of Vertex
		 * @return String name
		 */
		public String getName(){
			return _name;
		}
		
		/**
		 * Get the weight of Vertex
		 * @return int weight
		 */
		public int getWeight() {
			return _weight;
		}
		
		/**
		 * Get the critical path from one node
		 * @return int critical path from one node
		 */
		public int getBottomLevel() {
			return _bottomLvl;
		}
		
		/**
		 * Check to see if node is visited
		 * @return visited status
		 */
		public boolean isVisited(){
			return _visited;
		}
		
		
		//---Setter Methods---
		/**
		 * Set the weight of a node
		 * @param weight
		 */
		public void setWeight(int w) {
			this._weight = w;
		}
		
		/**
		 * Set new value to the bottomLvl
		 * @param value from bottomLvl
		 */
		public void setBottomLevel(int val) {
			_bottomLvl = val;
		}
		
		/**
		 * Set that the node is visited
		 */
		public void setVisited(){
			_visited = true;
		}
}