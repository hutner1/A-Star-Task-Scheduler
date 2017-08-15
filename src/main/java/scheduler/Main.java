package scheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import io.DataReader;
import io.InputParser;
import io.OutputWriter;
import scheduler.astar.AStar;
import scheduler.astar.AStarParallelised;
import scheduler.astar.Solution;
import scheduler.graphstructures.Vertex;
import visualization.Visualizer;
import visualization.gui.Gui;

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
		Visualizer graphVisualizer = null;
		Gui gui = null;
		
		
		while(dataReader.hasMoreGraphs()) {
			System.out.println("More graphs in file? " + dataReader.hasMoreGraphs());
			dataReader.readNextGraph();
			
			if(inputParser.isVisualise() == true){
				graphVisualizer = new Visualizer();
				graphVisualizer.add(dataReader.getGraph());
				graphVisualizer.displayGraph();
				final Visualizer graphVisualizer2 = graphVisualizer;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							
							Gui window = new Gui(graphVisualizer2);
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
			}
			//Create the optimal schedule
			/*Sorter sorter = new Sorter(dataReader.getGraph());
			List<Vertex> tSort = sorter.generateSort();
			Schedule sol = ScheduleGenerator.makeSolution(tSort);*/
			long startTime = System.nanoTime();
			AStar aStar = new AStar(dataReader.getGraph(),inputParser.getProcessors(), graphVisualizer, gui);
			Solution optimalSolution = aStar.execute();
			outWriter.createScheduleAStar(dataReader.getGraphName(),dataReader.getInfo(),dataReader.getRead(),optimalSolution,dataReader.getMapping());
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("\n Took " + totalTime/1000000 + "ms" + " : " + totalTime/1000000000 + " seconds");
			
		}
	}
}
