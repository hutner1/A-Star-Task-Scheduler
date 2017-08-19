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
	private boolean _launched = false;
	public Gantt(String title) {
		super(title);
		_title = title;
	}


	public Gantt(String title,Solution solution) {
		super(title);
		_sol = solution;
		
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(1200, 800));
		setContentPane(jpanel);
		
	}

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

	
	public void updateSolution(Solution sol) {
		_sol = sol;
		_chart.getCategoryPlot().setDataset(createDataset());
		//_chart.getXYPlot().setDataset(_chart.getXYPlot().getDataset());
		
	}
	
	public void setSolution(Solution sol) {
		_sol = sol;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}
	
	public boolean hasLaunched() {
		return _launched ;
	}

	public void launch() {
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
		_launched= true;
	}



}