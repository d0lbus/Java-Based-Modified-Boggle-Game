package View.Design;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.Point2D;

public class GradientSliderUI extends BasicSliderUI {
    public GradientSliderUI(JSlider b) {
        super(b);
        b.setBackground(new Color(255, 204, 213));
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle trackBounds = trackRect;

        int value = slider.getValue();
        int maximum = slider.getMaximum();
        int minimum = slider.getMinimum();

        float fraction = (float) (value - minimum) / (maximum - minimum);

        Color colorFrom = Color.RED;
        Color colorTo = Color.GREEN;
        Color trackColor = blendColor(colorFrom, colorTo, fraction);

        GradientPaint gradient = new GradientPaint(
                new Point2D.Float(trackBounds.x, trackBounds.y),
                colorFrom,
                new Point2D.Float(trackBounds.x + trackBounds.width, trackBounds.y),
                colorTo);

        g2d.setPaint(gradient);
        g2d.fillRect(trackBounds.x, trackBounds.y + trackBounds.height / 2 - 4,
                trackBounds.width, 8);

        super.paintTrack(g);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.WHITE);

        // Optionally, adjust the thumb dimensions and positioning as needed
        int thumbHeight = 20;
        int thumbWidth = 10;
        int thumbX = thumbRect.x + (thumbRect.width - thumbWidth) / 2;
        int thumbY = thumbRect.y + (thumbRect.height - thumbHeight) / 2;

        g2d.fillRect(thumbX, thumbY, thumbWidth, thumbHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(thumbX, thumbY, thumbWidth, thumbHeight);
    }

    private Color blendColor(Color color1, Color color2, float ratio) {
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        return new Color(r, g, b);
    }
}
