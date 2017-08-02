package basic_milestone;

/**
 * This class stores the information needed
 * for the final schedule
 */
public class NodeInfo {
	
	private int startTime;
	private int processorNo;
	
	public NodeInfo(int st, int pn) {
		this.startTime = st;
		this.processorNo = pn;
	}
	
	//---Getter Methods---
	
	public int getStartTime() {
		return startTime;
	}
	
	//Outputs a string for the solution
	public String outputString() {
		return ", Start=" + startTime + ", Processor=" + processorNo;
	}
}