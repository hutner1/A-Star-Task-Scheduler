package visualization.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.AbstractBorder;

@SuppressWarnings("serial")
class CustomBorder extends AbstractBorder{
	/* (non-Javadoc)
	 * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y,int width, int height) {
		// TODO Auto-generated method stubs
		super.paintBorder(c, g, x, y, width, height);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(10));
		g2d.setColor(new Color(68,184,224));
		g2d.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
	}   
}