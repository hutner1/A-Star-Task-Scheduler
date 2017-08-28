package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

import scheduler.graphstructures.Vertex;

/**
 * Processor containing tasks ran on it 
 * 
 * Stores a list of ProcessInfo, which is a list of task ran on the processor.
 */
public class Processor {

	private List<ProcessInfo> _processes;
	private int _earliestNextProcess = 0;

	/**
	 * Initialise a processor with no tasks ran on it
	 */
	public Processor() {
		_processes = new ArrayList<ProcessInfo>();
	}

	/**
	 * Create deep copy of processor
	 * @return deep copy of processor
	 */
	public Processor createDeepCopy() {
		Processor p = new Processor();
		for (ProcessInfo pI : _processes) {
			p._processes.add(pI);
		}
		p._earliestNextProcess = _earliestNextProcess;
		return p;
	}

	/**
	 * Get earliest possible start time for next task
	 * @return earliest possible start time for next task
	 */
	public int getTime() {
		if (_processes.size() > 0) {
			return _processes.get(_processes.size()-1).endTime(); //end time of last scheduled task
		} else {
			return 0;
		}
	}

	/**
	 * Add a task to the processor with a Vertex and a start time
	 * @param v task
	 * @param time start time
	 */
	public void addProcess(Vertex v, int time) {
		ProcessInfo p = new ProcessInfo(v,time);
		_processes.add(p);
		_earliestNextProcess = p.endTime();
	}

	/**
	 * Return ProcessInfo instance corresponding to the task,
	 * so that further information of the task on the processor
	 * can be obtained,
	 * 
	 * @param v task
	 * @return ProceessInfo of a task
	 */
	public ProcessInfo getProcess(Vertex v) {
		for (ProcessInfo pi : _processes) {
			if (pi.getVertex().equals(v)) {
				return pi;
			}
		}
		return null;
	}

	/**
	 * Checks if a task has been scheduled on the processor
	 * @param v task
	 */
	public boolean isScheduled(Vertex v) {
		for (ProcessInfo pI : _processes) {
			if (pI.getVertex().equals(v)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the start time of a task
	 * @param v task
	 * @return end time of a task
	 */
	public int startTimeOf(Vertex v) {
		for (ProcessInfo pi : _processes) {
			if (pi.getVertex().equals(v)) {
				return pi.startTime();
			}
		}
		return 0;
	}

	/**
	 * Checks the end time of a task
	 * @param v task
	 * @return end time of a task
	 */
	public int endTimeOf(Vertex v) {
		for (ProcessInfo pi : _processes) {
			if (pi.getVertex().equals(v)) {
				return pi.endTime();
			}
		}
		return 0;
	}

	/**
	 * @return the earliest possible start time for the next task on the processor
	 */
	public int earliestNextProcess() {
		return _earliestNextProcess;
	}

	/**
	 * Print the all the scheduled tasks' name
	 */
	public void printProcesses() {
		for (ProcessInfo pI : _processes) {
			System.out.print(pI.getVertex().getName() + " ");
		}
	}

	/**
	 * Print the all the scheduled tasks' name and start time
	 */
	public String getProcessessString() {
		StringBuilder s = new StringBuilder();
		for (ProcessInfo pI : _processes) {
			s.append(pI.getVertex().getName());
			s.append(pI.startTime());
		}
		return s.toString();
	}

	/**
	 * Returns the list of scheduled tasks on the processor 
	 * @return the list of scheduled tasks on the processor 
	 */
	public List<ProcessInfo> getProcesses() {
		return _processes;
	}

	/**
	 * Checks to see how long the processor has been idle states for
	 * @return time the processor has been idle for
	 */
	public int idleTime() {
		int idleTime = _earliestNextProcess;
		for (ProcessInfo pI : _processes) {
			idleTime += pI.startTime();
			idleTime -= pI.endTime();
		}
		return idleTime;
	}

	public List<Vertex> getProcessOrder() {
		List<Vertex> processOrder = new ArrayList<Vertex>();
		for (ProcessInfo pI : _processes) {
			processOrder.add(pI.getVertex());
		}
		return processOrder;
	}
}