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
        _colors.add(0, new Color(231, 76, 60));   // blood orange
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
	
    @Override

    public Paint getItemPaint(int row, int col) {
    	
    	
        if (this.row == row && this.col == col) {

            if (index < 11) {
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

