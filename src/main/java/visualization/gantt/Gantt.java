package visualization.gantt;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Solution;

public class Gantt extends ApplicationFrame{

	private static String _title;
	private static Solution _sol;
	private static JFreeChart _chart;
	public boolean _launched = false;
	public Gantt(String title) {
		super(title);
		_title = title;
	}


	public Gantt(String title,Solution solution) {
		super(title);
		_sol = solution;
	}
	
	/**
	 * Creates a gantt chart using data set. This gantt chart has no legend and 
	 * the x axis represent seconds
	 * A custom renderer to use to add colours to the chart. 
	 * @param dataset
	 * @return
	 */
	private static JFreeChart createChart(IntervalCategoryDataset dataset) {
		_chart = GanttChartFactory.createGanttChart(
				_title, "Task", "Value", dataset, false, true, true);
		
        CategoryPlot plot = (CategoryPlot) _chart.getPlot();
        CustomGanttRenderer renderer = new CustomGanttRenderer();
        renderer.setShadowVisible(false);
        plot.setRenderer(renderer);
        
        
        //remove reflection
        BarRenderer br = (BarRenderer) plot.getRenderer();
        br.setBarPainter(new StandardBarPainter());
        _chart.setNotify(true);
        
		return _chart;
	}
	/**
	 * createDataset looks at the best solution then loops over all its processors.
	 * For each processor it reads the start and finish times of each task in the processor,
	 * in order to create a subtask that will be displayed on the gantt chart.
	 * @return
	 */
	private static IntervalCategoryDataset createDataset() {
		
		
		TaskSeries ts = new TaskSeries("Best Solution");
		
		HashMap<Integer, Processor> processors = _sol.getProcess();
		
		int processNo = processors.size();
		
		for (int i = 1; i <= processNo; i++) {
			List<ProcessInfo> processList = processors.get(i).getProcesses();
			String processName = "Processor " + i;
			TaskNumeric main = new TaskNumeric(processName, 0, processors.get(i).getTime());
			for (ProcessInfo p: processList) {
				int startTime = getTaskStart(p);
				int endTime = getTaskEnd(p);
				
				main.addSubtask(new TaskNumeric(p.getTaskName(), startTime, endTime));
				System.out.println(processName);
				System.out.println(startTime);
				System.out.println(endTime);
				
			}
			ts.add(main);
		}
		
		TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
		taskSeriesCollection.add(ts);
		
		
		
		/*
		TaskSeries ts = new TaskSeries("Temp Solution");
		
		TaskNumeric p0 = new TaskNumeric("Processor 0", 1, 10);
		p0.addSubtask(new TaskNumeric("A",1,5));
		p0.addSubtask(new TaskNumeric("B",5,10));
		ts.add(p0);
		ts.add(new TaskNumeric("Processor 1", 2,9));
		

		
		TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
		taskSeriesCollection.add(ts);
		*/
		return taskSeriesCollection;
		
	}


	private static int getTaskStart(ProcessInfo p) {
		return p.startTime();
	}

	private static int getTaskEnd(ProcessInfo p) {
		return p.endTime();
	}

	/**
	 * Tells the chart to repaint itself with the dataset of the given solution
	 * @param sol
	 */
	public void updateSolution(Solution sol) {
		_sol = sol;
		_chart.getCategoryPlot().setDataset(createDataset());
		//_chart.getXYPlot().setDataset(_chart.getXYPlot().getDataset());
		
	}
	
	public void setSolution(Solution sol) {
		_sol = sol;
	}
	/**
	 * Creates the jpanel 
	 * @return
	 */
	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}
	/**
	 * Checks if this panel has been created
	 * @return
	 */
	public boolean hasLaunched() {
		return _launched ;
	}
	/**
	 * Inialises the gantt chart, makes the JPanel and puts it in a frame
	 */
	public void launch() {
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(1200, 800));
		setContentPane(jpanel);
		//this.pack();
		//RefineryUtilities.centerFrameOnScreen(this);
		//this.setVisible(true);
		_launched= true;
	}



}