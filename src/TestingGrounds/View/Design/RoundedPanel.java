package TestingGrounds.View.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
public class RoundedPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private int cornerRadius;

    public RoundedPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        g2d.setColor(getBackground());
        g2d.fill(roundedRectangle);

        g2d.dispose();
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        repaint();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

}

