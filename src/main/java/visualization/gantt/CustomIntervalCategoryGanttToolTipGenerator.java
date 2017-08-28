package visualization.gantt;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
/**
 * This class generates tooltips for subtasks. 
 * It also changes the display format of the tooltips to correctly 
 * show integer start/end times instead of showing Dates.
 */
@SuppressWarnings("serial")
public class CustomIntervalCategoryGanttToolTipGenerator extends
IntervalCategoryToolTipGenerator {
	/**
	 * Creates a new generator with a default number formatter.
	 */
	public CustomIntervalCategoryGanttToolTipGenerator() {
		super();
	}
	/**
	 * Creates a new generator with the specified number formatter.
	 *
	 * @param labelFormat the label format string (<code>null</code> not permitted).
	 * @param formatter the number formatter (<code>null</code> not permitted).
	 */
	public CustomIntervalCategoryGanttToolTipGenerator(String labelFormat,
			NumberFormat formatter) {
		super(labelFormat, formatter);
	}
	/**
	 * Creates a new generator with the specified date formatter.
	 *
	 * @param labelFormat the label format string (<code>null</code> not permitted).
	 * @param formatter the date formatter (<code>null</code> not permitted).
	 */
	public CustomIntervalCategoryGanttToolTipGenerator(String labelFormat,
			DateFormat formatter) {
		super(labelFormat, formatter);
	}
	/**
	 * Creates the array of items that can be passed to the <code>MessageFormat</code> class
	 * for creating labels.
	 *
	 * @param dataset the dataset (<code>null</code> not permitted).
	 * @param row the row index (zero-based).
	 * @param column the column index (zero-based).
	 *
	 * @return The items (never <code>null</code>).
	 */
	protected Object[] createItemArray(CategoryDataset
			dataset, int row, int column, int subinterval) {
		Object[] result = new Object[5];
		result[0] = dataset.getRowKey(row).toString();
		result[1] = dataset.getColumnKey(column).toString();
		Number value = dataset.getValue(row, column);
		if (getNumberFormat() != null) {
			result[2] = getNumberFormat().format(value);
		}
		else if (getDateFormat() != null) {
			result[2] = getDateFormat().format(value);
		}
		if (dataset instanceof GanttCategoryDataset) {
			GanttCategoryDataset gcd =
					(GanttCategoryDataset) dataset;
			Number start = gcd.getStartValue(row, column,
					subinterval);
			Number end = gcd.getEndValue(row, column,
					subinterval);
			if (getNumberFormat() != null) {
				result[3] = getNumberFormat().format(start);
				result[4] = getNumberFormat().format(end);
			}
			else if (getDateFormat() != null) {
				result[3] = getDateFormat().format(start);
				result[4] = getDateFormat().format(end);
			}
		}
		else if (dataset instanceof IntervalCategoryDataset) {
			IntervalCategoryDataset icd =
					(IntervalCategoryDataset) dataset;
			Number start = icd.getStartValue(row, column);
			Number end = icd.getEndValue(row, column);
			if (getNumberFormat() != null) {
				result[3] = getNumberFormat().format(start);
				result[4] = getNumberFormat().format(end);
			}
			else if (getDateFormat() != null) {
				result[3] = getDateFormat().format(start);
				result[4] = getDateFormat().format(end);
			}
		}
		return result;
	}
	/**
	 * Generates the tool tip text for an item in a dataset. 
	 * Note: in the current dataset
	 * implementation, each row is a series, and each column contains values for a
	 * particular category.
	 *
	 * @param dataset the dataset (<code>null</code> not permitted).
	 * @param row the row index (zero-based).
	 * @param column the column index (zero-based).
	 * @param subinterval the subinterval index (zero-based).
	 *
	 * @return The tooltip text (possibly <code>null</code>).
	 */
	public String generateToolTip(CategoryDataset dataset,
			int row, int column, int subinterval) {
		return generateLabelString(dataset, row, column,
				subinterval);
	}
	/**
	 * Generates a label for the specified item.
	 *
	 * @param dataset the dataset (<code>null</code> not
		permitted).
	 * @param row the row index (zero-based).
	 * @param column the column index (zero-based).
	 * @param subinterval the subinterval index (zero-based).
	 *
	 * @return The label (possibly <code>null</code>).
	 */
	protected String generateLabelString(CategoryDataset
			dataset,
			int row, int
			column, int subinterval) {
		if (dataset == null) {
			throw new IllegalArgumentException();
		}
		String result = null;
		Object[] items = createItemArray(dataset, row,
				column, subinterval);
		result = MessageFormat.format(getLabelFormat(), items);
		return result;
	}
}

