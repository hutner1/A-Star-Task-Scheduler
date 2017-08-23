package scheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

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
	
	public static long _startTime;

	public static void main(String[] args) {
		// 1. Instantiate InputParser to parse the command line arguments.
		InputParser inputParser = new InputParser(args);
		try {
			inputParser.parse();
		} catch (InputParserException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		// 2. Instantiate OutputWriter to write to the output file.
		OutputWriter outWriter = new OutputWriter(inputParser.getOutputFileName());
		outWriter.initialise();

		// 3. Instantiate DataReader to read the input file
		DataReader dataReader = new DataReader(inputParser.getFile());
		
		// 4. Declare the visualisers
		Visualizer graphVisualizer = null;
		Gantt gantt = null;
		StatisticTable stats = null;
		
		// 5. Find optimal solution for all the input task graphs.
		while(dataReader.hasMoreGraphs()) {
			System.out.println("More graphs in file? " + dataReader.hasMoreGraphs());
			dataReader.readNextGraph();
			
			if(inputParser.isVisualise() == true){
				graphVisualizer = new Visualizer();
				graphVisualizer.add(dataReader.getGraph());

				graphVisualizer.displayGraph();
				gantt = new Gantt("");
				stats = new StatisticTable(inputParser.getCores(),inputParser.getProcessors());
				final Gantt gant2 = gantt;
				final Visualizer graphVisualizer2 = graphVisualizer;
				final StatisticTable stats2 = stats;
				final int numProc = inputParser.getProcessors();
				//graphVisualizer.displayGraph();
				try {
					 // Set cross-platform Java L&F (also called "Metal")
			        UIManager.setLookAndFeel(
			            UIManager.getCrossPlatformLookAndFeelClassName());
					SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							
							Gui window = new Gui(graphVisualizer2,gant2,stats2,numProc);
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
			
			// start timing //TODO will this be included in final?
			long startTime = System.nanoTime();

			// decide whether to parallelise A* based on user input
			AStar aStar;
			if(inputParser.isParallelise() && inputParser.getCores() > 1){
				aStar = new AStarParallelised(dataReader.getGraph(), inputParser.getProcessors(), inputParser.getCores(), graphVisualizer, gantt, stats);
			} else {
				aStar = new AStar(dataReader.getGraph(),inputParser.getProcessors(), graphVisualizer, gantt, stats);
			}

			_startTime = System.nanoTime();
			
			// write the optimal schedule to the output file
			Solution optimalSolution = aStar.execute();
			outWriter.createScheduleAStar(dataReader.getGraphName(),dataReader.getVerticesAndEdgesInfo(),dataReader.getVerticesAndEdgesRead(),optimalSolution,dataReader.getMapping());
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("\n Took " + totalTime/1000000 + "ms" + " : " + totalTime/1000000000 + " seconds");
			System.out.println(aStar.getSolCreated());
			System.out.println(aStar.getSolPopped());
			System.out.println(aStar.getSolPruned());
			
			if(graphVisualizer != null){
				graphVisualizer.UpdateGraph(optimalSolution);
				gantt.updateSolution(optimalSolution);
				stats.updateStats(aStar.getSolCreated(), aStar.getSolPopped(), aStar.getSolPruned(), optimalSolution.getLastFinishTime());
				DefaultTableModel model = (DefaultTableModel)stats.getTable().getModel();
				model.setValueAt("Optimal Finish Time", 7, 0);
			}
			
		}
	}
}
