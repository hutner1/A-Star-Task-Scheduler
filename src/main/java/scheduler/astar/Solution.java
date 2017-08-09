package scheduler.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class Solution {
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;

	public Solution(int numberOfProcessors) {
		_numberOfProcessors = numberOfProcessors;
		_processors = new HashMap<Integer, Processor>();

		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}

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

	public void addProcess(Vertex v, int processorNumber, DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph) {

		//for every processor, get the latest starting parent, then determine the earliest possible start time of new process

		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		for (int i = 1; i <= _numberOfProcessors; i++) {
			int latestParentEndTime = 0;
			for (DefaultWeightedEdge e : graph.incomingEdgesOf(v)) {
				Vertex parent = graph.getEdgeSource(e);
				if (_processors.get(i).isScheduled(parent)) {
					if (_processors.get(i).endTimeOf(parent) > latestParentEndTime) {
						latestParentEndTime = _processors.get(i).endTimeOf(parent);
						if (processorNumber != i) {
							latestParentEndTime += graph.getEdgeWeight(e);
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

