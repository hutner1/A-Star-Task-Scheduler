package scheduler;
import java.util.List;

import Visualization.Visualizer;
import io.DataReader;
import io.InputParser;
import io.OutputWriter;
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
			Sorter sorter = new Sorter(dataReader.getGraph());
			List<Vertex> tSort = sorter.generateSort();
			Schedule sol = ScheduleGenerator.makeSolution(tSort);
			outWriter.createSchedule(dataReader.getGraphName(),dataReader.getInfo(),dataReader.getRead(),sol,dataReader.getMapping());
		}
		graphVisualizer.displayGraphs();
	}
}
