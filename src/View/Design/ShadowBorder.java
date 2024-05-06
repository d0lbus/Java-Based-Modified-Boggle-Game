package View.Design;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
public class ShadowBorder extends AbstractBorder {
    private static final int SHADOW_SIZE = 12;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50); // Semi-transparent black

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint shadow at the edges
        g2.setComposite(AlphaComposite.SrcOver.derive(0.5f)); // Control the transparency
        for (int i = 0; i < SHADOW_SIZE; i++) {
            g2.setColor(new Color(SHADOW_COLOR.getRed(), SHADOW_COLOR.getGreen(), SHADOW_COLOR.getBlue(), ((i + 1) * SHADOW_COLOR.getAlpha()) / SHADOW_SIZE));
            g2.fillRect(x, y + height - i, width, 1); // Bottom shadow
            g2.fillRect(x + width - i, y, 1, height); // Right shadow
        }
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, SHADOW_SIZE, SHADOW_SIZE);
    }
}
