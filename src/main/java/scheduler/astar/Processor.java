package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

import scheduler.basicmilestone.Vertex;

public class Processor {

	private List<ProcessInfo> processes;
	private int earliestNextProcess = 0;
		
	public Processor() {
		processes = new ArrayList<ProcessInfo>();
	}
	
	public int getTime() {
		return processes.get(processes.size()-1).endTime(); //end time of last scheduled task
	}
	
	public void addProcess(Vertex v) {
		ProcessInfo p = new ProcessInfo(v,earliestNextProcess);
		processes.add(p);
		earliestNextProcess = p.endTime();
	}
	
	public List<ProcessInfo> getProcesses() {
		return processes;
	}

	public boolean scheduled(Vertex v) {
		for (ProcessInfo pI : processes) {
			if (pI.getVertex().equals(v)) {
				return true;
			}
		}
		return false;
	}
}
