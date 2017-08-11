package scheduler.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;

/**
 * Represent partial/complete solution
 * Comparable because needed for comparison in priority queue (polling for best solution)
 */
public class Solution implements Comparable<Solution>{
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;
	private List<Vertex> _scheduledProcesses;
	private List<Vertex> _schedulableProcesses;
	private List<Vertex> _nonschedulableProcesses;
	private DefaultDirectedWeightedGraph _graph;

	public Solution(int numberOfProcessors, DefaultDirectedWeightedGraph graph, List<Vertex> scheduled, List<Vertex> schedulable, List<Vertex> nonschedulable) {

		_numberOfProcessors = numberOfProcessors;
		_processors = new HashMap<Integer, Processor>();
		_graph = graph;

		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}

		_scheduledProcesses = new ArrayList<Vertex>(scheduled);
		_schedulableProcesses = new ArrayList<Vertex>(schedulable);
		_nonschedulableProcesses = new ArrayList<Vertex>(nonschedulable);
	}
	
	/**
	 * The greatest last end time on all processors
	 */
	public int getTime() {
		int maximumTime = 0;

		for (Processor p : _processors.values()) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}

		return maximumTime;
	}

	/**
	 * Adds a process to a processor at its earliest possible time
	 * @param v
	 * @param processorNumber
	 */
	public void addProcess(Vertex v, int processorNumber) {

		// for every processor, get the latest starting parent, then determine the earliest possible start time of new process
		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		// finds task's dependencies, and finds earliest possible start time
		for (int i = 1; i <= _numberOfProcessors; i++) {
			int latestParentEndTime = 0;
			for (DefaultWeightedEdge e : _graph.incomingEdgesOf(v)) {
				Vertex parent = _graph.getEdgeSource(e);
				if (_processors.get(i).isScheduled(parent)) {

					int parentEndTime = _processors.get(i).endTimeOf(parent);
					if (processorNumber != i) {
						parentEndTime += _graph.getEdgeWeight(e);
					}

					if (parentEndTime > latestParentEndTime) {
						latestParentEndTime = parentEndTime;
					}

				}
			}
			startingTimes.add(latestParentEndTime);
		}
		int earliestStartTime = Collections.max(startingTimes);
		int earliestAvailableTime = _processors.get(processorNumber).earliestNextProcess();
		//System.out.println(earliestStartTime);
		//System.out.println(earliestAvailableTime);
		if (earliestStartTime > earliestAvailableTime) {
			_processors.get(processorNumber).addProcess(v,earliestStartTime);
		} else {
			_processors.get(processorNumber).addProcess(v,earliestAvailableTime);
		}

		// saying that this task has been scheduled and update the list of schedulables
		updateSchedulable(v);
	}

	@Override
	//TODO change & comment
	public int compareTo(Solution s) {
		if (s.getTime() == this.getTime()) {
			if (s._scheduledProcesses.size() > this._scheduledProcesses.size()) {
				return -1;
			} else if (s._scheduledProcesses.size() < this._scheduledProcesses.size()) {
				return 1;
			} else {
				return 0;
			}
		} else if (s.getTime() > this.getTime()) {
			return -1;
		} else {
			return 1;
		}
	}


	/**
	 * This method updates the list of schedulable and nonschedulable processes.
	 * It checks the children processes of the parent input and if all of the parent processes
	 * of the child have already been scheduled
	 * @param parent Parent process to be updated
	 * @param scheduled List of processes already scheduled
	 * @param schedulable List of processes available to be scheduled
	 * @param nonschedulable List of processes that have not had their dependencies met
	 */
	private void updateSchedulable(Vertex parent) {

		_scheduledProcesses.add(parent);
		_schedulableProcesses.remove(parent);

		for (DefaultWeightedEdge outEdge : _graph.outgoingEdgesOf(parent)) {
			Vertex child = _graph.getEdgeTarget(outEdge);

			boolean canBeScheduled = true;
			for (DefaultWeightedEdge inEdge : _graph.incomingEdgesOf(child)) {
				if (!_scheduledProcesses.contains(_graph.getEdgeSource(inEdge))) { 
					canBeScheduled = false;
				}
			}
			if (canBeScheduled && _nonschedulableProcesses.contains(child))  {
				_schedulableProcesses.add(child);
				_nonschedulableProcesses.remove(child);
			}
		}
	}

	/**
	 * Checks if all tasks have been scheduled
	 */
	public boolean isCompleteSchedule() {
		return _scheduledProcesses.size() == _graph.vertexSet().size();
	}

	/**
	 * Get children tasks as a deep copy
	 * @return
	 */
	public List<Solution> createChildren() {
		List<Solution> children = new ArrayList<Solution>();
		for (Vertex v : _schedulableProcesses) {
			for (int i = 1; i <= _numberOfProcessors; i++) {
				Solution child = createDuplicateSolution();
				child.addProcess(v, i);
				child.printTree();
				children.add(child);
			}
		}
		return children;
	}

	/**
	 * Debugging
	 */
	private void printTree() {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			System.out.print("P:" + i + " [ ");
			_processors.get(i).printProcesses();
			System.out.print("] ");
		}
		System.out.println();
	}

	/**
	 * Creates a hard copy of current solution
	 * @return
	 */
	public Solution createDuplicateSolution() {
		Solution s = new Solution(_numberOfProcessors, _graph, _scheduledProcesses, _schedulableProcesses, _nonschedulableProcesses);
		s.setProcessorSchedule(_processors);
		return s;
	}
	
	private void setProcessorSchedule(HashMap<Integer, Processor> processors) {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			_processors.put(i, processors.get(i).createDeepCopy());
		}
	}

	public String getVertexString(Vertex vertex) {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			if (_processors.get(i).isScheduled(vertex)) {				
				return ", Start=" + _processors.get(i).getProcess(vertex).startTime() + ", Processor=" + i;
			}
		}
		return null;
	}


	/*public boolean scheduled(Vertex v) {

		for (Processor p : _processors.values()) {
			if (p.isScheduled(v)) {
				return true;
			}
		}
		return false;
	}*/

}

