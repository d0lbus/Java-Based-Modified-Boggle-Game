package View;

import View.Design.FallingLettersPanel;
import View.Design.GradientSliderUI;
import View.Design.LetterCube;
import View.Design.RoundedPanel;
//import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ClientGUIFrame extends JFrame {

    private JPanel contentPane;
    private JTextField inputTextField, cUsernameTextfield;
    private JPasswordField pwField, confirmpwField;
    private JPanel homePanel, lobbyPanel, gamePanel, rankingPanel, leaderboardPanel, settingsPanel;
    private JLabel player1gamePic, player2gamePic, player3gamePic, player4gamePic;
    private Clip clip;
    private JSlider volumeSlider;
    private JLabel player1pic, player2pic, player3pic, player4pic, player1picRanking, player2picRanking, player3picRanking, player4picRanking;
    private JLabel player1username, player2username, player3username, player4username, player1usernameRanking, player2usernameRanking, player3usernameRanking, player4usernameRanking;
    private JButton randomButton, quitButton, createLobbyButton, joinButton, startButton;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20;
    private JLabel player1gamePoints, player2gamePoints, player3gamePoints, player4gamePoints;
    private JLabel player1gameUsername, player2gameUsername, player3gameUsername, player4gameUsername, timerLabel;
    private Timer timer;
    private JTextPane announcementTextpane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        /*try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } */

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUIFrame frame = new ClientGUIFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientGUIFrame() {
        setTitle("Boggled");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);
        setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/Icons/logo.png");
        setIconImage(icon);

        playMusic("music.wav");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1264, 681);
        setContentPane(layeredPane);

        homePanel = new JPanel();
        homePanel.setBackground(new Color(255, 204, 213));
        homePanel.setLayout(null);
        homePanel.setBounds(0, 0, 1280, 720);
        layeredPane.add(homePanel);

        FallingLettersPanel fallingLettersPanelHome = new FallingLettersPanel();
        fallingLettersPanelHome.setOpaque(false);
        fallingLettersPanelHome.setBounds(0, 0, 1280, 720);

        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(255, 204, 213));
        homePanel.setLayout(null);
        homePanel.setBounds(0, 0, 1280, 720);

        ImageIcon catIcon = new ImageIcon("src/Icons/cat.gif");
        Image image = catIcon.getImage();
        Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        catIcon = new ImageIcon(scaledImage);

        JLabel catLabel = new JLabel(catIcon);
        catLabel.setBounds(390, 385, 70, 70);
        homePanel.add(catLabel);

        randomButton = new JButton("Random");
        randomButton.setBackground(new Color(189, 224, 254));
        randomButton.setFont(new Font("Arial", Font.BOLD, 25));
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RandomGameFrame randomGameFrame = new RandomGameFrame();
                randomGameFrame.setVisible(true);
            }
        });
        randomButton.setBounds(440, 399, 400, 50);
        homePanel.add(randomButton);

        ImageIcon frogIcon = new ImageIcon("src/Icons/frog.gif");
        Image image2 = frogIcon.getImage();
        Image scaledImage2 = image2.getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        frogIcon = new ImageIcon(scaledImage2);

        JLabel frogLabel = new JLabel(frogIcon);
        frogLabel.setBounds(390, 435, 70, 70);
        homePanel.add(frogLabel);

        createLobbyButton = new JButton("Create Lobby");
        createLobbyButton.setBackground(new Color(189, 224, 254));
        createLobbyButton.setFont(new Font("Arial", Font.BOLD, 25));
        createLobbyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                createLobbyButton.setBackground(new Color(162,210,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                createLobbyButton.setBackground(new Color(189, 224, 254));

            }
        });
        createLobbyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(lobbyPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        createLobbyButton.setBounds(440, 450, 400, 50);
        homePanel.add(createLobbyButton);

        ImageIcon bearIcon = new ImageIcon("src/Icons/bear.gif");
        Image image3 = bearIcon.getImage();
        Image scaledImage3 = image3.getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        bearIcon = new ImageIcon(scaledImage3);

        JLabel bearLabel = new JLabel(bearIcon);
        bearLabel.setBounds(390, 487, 70, 70);
        homePanel.add(bearLabel);

        joinButton = new JButton("Join Game");
        joinButton.setBackground(new Color(189, 224, 254));
        joinButton.setFont(new Font("Arial", Font.BOLD, 25));

        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                joinButton.setBackground(new Color(162,210,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                joinButton.setBackground(new Color(189, 224, 254));

            }
        });

        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnterCodeFrame enterCodeFrame = new EnterCodeFrame();
                enterCodeFrame.setVisible(true);
            }
        });
        joinButton.setBounds(440, 500, 400, 50);
        homePanel.add(joinButton);

        quitButton = new JButton("Quit Game");
        quitButton.setFont(new Font("Arial", Font.BOLD, 10));
        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                quitButton.setBackground(new Color(255, 104, 107));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                quitButton.setBackground(new Color(240, 128, 128));
            }
        });
        quitButton.setBackground(new Color(240, 128, 128));
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        quitButton.setBounds(593, 570, 100, 40);
        homePanel.add(quitButton);

        LetterCube cubeB = new LetterCube("B");
        cubeB.setBounds(200, 100, 150, 150);
        homePanel.add(cubeB);

        LetterCube cubeO = new LetterCube("O");
        cubeO.setBounds(320, 150, 150, 150);
        homePanel.add(cubeO);

        LetterCube cubeG = new LetterCube("G");
        cubeG.setBounds(440, 100, 150, 150);
        homePanel.add(cubeG);

        LetterCube cubeG2 = new LetterCube("G");
        cubeG2.setBounds(560, 150, 150, 150);
        homePanel.add(cubeG2);

        LetterCube cubeL = new LetterCube("L");
        cubeL.setBounds(680, 100, 150, 150);
        homePanel.add(cubeL);

        LetterCube cubeE = new LetterCube("E");
        cubeE.setBounds(800, 150, 150, 150);
        homePanel.add(cubeE);

        LetterCube cubeD = new LetterCube("D");
        cubeD.setBounds(920, 100, 150, 150);
        homePanel.add(cubeD);

        JLabel settingsIcon = new JLabel("");
        settingsIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(settingsPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        settingsIcon.setIcon(new ImageIcon("src/Icons/Settings.png"));
        settingsIcon.setBounds(1217, 623, 37, 38);
        homePanel.add(settingsIcon);

        layeredPane.add(homePanel, JLayeredPane.DEFAULT_LAYER);
        homePanel.add(fallingLettersPanelHome);

        lobbyPanel = new JPanel();
        lobbyPanel.setBackground(new Color(255, 204, 213));
        lobbyPanel.setLayout(null);
        lobbyPanel.setBounds(0, 0, 1280, 720);

        FallingLettersPanel fallingLettersPanelLobby = new FallingLettersPanel();
        fallingLettersPanelLobby.setOpaque(false);
        fallingLettersPanelLobby.setBounds(0, 0, 1280, 720);

        player1pic = new JLabel();
        player1pic.setBackground(Color.GRAY);
        player1pic.setBounds(59, 233, 240, 230);
        lobbyPanel.add(player1pic);
        player1pic.setLayout(null);

        player2pic = new JLabel();
        player2pic.setBackground(Color.PINK);
        player2pic.setLayout(null);
        player2pic.setBounds(358, 233, 240, 230);
        lobbyPanel.add(player2pic);

        player3pic = new JLabel();
        player3pic.setBackground(Color.YELLOW);
        player3pic.setLayout(null);
        player3pic.setBounds(657, 233, 240, 230);
        lobbyPanel.add(player3pic);

        player4pic = new JLabel();
        player4pic.setBackground(Color.MAGENTA);
        player4pic.setLayout(null);
        player4pic.setBounds(963, 233, 240, 230);
        lobbyPanel.add(player4pic);

        startButton = new JButton("Start");
        startButton.setBackground(new Color(189, 224, 254));
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(162,210,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(189, 224, 254));
            }
        });
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(gamePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        startButton.setBounds(585, 567, 105, 38);
        lobbyPanel.add(startButton);

        JButton exitLobbyButton = new JButton("Exit Lobby");
        exitLobbyButton.setBackground(new Color(206,212,218));
        exitLobbyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitLobbyButton.setBackground(new Color(222,226,230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitLobbyButton.setBackground(new Color(206,212,218));
            }
        });
        exitLobbyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(homePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        exitLobbyButton.setBounds(585, 615, 105, 38);
        lobbyPanel.add(exitLobbyButton);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 238, 153));
        usernamePanel.setBounds(482, 22, 315, 68);
        lobbyPanel.add(usernamePanel);
        usernamePanel.setLayout(null);

        JLabel usernameLabel = new JLabel("\"Username\" Lobby");
        usernameLabel.setBounds(0, 0, 315, 68);
        usernamePanel.add(usernameLabel);
        usernameLabel.setBackground(Color.YELLOW);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 30));

        JLabel cLabel = new JLabel("Code:");
        cLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        cLabel.setBounds(541, 114, 58, 45);
        lobbyPanel.add(cLabel);

        JLabel codeLabel = new JLabel("JLabel");
        codeLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        codeLabel.setBounds(600, 114, 141, 45);
        lobbyPanel.add(codeLabel);

        JLabel settingsIcon1 = new JLabel("");
        settingsIcon1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(settingsPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        settingsIcon1.setIcon(new ImageIcon("src/Icons/Settings.png"));
        settingsIcon1.setBounds(1217, 623, 37, 38);
        lobbyPanel.add(settingsIcon1);

        player1username = new JLabel("JLabel");
        player1username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player1username.setHorizontalAlignment(SwingConstants.CENTER);
        player1username.setBounds(59, 485, 240, 32);
        lobbyPanel.add(player1username);

        player2username = new JLabel("JLabel1");
        player2username.setHorizontalAlignment(SwingConstants.CENTER);
        player2username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2username.setBounds(358, 485, 240, 32);
        lobbyPanel.add(player2username);

        player3username = new JLabel("JLabel2");
        player3username.setHorizontalAlignment(SwingConstants.CENTER);
        player3username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3username.setBounds(657, 485, 240, 32);
        lobbyPanel.add(player3username);

        player4username = new JLabel("JLabel3");
        player4username.setHorizontalAlignment(SwingConstants.CENTER);
        player4username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4username.setBounds(963, 485, 240, 32);
        lobbyPanel.add(player4username);

        lobbyPanel.add(fallingLettersPanelLobby);

        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(255, 204, 213));
        gamePanel.setLayout(null);
        gamePanel.setBounds(0, 0, 1280, 720);

        timerLabel = new JLabel("00:30");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setBounds(525, 10, 150, 50);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(timerLabel);

        JPanel timerBackground = new JPanel();
        timerBackground.setBackground(new Color(144, 224, 239));
        timerBackground.setBounds(500, 5, 200, 60);
        gamePanel.add(timerBackground);
        gamePanel.setComponentZOrder(timerLabel, 0);

        player1gamePic = new JLabel("Player 1");
        player1gamePic.setBounds(24, 30, 150, 140);
        gamePanel.add(player1gamePic);
        player1gamePic.setLayout(null);

        player2gamePic = new JLabel("Player 2");
        player2gamePic.setLayout(null);
        player2gamePic.setBounds(24, 188, 150, 140);
        gamePanel.add(player2gamePic);

        player3gamePic = new JLabel("Player 3");
        player3gamePic.setLayout(null);
        player3gamePic.setBounds(24, 347, 150, 140);
        gamePanel.add(player3gamePic);

        player4gamePic = new JLabel("Player 4");
        player4gamePic.setLayout(null);
        player4gamePic.setBounds(24, 509, 150, 140);
        gamePanel.add(player4gamePic);

        player1gameUsername = new JLabel("JLabel");
        player1gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player1gameUsername.setBounds(184, 43, 123, 30);
        gamePanel.add(player1gameUsername);

        player2gameUsername = new JLabel("JLabel1");
        player2gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player2gameUsername.setBounds(184, 206, 123, 30);
        gamePanel.add(player2gameUsername);

        player3gameUsername = new JLabel("JLabel2");
        player3gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player3gameUsername.setBounds(184, 366, 123, 30);
        gamePanel.add(player3gameUsername);

        player4gameUsername = new JLabel("JLabel3");
        player4gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player4gameUsername.setBounds(184, 532, 123, 30);
        gamePanel.add(player4gameUsername);

        player1gamePoints = new JLabel("JLabel");
        player1gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player1gamePoints.setBounds(184, 84, 123, 30);
        gamePanel.add(player1gamePoints);

        player2gamePoints = new JLabel("JLabel1");
        player2gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player2gamePoints.setBounds(184, 247, 123, 30);
        gamePanel.add(player2gamePoints);

        player3gamePoints = new JLabel("JLabel2");
        player3gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player3gamePoints.setBounds(184, 407, 123, 30);
        gamePanel.add(player3gamePoints);

        player4gamePoints = new JLabel("JLabel3");
        player4gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player4gamePoints.setBounds(184, 573, 123, 30);
        gamePanel.add(player4gamePoints);

        inputTextField = new JTextField();
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        inputTextField.setBounds(472, 106, 254, 30);
        gamePanel.add(inputTextField);
        inputTextField.setColumns(10);

        JLabel settingsIcon2 = new JLabel("");
        settingsIcon2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(settingsPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        settingsIcon2.setIcon(new ImageIcon("src/Icons/Settings.png"));
        settingsIcon2.setBounds(1217, 623, 37, 38);
        gamePanel.add(settingsIcon2);

        quitButton = new JButton("Leave Game");
        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                quitButton.setBackground(new Color(255, 104, 107));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                quitButton.setBackground(new Color(240, 128, 128));
            }
        });
        quitButton.setBackground(new Color(240, 128, 128));
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(rankingPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        quitButton.setBounds(1062, 626, 100, 30);
        gamePanel.add(quitButton);

        RoundedPanel aPanel = new RoundedPanel(30);
        aPanel.setBackground(new Color(255, 238, 153));
        aPanel.setBounds(947, 43, 286, 548);
        gamePanel.add(aPanel);
        aPanel.setLayout(null);

        announcementTextpane = new JTextPane();
        announcementTextpane.setEditable(false);
        announcementTextpane.setBounds(10, 28, 266, 486);
        aPanel.add(announcementTextpane);
        announcementTextpane.setBackground(new Color(255, 238, 153));
        announcementTextpane.setFont(new Font("Tahoma", Font.PLAIN, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(244, 204, 213));
        buttonPanel.setBounds(341, 167, 537, 411);
        gamePanel.add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(0, 5, 0, 0));

        button1 = new JButton("New button");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button1.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button1.setBackground(new Color(202,240,248));
            }
        });
        button1.setBackground(new Color(202,240,248));
        buttonPanel.add(button1);

        button2 = new JButton("New button");
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button2.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button2.setBackground(new Color(202,240,248));
            }
        });
        button2.setBackground(new Color(202,240,248));
        buttonPanel.add(button2);

        button3 = new JButton("New button");
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button3.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button3.setBackground(new Color(202,240,248));
            }
        });
        button3.setBackground(new Color(202,240,248));
        buttonPanel.add(button3);

        button4 = new JButton("New button");
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button4.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button4.setBackground(new Color(202,240,248));
            }
        });
        button4.setBackground(new Color(202,240,248));
        buttonPanel.add(button4);

        button5 = new JButton("New button");
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button5.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button5.setBackground(new Color(202,240,248));
            }
        });
        button5.setBackground(new Color(202,240,248));
        buttonPanel.add(button5);

        button6 = new JButton("New button");
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button6.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button6.setBackground(new Color(202,240,248));
            }
        });
        button6.setBackground(new Color(202,240,248));
        buttonPanel.add(button6);

        button7 = new JButton("New button");
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button7.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button7.setBackground(new Color(202,240,248));
            }
        });
        button7.setBackground(new Color(202,240,248));
        buttonPanel.add(button7);

        button8 = new JButton("New button");
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button8.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button8.setBackground(new Color(202,240,248));
            }
        });
        button8.setBackground(new Color(202,240,248));
        buttonPanel.add(button8);

        button9 = new JButton("New button");
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button9.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button9.setBackground(new Color(202,240,248));
            }
        });
        button9.setBackground(new Color(202,240,248));
        buttonPanel.add(button9);

        button10 = new JButton("New button");
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button10.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button10.setBackground(new Color(202,240,248));
            }
        });
        button10.setBackground(new Color(202,240,248));
        buttonPanel.add(button10);

        button11 = new JButton("New button");
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button11.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button11.setBackground(new Color(202,240,248));
            }
        });
        button11.setBackground(new Color(202,240,248));
        buttonPanel.add(button11);

        button12 = new JButton("New button");
        button12.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button12.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button12.setBackground(new Color(202,240,248));
            }
        });
        button12.setBackground(new Color(202,240,248));
        buttonPanel.add(button12);

        button13 = new JButton("New button");
        button13.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button13.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button13.setBackground(new Color(202,240,248));
            }
        });
        button13.setBackground(new Color(202,240,248));
        buttonPanel.add(button13);

        button14 = new JButton("New button");
        button14.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button14.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button14.setBackground(new Color(202,240,248));
            }
        });
        button14.setBackground(new Color(202,240,248));
        buttonPanel.add(button14);

        button15 = new JButton("New button");
        button15.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button15.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button15.setBackground(new Color(202,240,248));
            }
        });
        button15.setBackground(new Color(202,240,248));
        buttonPanel.add(button15);

        button16 = new JButton("New button");
        button16.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button16.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button16.setBackground(new Color(202,240,248));
            }
        });
        button16.setBackground(new Color(202,240,248));
        buttonPanel.add(button16);

        button17 = new JButton("New button");
        button17.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button17.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button17.setBackground(new Color(202,240,248));
            }
        });
        button17.setBackground(new Color(202,240,248));
        buttonPanel.add(button17);

        button18 = new JButton("New button");
        button18.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button18.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button18.setBackground(new Color(202,240,248));
            }
        });
        button18.setBackground(new Color(202,240,248));
        buttonPanel.add(button18);

        button19 = new JButton("New button");
        button19.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button19.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button19.setBackground(new Color(202,240,248));
            }
        });
        button19.setBackground(new Color(202,240,248));
        buttonPanel.add(button19);

        button20 = new JButton("New button");
        button20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button20.setBackground(new Color(144,224,239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button20.setBackground(new Color(202,240,248));
            }
        });
        button20.setBackground(new Color(202,240,248));
        buttonPanel.add(button20);

        rankingPanel= new JPanel();
        rankingPanel.setBackground(new Color(255, 204, 213));
        rankingPanel.setLayout(null);
        rankingPanel.setBounds(0, 0, 1280, 720);

        FallingLettersPanel fallingLettersPanelRanking= new FallingLettersPanel();
        fallingLettersPanelRanking.setOpaque(false);
        fallingLettersPanelRanking.setBounds(0, 0, 1280, 720);

        JLabel titleLabel = new JLabel("LEADERBOARDS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(0, 50, 1280, 100);
        rankingPanel.add(titleLabel);

        int startY = titleLabel.getY() + titleLabel.getHeight() + 20;

        int centerX = 640;

        player1picRanking = new JLabel();
        player1picRanking.setBackground(Color.GRAY);
        player1picRanking.setBounds(centerX - 190, startY, 70, 70);
        rankingPanel.add(player1picRanking);
        player1picRanking.setLayout(null);
        player1picRanking.add(createBadgeLabel(0, 0, new Color(248, 168, 197), "1st"));

        player1usernameRanking = new JLabel("JLabel");
        player1usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player1usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player1usernameRanking.setBounds(centerX - 110, startY, 300, 70);
        rankingPanel.add(player1usernameRanking);

        player2picRanking = new JLabel();
        player2picRanking.setBackground(Color.GRAY);
        player2picRanking.setBounds(centerX - 190, startY + 85, 70, 70);
        rankingPanel.add(player2picRanking);
        player2picRanking.setLayout(null);
        player2picRanking.add(createBadgeLabel(0, 0, new Color(124, 222, 225), "2nd"));

        player2usernameRanking = new JLabel("JLabel1");
        player2usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player2usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2usernameRanking.setBounds(centerX - 110, startY + 85, 300, 70);
        rankingPanel.add(player2usernameRanking);

        player3picRanking = new JLabel();
        player3picRanking.setBackground(Color.GRAY);
        player3picRanking.setBounds(centerX - 190, startY + 170, 70, 70);
        rankingPanel.add(player3picRanking);
        player3picRanking.setLayout(null);
        player3picRanking.add(createBadgeLabel(0, 0, new Color(225, 217, 31), "3rd"));

        player3usernameRanking = new JLabel("JLabel2");
        player3usernameRanking .setHorizontalAlignment(SwingConstants.CENTER);
        player3usernameRanking .setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3usernameRanking .setBounds(centerX - 110, startY + 170, 300, 70);
        rankingPanel.add(player3usernameRanking );

        player4picRanking = new JLabel();
        player4picRanking.setBackground(Color.GRAY);
        player4picRanking.setBounds(centerX - 190, startY + 255, 70, 70);
        rankingPanel.add(player4picRanking);
        player4picRanking.setLayout(null);
        player4picRanking.add(createBadgeLabel(0, 0, new Color(229, 179, 131), "4th"));

        player4usernameRanking = new JLabel("JLabel3");
        player4usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player4usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4usernameRanking.setBounds(centerX - 110, startY + 255, 300, 70);
        rankingPanel.add(player4usernameRanking);

        int buttonsY = Math.max(player4usernameRanking.getY() + player4usernameRanking.getHeight(), player4picRanking.getY() + player4picRanking.getHeight()) + 50;

        JButton backToLobbyButton = new JButton("Back to Lobby");
        backToLobbyButton.setFont(new Font("Arial", Font.PLAIN, 15));
        backToLobbyButton.setBounds(centerX - 75, buttonsY, 150, 30);
        backToLobbyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(lobbyPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        rankingPanel.add(backToLobbyButton);

        JButton backToHomeButton = new JButton("Back to Home");
        backToHomeButton.setFont(new Font("Arial", Font.PLAIN, 15));
        backToHomeButton.setBounds(centerX - 75, buttonsY + 40, 150, 30);
        backToHomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(homePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        rankingPanel.add(backToHomeButton);

        rankingPanel.add(fallingLettersPanelRanking);

        settingsPanel = new JPanel();
        settingsPanel.setBackground(new Color(255, 204, 213));
        settingsPanel.setLayout(null);
        settingsPanel.setBounds(0, 0, 1280, 720);

        JLabel musicLabel = new JLabel("Music");
        musicLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 25));
        musicLabel.setBounds(776, 143, 80, 38);
        settingsPanel.add(musicLabel);

        FallingLettersPanel fallingLettersPanelSettings = new FallingLettersPanel();
        fallingLettersPanelSettings.setOpaque(false);
        fallingLettersPanelSettings.setBounds(0, 0, 1280, 720);

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 40));
        settingsLabel.setBounds(921, 40, 190, 53);
        settingsPanel.add(settingsLabel);

        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(homePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        backButton.setBounds(25, 626, 89, 30);
        settingsPanel.add(backButton);

        volumeSlider = new JSlider(0, 100);
        volumeSlider.setValue(50);
        volumeSlider.setUI(new GradientSliderUI(volumeSlider));
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider.getValue() / 100f;
                setVolume(volume);
            }
        });
        volumeSlider.setBounds(877, 143, 318, 38);
        settingsPanel.add(volumeSlider);
        startTimer(timerLabel);

        RoundedPanel htpPanel = new RoundedPanel(30);
        htpPanel.setBackground(new Color(202,233,255));
        htpPanel.setBounds(60, 40, 614, 562);
        settingsPanel.add(htpPanel);
        htpPanel.setLayout(null);

        JLabel bogLabel = new JLabel("BOGGLED");
        bogLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        bogLabel.setBounds(216, 22, 176, 37);
        htpPanel.add(bogLabel);

        JTextArea howtoplayTextArea = new JTextArea();
        howtoplayTextArea.setBackground(new Color(202,233,255));
        howtoplayTextArea.setEditable(false);
        howtoplayTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        howtoplayTextArea.setText("1. Begin by starting a game. If another player joins within 10s, the game starts.\r\n\r\n2. Once the game starts, you'll receive 20 random letters\r\n\r\n3. Use the given letters to create words.\r\n    NOTE: Each word must have atleast four letters\r\n\r\n4. Submit as many words as you can before the timer expires\r\n\r\n5. The player with the highest score wins\r\n     NOTE: If there's a tie, no winner is declared");
        howtoplayTextArea.setBounds(10, 271, 594, 215);
        htpPanel.add(howtoplayTextArea);

        JTextArea bogTextArea = new JTextArea();
        bogTextArea.setBackground(new Color(202,233,255));
        bogTextArea.setEditable(false);
        bogTextArea.setText("\"Boggled\" is a word-forming game where players compete to create unique words \r\nfrom a set of random letters provided by the server. The goal is to accumulate \r\nthe highest score by forming valid words within the given time limit.");
        bogTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        bogTextArea.setBounds(10, 76, 594, 84);
        htpPanel.add(bogTextArea);

        JLabel lblHowToPlay = new JLabel("HOW TO PLAY");
        lblHowToPlay.setFont(new Font("Tahoma", Font.PLAIN, 35));
        lblHowToPlay.setBounds(196, 212, 248, 37);
        htpPanel.add(lblHowToPlay);

        JLabel changeUsernameLabel = new JLabel("Change Username");
        changeUsernameLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        changeUsernameLabel.setBounds(776, 211, 234, 38);
        settingsPanel.add(changeUsernameLabel);

        cUsernameTextfield = new JTextField();
        cUsernameTextfield.setBounds(776, 260, 419, 30);
        settingsPanel.add(cUsernameTextfield);
        cUsernameTextfield.setColumns(10);

        JLabel lblChangePassword = new JLabel("Change Password");
        lblChangePassword.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblChangePassword.setBounds(776, 317, 234, 38);
        settingsPanel.add(lblChangePassword);

        pwField = new JPasswordField();
        pwField.setBounds(776, 366, 419, 30);
        settingsPanel.add(pwField);

        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblConfirmPassword.setBounds(776, 429, 234, 38);
        settingsPanel.add(lblConfirmPassword);

        confirmpwField = new JPasswordField();
        confirmpwField.setBounds(776, 478, 419, 30);
        settingsPanel.add(confirmpwField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        saveButton.setBounds(959, 545, 89, 30);
        settingsPanel.add(saveButton);

        settingsPanel.add(fallingLettersPanelSettings);
    }

    private void startTimer(JLabel timerLabel) {
        class Time {
            int minutes = 0;
            int seconds = 30;
        }

        Time time = new Time();

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time.seconds--;

                if (time.seconds < 0) {
                    if (time.minutes > 0) {
                        time.seconds = 59;
                        time.minutes--;
                    } else {
                        timer.stop();
                    }
                }

                String formattedTime = String.format("%02d:%02d", time.minutes, time.seconds);

                timerLabel.setText(formattedTime);
            }
        });
        timer.start();
    }

    private void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    private void playMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JLabel createBadgeLabel(int x, int y, Color color, String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBackground(color);
        label.setOpaque(true);
        label.setBounds(x, y, 30, 20);
        return label;
    }

    public JLabel getPlayer1pic() {
        return player1pic;
    }
    public JLabel getPlayer2pic() {
        return player2pic;
    }
    public JLabel getPlayer3pic() {
        return player3pic;
    }
    public JLabel getPlayer4pic() {
        return player4pic;
    }
    public JTextField getInputTextField() {
        return inputTextField;
    }
    public JPanel getHomePanel() {
        return homePanel;
    }
    public void setHomePanel(JPanel homePanel) {
        this.homePanel = homePanel;
    }
    public JPanel getLobbyPanel() {
        return lobbyPanel;
    }
    public void setLobbyPanel(JPanel lobbyPanel) {
        this.lobbyPanel = lobbyPanel;
    }
    public JPanel getGamePanel() {
        return gamePanel;
    }
    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    public JPanel getSettingsPanel() {
        return settingsPanel;
    }
    public Clip getClip() {
        return clip;
    }
    public JSlider getVolumeSlider() {
        return volumeSlider;
    }
    public Timer getTimer() {
        return timer;
    }
    public JButton getRandomButton() {
        return randomButton;
    }
    public JLabel getPlayer1username() {
        return player1username;
    }
    public JLabel getPlayer2username() {
        return player2username;
    }
    public JLabel getPlayer3username() {
        return player3username;
    }
    public JLabel getPlayer4username() {
        return player4username;
    }
    public JButton getCreateLobbyButton() {
        return createLobbyButton;
    }
    public JButton getJoinButton() {
        return joinButton;
    }
    public JLabel getPlayer1gamePic() {
        return player1gamePic;
    }
    public JLabel getPlayer2gamePic() {
        return player2gamePic;
    }
    public JLabel getPlayer3gamePic() {
        return player3gamePic;
    }
    public JLabel getPlayer4gamePic() {
        return player4gamePic;
    }
    public JButton getQuitButton() {
        return quitButton;
    }
    public JButton getButton1() {
        return button1;
    }
    public JButton getButton2() {
        return button2;
    }
    public JButton getButton3() {
        return button3;
    }
    public JButton getButton4() {
        return button4;
    }
    public JButton getButton5() {
        return button5;
    }
    public JButton getButton6() {
        return button6;
    }
    public JButton getButton7() {
        return button7;
    }
    public JButton getButton8() {
        return button8;
    }
    public JButton getButton9() {
        return button9;
    }
    public JButton getButton10() {
        return button10;
    }
    public JButton getButton11() {
        return button11;
    }
    public JButton getButton12() {
        return button12;
    }
    public JButton getButton13() {
        return button13;
    }
    public JButton getButton14() {
        return button14;
    }
    public JButton getButton15() {
        return button15;
    }
    public JButton getButton16() {
        return button16;
    }
    public JButton getButton17() {
        return button17;
    }
    public JButton getButton18() {
        return button18;
    }
    public JButton getButton19() {
        return button19;
    }
    public JButton getButton20() {
        return button20;
    }
    public JLabel getPlayer1gamePoints() {
        return player1gamePoints;
    }
    public JLabel getPlayer2gamePoints() {
        return player2gamePoints;
    }
    public JLabel getPlayer3gamePoints() {
        return player3gamePoints;
    }
    public JLabel getPlayer4gamePoints() {
        return player4gamePoints;
    }
    public JLabel getPlayer1gameUsername() {
        return player1gameUsername;
    }
    public JLabel getPlayer2gameUsername() {
        return player2gameUsername;
    }
    public JLabel getPlayer3gameUsername() {
        return player3gameUsername;
    }
    public JLabel getPlayer4gameUsername() {
        return player4gameUsername;
    }
    public JTextPane getAnnouncementTextpane() {
        return announcementTextpane;
    }
    public JTextField getcUsernameTextfield() {
        return cUsernameTextfield;
    }
    public JPasswordField getPwField() {
        return pwField;
    }
    public JPasswordField getConfirmpwField() {
        return confirmpwField;
    }
    public JButton getStartButton() {
        return startButton;
    }
    public JLabel getTimerLabel() {
        return timerLabel;
    }
    public JLabel getPlayer1picRanking() {
        return player1picRanking;
    }
    public JLabel getPlayer2picRanking() {
        return player2picRanking;
    }
    public JLabel getPlayer3picRanking() {
        return player3picRanking;
    }
    public JLabel getPlayer4picRanking() {
        return player4picRanking;
    }
    public JLabel getPlayer1usernameRanking() {
        return player1usernameRanking;
    }
    public JLabel getPlayer2usernameRanking() {
        return player2usernameRanking;
    }
    public JLabel getPlayer3usernameRanking() {
        return player3usernameRanking;
    }
    public JLabel getPlayer4usernameRanking() {
        return player4usernameRanking;
    }
}


