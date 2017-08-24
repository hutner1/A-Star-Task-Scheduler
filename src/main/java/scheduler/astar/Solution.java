package scheduler.astar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	private int _upperBound; // TODO to be removed, as only used in A*
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;
	private List<Vertex> _scheduledProcesses;
	private List<Vertex> _schedulableProcesses;
	private List<Vertex> _nonschedulableProcesses;
	private Queue<Solution> _children;
	private boolean _partiallyExpanded = false;
	private Vertex _lastScheduledTask;
	private int _mostRecentlyScheduledProcessor;

	private DefaultDirectedWeightedGraph _graph;
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
	public Solution(int upperBound, int numberOfProcessors, DefaultDirectedWeightedGraph graph, List<Vertex> scheduled, List<Vertex> schedulable, List<Vertex> nonschedulable) {
		this(numberOfProcessors, graph);
		_upperBound = upperBound;
		_scheduledProcesses = new ArrayList<Vertex>(scheduled);
		_schedulableProcesses = new ArrayList<Vertex>(schedulable);
		_nonschedulableProcesses = new ArrayList<Vertex>(nonschedulable);
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
	 */
	private int earliestDataReadyTime(Vertex v, int processorNumber) {

		// for every processor, get the latest starting parent, then determine the earliest possible start time of new process
		ArrayList<Integer> startingTimes = new ArrayList<Integer>();

		// finds task's dependencies, and finds earliest possible start time
		for (int i = 1; i <= _numberOfProcessors; i++) {
			int latestParentEndTime = 0;
			for (DefaultWeightedEdge e : _graph.incomingEdgesOf(v)) {
				Vertex parent = _graph.getEdgeSource(e);
				if (_processors.get(i).isScheduled(parent)) {

					int parentEndTime = _processors.get(i).endTimeOf(parent);
					if (processorNumber != i) {
						parentEndTime += _graph.getEdgeWeight(e);
					}

					if (parentEndTime > latestParentEndTime) {
						latestParentEndTime = parentEndTime;
					}

				}
			}
			startingTimes.add(latestParentEndTime);
		}
		int earliestStartTime = Collections.max(startingTimes);
		int earliestAvailableTime = _processors.get(processorNumber).earliestNextProcess();
		//System.out.println(earliestStartTime);
		//System.out.println(earliestAvailableTime);
		if (earliestStartTime > earliestAvailableTime) {
			return earliestStartTime;
		} else {
			return earliestAvailableTime;
		}
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
			return s._scheduledProcesses.size() - this._scheduledProcesses.size();
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

		//System.out.println(costs.toString());

		return Collections.max(costs);
	}

	/**
	 * Part 1 of proposed cost function, returns the maximum end time of all
	 * vertices that have already been scheduled, by using their bottom level
	 * @return
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
	 * Part 3 of proposed cost function, returns the latest end time of all schedulable
	 * vertex given that they are scheduled at the next earliest time
	 * @return
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

		for (DefaultWeightedEdge outEdge : _graph.outgoingEdgesOf(parent)) {
			Vertex child = _graph.getEdgeTarget(outEdge);

			boolean canBeScheduled = true;
			for (DefaultWeightedEdge inEdge : _graph.incomingEdgesOf(child)) {
				if (!_scheduledProcesses.contains(_graph.getEdgeSource(inEdge))) { 
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
					//TODO child.printTree();
					_children.add(child);
					//TODO System.out.println(child.maxCostFunction());
				} 
			}

		}

		return _children;
	}

	/**
	 * Debugging //TODO
	 */
	private void printTree() {
		for (int i = 1; i <= _numberOfProcessors; i++) {
			System.out.print("P:" + i + " [ ");
			_processors.get(i).printProcesses();
			System.out.print("] ");
		}
		System.out.println();
	}

	/**
	 * Creates a hard copy of current solution
	 * 
	 * Used by createChildren() when adding children tasks to the current schedule
	 * 
	 * @return Hard copy of the current solution
	 */
	public Solution createDuplicateSolution() {
		Solution s = new Solution(_upperBound, _numberOfProcessors, _graph, _scheduledProcesses, _schedulableProcesses, _nonschedulableProcesses);
		s.setProcessorSchedule(_processors);
		return s;
	}

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
		return null; // TODO
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
			processesThisSolution.add(_processors.get(i).getProcessesString());
			processesOtherSolution.add(otherSolution._processors.get(i).getProcessesString());
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

	//TODO hash code, remove it?
	/**
	public int hashCode(){
	}
	 */

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

	// TODO don't think we ever used upper bound
	public int getUpperBound() {

		return _upperBound;
	}

	public void setExpansionStatus(boolean status) {
		_partiallyExpanded = status;
	}
	
	/**
	 * Returns the size of a schedule, the number of nodes scheduled on the schedule 
	 * @return the number of nodes scheduled on the schedule 
	 */
	public int getSize() {
		return _scheduledProcesses.size();
	}

	public boolean isEquivalent() {

		Collections.sort(_scheduledProcesses);

		Processor p = _processors.get(_mostRecentlyScheduledProcessor);
		int tMax = p.getTime();

		List<Vertex> processOrder = p.getProcessOrder();
		int i = processOrder.size() - 2;

		while (i >= 0 && _scheduledProcesses.indexOf(_lastScheduledTask) < _scheduledProcesses.indexOf(processOrder.get(i))) {
			processOrder.remove(_lastScheduledTask);
			processOrder.add(i, _lastScheduledTask);
			
			

			/*if (p.getTime() <= tMax && outgoingCommsOK(this)) {
				return true;
			}
			*/
			processOrder = p.getProcessOrder();
			i--;
		}
		return false;
	}


	public void swapProcess(Processor p, Vertex vertexA, Vertex vertexB) {
	}
}

