package Java.View.Design;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.border.LineBorder;

public class LetterCube extends JPanel {
    private String letter;
    private  Font customFont;

    public LetterCube(String letter, String fontFilePath) {
        this.letter = letter;
        loadCustomFont(fontFilePath);
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
        setBorder(new LineBorder(Color.BLACK));
        setBorder(new RoundBorder(40));
    }

    private void loadCustomFont(String fontFilePath) {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(Font.PLAIN, 80);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (customFont == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.YELLOW);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        g2d.setColor(Color.BLACK);
        g2d.setFont(customFont);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(letter)) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(letter, x, y);
        g2d.dispose();
    }

    public void setFontSize(int size) {
        if (customFont != null) {
            customFont = customFont.deriveFont(Font.PLAIN, size);
        }
        repaint(); 
    }

}
