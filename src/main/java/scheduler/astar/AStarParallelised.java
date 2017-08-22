package scheduler.astar;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

import visualization.gantt.Gantt;

import visualization.gui.Gui;

/**
 * AStar thread class that will be added to allow solution search in parallel
 * TODO: Incorporate Paratask or Bananas in Pyjamas
 */
public class AStarParallelised extends AStar{

	// Field for number of threads (cores to use)
	protected int _numberOfThreads;

	// Array of Threads to store AStarThreads(Runnable)
	// Runnables can only be run by having a Thread wrap around it
	Thread[] _threads;
	// For invoking AStarThread#getSolution() to compare optimal solutions of different threads
	AStarThread[] _aStarThreads;

	/**
	 * AStarParallelised's constructor
	 * @param graph task digraph
	 * @param numberOfProcessors number of processors to do task scheduling on
	 * @param numberOfThreads number of cores to use for scheduling
	 * @param Visualizer the visualizer // TODO
	 */
	public AStarParallelised(DefaultDirectedWeightedGraph graph, int numberOfProcessors, int numberOfThreads, Visualizer Visualizer, Gantt gantt) {
		super(graph, numberOfProcessors, Visualizer, gantt);
		_numberOfThreads = numberOfThreads;
		_threads = new Thread[_numberOfThreads];
		_aStarThreads = new AStarThread[_numberOfThreads];
	}

	/**
	 * Execute A* algorithm in parallel
	 */
	@Override
	public Solution execute() {
		// first initialise the solution space for sharing between threads
		initialiseSolutionSpace();
		return executeInParallel();
	}

	/**
	 * Execute the A* algorithm in parallel using separate threads
	 * @return optimal solution found in parallel
	 */
	protected Solution executeInParallel() {
		// Start threading process, assign each thread(core) an ASTarThread with shared solution space and closed solution space
		for (int i = 0; i < _numberOfThreads; i++) {

			_aStarThreads[i] = new AStarThread(i, _graph, _solutionSpace, _closedSolutions, _numberOfProcessors, _visualizer, _upperBound,this, _gantt);

			//Add the custom thread with all the AStar fields into a thread
			_threads[i] = new Thread(_aStarThreads[i]);
			_threads[i].setName("Thread-"+i);
		}
		
		// Start the threads
		for (Thread t : _threads) {
			t.start();
		}


		// Wait for all the threads to finish
		for (int i = 0; i <_numberOfThreads; i++) {
			try {
				_threads[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getBestSolution(_aStarThreads);
	}

	/**
	 * Compares all AStarThreads that ran and return the optimal solution
	 * @param aStarThreads Array of AStarThreads that ran
	 * @return the OPTIMAL solution found in parallel
	 */
	private Solution getBestSolution(AStarThread[] aStarThreads) {
		//Loop through all the threads
		for (int i = 0; i < _numberOfThreads; i++) {
			if (aStarThreads[i].getSolution() != null) {
				System.out.println("Finish time  : "+aStarThreads[i].getSolution().getLastFinishTime());
				return aStarThreads[i].getSolution();
			}
		}
		return null;
	}

}
