package visualization.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

class CustomButton extends JButton {

    private Color hoverBackgroundColor = new Color(255, 155, 155);

    public CustomButton() {
        this(null);
   
    }

    public CustomButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
       Border empty = BorderFactory.createEmptyBorder();
        this.setBorder(empty);
        this.setFocusable(false);
        this.setBackground(new Color(255, 59, 63));
        this.setForeground(Color.white);
        this.setFont(new Font("SansSerif", Font.BOLD, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
       if (getModel().isRollover()) {
            g.setColor(hoverBackgroundColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

}
