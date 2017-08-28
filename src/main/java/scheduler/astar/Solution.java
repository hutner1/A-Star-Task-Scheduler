package scheduler.astar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;

/**
 * Represent partial/complete solution
 * 
 * Comparable because needed for comparison in priority queue 
 * using maxCostFunction() (polling for best solution)
 */
public class Solution implements Comparable<Solution>, Schedule{
	private int _upperBound; 
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;
	private List<Vertex> _scheduledProcesses;
	private List<Vertex> _schedulableProcesses;
	private List<Vertex> _nonschedulableProcesses;

	private Queue<Solution> _children;
	Vertex _lastScheduledTask;
	private int _mostRecentlyScheduledProcessor;

	private DefaultDirectedWeightedGraph _graph;
	private boolean _fixedOrder = false;;
	private static HashMap<Vertex, Integer> _btmLevels;

	/**
	 * Create a solution with list of scheduled, schedulable, non-schedulable and the task digraph.
	 * Empty processors are created to store tasks.
	 * @param numberOfProcessors
	 * @param graph
	 * @param scheduled scheduled tasks
	 * @param schedulable tasks with ran dependencies
	 * @param nonschedulable tasks with dependencies not run
	 */
	public Solution(int upperBound, int numberOfProcessors, DefaultDirectedWeightedGraph graph, List<Vertex> scheduled, List<Vertex> schedulable, List<Vertex> nonschedulable, boolean fixedOrder) {
		this(numberOfProcessors, graph);
		_upperBound = upperBound;
		_scheduledProcesses = new ArrayList<Vertex>(scheduled);
		_schedulableProcesses = new ArrayList<Vertex>(schedulable);
		_nonschedulableProcesses = new ArrayList<Vertex>(nonschedulable);
		_fixedOrder = fixedOrder;
	}

	/**
	 * Meant to be called directly only by ListScheduler which will
	 * allocate tasks in correct order because of topological sorting,
	 * hence schedulable and non-schedulables don't need to be tracked.
	 * 
	 * Create a solution with the task digraph.
	 * Empty processors are created to store tasks.
	 * @param numberOfProcessors
	 * @param graph
	 */
	public Solution(int numberOfProcessors, DefaultDirectedWeightedGraph graph){
		_numberOfProcessors = numberOfProcessors;
		_processors = new HashMap<Integer, Processor>();
		_graph = graph;
		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}
	}

	/**
	 * The greatest last end time on all the processors
	 */
	public int getLastFinishTime() {
		int maximumTime = 0;

		for (Processor p : _processors.values()) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}

		return maximumTime;
	}

	/**
	 * Returns the earliest time that a vertex can start on the given processor number.
	 * 
	 * Does this by looping through all the Processors and then getting the latest start time depending
	 * on the location of the parent task and also the communication time. Then returns the larger one
	 * of the latest start time depending on parents and the latest finishing time on the current Processor.
	 * 
	 * @param v task vertex
	 * @param processorNumber number of the processor to allocate task on 
	 * @return earliest possible task allocation time on selected processor
	 * @throws SolutionException 
	 */
	private int earliestDataReadyTime(Vertex v, int processorNumber){

		// for every processor, get the latest starting parent, then determine the earliest possible start time of new process
		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		// finds task's dependencies, and finds earliest possible start time
		for (DefaultWeightedEdge e : _graph.incomingEdgesOf(v)) {
			int parentEndTime = -1;
			Vertex parent = _graph.getEdgeSource(e);
			for (int i = 1; i <= _numberOfProcessors; i++) {
				if (_processors.get(i).isScheduled(parent)) {
					parentEndTime =_processors.get(i).endTimeOf(parent);
					if (processorNumber != i) {
						parentEndTime += _graph.getEdgeWeight(e);
					}
				}
			}
			if (parentEndTime == -1) {
				return -1;
			} else {
				startingTimes.add(parentEndTime);
			}
		}
		startingTimes.add(_processors.get(processorNumber).earliestNextProcess());
		int earliestStartTime = Collections.max(startingTimes);

		return earliestStartTime;
	}

	/**
	 * Adds a process to a processor at its earliest possible start time
	 * @param v task vertex
	 * @param processorNumber processor to allocate task on
	 */
	public void addProcess(Vertex v, int processorNumber) {

		_processors.get(processorNumber).addProcess(v,earliestDataReadyTime(v, processorNumber));

		// saying that this task has been scheduled and update the list of schedulables
		updateSchedulable(v);
	}

	/**
	 * For ListScheduler's usage only, adds a process at earliest possible time on
	 * any processor.
	 * @param v task vertex
	 */
	public void addProcessAtEarliestPossibleTime(Vertex v){
		int minTime = Integer.MAX_VALUE;
		int processorNumber = 1;

		for (int i = 1; i <= _numberOfProcessors; i++) {
			int earliestTime = earliestDataReadyTime(v, i);
			if (earliestTime < minTime) {
				minTime = earliestTime;
				processorNumber = i;
			}
		}

		_processors.get(processorNumber).addProcess(v,earliestDataReadyTime(v, processorNumber));
	}

	@Override
	/**
	 * Overrides the compareTo method in order for Solution to work in a priority queue
	 * a solution is better than another its the highest end time is lower than the other
	 * The end time is done by checking the bottom level of each vertex added to its 
	 * individual start time
	 */
	public int compareTo(Solution s) {
		int maxThis = maxCostFunction();
		int maxOther = s.maxCostFunction();

		if (maxThis < maxOther) {
			return -1;
		}  else if (maxThis == maxOther) {
			if (s._scheduledProcesses.size() - this._scheduledProcesses.size() != 0) {
				return s._scheduledProcesses.size() - this._scheduledProcesses.size();
			}
			else {
				if (this.getLastFinishTime() - s.getLastFinishTime() != 0) {
					return this.getLastFinishTime() - s.getLastFinishTime();
				} else {
					return s.hashCode() - hashCode();
				}
			}
		} else {
			return 1;
		}
	}


	/**
	 * Helper function that calls all three parts of the proposed cost function
	 * and returns the maximum of those three values.
	 * 
	 * Parts
	 * 1. maximum of start time and bottom level of any task in schedule
	 * 2. idle time plus computation load
	 * 3. maximum of earliest start time and bottom level of any free tasks
	 * 
	 * @return the cost function 
	 */
	public int maxCostFunction() {
		ArrayList<Integer> costs = new ArrayList<Integer>();

		costs.add(maximumEndTimeOfPartialSchedule());
		costs.add(idleTimePlusComputationLoad());
		costs.add(maximumEndTimeOfFreeVertices());


		return Collections.max(costs);
	}

	/**
	 * Part 1 of proposed cost function, returns the maximum end time of all
	 * vertices that have already been scheduled, by using their bottom level
	 * @return maximum end time of all vertices that have already been scheduled, by using their bottom level
	 */
	private int maximumEndTimeOfPartialSchedule() {
		ArrayList<Integer> maxBottomLevels = new ArrayList<Integer>();

		for (Processor p : _processors.values()) {
			for (ProcessInfo pI : p.getProcesses()) {
				int btmLevel = _btmLevels.get(pI.getVertex());
				btmLevel += pI.startTime();
				maxBottomLevels.add(btmLevel);
			}
		}

		if (maxBottomLevels.isEmpty()) {
			return 0;
		} else {
			return Collections.max(maxBottomLevels);
		}
	}

	/**
	 * Part 2 of the proposed cost function, returns the total idle time of the
	 * schedule added to the total cost of all vertices, all divided by the 
	 * number of processors
	 * @return  total idle time of the schedule added to the total cost of all vertices, all divided by the number of processors
	 */
	private int idleTimePlusComputationLoad() {

		int idleTime = 0;
		int totalWeight = 0;

		for (Processor p : _processors.values()) {
			idleTime += p.idleTime();
		}

		for (Vertex v : _graph.vertexSet()) {
			totalWeight += v.getWeight();
		}

		int totalTime = idleTime + totalWeight;

		return totalTime/_numberOfProcessors;
	}

	/**
	 * Part 3 of proposed cost function, maximum of earliest start time and bottom level of any schedulable tasks
	 * @return maximum of earliest start time and bottom level of any schedulable tasks
	 */
	private int maximumEndTimeOfFreeVertices() {
		ArrayList<Integer> maxTimes = new ArrayList<Integer>();

		for (Vertex v : _schedulableProcesses) {
			maxTimes.add(minimalDataReadyTime(v) + _btmLevels.get(v));
		}

		if (maxTimes.isEmpty()) {
			return 0;
		} else {
			return Collections.max(maxTimes);
		}
	}

	/**
	 * Returns the earliest time that a vertex can start on any processor
	 * @param v task vertex
	 * @return
	 */
	private int minimalDataReadyTime(Vertex v) {
		int minTime = Integer.MAX_VALUE;

		for (int i = 1; i <= _numberOfProcessors; i++) {
			int earliestTime = earliestDataReadyTime(v, i);
			if (earliestTime < minTime) {
				minTime = earliestTime;
			}
		}

		return minTime;
	}


	/**
	 * This method updates the list of schedulable and nonschedulable processes.
	 * It checks the children processes of the parent input and if all of the parent processes
	 * of the child have already been scheduled
	 * 
	 * @param parent Parent process to be updated
	 * @param scheduled List of processes already scheduled
	 * @param schedulable List of processes available to be scheduled
	 * @param nonschedulable List of processes that have not had their dependencies met
	 */
	private void updateSchedulable(Vertex parent) {

		_scheduledProcesses.add(parent);
		_schedulableProcesses.remove(parent);

		for (Vertex child : _graph.getChildren(parent)) {

			boolean canBeScheduled = true;
			for (Vertex childsParent : _graph.getParents(child)) {
				if (!_scheduledProcesses.contains(childsParent)) { 
					canBeScheduled = false;
				}
			}
			if (canBeScheduled && _nonschedulableProcesses.contains(child))  {
				_schedulableProcesses.add(child);
				_nonschedulableProcesses.remove(child);
			}
		}
	}

	/**
	 * Checks if all tasks have been scheduled
	 */
	public boolean isCompleteSchedule() {
		return _scheduledProcesses.size() == _graph.vertexSet().size();
	}

	/**
	 * Get children tasks as a deep copy, bounds off children who's times are able
	 * to be below the upper bound
	 * @return
	 */
	public Queue<Solution> createChildren() {

		if (_children == null) {
			_children = new ArrayDeque<Solution>();
			for (Vertex v : _schedulableProcesses) {
				for (int i = 1; i <= _numberOfProcessors; i++) {
					Solution child = createDuplicateSolution();
					child.addProcess(v, i);
					child._lastScheduledTask = v;
					child._mostRecentlyScheduledProcessor = i;
					_children.add(child);
				} 
			}

		}

		return _children;
	}


	/**
	 * Creates a hard copy of current solution
	 * 
	 * Used by createChildren() when adding children tasks to the current schedule
	 * 
	 * @return Hard copy of the current solution
	 */
	public Solution createDuplicateSolution() {
		Solution s = new Solution(_upperBound, _numberOfProcessors, _graph, _scheduledProcesses, _schedulableProcesses, _nonschedulableProcesses, _fixedOrder);
		s.setProcessorSchedule(_processors);
		return s;
	}

	/**
	 * Sets the processor schedule
	 * @param HashMap of processors
	 */
	private void setProcessorSchedule(HashMap<Integer, Processor> processors) {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			_processors.put(i, processors.get(i).createDeepCopy());
		}
	}

	/**
	 * Returns the additional information on task vertex tha needs to be added to output file
	 * 
	 * Used by OutputWriter obj of the io package to print out the optimal schedule's information on each task.
	 * 
	 * @param vertex vertex to obtain optimal solution's information on 
	 * @return String representing optimal solution's information on vertex
	 */
	public String getVertexString(Vertex vertex) {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			if (_processors.get(i).isScheduled(vertex)) {				
				return ", Start=" + _processors.get(i).getProcess(vertex).startTime() + ", Processor=" + i;
			}
		}
		return null; 
	}

	/**
	 * Overrides the equals method to be used for checking if two solutions are equivalent
	 * Two schedules are equivalent if all processes are done the same, or if 
	 * its processes are done in the same order but on different processors
	 */
	@Override
	public boolean equals(Object o) {
		Solution otherSolution = (Solution)o;

		// Store the processors' processes' info
		ArrayList<String> processesThisSolution = new ArrayList<String>();
		ArrayList<String> processesOtherSolution = new ArrayList<String>();

		for (int i = 1; i <= _numberOfProcessors; i++) {
			processesThisSolution.add(_processors.get(i).getProcessessString());
			processesOtherSolution.add(otherSolution._processors.get(i).getProcessessString());
		}

		for (String processorString : processesThisSolution) {
			if (processesOtherSolution.contains(processorString)) {
				processesOtherSolution.remove(processorString);
			} else {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		ArrayList<String> hashString = new ArrayList<String>();

		for (Processor p : _processors.values()) {
			hashString.add(p.getProcessessString());
		}

		Collections.sort(hashString);

		StringBuilder sb = new StringBuilder();

		for (String s : hashString) {
			sb.append(s);
		}

		return sb.toString().hashCode();
	}

	/**
	 * Algorithm 2 from the 2013 Sinnen paper.
	 * This algorithm tries to reorder the tasks in a schedule
	 * by perform swaps for tasks that do not break the dependencies
	 * for each task in the graph of nodes. If one reordering is 
	 * found with an equivalent schedule horizon, it is deemed as
	 * equivalent and it is discarded
	 * @return a boolean to find whether a solution is equivalent 
	 */
	public boolean isEquivalent() {

		Collections.sort(_scheduledProcesses); // Sort in topological order

		Processor p = _processors.get(_mostRecentlyScheduledProcessor);
		int tMax = p.getTime(); // final schedule horizon end time for m

		List<Vertex> processOrder = p.getProcessOrder();
		int i = processOrder.size() - 2;

		while (i >= 0 && _scheduledProcesses.indexOf(_lastScheduledTask) < _scheduledProcesses.indexOf(processOrder.get(i))) {
			processOrder.remove(_lastScheduledTask);
			processOrder.add(i, _lastScheduledTask); //swap m with forgoing task

			Processor temp = new Processor();

			_processors.put(_mostRecentlyScheduledProcessor, temp);
			for (Vertex v : processOrder) {
				try {
					addProcessWithoutUpdating(v, _mostRecentlyScheduledProcessor); // try to add tasks at the earliest possible time
				} catch (SolutionException e) {
					_processors.put(_mostRecentlyScheduledProcessor, p);
					return false;
				}
			}

			if (temp.getTime() <= tMax && outgoingCommsOK(this, p)) {
				return true;
			}

			processOrder = p.getProcessOrder();
			i--;
		}

		_processors.put(_mostRecentlyScheduledProcessor, p);
		return false;
	}

	/**
	 * Algorithm 3 from the 2013 Sinnen paper.
	 * This algorithm checks that all outgoing communications of the swapped tasks
	 * done in Algorithm 2 do not delay any descendants, thus keeping a constant
	 * schedule horizon
	 * @param childSol
	 * @return a boolean to find whether a solution is equivalent 
	 */
	public boolean outgoingCommsOK(Solution childSol, Processor originalProcessor){
		for (ProcessInfo pI: originalProcessor.getProcesses()){
			Vertex v = pI.getVertex();
			// if the start time of the task after m is later than before, analyse child tasks
			if (childSol._processors.get(_mostRecentlyScheduledProcessor).startTimeOf(v) > pI.startTime()) {
				for (Vertex nc : _graph.getDirectChildren(v)) {
					ArrayList<DefaultWeightedEdge> childEdges = _graph.outgoingEdgesOf(v);
					int edgeCost = 0;
					for (DefaultWeightedEdge e : childEdges) {
						if (e.getDest().equals(nc)) {
							edgeCost = e.getWeight(); //get the edge cost from task to child
							break;
						}
					}
					int time = childSol._processors.get(_mostRecentlyScheduledProcessor).endTimeOf(v) + edgeCost;
					if (isScheduled(nc)){ //if the child is scheduled
						if (_processors.get(scheduledOnProcessorNumber(nc)).startTimeOf(nc)>time && !_processors.get(_mostRecentlyScheduledProcessor).isScheduled(nc)){
							return false;
						}
					} else {
						for (int i = 1; i <= _numberOfProcessors && i != _mostRecentlyScheduledProcessor; i++){
							boolean atLeastOneLater = false;
							ArrayList<Vertex> parents= _graph.getDirectParents(nc);
							parents.remove(v);
							for (Vertex cv: parents){
								int dataArrival;
								try {
									dataArrival = endTimeOf(cv);
									if (i != scheduledOnProcessorNumber(cv)) {
										for (DefaultWeightedEdge e : _graph.outgoingEdgesOf(cv)) {
											if (e.getDest().equals(nc)) {
												dataArrival += e.getWeight();
											}
										}
									}
								} catch (NullPointerException e) {
									int earliestTime = earliestDataReadyTime(cv,1);
									int procNumber = 1;
									for (int j = 2; j <= _numberOfProcessors; j++) {
										int drt = earliestDataReadyTime(cv,j);
										if (drt < earliestTime) {
											earliestTime = drt;
											procNumber = j;
										}
									}
									dataArrival = earliestTime;
									if (i != procNumber) {
										for (DefaultWeightedEdge e2 : _graph.outgoingEdgesOf(cv)) {
											if (e2.getDest().equals(nc)) {
												dataArrival += e2.getWeight();
											}
										}
									}
								}
								if (dataArrival >= time) {
									atLeastOneLater = true;
								}
							}
							if (!atLeastOneLater){
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	private int endTimeOf(Vertex v) {
		return _processors.get(scheduledOnProcessorNumber(v)).endTimeOf(v);
	}

	/**
	 * Custom function to check whether a vertex is scheduled on a processor
	 * @param v
	 * @return
	 */
	private boolean isScheduled(Vertex v) {
		for (Processor p : _processors.values()) {
			if (p.isScheduled(v)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function to check which processor a process is scheduled on
	 */
	private int scheduledOnProcessorNumber(Vertex v) {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			if (_processors.get(i).isScheduled(v)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Sets the static mapping of bottom levels for all vertices in the graph
	 * @param btmLevels
	 */
	public void setBtmLevels(HashMap<Vertex, Integer> btmLevels) {
		_btmLevels = btmLevels;
	}

	/**
	 * Get the mapping from processor number to Processor instances
	 */
	public HashMap<Integer, Processor> getProcess(){
		return _processors;
	}

	public int getUpperBound() {
		return _upperBound;
	}

	/**
	 * Returns the size of a schedule, the number of nodes scheduled on the schedule 
	 * @return the number of nodes scheduled on the schedule 
	 */
	public int getSize() {
		return _scheduledProcesses.size();
	}


	/**
	 * Adds a process to a processor at its earliest possible time, to be used for reassigning tasks
	 * in a different order
	 * @param v
	 * @param processorNumber
	 * @throws SolutionException when a task is trying to be scheduled without having all dependencies met
	 */
	public void addProcessWithoutUpdating(Vertex v, int processorNumber) throws SolutionException {
		int dataReadyTime = earliestDataReadyTime(v, processorNumber);
		if (dataReadyTime == -1) {
			throw new SolutionException();
		}
		_processors.get(processorNumber).addProcess(v,dataReadyTime);
	}

	/**
	 * The first condition for checking if it is possible to fix the order of a schedule based on its current
	 * list of free tasks
	 * @return
	 */
	public boolean canFixOrder() {

		Vertex commonChild = null;
		int commonParentProcessorNumber = 0;

		// For every free task, check if it satisfies 3 conditions
		for (Vertex v : _schedulableProcesses) {
			// The task must have at most one parent and at most one child
			if (!(_graph.getParents(v).size() <= 1 && _graph.getChildren(v).size() <= 1)) {
				return false;
			}

			if (_graph.getChildren(v).size() == 1) {

				Vertex child = _graph.getChildren(v).get(0);
				// All tasks with a child must share the same child
				if (commonChild != null) {
					if (!commonChild.equals(child)) {
						return false;
					}
				} else {
					commonChild = child;
				}
			}

			if (_graph.getParents(v).size() == 1) {

				Vertex parent = _graph.getParents(v).get(0);
				// All tasks with parents must have their parents allocated to the same processor
				if (commonParentProcessorNumber != 0) {
					if(commonParentProcessorNumber != scheduledOnProcessorNumber(parent)) {
						return false;
					}
				} else {
					commonParentProcessorNumber = scheduledOnProcessorNumber(parent);
				}
			}
		}
		return true;
	}

	/**
	 * Attempts to fix the order that the free tasks in a partial solution are able to be scheduled in
	 * if the final order obtained does not satisfy both fork order and join order, then the order is not
	 * fixed
	 */
	public void fixOrder() {

		HashMap<Integer, List<Vertex>> freeTasks = new HashMap<Integer, List<Vertex>>();
		PriorityQueue<Integer> edrts = new PriorityQueue<Integer>();

		// Gets the tasks in fork order
		for (Vertex v : _schedulableProcesses) {
			int edrt = earliestDataReadyTimeWithEdges(v);

			if (freeTasks.containsKey(edrt)) {
				freeTasks.get(edrt).add(v);
			} else {
				List<Vertex> sameCostTasks = new ArrayList<Vertex>();
				sameCostTasks.add(v);
				freeTasks.put(edrt, sameCostTasks);
				edrts.add(edrt);
			}
		}

		//Attempts to break ties by sorting in non-increasing out edge cost
		for (List<Vertex> sameCosts : freeTasks.values()) {
			if (sameCosts.size() > 1) {
				sortNonIncreasingOutEdge(sameCosts);
			}
		}

		//Creates the final fixed ordering
		List<Vertex> finalFixedOrder = new ArrayList<Vertex>();

		while (!edrts.isEmpty()) {
			for (Vertex v : freeTasks.get(edrts.poll())) {
				finalFixedOrder.add(v);
			}
		}
		
		//Checks that the final fixed order is in fork-join order, as currently it is
		//only guaranteed to be in fork order, so the conditions for join order must be
		//verified
		if (verifyForkJoinOrder(finalFixedOrder)) {
			_fixedOrder = true;
			
			//If the fork-join condition is met, then you can reduce the list of
			//free tasks to a list, with only one task being scheduled each time
			for (int i = 0; i < finalFixedOrder.size() - 1; i++) {
				_graph.addChild(finalFixedOrder.get(i),finalFixedOrder.get(i+1));
			}

			for (int i = 1; i < finalFixedOrder.size(); i++) {
				_schedulableProcesses.remove(finalFixedOrder.get(i));
				_nonschedulableProcesses.add(finalFixedOrder.get(i));
			}
		}

		
	}

	/**
	 * Checks that the ordering of a fork ordered list is also a join ordered list,
	 * if it is, then it is suitable for fork-join, otherwise it is not
	 * @param finalFixedOrder
	 * @return
	 */
	private boolean verifyForkJoinOrder(List<Vertex> finalFixedOrder) {
		for (int i = 0; i < finalFixedOrder.size() - 1; i++) {
			Vertex vertexA = finalFixedOrder.get(i);
			Vertex vertexB = finalFixedOrder.get(i + 1);
			int aCost = 0;
			int bCost = 0;
			
			try {
				aCost = _graph.outgoingEdgesOf(vertexA).get(0).getWeight();
			} catch (IndexOutOfBoundsException e) {}
			try {
				bCost = _graph.outgoingEdgesOf(vertexB).get(0).getWeight();
			} catch (IndexOutOfBoundsException e) {}
			
			if (aCost < bCost) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the boolean for whether the solution has used a fixed order list, if
	 * it has, then schedule horizon is no longer viable and should be disabled
	 * @return
	 */
	public boolean isFixedOrder() {
		return _fixedOrder ;
	}

	/**
	 * Sorts a list of vertices with equal data ready times based on their out edge weight,
	 * in decreasing order of out edge weight
	 * @param list
	 */
	private void sortNonIncreasingOutEdge(List<Vertex> list) {
		Collections.sort(list, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex arg0, Vertex arg1) {
				int costA = 0;
				int costB = 0;
				try{
					costA = _graph.outgoingEdgesOf(arg0).get(0).getWeight();
				}catch(IndexOutOfBoundsException e){
				}
				try{
					costB = _graph.outgoingEdgesOf(arg1).get(0).getWeight();
				}catch(IndexOutOfBoundsException e){
				}
				return costB-costA;
			}
		});
		}

	/**
	 * Returns the earliest data ready time for a vertex, assuming that you
	 * always have to incur the penalty for switching between processors after
	 * a parent task finishes
	 * @param v
	 * @return
	 */
	private int earliestDataReadyTimeWithEdges(Vertex v){

		// for every processor, get the latest starting parent, then determine the earliest possible start time of new process
		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		// finds task's dependencies, and finds earliest possible start time
		for (DefaultWeightedEdge e : _graph.incomingEdgesOf(v)) {
			int parentEndTime = 0;
			Vertex parent = _graph.getEdgeSource(e);
			for (int i = 1; i <= _numberOfProcessors; i++) {
				if (_processors.get(i).isScheduled(parent)) {
					parentEndTime =_processors.get(i).endTimeOf(parent);
					parentEndTime += _graph.getEdgeWeight(e);	
				}
			}

			startingTimes.add(parentEndTime);
		}

		if (startingTimes.size() == 0) {
			return 0;
		} else {
			return Collections.max(startingTimes);
		}
	}

}