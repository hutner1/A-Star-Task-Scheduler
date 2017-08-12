package scheduler.astar;

import java.util.PriorityQueue;
import java.util.Set;

import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;

/**
 * AStar thread class that will be added to allow solution search in parallel
 */
public class AStarThread extends AStar implements Runnable{
	int _threadNo; //Number identifier for thread
	private Solution bestSol = null;
	
	public AStarThread(int i, DefaultDirectedWeightedGraph graph, PriorityQueue<Solution> solutionSpace, Set<Solution> closedSolutions, int numberOfProcessors) {
		super(graph, numberOfProcessors);
		this._threadNo = i;
		this._solutionSpace = solutionSpace;
		this._closedSolutions = closedSolutions;
	}
	
	public void run() {
		bestSol = super.execute();
	}
	
	public Solution execute() {
		return bestSol;
	}
}
