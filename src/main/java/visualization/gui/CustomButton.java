package visualization.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Generates a button with the custom settings that cannot be set with the default JButton class.
 */
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

	/** Override paintComponet which allows following things:
	 * - Change the color of buttons when hovered into dark blue
	 * - Change the corners into round shape
	 */
	@Override
	protected void paintComponent(Graphics g) {
		//When mouse is hovered
		if (getModel().isRollover()) {
			g.setColor(hoverBackgroundColor);
		} else {
			//when mouse is moved away
			g.setColor(getBackground());
		}
		
		//Filling the button with background color without going outside the round corners 
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

		//Drawing the button with round corners
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

		super.paintComponent(g);


	}

	

}
