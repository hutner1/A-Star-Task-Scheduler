package visualization.gantt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

/**
 * Renders/paints the gantt chart
 * @author Jack
 *
 */
public class CustomGanttRenderer extends GanttRenderer {

	private int row;
	private int col;
	private int index = -1;
	private ArrayList<Color> _colors = new ArrayList<Color>();
	private int NUMBER_OF_COLOURS = 1;
	private TaskSeries _series;

	/**
	 * This renderer allows labels to be added to Gantt bars representing each task
	 * @param series
	 */
	public CustomGanttRenderer(TaskSeries series) {
		super();
		generateColours();
		setIncludeBaseInRange(false);
		this._series = series;

		setBaseItemLabelGenerator(new IntervalCategoryItemLabelGenerator());

		// Customise some of the visuals of gantt chart (colour, outlines, position)
		//setSeriesPaint(0, Color.decode("#FFFFFF"));
		setDrawBarOutline(true);
		setBaseItemLabelsVisible(true);
		setBaseItemLabelPaint(Color.BLACK);
		this.setSeriesItemLabelFont(0,new Font("Arial",0,20));
		setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));

	}

	/**
	 * Holds the colours used for the Gantt chart (change to alternating as per Guyver's advice)
	 */
	private void generateColours() {
		_colors.add(0, new Color(255,255,200));   //blue
		_colors.add(1, new Color(255, 195, 0));  // yellow     
		_colors.add(2, new Color(29, 131, 72));  // dark green
		_colors.add(3, new Color(142, 68, 173));  //  purple
		_colors.add(4, new Color(40, 116, 166));  // navy blue
		_colors.add(5, new Color(230, 126, 34));  // orange
		_colors.add(6, new Color(93, 109, 126));  // grey
		_colors.add(7, new Color(69, 179, 157)); //mint
		_colors.add(8, new Color(174, 214, 241)); //light light blue
		_colors.add(9, new Color(217, 252, 103)); //greenish yellow
		_colors.add(10, new Color(204, 92, 146)); //magenta
		_colors.add(11, new Color(240, 160, 160)); //peach

	}

	/* (non-Javadoc)
	 * @see org.jfree.chart.renderer.AbstractRenderer#getItemPaint(int, int)
	 */
	@Override
	public Paint getItemPaint(int row, int col) {


		if (this.row == row && this.col == col) {

			if (index < NUMBER_OF_COLOURS - 1) {
				index++;
			} else {
				index = 0;
			}
		} else {
			this.row = row;
			this.col = col;
			index = 0;
		}
		return _colors.get(index);

	}

	/* (non-Javadoc)
	 * @see org.jfree.chart.renderer.category.GanttRenderer#drawTasks(java.awt.Graphics2D, 
	 * org.jfree.chart.renderer.category.CategoryItemRendererState, java.awt.geom.Rectangle2D, 
	 * org.jfree.chart.plot.CategoryPlot, org.jfree.chart.axis.CategoryAxis, org.jfree.chart.axis.ValueAxis, 
	 * org.jfree.data.gantt.GanttCategoryDataset, int, int)
	 */
	@Override
	protected void drawTasks(Graphics2D g2,
			CategoryItemRendererState state,
			Rectangle2D dataArea,
			CategoryPlot plot,
			CategoryAxis domainAxis,
			ValueAxis rangeAxis,
			GanttCategoryDataset dataset,
			int row,
			int column) {

		int count = dataset.getSubIntervalCount(row, column);
		if (count == 0) {
			drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis, 
					dataset, row, column);
		}
		for (int subinterval = 0; subinterval < count; subinterval++) {

			RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

			// value 0
			Number value0 = dataset.getStartValue(row, column, subinterval);
			if (value0 == null) {
				return;
			}
			double translatedValue0 = rangeAxis.valueToJava2D(
					value0.doubleValue(), dataArea, rangeAxisLocation);

			// value 1
			Number value1 = dataset.getEndValue(row, column, subinterval);
			if (value1 == null) {
				return;
			}
			double translatedValue1 = rangeAxis.valueToJava2D(
					value1.doubleValue(), dataArea, rangeAxisLocation);

			if (translatedValue1 < translatedValue0) {
				double temp = translatedValue1;
				translatedValue1 = translatedValue0;
				translatedValue0 = temp;
			}

			double rectStart = calculateBarW0(plot, plot.getOrientation(), 
					dataArea, domainAxis, state, row, column);
			double rectLength = Math.abs(translatedValue1 - translatedValue0);
			double rectBreadth = state.getBarWidth();

			// DRAW THE BARS...
			Rectangle2D bar = null;

			if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
				bar = new Rectangle2D.Double(translatedValue0, rectStart, 
						rectLength, rectBreadth);
			}
			else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
				bar = new Rectangle2D.Double(rectStart, translatedValue0, 
						rectBreadth, rectLength);
			}

			Rectangle2D completeBar = null;
			Rectangle2D incompleteBar = null;
			Number percent = dataset.getPercentComplete(row, column, 
					subinterval);
			double start = getStartPercent();
			double end = getEndPercent();
			if (percent != null) {
				double p = percent.doubleValue();
				if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
					completeBar = new Rectangle2D.Double(translatedValue0, 
							rectStart + start * rectBreadth, rectLength * p, 
							rectBreadth * (end - start));
					incompleteBar = new Rectangle2D.Double(translatedValue0 
							+ rectLength * p, rectStart + start * rectBreadth, 
							rectLength * (1 - p), rectBreadth * (end - start));
				}
				else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
					completeBar = new Rectangle2D.Double(rectStart + start 
							* rectBreadth, translatedValue0 + rectLength 
							* (1 - p), rectBreadth * (end - start), 
							rectLength * p);
					incompleteBar = new Rectangle2D.Double(rectStart + start 
							* rectBreadth, translatedValue0, rectBreadth 
							* (end - start), rectLength * (1 - p));
				}

			}

			Paint seriesPaint = getItemPaint(row, column);
			g2.setPaint(seriesPaint);
			g2.fill(bar);

			if (completeBar != null) {
				g2.setPaint(getCompletePaint());
				g2.fill(completeBar);
			}
			if (incompleteBar != null) {
				g2.setPaint(getIncompletePaint());
				g2.fill(incompleteBar);
			}
			if (isDrawBarOutline() 
					&& state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
				g2.setStroke(getItemStroke(row, column));
				g2.setPaint(getItemOutlinePaint(row, column));
				g2.draw(bar);
			}


			/*---------------------------ADDITIONAL PART---------------------------*/
			// Call subtask label generator and pass in subinterval info
			CategoryItemLabelGenerator generator = getItemLabelGenerator(row, column);
			if (generator != null && isItemLabelVisible(row, column)) {
				drawItemLabel(g2, dataset, row, column, plot, generator, bar, false, subinterval);
			}

			// collect entity and tool tip information...
			if (state.getInfo() != null) {
				EntityCollection entities = state.getEntityCollection();
				if (entities != null) {
					//System.out.println("Here 0");
					String tip = null;
					CategoryToolTipGenerator cttg = new CustomIntervalCategoryGanttToolTipGenerator("{1}: {3} - {4}", NumberFormat.getNumberInstance());
							//getToolTipGenerator(row, column);
					if (cttg != null) {
						if (cttg instanceof
								CustomIntervalCategoryGanttToolTipGenerator) {
							tip =
									((CustomIntervalCategoryGanttToolTipGenerator)cttg).generateToolTip(
											dataset, row, column,
											subinterval
											);
						}
						else {
							tip = cttg.generateToolTip(
									dataset, row, column
									);
						}
					}
					String url = null;
					if (getItemURLGenerator(row, column) != null) {
						url = getItemURLGenerator(row, column).generateURL(
								dataset, row, column);
					}
					CategoryItemEntity entity = new CategoryItemEntity(
							bar, tip, url, dataset, dataset.getRowKey(row), 
							dataset.getColumnKey(column));
					entities.add(entity);
				}
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.jfree.chart.renderer.category.GanttRenderer#drawItemLabel(Graphics2D g2, CategoryDataset data,
        int row, int column, CategoryPlot plot, CategoryItemLabelGenerator generator, Rectangle2D bar,
        boolean negative, int subinterval)
	 */
	/**
	 * This method replaces one of the methods in the GanttRenderer class in JFreeChart. This class generates
	 * and draws the correct label, given a subtask.
	 * @param subinterval - the index of a task in their respective processor
	 */
	protected void drawItemLabel(Graphics2D g2, CategoryDataset data, int row, int column, CategoryPlot plot,
			CategoryItemLabelGenerator generator, Rectangle2D bar, boolean negative, int subinterval) {

		/*------------------------ADDITIONAL PART---------------------------*/    	
		// Sets the label of the subtask as its label
		String label =  _series.get(column).getSubtask(subinterval).getDescription();

		if (label == null) {
			return;  // nothing to do   
		}

		Font labelFont = getItemLabelFont(row, column);
		g2.setFont(labelFont);
		Paint paint = getItemLabelPaint(row, column);
		g2.setPaint(paint);

		// find out where to place the label...
		ItemLabelPosition position = null;
		if (!negative) {
			position = getPositiveItemLabelPosition(row, column);
		}
		else {
			position = getNegativeItemLabelPosition(row, column);
		}

		// work out the label anchor point...
		Point2D anchorPoint = calculateLabelAnchorPoint(
				position.getItemLabelAnchor(), bar, plot.getOrientation());

		if (isInternalAnchor(position.getItemLabelAnchor())) {
			Shape bounds = TextUtilities.calculateRotatedStringBounds(label, 
					g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(),
					position.getTextAnchor(), position.getAngle(),
					position.getRotationAnchor());

			if (bounds != null) {
				if (!bar.contains(bounds.getBounds2D())) {
					if (!negative) {
						position = getPositiveItemLabelPositionFallback();
					}
					else {
						position = getNegativeItemLabelPositionFallback();
					}
					if (position != null) {
						anchorPoint = calculateLabelAnchorPoint(
								position.getItemLabelAnchor(), bar, 
								plot.getOrientation());
					}
				}
			}
		}

		if (position != null) {
			TextUtilities.drawRotatedString(label, g2, 
					(float) anchorPoint.getX(), (float) anchorPoint.getY(),
					position.getTextAnchor(), position.getAngle(), 
					position.getRotationAnchor());
		}        
	}

	/* (non-Javadoc)
	 * @see org.jfree.chart.renderer.category.GanttRenderer#calculateLabelAnchorPoint(ItemLabelAnchor anchor,
       Rectangle2D bar, PlotOrientation orientation)
	 */
	/**
	 * This class replaces a method from GanttChartRenderer that is set to private to be used by this class
	 */

	private Point2D calculateLabelAnchorPoint(ItemLabelAnchor anchor, Rectangle2D bar, PlotOrientation orientation) {

		Point2D result = null;
		double offset = getItemLabelAnchorOffset();
		double x0 = bar.getX() - offset;
		double x1 = bar.getX();
		double x2 = bar.getX() + offset;
		double x3 = bar.getCenterX();
		double x4 = bar.getMaxX() - offset;
		double x5 = bar.getMaxX();
		double x6 = bar.getMaxX() + offset;

		double y0 = bar.getMaxY() + offset;
		double y1 = bar.getMaxY();
		double y2 = bar.getMaxY() - offset;
		double y3 = bar.getCenterY();
		double y4 = bar.getMinY() + offset;
		double y5 = bar.getMinY();
		double y6 = bar.getMinY() - offset;

		if (anchor == ItemLabelAnchor.CENTER) {
			result = new Point2D.Double(x3, y3);
		}
		else if (anchor == ItemLabelAnchor.INSIDE1) {
			result = new Point2D.Double(x4, y4);
		}
		else if (anchor == ItemLabelAnchor.INSIDE2) {
			result = new Point2D.Double(x4, y4);
		}
		else if (anchor == ItemLabelAnchor.INSIDE3) {
			result = new Point2D.Double(x4, y3);
		}
		else if (anchor == ItemLabelAnchor.INSIDE4) {
			result = new Point2D.Double(x4, y2);
		}
		else if (anchor == ItemLabelAnchor.INSIDE5) {
			result = new Point2D.Double(x4, y2);
		}
		else if (anchor == ItemLabelAnchor.INSIDE6) {
			result = new Point2D.Double(x3, y2);
		}
		else if (anchor == ItemLabelAnchor.INSIDE7) {
			result = new Point2D.Double(x2, y2);
		}
		else if (anchor == ItemLabelAnchor.INSIDE8) {
			result = new Point2D.Double(x2, y2);
		}
		else if (anchor == ItemLabelAnchor.INSIDE9) {
			result = new Point2D.Double(x2, y3);
		}
		else if (anchor == ItemLabelAnchor.INSIDE10) {
			result = new Point2D.Double(x2, y4);
		}
		else if (anchor == ItemLabelAnchor.INSIDE11) {
			result = new Point2D.Double(x2, y4);
		}
		else if (anchor == ItemLabelAnchor.INSIDE12) {
			result = new Point2D.Double(x3, y4);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE1) {
			result = new Point2D.Double(x5, y6);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE2) {
			result = new Point2D.Double(x6, y5);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE3) {
			result = new Point2D.Double(x6, y3);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE4) {
			result = new Point2D.Double(x6, y1);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE5) {
			result = new Point2D.Double(x5, y0);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE6) {
			result = new Point2D.Double(x3, y0);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE7) {
			result = new Point2D.Double(x1, y0);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE8) {
			result = new Point2D.Double(x0, y1);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE9) {
			result = new Point2D.Double(x0, y3);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE10) {
			result = new Point2D.Double(x0, y5);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE11) {
			result = new Point2D.Double(x1, y6);
		}
		else if (anchor == ItemLabelAnchor.OUTSIDE12) {
			result = new Point2D.Double(x3, y6);
		}

		return result;

	}

	/* (non-Javadoc)
	 * @see org.jfree.chart.renderer.category.GanttRenderer#isInternalAnchor(ItemLabelAnchor anchor)
	 */
	/**
	 * This class replaces a method from GanttChartRenderer that is set to private to be used by this class
	 */
	private boolean isInternalAnchor(ItemLabelAnchor anchor) {
		return anchor == ItemLabelAnchor.CENTER 
				|| anchor == ItemLabelAnchor.INSIDE1
				|| anchor == ItemLabelAnchor.INSIDE2
				|| anchor == ItemLabelAnchor.INSIDE3
				|| anchor == ItemLabelAnchor.INSIDE4
				|| anchor == ItemLabelAnchor.INSIDE5
				|| anchor == ItemLabelAnchor.INSIDE6
				|| anchor == ItemLabelAnchor.INSIDE7
				|| anchor == ItemLabelAnchor.INSIDE8
				|| anchor == ItemLabelAnchor.INSIDE9
				|| anchor == ItemLabelAnchor.INSIDE10
				|| anchor == ItemLabelAnchor.INSIDE11
				|| anchor == ItemLabelAnchor.INSIDE12;  
	}
}

