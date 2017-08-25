package scheduler.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;
import visualization.gantt.Gantt;
import visualization.graph.Visualizer;
import visualization.gui.StatisticTable;


/**
 * AStar creates optimal solution with A*
 */
public class AStar {
	protected DefaultDirectedWeightedGraph _graph;
	protected int _numberOfProcessors;

	/**
	 * OPEN solutions
	 */
	protected PriorityBlockingQueue<Solution> _solutionSpace;

	/**
	 * CLOSED solutions
	 */
	protected Set<Solution> _closedSolutions;

	protected Visualizer _visualizer;

	protected StatisticTable _stats;

	//Upper bound obtained from list scheduling
	protected int _upperBound;
	protected Gantt _gantt;
	protected static int _counter=0;
	protected static int _solCreated=0;
	protected static int _solPopped=0;
	protected static int _solPruned=0;
	protected int _currentCost = Integer.MAX_VALUE;
	protected Solution bestCurrentSolution;
	protected static boolean _updated = false;

	/**
	 * AStar's constructor
	 * @param graph task digraph
	 * @param numberOfProcessors number of processors to do task scheduling on
	 * @param graphVisualizer the visualizer
	 */
	public AStar(DefaultDirectedWeightedGraph graph, int numberOfProcessors, Visualizer graphVisualizer, Gantt gantt, StatisticTable stats) {
		_graph = graph;
		_numberOfProcessors = numberOfProcessors;
		_solutionSpace = new PriorityBlockingQueue<Solution>(); //data structure does not permit null elements
		_closedSolutions = new HashSet<Solution>(); //threadsafe set
		_visualizer = graphVisualizer;
		_gantt = gantt;
		_stats = stats;
	}


	/**
	 * Execute A* algorithm
	 * @return optimal solution found sequentially
	 */
	public Solution execute() {
		initialiseSolutionSpace();
		
		return findOptimalSolution();
	}

	/**
	 * Inintialise the solution space and bottom level information
	 */
	protected void initialiseSolutionSpace(){
		//Create initial solution and add to priority queue
		//Pop solution and create children solutions for that, read children to queue
		//Pop most efficient child and add create children, read
		//Repeat until child is a complete graph, that is the optimal schedule
		HashMap<Vertex, Integer> btmLevel = new HashMap<Vertex, Integer>();
		List<Vertex> schedulable = new ArrayList<Vertex>(); // dependencies all met
		List<Vertex> nonschedulable = new ArrayList<Vertex>(); // dependencies not met
		// fill lists of schedulables

		for (Vertex v : _graph.vertexSet()) {
			if (_graph.inDegreeOf(v) == 0) { //get source nodes
				schedulable.add(v);
			} else {
				nonschedulable.add(v);
			}
			btmLevel.put(v, getBottomLevel(v));
		}

		ListScheduler listScheduler = new ListScheduler(_graph, _numberOfProcessors);
		_upperBound = listScheduler.getResult();

		// Create empty solution and then commence the looping
		Solution emptySolution = new Solution(_upperBound, _numberOfProcessors, _graph, new ArrayList<Vertex>(), schedulable, nonschedulable);
		emptySolution.setBtmLevels(btmLevel);
		_solutionSpace.add(emptySolution);	
	}


	/**
	 * Finds the optimal solution using A* algorithm
	 *
	 * All the null checks in the code are for when solution space is polled too quickly in 
	 * parallelised AStar
	 *
	 * @return optimal solution
	 */
	protected Solution findOptimalSolution(){
		// BEST priority solution
		bestCurrentSolution = _solutionSpace.poll();
		_solPopped ++;
		
		while(bestCurrentSolution == null){
			bestCurrentSolution = _solutionSpace.poll();
			/*_solPopped ++;*/
		}

		//Timer stuff
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				
				/*if(_updated == false){*/
					if(_visualizer != null){
						_visualizer.updateGraph(bestCurrentSolution);
						_gantt.updateSolution(bestCurrentSolution);
					} 
					
					if (_gantt != null) {
						if (_gantt.hasLaunched()) {
							_gantt.updateSolution(bestCurrentSolution);

						} else {
							_gantt.setSolution(bestCurrentSolution);
						}
					}
				/*}*/
				
				/*_updated = true;*/
				
			}
			
		}, 0, 2000);
		
		
		// keep polling until the best cost solution is a complete schedule, hence an optimal solution
		while (!bestCurrentSolution.isCompleteSchedule()) {
			
			// if solution is not already examined
			while ((_closedSolutions.contains(bestCurrentSolution)) || (bestCurrentSolution == null)) {
				bestCurrentSolution = _solutionSpace.poll();
				if (bestCurrentSolution != null) {
					_solPopped ++;
					_solPruned ++;
				}

			}

			
			// expand the solution
			Queue<Solution> childSolutions = bestCurrentSolution.createChildren();

			Solution s;
			boolean fullyExpanded = true;
			
			while ((s = childSolutions.poll()) != null) {
				int childCost = s.maxCostFunction();
				_solCreated ++;
				if (s.isEquivalent()) {
					_solPruned ++;
				} else if (!_closedSolutions.contains(s)) {
					if (childCost > _upperBound){
						// DO NOTHING AS IT WILL NOT BE CONSIDERED
						_solPruned ++;
					} else {
						_solutionSpace.add(s); 
						if (childCost == bestCurrentSolution.maxCostFunction()) {
							if (!childSolutions.isEmpty()) {
								fullyExpanded = false;
								bestCurrentSolution.setExpansionStatus(true);
								_solutionSpace.add(bestCurrentSolution);
								_solCreated += childSolutions.size();
								break;
							} else {
								bestCurrentSolution.setExpansionStatus(false);
							}
						}
					}
				} else {
					_solPruned ++;
				}

			}
			if(fullyExpanded){
				_closedSolutions.add(bestCurrentSolution);
			}


			/*
			if (_gantt != null) {
				if (_gantt.hasLaunched()) {
					if(_counter == 10){  
						_gantt.updateSolution(bestCurrentSolution);
					}
				} else {
					_gantt.setSolution(bestCurrentSolution);
				}
			}
			*/

			if(_visualizer != null){  
				if(_counter == 100){  
					_counter = 0;  
					/*_visualizer.updateGraph(bestCurrentSolution);  */
					_stats.updateStats(_solCreated, _solPopped, _solPruned, bestCurrentSolution.maxCostFunction());
				} else {  
					_counter++;  
				}  

			} 

			_solPopped ++;
			bestCurrentSolution = _solutionSpace.poll();
			while(bestCurrentSolution == null){
				bestCurrentSolution = _solutionSpace.poll();
			}
			_updated = false;
			_currentCost = bestCurrentSolution.maxCostFunction();
		}
		
		if(_visualizer != null){
			_visualizer.updateGraph(bestCurrentSolution);
		}

		_solutionSpace.add(bestCurrentSolution);
		timer.cancel();
		return bestCurrentSolution;

	}

	/**
	 * Get children vertices
	 * @param vertex
	 * @return children vertices
	 */
	private ArrayList<Vertex> getChildren(Vertex vertex){
		ArrayList<Vertex> children = new ArrayList<>();
		for(DefaultWeightedEdge e : _graph.outgoingEdgesOf(vertex)){
			children.add(_graph.getEdgeTarget(e));
		}
		return children;
	}

	/**
	 * Returns the bottom level of a vertex
	 * @param vertex
	 * @return bottom level
	 */
	private int getBottomLevel(Vertex vertex){
		ArrayList<Vertex> children = getChildren(vertex);
		int myWeight = vertex.getWeight();
		if(children.isEmpty()){
			return myWeight;
		} else {
			int btmLvl = 0;
			for (Vertex v : children){
				int lvl = getBottomLevel(v);
				if(lvl > btmLvl){
					btmLvl = lvl;
				}
			}
			return myWeight + btmLvl;
		}
	}

	/**
	 * Getter for number of solutions created
	 */
	public int getSolCreated(){
		return _solCreated;
	}

	/**
	 * Getter for number of solutions pruned
	 */	
	public int getSolPruned(){
		return _solPruned;
	}

	/**
	 * Getter for number of solutions popped
	 */	
	public int getSolPopped(){
		return _solPopped;
	}

}