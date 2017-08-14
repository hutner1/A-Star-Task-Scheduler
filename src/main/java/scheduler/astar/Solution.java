package scheduler.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.DefaultWeightedEdge;
import scheduler.graphstructures.Vertex;

/**
 * Represent partial/complete solution
 * Comparable because needed for comparison in priority queue (polling for best solution)
 */
public class Solution implements Comparable<Solution>{
	private int _upperBound;
	private HashMap<Integer, Processor> _processors;
	private int _numberOfProcessors;
	private List<Vertex> _scheduledProcesses;
	private List<Vertex> _schedulableProcesses;
	private List<Vertex> _nonschedulableProcesses;

	private DefaultDirectedWeightedGraph _graph;
	private static HashMap<Vertex, Integer> _btmLevels;

	public Solution(int upperBound, int numberOfProcessors, DefaultDirectedWeightedGraph graph, List<Vertex> scheduled, List<Vertex> schedulable, List<Vertex> nonschedulable) {
		_upperBound = upperBound;
		_numberOfProcessors = numberOfProcessors;
		_processors = new HashMap<Integer, Processor>();
		_graph = graph;

		for (int i = 1; i <= numberOfProcessors; i++) {
			_processors.put(i, new Processor());
		}

		_scheduledProcesses = new ArrayList<Vertex>(scheduled);
		_schedulableProcesses = new ArrayList<Vertex>(schedulable);
		_nonschedulableProcesses = new ArrayList<Vertex>(nonschedulable);
	}

	/**
	 * The greatest last end time on all processors
	 */
	public int getTime() {
		int maximumTime = 0;

		for (Processor p : _processors.values()) {
			if (p.getTime() > maximumTime) {
				maximumTime = p.getTime();
			}
		}

		return maximumTime;
	}

	/**
	 * Returns the earliest time that a vertex can start on the given processor number
	 * @param v
	 * @param processorNumber
	 * @return
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
	 * Adds a process to a processor at its earliest possible time
	 * @param v
	 * @param processorNumber
	 */
	public void addProcess(Vertex v, int processorNumber) {

		_processors.get(processorNumber).addProcess(v,earliestDataReadyTime(v, processorNumber));

		// saying that this task has been scheduled and update the list of schedulables
		updateSchedulable(v);
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
		}  else {
			return 1;
		}
	}

	
	/**
	 * Helper function that calls all three parts of the proposed cost function
	 * and returns the maximum of those three values
	 * @return
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

		for (Processor p : _processors.values()) {
			idleTime += p.idleTime();
		}

		int totalTime = idleTime + _upperBound;

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
	 * @param v
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
	public List<Solution> createChildren() {
		List<Solution> children = new ArrayList<Solution>();
		for (Vertex v : _schedulableProcesses) {
			for (int i = 1; i <= _numberOfProcessors; i++) {
				Solution child = createDuplicateSolution();
				child.addProcess(v, i);
				//TODO child.printTree();
				if (child.isBelowUpperBound()) {
					children.add(child);
					//TODO System.out.println(child.maxCostFunction());
				}
			} 
		}
		return children;
	}

	/**
	 * Checks if a possible solution is still able to be optimal
	 * @return
	 */
	private boolean isBelowUpperBound() {

		int maxTimeRemaining = 0;

		for (Vertex v : _schedulableProcesses) {
			maxTimeRemaining += v.getWeight();
		}
		for (Vertex v : _nonschedulableProcesses) {
			maxTimeRemaining += v.getWeight();
		}

		return (getTime() + maxTimeRemaining) <= _upperBound;
	}

	/**
	 * Debugging
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
	 * @return
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
		Solution s = (Solution)o;

		ArrayList<String> processesThisSolution = new ArrayList<String>();
		ArrayList<String> processesOtherSolution = new ArrayList<String>();

		for (int i = 1; i <= _numberOfProcessors; i++) {
			processesThisSolution.add(_processors.get(i).getProcessesString());
			processesOtherSolution.add(s._processors.get(i).getProcessesString());
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
	
	//TODO hash code
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

	public HashMap<Integer, Processor> getProcess(){
		return _processors;
	}
	
	

}

