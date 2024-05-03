package View.Design;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class LetterCube extends JPanel {
    private String letter;

    public LetterCube(String letter) {
        this.letter = letter;
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
        setBorder(new LineBorder(Color.BLACK));
        setBorder(new RoundBorder(40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.YELLOW);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 80));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(letter)) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(letter, x, y);
        g2d.dispose();
    }
}
