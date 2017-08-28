package visualization.gantt;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Schedule;
import scheduler.astar.Solution;

/**
 * Represents a single Gantt chart. It takes in a schedule, and generates a dataset.
 * The dataset is then used to render a Gantt chart which will be put into a JPanel.
 *
 */
@SuppressWarnings("serial")
public class Gantt extends ApplicationFrame{

	private static String _title;
	private static Schedule _sol;
	private static JFreeChart _chart;
	private boolean _launched = false;
	public JPanel _jpanel;
	/**
	 * Constructor for Gantt chart class with title parameter
	 * @param title
	 */
	public Gantt(String title) {
		super(title);
		_title = title;
		_jpanel = new JPanel();
		_jpanel.setLayout(new BorderLayout());
	}


	/**
	 * Constructor for Gantt chart class with title and solution parameters
	 * @param title
	 * @param solution
	 */
	public Gantt(String title,Schedule solution) {
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
				_title, "Processors", "Time", dataset, false, true, true);

		CategoryPlot plot = (CategoryPlot) _chart.getPlot();
		TaskSeriesCollection tsc = (TaskSeriesCollection) dataset;
		CustomGanttRenderer renderer = new CustomGanttRenderer(tsc.getSeries(0));
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


		TaskSeries ts = new TaskSeries("Current Solution");

		HashMap<Integer, Processor> processors = _sol.getProcess();

		int processNo = processors.size();

		for (int i = 1; i <= processNo; i++) {
			List<ProcessInfo> processList = processors.get(i).getProcesses();
			String processName = "Processor " + i;
			TaskNumeric main;
			if (_sol.getProcess().get(i).getTime() == 0) {
				main = new TaskNumeric(processName, 0, 0);
			} else {
				main = new TaskNumeric(processName, 0, _sol.getUpperBound());
			}
			for (ProcessInfo p: processList) {
				int startTime = getTaskStart(p);
				int endTime = getTaskEnd(p);

				main.addSubtask(new TaskNumeric(p.getTaskName(), startTime, endTime));


			}
			ts.add(main);
		}

		TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
		taskSeriesCollection.add(ts);

		return taskSeriesCollection;

	}


	/**
	 * This method returns the start time for a task
	 * @param ProcessInfo p, storing vertex start and end time
	 * @return int
	 */
	private static int getTaskStart(ProcessInfo p) {
		return p.startTime();
	}

	/**
	 * This method returns the end time for a task
	 * @param ProcessInfo p, storing vertex start and end time
	 * @return int
	 */
	private static int getTaskEnd(ProcessInfo p) {
		return p.endTime();
	}

	/**
	 * Tells the chart to repaint itself with the dataset of the given solution
	 * @param sol
	 */
	public void updateSolution(Solution sol) {
		_sol = sol;
		//_chart.getCategoryPlot().setDataset(createDataset());
		//_chart.getXYPlot().setDataset(_chart.getXYPlot().getDataset());
		_jpanel.removeAll();


		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);

		_jpanel.add(chartpanel);
		_jpanel.revalidate();
		_jpanel.repaint();

	}

	/**
	 * Setter method for solution
	 * @param sol
	 */
	public void setSolution(Schedule sol) {
		_sol = (Solution) sol;
	}

	/**
	 * Creates the jpanel 
	 * @return
	 */
	public JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		_jpanel.add(chartpanel);
		_launched = true;
		return _jpanel;
	}
	/**
	 * Checks if this panel has been created
	 * @return
	 */
	public boolean hasLaunched() {
		return _launched ;
	}
	
	/**
	 * A public class that returns the dataset. Used for testing
	 * @return
	 */
	public IntervalCategoryDataset getDataset() {
		return createDataset();
	}
	/**
	 * Inialises the gantt chart, makes the JPanel and puts it in a frame
	 */
	public void launch() {
		_jpanel = createDemoPanel();
		//jpanel.setPreferredSize(new Dimension(1200, 800));
		setContentPane(_jpanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
		_launched= true;

	}



}