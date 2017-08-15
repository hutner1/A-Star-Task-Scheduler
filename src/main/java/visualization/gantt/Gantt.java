package visualization.gantt;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import scheduler.astar.ProcessInfo;
import scheduler.astar.Processor;
import scheduler.astar.Solution;

public class Gantt extends ApplicationFrame{

	private static Solution _sol;

	public Gantt(String title) {
		super(title);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}


	public Gantt(String title,Solution solution) {
		super(title);
		_sol = solution;
		
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
		
	}

	private static JFreeChart createChart(IntervalCategoryDataset dataset) {
		final JFreeChart chart = GanttChartFactory.createGanttChart(
				"Gantt Chart Demo", "Task", "Value", dataset, true, true, false);
		return chart;
	}

	private static IntervalCategoryDataset createDataset() {
		
		TaskSeries ts = new TaskSeries("Best Solution");
		
		HashMap<Integer, Processor> processors = _sol.getProcess();
		int processNo = processors.size();
		for (int i = 1; i <= processNo; i++) {
			List<ProcessInfo> processList = processors.get(i).getProcesses();
			String processName = "Processor " + i;
			for (ProcessInfo p: processList) {
				int startTime = getTaskStart(p);
				int endTime = getTaskEnd(p);
				
				ts.add(new TaskNumeric(processName ,startTime,endTime));
				System.out.println(processName);
				System.out.println(startTime);
				System.out.println(endTime);
				
			}
		}
		
		TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
		taskSeriesCollection.add(ts);
		
		
		/*
		TaskSeries taskseries1 = new TaskSeries("Current Solution");
		Task task0 = new TaskNumeric("Processor 0", 0, 5);
		//task.setPercentComplete(0.9D);
		taskseries1.add(task0);
		Task task1 = new TaskNumeric("Processor 1", 2,9);
		taskseries1.add(task1);
		Task task2 = TaskNumeric.duration("Processor 2", 6, 5);
		taskseries1.add(task2);
		TaskSeries t2 = new TaskSeries("Best Solution");
		t2.add(new TaskNumeric("Processor 0", 0, 3));
		t2.add(new TaskNumeric("Processor 1", 3, 9));
		t2.add(new TaskNumeric("Processor 2", 5, 10));
		TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
		taskSeriesCollection.add(taskseries1);
		taskSeriesCollection.add(t2);
		*/
		return taskSeriesCollection;

	}


	private static int getTaskStart(ProcessInfo p) {
		return p.startTime();
	}

	private static int getTaskEnd(ProcessInfo p) {
		return p.endTime();
	}


	private void update(Solution s) {
		//TODO
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public void launch() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}



}