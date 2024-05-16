package Java.View.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmallFallingLettersPanel extends JPanel {
    private static final int LETTER_SIZE = 30;
    private static final int MAX_LETTERS = 20;
    private static final int FALLING_SPEED = 3;
    private static final int LETTER_DISTANCE = 80;
    private List<FallingLetter> fallingLetters;
    private Timer timer;
    private Font customFont;

    public SmallFallingLettersPanel() {
        fallingLetters = new ArrayList<>();
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/TestingGrounds/View/Design/Fonts/Wedges.ttf")).deriveFont(Font.BOLD, LETTER_SIZE);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, LETTER_SIZE);
        }
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fallingLetters.size() < MAX_LETTERS) {
                    addFallingLetter();
                }
                moveLetters();
                repaint();
            }
        });
        timer.start();
    }


    private void addFallingLetter() {
        Random random = new Random();
        char letter = (char) (random.nextInt(26) + 'A');
        int x = random.nextInt(getWidth());
        int y = -LETTER_SIZE - (fallingLetters.size() * LETTER_DISTANCE);
        Color[] colors = {new Color(255, 84, 146),
                new Color(250, 37, 91),
                new Color(56, 182, 255),
                new Color(7, 120, 218),
                new Color(248, 168, 197)};
        Color color = colors[random.nextInt(colors.length)];
        fallingLetters.add(new FallingLetter(letter, x, y, FALLING_SPEED, color));
    }

    private void moveLetters() {
        for (FallingLetter letter : fallingLetters) {
            letter.move();
            if (letter.getY() > getHeight()) {
                letter.setY(-LETTER_SIZE);
                letter.setX(new Random().nextInt(getWidth()));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (FallingLetter letter : fallingLetters) {
            g.setColor(letter.getColor());
            g.setFont(customFont);
            g.drawString(String.valueOf(letter.getLetter()), letter.getX(), letter.getY());
        }
    }

    private class FallingLetter {
        private char letter;
        private int x;
        private int y;
        private int speed;
        private Color color;

        private FallingLetter(char letter, int x, int y, int speed, Color color) {
            this.letter = letter;
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = color;
        }

        public char getLetter() {
            return letter;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Color getColor() {
            return color;
        }

        public void move() {
            y += speed;
        }
    }

}
