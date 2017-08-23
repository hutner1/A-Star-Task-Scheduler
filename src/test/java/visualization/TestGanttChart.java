package visualization;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.junit.Before;
import org.junit.Test;

import scheduler.astar.Solution;
import visualization.gantt.Gantt;
import visualization.gantt.TaskNumeric;

public class TestGanttChart {
	Gantt _gantt;
	TaskSeriesCollection _tsc;
	
	@Before
	public void initialise() {
		_gantt = new Gantt("Test", new MockSolution());
		_tsc = (TaskSeriesCollection) _gantt.getDataset();
	}
	
	@Test
	public void testGanttSize() {
		assertEquals(1 ,_tsc.getSeriesCount());
		
	}
	
	@Test
	public void testGanttTaskCount() {
		TaskSeries ts = _tsc.getSeries(0);
		
		assertEquals(4,ts.getItemCount());
	}
	
	@Test
	public void testMainTasks() {
		TaskSeries ts = _tsc.getSeries(0);
		List<TaskNumeric> tasks = ts.getTasks();
		
		assertEquals(30,tasks.get(0).getEndTime());
		assertEquals("Processor 1", tasks.get(0).getDesc());
		
		assertEquals(0, tasks.get(2).getStartTime());
		
		assertEquals(0,tasks.get(3).getEndTime());
		assertEquals("Processor 4", tasks.get(3).getDesc());
		
	}
	
	@Test
	public void testSubTasks() {
		TaskSeries ts = _tsc.getSeries(0);
		List<TaskNumeric> tasks = ts.getTasks();
		
		TaskNumeric subtask00 = (TaskNumeric) tasks.get(0).getSubtask(0);
		assertEquals("aaaa",subtask00.getDesc());
		assertEquals(5, subtask00.getEndTime());
		
		TaskNumeric subtask01 = (TaskNumeric) tasks.get(0).getSubtask(1);
		assertEquals("bbbb",subtask01.getDesc());
		assertEquals(5, subtask01.getStartTime());
		
		TaskNumeric subtask02 = (TaskNumeric) tasks.get(0).getSubtask(2);
		assertEquals(10, subtask02.getStartTime());
		
		TaskNumeric subtask20 = (TaskNumeric) tasks.get(2).getSubtask(0);
		assertEquals(27, subtask20.getStartTime());
		
		assertEquals(0 ,tasks.get(3).getSubtaskCount());
		
		
	}
}
