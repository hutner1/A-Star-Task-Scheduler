package scheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Visualization.Visualizer;
import io.DataReader;
import io.InputParser;
import io.OutputWriter;
import scheduler.astar.AStar;
import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.ScheduleGenerator;
import scheduler.basicmilestone.Sorter;
import scheduler.basicmilestone.Vertex;

/**
 * This is the main class for the task scheduler program.
 */
public class Main {

	public static void main(String[] args) {
		InputParser inputParser = new InputParser(args);
		inputParser.parse();
		
		OutputWriter outWriter = new OutputWriter(inputParser.getOutputFileName());
		outWriter.initialise();

		DataReader dataReader = new DataReader(inputParser.getFile());
		Visualizer graphVisualizer = new Visualizer();
		
		while(dataReader.hasMoreGraphs()) {
			System.out.println("More graphs in file? " + dataReader.hasMoreGraphs());
			dataReader.readNextGraph();
			
			if(inputParser.isVisualise() == true){
				graphVisualizer.add(dataReader.getGraph());
			}
			
			//Create the optimal schedule
			/*Sorter sorter = new Sorter(dataReader.getGraph());
			List<Vertex> tSort = sorter.generateSort();
			Schedule sol = ScheduleGenerator.makeSolution(tSort);*/
			long startTime = System.nanoTime();
			AStar aStar = new AStar(dataReader.getGraph(),inputParser.getProcessors());
			outWriter.createScheduleAStar(dataReader.getGraphName(),dataReader.getInfo(),dataReader.getRead(),aStar.execute(),dataReader.getMapping());
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("\n Took " + totalTime/1000000 + "ms" + " : " + totalTime/1000000000 + " seconds");
		}
		graphVisualizer.displayGraphs();
	}
}
