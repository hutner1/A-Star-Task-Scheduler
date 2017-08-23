package visualization.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

class CustomButton extends JButton {

	private Color hoverBackgroundColor = new Color(6, 47, 79);

	/**
	 * Constructor for a custom button with no parameters
	 */
	public CustomButton() {
		this(null);

	}

	/**
	 * Constructor for a custom button with a text parameter
	 * @param text
	 */
	public CustomButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
		Border empty = BorderFactory.createEmptyBorder();
		this.setBorder(empty);
		this.setFocusable(false);
		this.setBackground(new Color(13, 90, 150));
		this.setForeground(Color.white);
		this.setFont(new Font("SansSerif", Font.BOLD, 18));
		this.setContentAreaFilled(false);
		this.setOpaque(false);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isRollover()) {
			g.setColor(hoverBackgroundColor);
		} else {
			g.setColor(getBackground());
		}
		/*g.fillRect(0, 0, getWidth(), getHeight());*/
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

		/*g.setColor(getBackground().darker().darker().darker());*/
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

		super.paintComponent(g);


	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractButton#setContentAreaFilled(boolean)
	 */
	@Override
	public void setContentAreaFilled(boolean b) {
	}

}
