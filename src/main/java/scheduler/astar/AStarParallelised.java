package scheduler.astar;

import java.util.PriorityQueue;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

import visualization.gantt.Gantt;

import visualization.gui.Gui;

/**
 * AStar thread class that will be added to allow solution search in parallel
 * TODO: Incorporate Paratask or Bananas in Pyjamas
 */
public class AStarParallelised extends AStar{

	// Field for number of threads
	protected int _numberOfThreads;

	//Array of threads and custom threads
	AStarThread[] AStarThreads = new AStarThread[_numberOfThreads];
	Thread[] threads = new Thread[_numberOfThreads];


	public AStarParallelised(DefaultDirectedWeightedGraph graph, int numberOfProcessors, int numberOfThreads, Visualizer Visualizer, Gantt gantt) {
		super(graph, numberOfProcessors, Visualizer, gantt);

		this._numberOfThreads = numberOfThreads;
	}

	@Override
	public Solution execute() {
		return executeInParallel();
	}


	/**
	 * Execute the A* algorithm in parallel using separate threads
	 * @return optimal solution
	 */
	@SuppressWarnings("unchecked")
	protected Solution executeInParallel() {



		//Start threading process. 
		for (int i = 0; i < _numberOfThreads; i++) {

			AStarThreads[i] = new AStarThread(i, _graph, _solutionSpace, _closedSolutions, _numberOfProcessors, _visualizer, _gantt);

			//Add the custom thread with all the AStar fields into a thread
			threads[i] = new Thread(AStarThreads[i]);
			threads[i].run();
		}



		//Try to join threads once the threads have finished
		for (int i = 0; i <_numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getBestSolution(AStarThreads);
	}

	private Solution getBestSolution(AStarThread[] threads) {
		Solution bestSolution = AStarThreads[0].execute();
		for (int i = 1; i < _numberOfThreads; i++) {
			if (bestSolution.getTime()< AStarThreads[i].execute().getTime()) {

				bestSolution = AStarThreads[i].execute(); //update the best solution
			}
		}

		return bestSolution;
	}
}
