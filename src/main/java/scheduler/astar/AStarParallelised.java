package scheduler.astar;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import visualization.Visualizer;

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
	public AStarParallelised(DefaultDirectedWeightedGraph graph, int numberOfProcessors, int numberOfThreads, Visualizer Visualizer) {
		super(graph, numberOfProcessors, Visualizer);
		_numberOfThreads = numberOfThreads;
		_threads = new Thread[_numberOfThreads];
		_aStarThreads = new AStarThread[_numberOfThreads];
	}

	/**
	 * Execute A* algorithm in parallel
	 */
	@Override
	public Solution execute() {
		initialiseSolutionSpace();
		return executeInParallel();
	}

	/**
	 * Execute the A* algorithm in parallel using separate threads
	 * @return optimal solution
	 */
	protected Solution executeInParallel() {
		//Start threading process, assign each thread(core) an ASTarThread with shared solution space and closed solution space
		for (int i = 0; i < _numberOfThreads; i++) {
			_aStarThreads[i] = new AStarThread(i, _graph, _solutionSpace, _closedSolutions, _numberOfProcessors, _visualizer);
			//Add the custom thread with all the AStar fields into a thread
			_threads[i] = new Thread(_aStarThreads[i]);
			_threads[i].run();
		}

		//Try to join threads once the threads have finished
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
	 * @return the OPTIMAL solution
	 */
	private Solution getBestSolution(AStarThread[] aStarThreads) {
		Solution bestSolution = aStarThreads[0].getSolution();
		System.out.println("Timmeeweema  : "+bestSolution.getLastFinishTime());
		for (int i = 1; i < _numberOfThreads; i++) {
			System.out.println("Timmeeweema  : "+aStarThreads[i].getSolution().getLastFinishTime());
			if (bestSolution.getLastFinishTime()< aStarThreads[i].getSolution().getLastFinishTime()) {
				bestSolution = aStarThreads[i].getSolution(); //update the best solution
			}
		}

		return bestSolution;
	}
}
