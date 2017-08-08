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
	
	public void addProcess(Vertex v, int time) {
		ProcessInfo p = new ProcessInfo(v,time);
		processes.add(p);
		earliestNextProcess = p.endTime();
	}
	
	public List<ProcessInfo> getProcesses() {
		return processes;
	}

	public boolean isScheduled(Vertex v) {
		for (ProcessInfo pI : processes) {
			if (pI.getVertex().equals(v)) {
				return true;
			}
		}
		return false;
	}
	
	public int endTimeOf(Vertex v) {
		for (ProcessInfo pi : processes) {
			if (pi.getVertex().equals(v)) {
				return pi.endTime();
			}
		}
		return 0;
	}
}
