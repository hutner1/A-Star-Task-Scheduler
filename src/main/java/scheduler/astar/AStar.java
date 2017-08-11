package scheduler.astar;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

/**
 * AStar creates optimal solution with A*
 */
public class AStar {
	private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> _graph;
	private int _numberOfProcessors;

	public AStar(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph, int numberOfProcessors) {
		_graph = graph;
		_numberOfProcessors = numberOfProcessors;
	}

	/**
	 * Execute A* algorithm
	 * @return
	 */
	public Solution execute() {

		//Create initial solution and add to priority queue
		//Pop solution and create children solutions for that, readd children to queue
		//Pop most efficient child and add create children, readd
		//Repeat until child is a complete graph, that is the optimal schedule
	
		List<Vertex> schedulable = new ArrayList<Vertex>(); // dependencies all met
		List<Vertex> nonschedulable = new ArrayList<Vertex>(); // dependencies not met
		// fill lists of schedulables
		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) { //get source nodes
				schedulable.add(v);
			} else {
				nonschedulable.add(v);
			}
		}
		
		// state space
		PriorityQueue<Solution> solutionSpace = new PriorityQueue<Solution>();
		
		// creating all possible valid schedules with only the root nodes
		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) { //get source nodes
				Solution s = new Solution(_numberOfProcessors, _graph, new ArrayList<Vertex>(), schedulable, nonschedulable);
				s.addProcess(v, 1);
				solutionSpace.add(s); //list of solutions starting source node
			} 
		}
		
		// BEST priority solution
		Solution bestCurrentSolution = solutionSpace.poll();
		
		// if not complete, consider the children in generating the solution and poll again
		while (!bestCurrentSolution.isCompleteSchedule()) {
			
			solutionSpace.addAll(bestCurrentSolution.createChildren());
			bestCurrentSolution = solutionSpace.poll();
		
		}
		
		return bestCurrentSolution;



		/*
		List<Solution> initialSolutions = new ArrayList<Solution>();


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
		}*/


	}

}
