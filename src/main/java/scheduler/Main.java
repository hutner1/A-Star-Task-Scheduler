package scheduler;
import java.util.List;

import io.DataReader;
import io.InputParser;
import io.OutputWriter;
import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.ScheduleGenerator;
import scheduler.basicmilestone.Sorter;
import scheduler.basicmilestone.Vertex;
import scheduler.dfsbranchandbound.SolutionGenerator;

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
		
		while(dataReader.hasMoreGraphs()) {
			System.out.println("More graphs in file? " + dataReader.hasMoreGraphs());
			dataReader.readNextGraph();
			
			//Create the optimal schedule
			/**
			Sorter sorter = new Sorter(dataReader.getGraph());
			List<Vertex> tSort = sorter.generateSort();
			Schedule sol = ScheduleGenerator.makeSolution(tSort);
			**/
			SolutionGenerator solutionGenerator = new SolutionGenerator(dataReader.getGraph(), 2);
			solutionGenerator.generateSolution();
			
			//outWriter.createSchedule(dataReader.getGraphName(),dataReader.getInfo(),dataReader.getRead(),sol,dataReader.getMapping());
			
			outWriter.createScheduleDFS(dataReader.getGraphName(),dataReader.getInfo(),dataReader.getRead(),solutionGenerator,dataReader.getMapping());

		}
	}
}
