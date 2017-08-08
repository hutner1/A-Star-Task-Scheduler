package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

public class Processor {

	private List<ProcessInfo> processes;
		
	public Processor() {
		processes = new ArrayList<ProcessInfo>();
	}
	
	public int getTime() {
		return processes.get(processes.size()-1).endTime();
	}
	
	public void addProcess(ProcessInfo p) {
		processes.add(p);
	}
	
	public List<ProcessInfo> getProcesses() {
		return processes;
	}
}
