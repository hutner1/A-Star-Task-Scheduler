package visualization;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerPipe;

public class MouseScrollListener implements MouseWheelListener, MouseMotionListener {

	private View _view = null;

	public MouseScrollListener(View view) { 
		this._view = view; 
		this._view.addMouseMotionListener( this); 
	} 

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) { 
		if(_view != null){ 
			int notches = e.getWheelRotation(); 
			Point point = e.getPoint(); 
			double i = _view.getCamera().getViewPercent(); 
			if(i < 1){ 
				if(point.getX() < 400){ 
					if(point.getY() < 300){ 
						_view.getCamera().getViewCenter().move(-1, 1); 
					} 
					else if(point.getY() < 600){ 
						_view.getCamera().getViewCenter().move(-1, 0); 
					} 
					else{ 
						_view.getCamera().getViewCenter().move(-1, -1); 
					} 
				} 
				else if(point.getX() < 800){ 
					if(point.getY() < 300){ 
						_view.getCamera().getViewCenter().move(0, 1); 
					} 
					else if(point.getY() < 600){ 
						_view.getCamera().getViewCenter().move(0, 0); 
					} 
					else{ 
						_view.getCamera().getViewCenter().move(0, -1); 
					} 
				} 
				else{ 
					if(point.getY() < 300){ 
						_view.getCamera().getViewCenter().move(1, 1); 
					} 
					else if(point.getY() < 600){ 
						_view.getCamera().getViewCenter().move(1, 0); 
					} 
					else{ 
						_view.getCamera().getViewCenter().move(1, -1); 
					} 
				} 
			} 
			else{ 
				_view.getCamera().resetView(); 
			} 


			if(notches > 0){ 
				_view.getCamera().setViewPercent(i * 1.1); 
			} 
			else{ 

				_view.getCamera().setViewPercent(i * 0.9); 
			} 
		} 
	} 

}
