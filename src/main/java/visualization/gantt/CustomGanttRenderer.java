package visualization.gantt;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;

import org.jfree.chart.renderer.category.GanttRenderer;

public class CustomGanttRenderer extends GanttRenderer {
	
    private int row;
    private int col;
    private int index = -1;
	private ArrayList<Color> _colors = new ArrayList<Color>();
	
	public CustomGanttRenderer() {
        _colors.add(0, new Color(51, 102, 255));   // blue
        _colors.add(1, new Color(100, 255, 102));  // light green      
        _colors.add(2, new Color(255, 102, 102));  // light red
        _colors.add(3, new Color(204, 153, 255));  // light light purple
        _colors.add(4, new Color(100, 204, 204));  // cyan
        _colors.add(5, new Color(153, 153, 255));  // light blue
        _colors.add(6, new Color(255, 204, 102));  // yellow
	}
	
    @Override

    public Paint getItemPaint(int row, int col) {
    	
    	
        if (this.row == row && this.col == col) {

            if (index < 6) {
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
}

