package scheduler.astar;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class AStar {
	private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _graph;
	private int _numberOfProcessors;

	public AStar(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph, int numberOfProcessors) {
		_graph = graph;
	}

	public void execute() {

		List<Solution> initialSolutions = new ArrayList<Solution>();
		List<Vertex> scheduledProcesses = new ArrayList<Vertex>();
		List<Vertex> schedulableProcesses = new ArrayList<Vertex>();
		List<Vertex> nonschedulableProcesses = new ArrayList<Vertex>();

		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) { //get source nodes
				Solution s = new Solution(_numberOfProcessors);
				s.addProcess(v, 1, _graph);
				initialSolutions.add(s); //list of solutions starting source node
				schedulableProcesses.add(v); 
			} else {
				nonschedulableProcesses.add(v);
			}
		}

		Solution bestInitialSolution = null; 
		int minTime = Integer.MAX_VALUE;

		for (Solution s : initialSolutions) {
			if (s.getTime() < minTime) {
				bestInitialSolution = s;
				minTime = s.getTime(); //min time of the source nodes
			}
		}

		for (Vertex v : schedulableProcesses) {
			for (int i = 1; i <= _numberOfProcessors; i++) {
				bestInitialSolution.addProcess(v, i, _graph);
				updateSchedulable(v, scheduledProcesses, schedulableProcesses, nonschedulableProcesses);
			}
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
	private void updateSchedulable(Vertex parent, List<Vertex> scheduled, List<Vertex> schedulable, List<Vertex> nonschedulable) {
		
		scheduled.add(parent);
		
		for (DefaultWeightedEdge outEdge : _graph.outgoingEdgesOf(parent)) {
			Vertex child = _graph.getEdgeTarget(outEdge);
			
			boolean canBeScheduled = true;
			for (DefaultWeightedEdge inEdge : _graph.incomingEdgesOf(child)) {
				if (!scheduled.contains(_graph.getEdgeSource(inEdge))) { 
					canBeScheduled = false;
				}
			}
			if (canBeScheduled && nonschedulable.contains(child))  {
				schedulable.add(child);
				nonschedulable.remove(child);
			}
		}
	}
}
