package scheduler.basicmilestone;
/**
 * This class is used to store information about a node
 * while it is processed in an algorithm.
 * 
 * Name and weight will be set when initialising the Vertex.
 * 
 * BottomLvl will be set later, it is the lvl from the leaf 
 */
public class Vertex {
	private String name;
	private int weight;
	private int bottomLvl;
	private int touchCount;
	
	public Vertex(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}
	
	//---Getter Methods---
	
		/**
		 * Get the name of Vertex
		 * @return String name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * Get the weight of Vertex
		 * @return int weight
		 */
		public int getWeight() {
			return weight;
		}
		
		/**
		 * Get the critical path from one node
		 * @return int critical path from one node
		 */
		public int getBottomLevel() {
			return bottomLvl;
		}
		
		/**
		 * Get the number of times this vertex is accessed in a solution
		 * To be used in the A* implementation
		 * @return int number of use
		 */
		public int getTouchCount() {
			return touchCount;
		}
		
		
		//---Setter Methods---
		
		
		/**
		 * Set the weight of a node
		 * @param weight
		 */
		public void setWeight(int w) {
			this.weight = w;
		}
		
		/**
		 * Set new value to the bottomLvl
		 * @param value from bottomLvl
		 */
		public void setBottomLevel(int val) {
			bottomLvl = val;
		}
}