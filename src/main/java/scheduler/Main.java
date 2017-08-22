package scheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import io.DataReader;
import io.InputParser;
import io.InputParserException;
import io.OutputWriter;
import scheduler.astar.AStar;
import scheduler.astar.AStarParallelised;
import scheduler.astar.Solution;
import scheduler.graphstructures.Vertex;
import visualization.Visualizer;
import visualization.gantt.Gantt;
import visualization.gui.Gui;
import visualization.gui.StatisticTable;


/**
 * This is the main class for the task scheduler program.
 */
public class Main {

	public static void main(String[] args) {
		InputParser inputParser = new InputParser(args);
		try {
			inputParser.parse();
		} catch (InputParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OutputWriter outWriter = new OutputWriter(inputParser.getOutputFileName());
		outWriter.initialise();

		DataReader dataReader = new DataReader(inputParser.getFile());
		Visualizer graphVisualizer = null;

		Gantt gantt = null;
		StatisticTable stats = null;
		
		while(dataReader.hasMoreGraphs()) {
			System.out.println("More graphs in file? " + dataReader.hasMoreGraphs());
			dataReader.readNextGraph();
			
			if(inputParser.isVisualise() == true){
				graphVisualizer = new Visualizer();
				graphVisualizer.add(dataReader.getGraph());

				graphVisualizer.displayGraph();
				gantt = new Gantt("");
				stats = new StatisticTable(inputParser.getCores());
				final Gantt gant2 = gantt;
				final Visualizer graphVisualizer2 = graphVisualizer;
				final StatisticTable stats2 = stats;
				//graphVisualizer.displayGraph();
				try {
					 // Set cross-platform Java L&F (also called "Metal")
			        UIManager.setLookAndFeel(
			            UIManager.getCrossPlatformLookAndFeelClassName());
					SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							
							Gui window = new Gui(graphVisualizer2,gant2,stats2);
							window.frame.setVisible(true);
							graphVisualizer2.setGuiListener(window);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//Create the optimal schedule
			/*Sorter sorter = new Sorter(dataReader.getGraph());
			List<Vertex> tSort = sorter.generateSort();
			Schedule sol = ScheduleGenerator.makeSolution(tSort);*/
			long startTime = System.nanoTime();


			AStar aStar;
			if(inputParser.isParallelise() && inputParser.getCores() > 1){
				aStar = new AStarParallelised(dataReader.getGraph(), inputParser.getProcessors(), inputParser.getCores(), graphVisualizer, gantt);
			} else {
				aStar = new AStar(dataReader.getGraph(),inputParser.getProcessors(), graphVisualizer, gantt);
			}

			Solution optimalSolution = aStar.execute();
			outWriter.createScheduleAStar(dataReader.getGraphName(),dataReader.getVerticesAndEdgesInfo(),dataReader.getVerticesAndEdgesRead(),optimalSolution,dataReader.getMapping());
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("\n Took " + totalTime/1000000 + "ms" + " : " + totalTime/1000000000 + " seconds");
			System.out.println(aStar.getSolCreated());
			System.out.println(aStar.getSolPopped());
			System.out.println(aStar.getSolPruned());
			
			if(stats != null){
				stats.updateStats(aStar.getSolCreated(), aStar.getSolPopped(), aStar.getSolPruned(), (int) (totalTime/1000000), optimalSolution.getLastFinishTime());
			}
			
			
			/*
			if(inputParser.isVisualise() == true){
				gantt = new Gantt("Test");
				gantt.setSolution(optimalSolution);
				gantt.launch();
				
			}
			*/
			
		}
	}
}
