package scheduler.astar;

import java.util.PriorityQueue;
import java.util.Set;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

/**
 * AStar runnable class that will be added to allow solution search in parallel
 * Needs to be wrapped by Thread() to start
 */
public class AStarThread extends AStar implements Runnable{
	int _threadNo; //Number identifier for thread
	private Solution _bestSolution = null;
	
	/**
	 * AStarThread constructor
	 * @param id thread identifier (a number to identify)
	 * @param graph task digraph
	 * @param solutionSpace shared OPEN solution space (PriorityQueue)
	 * @param closedSolutions shared CLOSED solution space (Set)
	 * @param numberOfProcessors number of processors the task scheduling is done on
	 * @param Visualizer graph visualization
	 */
	public AStarThread(int id, DefaultDirectedWeightedGraph graph, PriorityQueue<Solution> solutionSpace, Set<Solution> closedSolutions, int numberOfProcessors, Visualizer Visualizer) {
		super(graph, numberOfProcessors, Visualizer);
		this._threadNo = id;
		this._solutionSpace = solutionSpace;
		this._closedSolutions = closedSolutions;
	}
	
	/**
	 * Find optimal solution with the shared OPEN & CLOSED solution space
	 * Thread#start() will allow this function to run
	 */
	public void run() {
		_bestSolution = findOptimalSolution();
		System.out.println("Thread " + _threadNo + " --> "+System.nanoTime()/1000000000 + " seconds");
	}
	
	/**
	 * Get the optimal solution from this thread
	 * @return optimal solution from this thread
	 */
	public Solution getSolution(){
		return _bestSolution;
	}
	
}
