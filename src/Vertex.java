/**
 * Used to store information about a task
 */
public class Vertex {
	private String name;
	private int w;
	private int bottomLvl;
	private int touchCount;
	
	public Vertex(String name, int w) {
		this.name = name;
		this.w = w;
	}
	
	//---Get Methods---
	
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
			return w;
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
		 * @return int number of use
		 */
		public int getTouchCount() {
			return touchCount;
		}
		
		
		//---Set Methods---
		
		
		/**
		 * Set the weight of a node
		 * @param weight
		 */
		public void setWeight(int w) {
			this.w = w;
		}
		
		/**
		 * Set new value to the bottomLvl
		 * @param value from bottomLvl
		 */
		public void setBottomLevel(int val) {
			bottomLvl = val;
		}
}