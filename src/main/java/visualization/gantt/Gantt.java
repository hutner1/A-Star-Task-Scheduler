package visualization.gantt;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Gantt extends ApplicationFrame{
	 
	public Gantt(String title) {
	        super(title);
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
	        
	        TaskSeriesCollection taskseriescollection = new TaskSeriesCollection();
	        taskseriescollection.add(taskseries1);
	        taskseriescollection.add(t2);
	        
	        return taskseriescollection;
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
