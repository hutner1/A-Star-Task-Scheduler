package scheduler.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class Solution implements Comparable<Solution>{
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;
	private List<Vertex> scheduledProcesses = new ArrayList<Vertex>();
	private List<Vertex> schedulableProcesses = new ArrayList<Vertex>();
	private List<Vertex> nonschedulableProcesses = new ArrayList<Vertex>();
	private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _graph;

	public Solution(int numberOfProcessors, DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph) {
		_numberOfProcessors = numberOfProcessors;
		_processors = new HashMap<Integer, Processor>();
		_graph = graph;

		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}
	}
	
	private Solution createCopy() {
		Solution s = new Solution();
		s._numberOfProcessors = this._numberOfProcessors;
		s.scheduledProcesses = this.scheduledProcesses;
		s.schedulableProcesses = this.schedulableProcesses;
		s.nonschedulableProcesses = this.nonschedulableProcesses;
		return s;
	}

	public int getTime() {
		int maximumTime = 0;

		for (Processor p : _processors.values()) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}

		return maximumTime;
	}

	public void addProcess(Vertex v, int processorNumber) {

		//for every processor, get the latest starting parent, then determine the earliest possible start time of new process

		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		for (int i = 1; i <= _numberOfProcessors; i++) {
			int latestParentEndTime = 0;
			for (DefaultWeightedEdge e : _graph.incomingEdgesOf(v)) {
				Vertex parent = _graph.getEdgeSource(e);
				if (_processors.get(i).isScheduled(parent)) {
					if (_processors.get(i).endTimeOf(parent) > latestParentEndTime) {
						latestParentEndTime = _processors.get(i).endTimeOf(parent);
						if (processorNumber != i) {
							latestParentEndTime += _graph.getEdgeWeight(e);
						}
					}
				}
			}
			startingTimes.add(latestParentEndTime);
		}

		int earliestStartTime = Collections.max(startingTimes);
		int earliestAvailableTime = _processors.get(processorNumber).earliestNextProcess();
		
		if (earliestStartTime > earliestAvailableTime) {
			_processors.get(processorNumber).addProcess(v,earliestStartTime);
		} else {
			_processors.get(processorNumber).addProcess(v,earliestAvailableTime);
		}
	
		updateSchedulable(v);
	}

	@Override
	public int compareTo(Solution s) {
		if (s.getTime() == this.getTime()) {
			if (s.scheduledProcesses.size() > this.scheduledProcesses.size()) {
				return -1;
			} else if (s.scheduledProcesses.size() < this.scheduledProcesses.size()) {
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
		
		scheduledProcesses.add(parent);
		
		for (DefaultWeightedEdge outEdge : _graph.outgoingEdgesOf(parent)) {
			Vertex child = _graph.getEdgeTarget(outEdge);
			
			boolean canBeScheduled = true;
			for (DefaultWeightedEdge inEdge : _graph.incomingEdgesOf(child)) {
				if (!scheduledProcesses.contains(_graph.getEdgeSource(inEdge))) { 
					canBeScheduled = false;
				}
			}
			if (canBeScheduled && nonschedulableProcesses.contains(child))  {
				schedulableProcesses.add(child);
				nonschedulableProcesses.remove(child);
			}
		}
	}
	
	public boolean isCompleteSchedule() {
		return scheduledProcesses.size() == _graph.vertexSet().size();
	}

	public List<Solution> createChildren() {
		
		List<Solution> children = new ArrayList<Solution>();
		
		for (Vertex v : schedulableProcesses) {
			for (int i = 1; i <= _numberOfProcessors; i++) {
				Solution child = createDeepCopy();
				child.addProcess(v, i);
				children.add(child);
			}
		}
	
	}
	
	public Solution createDeepCopy() {
		
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

