package visualization;

import scheduler.astar.Solution;
import visualization.gantt.Gantt;

public class TestGanttChart extends Gantt{

	public TestGanttChart(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public TestGanttChart(String title, Solution sol) {
		super(title, sol);
	}

	
	
}
