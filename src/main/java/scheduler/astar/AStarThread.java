package scheduler.astar;

import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

import visualization.gantt.Gantt;

import visualization.gui.Gui;
import visualization.gui.StatisticTable;


/**
 * AStar runnable class that will be added to allow solution search in parallel
 */
public class AStarThread extends AStar implements Runnable{
	int _threadNo; //Number identifier for thread
	private Solution _bestSolution = null;
	private AStarParallelised _asp;

	/**
	 * AStarThread constructor
	 * @param id thread identifier (a number to identify)
	 * @param graph task digraph
	 * @param solutionSpace shared OPEN solution space (PriorityBlockingQueue)
	 * @param closedSolutions shared CLOSED solution space (Set)
	 * @param numberOfProcessors number of processors the task scheduling is done on
	 * @param Visualizer graph visualization
	 */
	public AStarThread(int id, DefaultDirectedWeightedGraph graph, PriorityBlockingQueue<Solution> solutionSpace, Set<Solution> closedSolutions, int numberOfProcessors, Visualizer Visualizer, int upperBound, AStarParallelised asp, Gantt gantt, StatisticTable stats) {
		super(graph, numberOfProcessors, Visualizer, gantt, stats);
		this._threadNo = id;
		this._solutionSpace = solutionSpace;
		this._closedSolutions = closedSolutions;
		this._upperBound = upperBound;
		this._asp = asp;
	}
	
	/**
	 * Find optimal solution with the shared OPEN & CLOSED solution space
	 * Thread#start() will allow this function to run
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		_bestSolution = findOptimalSolution();
		System.out.println("Thread " + _threadNo + " --> "+System.nanoTime()/1000000000 + " seconds");
		
		// sleep for 1 second for other threads near completion to finish
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// fisrt thread to finish will stop other threads from running
		for(int i = 0; i<_asp._numberOfThreads; i++){
			if(i != _threadNo){
				_asp._threads[i].stop(); //Return the solution that one thread has
			}
		}
	}
	
	/**
	 * Get the optimal solution from this thread
	 * @return optimal solution from this thread
	 */
	public Solution getSolution(){
		return _bestSolution;
	}
	
}
