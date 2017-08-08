package scheduler.basicmilestone;

/**
 * This class stores the information needed
 * for the final schedule, i.e. Start and Processor
 */
public class VertexInfo {
	
	private int startTime;
	private int processorNo;
	
	public VertexInfo(int st, int pn) {
		this.startTime = st;
		this.processorNo = pn;
	}
	
	//---Getter Methods---
	
	public int getStartTime() {
		return startTime;
	}
	
	/**
	 * Outputs a string for the solution
	 * @return Start= startTime , Processor= processorNo
	 */
	public String outputString() {
		return ", Start=" + startTime + ", Processor=" + processorNo;
	}
}