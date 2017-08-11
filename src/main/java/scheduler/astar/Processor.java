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

	public Processor getClone() {
		Processor p = new Processor();
		for (ProcessInfo pI : processes) {
			p.processes.add(pI);
		}
		p.earliestNextProcess = earliestNextProcess;
		return p;
	}

	public int getTime() {
		if (processes.size() > 0) {
			return processes.get(processes.size()-1).endTime(); //end time of last scheduled task
		} else {
			return 0;
		}
	}

	public void addProcess(Vertex v, int time) {
		//System.out.println("ADDED " + v.getName());
		ProcessInfo p = new ProcessInfo(v,time);
		processes.add(p);
		earliestNextProcess = p.endTime();
	}

	public ProcessInfo getProcess(Vertex v) {
		for (ProcessInfo pi : processes) {
			if (pi.getVertex().equals(v)) {
				return pi;
			}
		}
		return null;
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

	public int earliestNextProcess() {
		return earliestNextProcess;
	}

	public void printProcesses() {
		for (ProcessInfo pI : processes) {
			System.out.print(pI.getVertex().getName() + " ");
		}
	}
}
