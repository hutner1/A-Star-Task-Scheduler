package scheduler.astar;

import java.util.PriorityQueue;
import java.util.Set;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;
import visualization.Visualizer;
import visualization.gui.Gui;

/**
 * AStar thread class that will be added to allow solution search in parallel
 */
public class AStarThread extends AStar implements Runnable{
	int _threadNo; //Number identifier for thread
	private Solution bestSol = null;
	
	public AStarThread(int i, DefaultDirectedWeightedGraph graph, PriorityQueue<Solution> solutionSpace, Set<Solution> closedSolutions, int numberOfProcessors, Visualizer Visualizer,Gui gui) {
		super(graph, numberOfProcessors, Visualizer, gui);
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
