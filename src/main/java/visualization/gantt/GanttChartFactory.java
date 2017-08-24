package visualization.gantt;

import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.IntervalCategoryDataset;

/**
 * 
 *
 */
public class GanttChartFactory extends ChartFactory{

	/**
	 * This static method makes the skeleton of the Gantt Chart
	 * @param title
	 * @param categoryAxisLabel
	 * @param valueAxisLabel
	 * @param dataset
	 * @param legend
	 * @param tooltips
	 * @param urls
	 * @return
	 */
	public static JFreeChart createGanttChart(String title,
			String categoryAxisLabel, String valueAxisLabel,
			IntervalCategoryDataset dataset, boolean legend, boolean tooltips,
			boolean urls) {

		CategoryAxis categoryAxis = new CategoryAxis(categoryAxisLabel);
		ValueAxis valueAxis = new NumberAxis(valueAxisLabel);

		CategoryItemRenderer renderer = new GanttRenderer();
		
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis,
				renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, legend);
		ChartTheme currentTheme = new StandardChartTheme("Something");
		currentTheme.apply(chart);
		return chart;
	}
}