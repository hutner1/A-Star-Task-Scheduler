package scheduler.astar;

import java.util.PriorityQueue;
import java.util.Set;

import scheduler.basicmilestone.Vertex;
import scheduler.graphstructures.DefaultDirectedWeightedGraph;

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
	
	public AStarParallelised(DefaultDirectedWeightedGraph graph, int numberOfProcessors, int numberOfThreads) {
		super(graph, numberOfProcessors);
		this._numberOfThreads = numberOfThreads;
	}
	
	@Override
	public Solution execute() {
		Solution sol = super.execute();
		
		//If solution was found in non-parallel search, return
		if (sol != null) {
			return sol;
		}
		return executeInParallel();
	}

	/**
	 * Execute the A* algorithm in parallel using separate threads
	 * @return optimal solution
	 */
	protected Solution executeInParallel() {
		//Fields to loop through solutions
		Solution sol = null;
		int index = 0;
		//Make queues that store execution threads
		PriorityQueue<Solution>[] threadQueue = new PriorityQueue[_numberOfThreads];
		
		for (int i = 0; i < _numberOfThreads ; i++) {
			threadQueue[i] = new PriorityQueue<Solution>();
		}
		
		//For one thread, just add everything to the first queue
		if (_numberOfThreads == 1) {
			threadQueue[0] = _solutionSpace;
		} else {
			while ((sol = _solutionSpace.poll()) != null) {
				threadQueue[0].add(sol);
				index++;
				
				if (index == _numberOfThreads) { //loop back to zero for equal loading (hopefully)
					index = 0;
				}
			}
		}
		
		_solutionSpace = null;
		
		//Start threading process. Index 1 is used as Index 0 is reserved for the main thread
		for (int i = 1; i < _numberOfThreads; i++) {
			AStarThreads[i] = new AStarThread(i, _graph, threadQueue[i], _solutionSpace, _closedSolutions, _numberOfProcessors);
			//Add the custom thread with all the AStar fields into a thread
			threads[i] = new Thread(AStarThreads[i]);
			threads[i].run();
		}
		
		//Initialise main thread (read all about it in SOFTENG 370)
		AStarThreads[0] = new AStarThread(0, _graph, threadQueue[0], _solutionSpace, _closedSolutions, _numberOfProcessors);
		AStarThreads[0].run();
		
		//Try to join threads once the threads have finished
		for (int i = 1; i <_numberOfThreads; i++) {
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
