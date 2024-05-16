package TestingGrounds.View;

import TestingGrounds.View.Design.*;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
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
import java.io.IOException;

public class ClientGUIFrame extends JFrame {

    private JPanel contentPane;
    private JTextField inputTextField, cUsernameTextfield, cUsernameTextfield1, cUsernameTextfield2;
    private JPasswordField pwField, confirmpwField, pwField1, pwField2, confirmpwField1, confirmpwField2;
    private JPanel homePanel, lobbyPanel, gamePanel, rankingPanel, leaderboardsPanel, settingsPanel, sPanel, nPanel, settingsPanelLobby, settingsPanelGame;
    private JLabel player1gamePic, player2gamePic, player3gamePic, player4gamePic;
    private Clip clip;
    private JSlider volumeSlider;
    private JLabel player1pic, player2pic, player3pic, player4pic, player1Ready, player2Ready, player3Ready, player4Ready, player1picRanking, player2picRanking, player3picRanking, player4picRanking, waitingTimeLabel, usernameLabel;
    private JLabel player1username, player2username, player3username, player4username, player1usernameRanking, roundsWonLabel, player1roundsWon, player2roundsWon, player3roundsWon, player4roundsWon,
            player2usernameRanking, player3usernameRanking, player4usernameRanking, inputJLabel;
    private JButton randomButton, quitButton, leaveButton, createLobbyButton, joinButton, startButton, exitLobbyButton;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10,
            button11, button12, button13, button14, button15, button16, button17, button18, button19, button20;
    private JLabel player1gamePoints, player2gamePoints, player3gamePoints, player4gamePoints;
    private JLabel player1gameUsername, player2gameUsername, player3gameUsername, player4gameUsername, lobbyTimerLabel, timerLabel;
    private JLabel lfirstUsername, lsecondUsername, lthirdUsername, lfourthUsername, lfifthUsername, lpoint1, lpoint2, lpoint3, lpoint4, lpoint5;
    private Timer timer;
    private JTextPane announcementTextpane;
    private Font rankingFont, leaderboardsFont;
    private WindowListener windowListener;
    private JLabel player1RoundsWonInGame, player2RoundsWonInGame, player3RoundsWonInGame, player4RoundsWonInGame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

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

        ImageIcon catIcon = new ImageIcon("src/Icons/CAT.gif");
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

        ImageIcon frogIcon = new ImageIcon("src/Icons/FROG.gif");
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

        ImageIcon bearIcon = new ImageIcon("src/Icons/BEAR.gif");
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
//                EnterCodeFrame enterCodeFrame = new EnterCodeFrame();
//                enterCodeFrame.setVisible(true);
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
        quitButton.setBounds(593, 570, 100, 40);
        homePanel.add(quitButton);
        LetterCube cubeB = new LetterCube("B", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeB.setBounds(200, 100, 150, 150);
        homePanel.add(cubeB);


        LetterCube cubeO = new LetterCube("O", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeO.setBounds(320, 150, 150, 150);
        homePanel.add(cubeO);

        LetterCube cubeG = new LetterCube("G", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeG.setBounds(440, 100, 150, 150);
        homePanel.add(cubeG);

        LetterCube cubeG2 = new LetterCube("G", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeG2.setBounds(560, 150, 150, 150);
        homePanel.add(cubeG2);

        LetterCube cubeL = new LetterCube("L", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeL.setBounds(680, 100, 150, 150);
        homePanel.add(cubeL);

        LetterCube cubeE = new LetterCube("E", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
        cubeE.setBounds(800, 150, 150, 150);
        homePanel.add(cubeE);

        LetterCube cubeD = new LetterCube("D", "src/TestingGrounds/View/Design/Fonts/gomarice_bubble_head.ttf");
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
        settingsIcon.setIcon(new ImageIcon(""));
        settingsIcon.setBounds(1217, 623, 37, 38);
        homePanel.add(settingsIcon);

        JLabel leaderboardsIcon = new JLabel("leaderboard");
        leaderboardsIcon.setIcon(new ImageIcon("src/Icons/Leaderboard.png"));
        leaderboardsIcon.setBounds(1170, 623, 37, 38);
        leaderboardsIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(leaderboardsPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        homePanel.add(leaderboardsIcon);

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
        player1pic.setBounds(132, 233, 150, 140);
        lobbyPanel.add(player1pic);
        player1pic.setLayout(null);

        player2pic = new JLabel();
        player2pic.setLayout(null);
        player2pic.setBounds(399, 233, 150, 140);
        lobbyPanel.add(player2pic);

        player3pic = new JLabel();
        player3pic.setLayout(null);
        player3pic.setBounds(685, 233, 150, 140);
        lobbyPanel.add(player3pic);

        player4pic = new JLabel();
        player4pic.setLayout(null);
        player4pic.setBounds(978, 233, 150, 140);
        lobbyPanel.add(player4pic);

        startButton = new JButton("Ready");
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
                //layeredPane.removeAll();
                //layeredPane.add(gamePanel);
                //layeredPane.repaint();
                //layeredPane.revalidate();
            }
        });
        startButton.setBounds(556, 550, 105, 38);
        lobbyPanel.add(startButton);

        exitLobbyButton = new JButton("Exit Lobby");
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
        exitLobbyButton.setBounds(556, 615, 105, 38); //556, 550, 105, 38
        lobbyPanel.add(exitLobbyButton);

        JPanel lobbyTimerBackground = new JPanel();
        lobbyTimerBackground.setBackground(new Color(144, 224, 239));
        lobbyTimerBackground.setBounds(800, 22, 150, 68);
        lobbyPanel.add(lobbyTimerBackground);

        lobbyTimerLabel = new JLabel("00:00");
        lobbyTimerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        lobbyTimerLabel.setBounds(0, 0, 150, 68);
        lobbyTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyTimerBackground.setLayout(null);
        lobbyTimerBackground.add(lobbyTimerLabel);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 238, 153));
        usernamePanel.setBounds(482, 22, 315, 68);
        lobbyPanel.add(usernamePanel);
        usernamePanel.setLayout(null);

        usernameLabel = new JLabel("CODE: ");
        usernameLabel.setBounds(0, 0, 315, 68);
        usernamePanel.add(usernameLabel);
        usernameLabel.setBackground(Color.YELLOW);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 30));


        JLabel settingsIcon1 = new JLabel("");
        settingsIcon1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(settingsPanelLobby);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        settingsIcon1.setIcon(new ImageIcon(""));
        settingsIcon1.setBounds(1217, 623, 37, 38);
        lobbyPanel.add(settingsIcon1);

        player1username = new JLabel("JLabel");
        player1username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player1username.setHorizontalAlignment(SwingConstants.CENTER);
        player1username.setBounds(81, 407, 240, 32);
        lobbyPanel.add(player1username);

        waitingTimeLabel = new JLabel("Waiting time for players to join a game: 10 SECONDS");
        waitingTimeLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        waitingTimeLabel.setBounds(81, 600, 400, 32);
        lobbyPanel.add(waitingTimeLabel);

        player1Ready = new JLabel("Not Ready");
        player1Ready.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player1Ready.setHorizontalAlignment(SwingConstants.CENTER);
        player1Ready.setBounds(81, 470, 240, 32);
        lobbyPanel.add(player1Ready);


        player2username = new JLabel("JLabel1");
        player2username.setHorizontalAlignment(SwingConstants.CENTER);
        player2username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2username.setBounds(357, 407, 240, 32);
        lobbyPanel.add(player2username);

        player2Ready = new JLabel("Not Ready");
        player2Ready.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2Ready.setHorizontalAlignment(SwingConstants.CENTER);
        player2Ready.setBounds(357, 470, 240, 32);
        lobbyPanel.add(player2Ready);

        player3username = new JLabel("JLabel2");
        player3username.setHorizontalAlignment(SwingConstants.CENTER);
        player3username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3username.setBounds(640, 407, 240, 32);
        lobbyPanel.add(player3username);

        player3Ready = new JLabel("Not Ready");
        player3Ready.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3Ready.setHorizontalAlignment(SwingConstants.CENTER);
        player3Ready.setBounds(640, 470, 240, 32);
        lobbyPanel.add(player3Ready);

        player4username = new JLabel("JLabel3");
        player4username.setHorizontalAlignment(SwingConstants.CENTER);
        player4username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4username.setBounds(937, 407, 240, 32);
        lobbyPanel.add(player4username);

        player4Ready = new JLabel("Not Ready");
        player4Ready.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4Ready.setHorizontalAlignment(SwingConstants.CENTER);
        player4Ready.setBounds(937, 470, 240, 32);
        lobbyPanel.add(player4Ready);

        lobbyPanel.add(fallingLettersPanelLobby);

        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(255, 204, 213));
        gamePanel.setLayout(null);
        gamePanel.setBounds(0, 0, 1280, 720);

        SmallFallingLettersPanel smallFallingLettersPanelGame= new SmallFallingLettersPanel();
        smallFallingLettersPanelGame.setOpaque(false);
        smallFallingLettersPanelGame.setBounds(0, 0, 1280, 720);

        timerLabel = new JLabel("00:30");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setBounds(525, 10, 450, 50);
        gamePanel.add(timerLabel);

        JPanel timerBackground = new JPanel();
        timerBackground.setBackground(new Color(255, 204, 213));
        timerBackground.setBounds(500, 5, 450, 60);
        gamePanel.add(timerBackground);
        gamePanel.setComponentZOrder(timerLabel, 0);

        player1gamePic = new JLabel();
        player1gamePic.setBounds(24, 30, 150, 140);
        gamePanel.add(player1gamePic);
        player1gamePic.setLayout(null);

        player2gamePic = new JLabel();
        player2gamePic.setLayout(null);
        player2gamePic.setBounds(24, 188, 150, 140);
        gamePanel.add(player2gamePic);

        player3gamePic = new JLabel();
        player3gamePic.setLayout(null);
        player3gamePic.setBounds(24, 347, 150, 140);
        gamePanel.add(player3gamePic);

        player4gamePic = new JLabel();
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



        player1RoundsWonInGame = new JLabel("Rounds Won:");
        player1RoundsWonInGame.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player1RoundsWonInGame.setBounds(184, 125, 140, 30);
        gamePanel.add(player1RoundsWonInGame);

        player2RoundsWonInGame = new JLabel("Rounds Won:");
        player2RoundsWonInGame.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player2RoundsWonInGame.setBounds(184, 288, 140, 30);
        gamePanel.add(player2RoundsWonInGame);

        player3RoundsWonInGame = new JLabel("Rounds Won:");
        player3RoundsWonInGame.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player3RoundsWonInGame.setBounds(184, 448, 140, 30);
        gamePanel.add(player3RoundsWonInGame);

        player4RoundsWonInGame = new JLabel("Rounds Won:");
        player4RoundsWonInGame.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player4RoundsWonInGame.setBounds(184, 614, 140, 30);
        gamePanel.add(player4RoundsWonInGame);


        inputTextField = new JTextField();
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        inputTextField.setBounds(489, 114, 245, 30);
        gamePanel.add(inputTextField);
        inputTextField.setColumns(10);

        inputJLabel = new JLabel("");
        inputJLabel.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 16));
        inputJLabel.setBounds(489, 148, 245, 30);
        gamePanel.add(inputJLabel);

        JLabel settingsIcon2 = new JLabel("");
        settingsIcon2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(settingsPanelGame);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        settingsIcon2.setIcon(new ImageIcon(""));
        settingsIcon2.setBounds(1217, 623, 37, 38);
        gamePanel.add(settingsIcon2);

        leaveButton = new JButton("Leave Game");
        leaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leaveButton.setBackground(new Color(255, 104, 107));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                leaveButton.setBackground(new Color(240, 128, 128));
            }
        });
        leaveButton.setBackground(new Color(240, 128, 128));
        leaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(lobbyPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        leaveButton.setBounds(1042, 626, 118, 30);
        gamePanel.add(leaveButton);

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
        buttonPanel.setBounds(380, 174, 485, 390);
        gamePanel.add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(0, 5, 0, 0));

        button1 = new JButton("New Button");
        button1.setFont(button1.getFont().deriveFont(35f));
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
        button2.setFont(button2.getFont().deriveFont(35f));
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
        button3.setFont(button3.getFont().deriveFont(35f));
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
        button4.setFont(button4.getFont().deriveFont(35f));
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
        button5.setFont(button5.getFont().deriveFont(35f));
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
        button6.setFont(button6.getFont().deriveFont(35f));
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
        button7.setFont(button7.getFont().deriveFont(35f));
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
        button8.setFont(button8.getFont().deriveFont(35f));
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
        button9.setFont(button9.getFont().deriveFont(35f));
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
        button10.setFont(button10.getFont().deriveFont(35f));
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
        button11.setFont(button11.getFont().deriveFont(35f));
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
        button12.setFont(button12.getFont().deriveFont(35f));
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
        button13.setFont(button13.getFont().deriveFont(35f));
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
        button14.setFont(button14.getFont().deriveFont(35f));
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
        button15.setFont(button15.getFont().deriveFont(35f));
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
        button16.setFont(button16.getFont().deriveFont(35f));
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
        button17.setFont(button17.getFont().deriveFont(35f));
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
        button18.setFont(button18.getFont().deriveFont(35f));
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
        button19.setFont(button19.getFont().deriveFont(35f));
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
        button20.setFont(button20.getFont().deriveFont(35f));
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

        gamePanel.add(smallFallingLettersPanelGame);

        rankingPanel = new JPanel();
        rankingPanel.setBackground(new Color(255, 204, 213));
        rankingPanel.setLayout(null);
        rankingPanel.setBounds(0, 0, 1280, 720);

        FallingLettersPanel fallingLettersPanelRanking = new FallingLettersPanel();
        fallingLettersPanelRanking.setOpaque(false);
        fallingLettersPanelRanking.setBounds(0, 0, 1280, 720);

        JPanel rPanel = new JPanel();
        rPanel.setBackground(new Color(255, 238, 153));
        rPanel.setBounds(482, 50, 315, 68);
        rankingPanel.add(rPanel);

        JLabel titleLabel = new JLabel("RANKING", SwingConstants.CENTER);
        try {
            rankingFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/TestingGrounds/View/Design/Fonts/Wedges.ttf")).deriveFont(Font.BOLD, 50);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(rankingFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            rankingFont = new Font("Arial", Font.BOLD, 50);
        }
        rPanel.add(titleLabel);
        titleLabel.setFont(rankingFont);
        titleLabel.setForeground(new Color(7, 120, 218));
        titleLabel.setBounds(0, 50, 1280, 100);

        roundsWonLabel = new JLabel("ROUNDS WON", SwingConstants.CENTER);
        roundsWonLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        roundsWonLabel.setForeground(Color.BLACK);
        roundsWonLabel.setBounds(782, 50 + 68 + 10, 200, 30);
        rankingPanel.add(roundsWonLabel);

        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        int startY = titleLabel.getY() + titleLabel.getHeight() + 20;
        int centerX = 640;

        player1picRanking = new JLabel(catIcon);
        player1picRanking.setBackground(Color.GRAY);
        player1picRanking.setBounds(centerX - 300, startY, 70, 70);
        rankingPanel.add(player1picRanking);
        player1picRanking.setLayout(null);
        player1picRanking.add(createBadgeLabel(40, 0, "src/Icons/1s-5th/1.gif"));

        player1usernameRanking = new JLabel("JLabel");
        player1usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player1usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player1usernameRanking.setBounds(centerX - 220, startY, 300, 70);
        rankingPanel.add(player1usernameRanking);

        player1roundsWon = new JLabel("p1 rounds", SwingConstants.CENTER);
        player1roundsWon.setFont(new Font("Arial", Font.PLAIN, 15));
        player1roundsWon.setForeground(Color.BLACK);
        player1roundsWon.setBounds(732, startY, 300, 70);
        rankingPanel.add(player1roundsWon);

        player2picRanking = new JLabel(frogIcon);
        player2picRanking.setBackground(Color.GRAY);
        player2picRanking.setBounds(centerX - 300, startY + 85, 70, 70);
        rankingPanel.add(player2picRanking);
        player2picRanking.setLayout(null);
        player2picRanking.add(createBadgeLabel(40, 0, "src/Icons/1s-5th/2.gif"));

        player2usernameRanking = new JLabel("JLabel1");
        player2usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player2usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2usernameRanking.setBounds(centerX - 220, startY + 85, 300, 70);
        rankingPanel.add(player2usernameRanking);

        player2roundsWon = new JLabel("p2 rounds", SwingConstants.CENTER);
        player2roundsWon.setFont(new Font("Arial", Font.PLAIN, 15));
        player2roundsWon.setForeground(Color.BLACK);
        player2roundsWon.setBounds(732, startY + 85, 300, 70);
        rankingPanel.add(player2roundsWon);

        ImageIcon chicIcon = new ImageIcon("src/Icons/CHIC.gif");
        player3picRanking = new JLabel(chicIcon);
        player3picRanking.setBackground(Color.GRAY);
        player3picRanking.setBounds(centerX - 300, startY + 170, 70, 70);
        rankingPanel.add(player3picRanking);
        player3picRanking.setLayout(null);
        player3picRanking.add(createBadgeLabel(40, 0, "src/Icons/1s-5th/3.gif"));

        player3usernameRanking = new JLabel("JLabel2");
        player3usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player3usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3usernameRanking.setBounds(centerX - 220, startY + 170, 300, 70);
        rankingPanel.add(player3usernameRanking);

        player3roundsWon = new JLabel("p3 rounds", SwingConstants.CENTER);
        player3roundsWon.setFont(new Font("Arial", Font.PLAIN, 15));
        player3roundsWon.setForeground(Color.BLACK);
        player3roundsWon.setBounds(732, startY + 170, 300, 70);
        rankingPanel.add(player3roundsWon);

        ImageIcon owlIcon = new ImageIcon("src/Icons/OWL.gif");
        player4picRanking = new JLabel(owlIcon);
        player4picRanking.setBackground(Color.GRAY);
        player4picRanking.setBounds(centerX - 300, startY + 255, 70, 70);
        rankingPanel.add(player4picRanking);
        player4picRanking.setLayout(null);
        player4picRanking.add(createBadgeLabel(40, 0, "src/Icons/1s-5th/4.gif"));

        player4usernameRanking = new JLabel("JLabel3");
        player4usernameRanking.setHorizontalAlignment(SwingConstants.CENTER);
        player4usernameRanking.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4usernameRanking.setBounds(centerX - 220, startY + 255, 300, 70);
        rankingPanel.add(player4usernameRanking);

        player4roundsWon = new JLabel("p4 rounds", SwingConstants.CENTER);
        player4roundsWon.setFont(new Font("Arial", Font.PLAIN, 15));
        player4roundsWon.setForeground(Color.BLACK);
        player4roundsWon.setBounds(732, startY + 255, 300, 70);
        rankingPanel.add(player4roundsWon);

        int buttonsY = Math.max(player4usernameRanking.getY() + player4usernameRanking.getHeight(), player4picRanking.getY() + player4picRanking.getHeight()) + 50;

        JButton backToLobbyButton = new JButton("Back to Lobby");
        backToLobbyButton.setFont(new Font("Arial", Font.PLAIN, 15));
        backToLobbyButton.setBounds(centerX - 105, buttonsY, 150, 30);
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
        backToHomeButton.setBounds(centerX - 105, buttonsY + 40, 150, 30);
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

        leaderboardsPanel = new JPanel();
        leaderboardsPanel.setBackground(new Color(255, 204, 213));
        leaderboardsPanel.setLayout(null);
        leaderboardsPanel.setBounds(0, 0, 1280, 720);

        FallingLettersPanel fallingLettersPanelLeaderboard= new FallingLettersPanel();
        fallingLettersPanelLeaderboard.setOpaque(false);
        fallingLettersPanelLeaderboard.setBounds(0, 0, 1280, 720);

        RoundedPanel lPanel = new RoundedPanel(20);
        lPanel.setBackground(new Color(250, 229, 139));
        lPanel.setBounds(198, 51, 894, 578);
        leaderboardsPanel.add(lPanel);
        lPanel.setLayout(null);

        RoundedPanel l2Panel = new RoundedPanel(50);
        l2Panel.setBackground(new Color(153, 214, 234));
        l2Panel.setBounds(227, 0, 478, 75);
        lPanel.add(l2Panel);
        l2Panel.setLayout(null);

        JLabel lLabel = new JLabel("LEADERBOARDS", SwingConstants.CENTER);
        try {
            leaderboardsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/TestingGrounds/View/Design/Fonts/Wedges.ttf")).deriveFont(Font.BOLD, 50);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(leaderboardsFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            leaderboardsFont = new Font("Arial", Font.BOLD, 50);
        }
        l2Panel.add(lLabel);
        lLabel.setFont(rankingFont);
        lLabel.setForeground(new Color(7, 120, 218));
        lLabel.setBounds(23, 0, 427, 75);

        ImageIcon medal1Icon = new ImageIcon("src/Icons/1s-5th/1.gif");
        Image image11 = medal1Icon.getImage();
        Image scaledImage11 = image11.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        medal1Icon = new ImageIcon(scaledImage11);

        JLabel firstMedal = new JLabel(medal1Icon);
        firstMedal.setBounds(164, 105, 65, 65);
        lPanel.add(firstMedal);

        ImageIcon medal2Icon = new ImageIcon("src/Icons/1s-5th/2.gif");
        Image image22 = medal2Icon.getImage();
        Image scaledImage22 = image22.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        medal2Icon = new ImageIcon(scaledImage22);

        JLabel secondMedal = new JLabel(medal2Icon);
        secondMedal.setBounds(164, 194, 65, 65);
        lPanel.add(secondMedal);

        ImageIcon medal3Icon = new ImageIcon("src/Icons/1s-5th/3.gif");
        Image image33 = medal3Icon.getImage();
        Image scaledImage33 = image33.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        medal3Icon = new ImageIcon(scaledImage33);

        JLabel thirdMedal = new JLabel(medal3Icon);
        thirdMedal.setBounds(164, 280, 65, 65);
        lPanel.add(thirdMedal);

        ImageIcon medal4Icon = new ImageIcon("src/Icons/1s-5th/4.gif");
        Image image44 = medal4Icon.getImage();
        Image scaledImage44 = image44.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        medal4Icon = new ImageIcon(scaledImage44);

        JLabel fourthMedal = new JLabel(medal4Icon);
        fourthMedal.setBounds(164, 370, 65, 65);
        lPanel.add(fourthMedal);

        ImageIcon medal5Icon = new ImageIcon("src/Icons/1s-5th/5.gif");
        Image image55 = medal5Icon.getImage();
        Image scaledImage55 = image55.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        medal5Icon = new ImageIcon(scaledImage55);

        JLabel fifthMedal = new JLabel(medal5Icon);
        fifthMedal.setBounds(164, 467, 65, 65);
        lPanel.add(fifthMedal);

        lfirstUsername = new JLabel("top1");
        lfirstUsername.setBounds(271, 105, 396, 65);
        lPanel.add(lfirstUsername);

        lsecondUsername = new JLabel("top2");
        lsecondUsername.setBounds(271, 194, 396, 65);
        lPanel.add(lsecondUsername);

        lthirdUsername = new JLabel("top3");
        lthirdUsername.setBounds(271, 280, 396, 65);
        lPanel.add(lthirdUsername);

        lfourthUsername = new JLabel("top4");
        lfourthUsername.setBounds(271, 370, 396, 65);
        lPanel.add(lfourthUsername);

        lfifthUsername = new JLabel("top5");
        lfifthUsername.setBounds(271, 467, 396, 65);
        lPanel.add(lfifthUsername);

        lpoint1 = new JLabel("1st point");
        lpoint1.setBounds(711, 105, 65, 65);
        lPanel.add(lpoint1);

        lpoint2 = new JLabel("2nd point");
        lpoint2.setBounds(711, 194, 65, 65);
        lPanel.add(lpoint2);

        lpoint3 = new JLabel("3rd point");
        lpoint3.setBounds(711, 280, 65, 65);
        lPanel.add(lpoint3);

        lpoint4 = new JLabel("4th point");
        lpoint4.setBounds(711, 370, 65, 65);
        lPanel.add(lpoint4);

        lpoint5 = new JLabel("5th point");
        lpoint5.setBounds(711, 467, 65, 65);
        lPanel.add(lpoint5);

        JButton lBackButton = new JButton("Back");
        lBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(homePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        
        lBackButton.setBounds(20, 628, 89, 30);
        leaderboardsPanel.add(lBackButton);

        leaderboardsPanel.add(fallingLettersPanelLeaderboard);

        settingsPanel = new JPanel();
        settingsPanel.setBackground(new Color(255, 204, 213));
        settingsPanel.setLayout(null);
        settingsPanel.setBounds(0, 0, 1280, 720);

        JTextArea errorsTextArea = new JTextArea();
        errorsTextArea.setEditable(false);
        errorsTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
        errorsTextArea.setForeground(Color.RED);
        errorsTextArea.setBackground(new Color(255, 204, 213));
        errorsTextArea.setBounds(471, 148, 293, 22);
        gamePanel.add(errorsTextArea);

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





        settingsPanelLobby = new JPanel();
        settingsPanelLobby.setBackground(new Color(255, 204, 213));
        settingsPanelLobby.setLayout(null);
        settingsPanelLobby.setBounds(0, 0, 1280, 720);

        JLabel musicLabel1 = new JLabel("Music");
        musicLabel1.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 25));
        musicLabel1.setBounds(776, 143, 80, 38);
        settingsPanelLobby.add(musicLabel1);

        FallingLettersPanel fallingLettersPanelSettings1 = new FallingLettersPanel();
        fallingLettersPanelSettings1.setOpaque(false);
        fallingLettersPanelSettings1.setBounds(0, 0, 1280, 720);

        JLabel settingsLabel1 = new JLabel("Settings");
        settingsLabel1.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 40));
        settingsLabel1.setBounds(921, 40, 190, 53);
        settingsPanelLobby.add(settingsLabel1);

        JButton backButtonSettingsLobby = new JButton("Back");
        backButtonSettingsLobby.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        backButtonSettingsLobby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(lobbyPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        backButtonSettingsLobby.setBounds(25, 626, 89, 30);
        settingsPanelLobby.add(backButtonSettingsLobby);

        JSlider volumeSlider1 = new JSlider(0, 100);
        volumeSlider1.setValue(50);
        volumeSlider1.setUI(new GradientSliderUI(volumeSlider1));
        volumeSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider1.getValue() / 100f;
                setVolume(volume);
            }
        });
        volumeSlider1.setBounds(877, 143, 318, 38);
        settingsPanelLobby.add(volumeSlider1);
        startTimer(timerLabel);

        RoundedPanel htpPanel1 = new RoundedPanel(30);
        htpPanel1.setBackground(new Color(202,233,255));
        htpPanel1.setBounds(60, 40, 614, 562);
        settingsPanelLobby.add(htpPanel1);
        htpPanel1.setLayout(null);

        JLabel bogLabel1 = new JLabel("BOGGLED");
        bogLabel1.setFont(new Font("Tahoma", Font.PLAIN, 35));
        bogLabel1.setBounds(216, 22, 176, 37);
        htpPanel1.add(bogLabel1);

        JTextArea howtoplayTextArea1 = new JTextArea();
        howtoplayTextArea1.setBackground(new Color(202,233,255));
        howtoplayTextArea1.setEditable(false);
        howtoplayTextArea1.setFont(new Font("Arial", Font.PLAIN, 16));
        howtoplayTextArea1.setText("1. Begin by starting a game. If another player joins within 10s, the game starts.\r\n\r\n2. Once the game starts, you'll receive 20 random letters\r\n\r\n3. Use the given letters to create words.\r\n    NOTE: Each word must have atleast four letters\r\n\r\n4. Submit as many words as you can before the timer expires\r\n\r\n5. The player with the highest score wins\r\n     NOTE: If there's a tie, no winner is declared");
        howtoplayTextArea1.setBounds(10, 271, 594, 215);
        htpPanel1.add(howtoplayTextArea1);

        JTextArea bogTextArea1 = new JTextArea();
        bogTextArea1.setBackground(new Color(202,233,255));
        bogTextArea1.setEditable(false);
        bogTextArea1.setText("\"Boggled\" is a word-forming game where players compete to create unique words \r\nfrom a set of random letters provided by the server. The goal is to accumulate \r\nthe highest score by forming valid words within the given time limit.");
        bogTextArea1.setFont(new Font("Arial", Font.PLAIN, 16));
        bogTextArea1.setBounds(10, 76, 594, 84);
        htpPanel1.add(bogTextArea1);

        JLabel lblHowToPlay1 = new JLabel("HOW TO PLAY");
        lblHowToPlay1.setFont(new Font("Tahoma", Font.PLAIN, 35));
        lblHowToPlay1.setBounds(196, 212, 248, 37);
        htpPanel1.add(lblHowToPlay1);

        JLabel changeUsernameLabel1 = new JLabel("Change Username");
        changeUsernameLabel1.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        changeUsernameLabel1.setBounds(776, 211, 234, 38);
        settingsPanelLobby.add(changeUsernameLabel1);

        cUsernameTextfield1 = new JTextField();
        cUsernameTextfield1.setBounds(776, 260, 419, 30);
        settingsPanelLobby.add(cUsernameTextfield1);
        cUsernameTextfield1.setColumns(10);

        JLabel lblChangePassword1 = new JLabel("Change Password");
        lblChangePassword1.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblChangePassword1.setBounds(776, 317, 234, 38);
        settingsPanelLobby.add(lblChangePassword1);

        pwField1 = new JPasswordField();
        pwField1.setBounds(776, 366, 419, 30);
        settingsPanelLobby.add(pwField1);

        JLabel lblConfirmPassword1 = new JLabel("Confirm Password");
        lblConfirmPassword1.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblConfirmPassword1.setBounds(776, 429, 234, 38);
        settingsPanelLobby.add(lblConfirmPassword1);

        confirmpwField1 = new JPasswordField();
        confirmpwField1.setBounds(776, 478, 419, 30);
        settingsPanelLobby.add(confirmpwField1);

        JButton saveButton1 = new JButton("Save");
        saveButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        saveButton1.setBounds(959, 545, 89, 30);
        settingsPanelLobby.add(saveButton1);

        settingsPanelLobby.add(fallingLettersPanelSettings1);







        settingsPanelGame = new JPanel();
        settingsPanelGame.setBackground(new Color(255, 204, 213));
        settingsPanelGame.setLayout(null);
        settingsPanelGame.setBounds(0, 0, 1280, 720);

        JLabel musicLabel2 = new JLabel("Music");
        musicLabel2.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 25));
        musicLabel2.setBounds(776, 143, 80, 38);
        settingsPanelGame.add(musicLabel2);

        FallingLettersPanel fallingLettersPanelSettings2 = new FallingLettersPanel();
        fallingLettersPanelSettings2.setOpaque(false);
        fallingLettersPanelSettings2.setBounds(0, 0, 1280, 720);

        JLabel settingsLabel2 = new JLabel("Settings");
        settingsLabel2.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 40));
        settingsLabel2.setBounds(921, 40, 190, 53);
        settingsPanelLobby.add(settingsLabel2);

        JButton backButtonSettingsGame = new JButton("Back");
        backButtonSettingsGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        backButtonSettingsGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layeredPane.removeAll();
                layeredPane.add(gamePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        backButtonSettingsGame.setBounds(25, 626, 89, 30);
        settingsPanelGame.add(backButtonSettingsGame);

        JSlider volumeSlider2 = new JSlider(0, 100);
        volumeSlider2.setValue(50);
        volumeSlider2.setUI(new GradientSliderUI(volumeSlider2));
        volumeSlider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider2.getValue() / 100f;
                setVolume(volume);
            }
        });
        volumeSlider2.setBounds(877, 143, 318, 38);
        settingsPanelGame.add(volumeSlider2);
        startTimer(timerLabel);

        RoundedPanel htpPanel2 = new RoundedPanel(30);
        htpPanel2.setBackground(new Color(202,233,255));
        htpPanel2.setBounds(60, 40, 614, 562);
        settingsPanelGame.add(htpPanel2);
        htpPanel2.setLayout(null);

        JLabel bogLabel2 = new JLabel("BOGGLED");
        bogLabel2.setFont(new Font("Tahoma", Font.PLAIN, 35));
        bogLabel2.setBounds(216, 22, 176, 37);
        htpPanel2.add(bogLabel2);

        JTextArea howtoplayTextArea2 = new JTextArea();
        howtoplayTextArea2.setBackground(new Color(202,233,255));
        howtoplayTextArea2.setEditable(false);
        howtoplayTextArea2.setFont(new Font("Arial", Font.PLAIN, 16));
        howtoplayTextArea2.setText("1. Begin by starting a game. If another player joins within 10s, the game starts.\r\n\r\n2. Once the game starts, you'll receive 20 random letters\r\n\r\n3. Use the given letters to create words.\r\n    NOTE: Each word must have atleast four letters\r\n\r\n4. Submit as many words as you can before the timer expires\r\n\r\n5. The player with the highest score wins\r\n     NOTE: If there's a tie, no winner is declared");
        howtoplayTextArea2.setBounds(10, 271, 594, 215);
        htpPanel2.add(howtoplayTextArea2);

        JTextArea bogTextArea2 = new JTextArea();
        bogTextArea2.setBackground(new Color(202,233,255));
        bogTextArea2.setEditable(false);
        bogTextArea2.setText("\"Boggled\" is a word-forming game where players compete to create unique words \r\nfrom a set of random letters provided by the server. The goal is to accumulate \r\nthe highest score by forming valid words within the given time limit.");
        bogTextArea2.setFont(new Font("Arial", Font.PLAIN, 16));
        bogTextArea2.setBounds(10, 76, 594, 84);
        htpPanel2.add(bogTextArea2);

        JLabel lblHowToPlay2 = new JLabel("HOW TO PLAY");
        lblHowToPlay2.setFont(new Font("Tahoma", Font.PLAIN, 35));
        lblHowToPlay2.setBounds(196, 212, 248, 37);
        htpPanel2.add(lblHowToPlay2);

        JLabel changeUsernameLabel2 = new JLabel("Change Username");
        changeUsernameLabel2.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        changeUsernameLabel2.setBounds(776, 211, 234, 38);
        settingsPanelGame.add(changeUsernameLabel2);

        cUsernameTextfield2 = new JTextField();
        cUsernameTextfield2.setBounds(776, 260, 419, 30);
        settingsPanelGame.add(cUsernameTextfield2);
        cUsernameTextfield2.setColumns(10);

        JLabel lblChangePassword2 = new JLabel("Change Password");
        lblChangePassword2.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblChangePassword2.setBounds(776, 317, 234, 38);
        settingsPanelGame.add(lblChangePassword2);

        pwField2 = new JPasswordField();
        pwField2.setBounds(776, 366, 419, 30);
        settingsPanelGame.add(pwField2);

        JLabel lblConfirmPassword2 = new JLabel("Confirm Password");
        lblConfirmPassword2.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        lblConfirmPassword2.setBounds(776, 429, 234, 38);
        settingsPanelGame.add(lblConfirmPassword2);

        confirmpwField2 = new JPasswordField();
        confirmpwField2.setBounds(776, 478, 419, 30);
        settingsPanelGame.add(confirmpwField2);

        JButton saveButton2 = new JButton("Save");
        saveButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        saveButton2.setBounds(959, 545, 89, 30);
        settingsPanelGame.add(saveButton2);

        settingsPanelGame.add(fallingLettersPanelSettings2);













        sPanel = new JPanel();
        sPanel.setBackground(new Color(255, 204, 213));
        sPanel.setLayout(null);
        sPanel.setBounds(0, 0, 1280, 720);

        JLabel startingGameLabel = new JLabel("Starting Game");
        startingGameLabel.setFont(new Font("Tahoma", Font.PLAIN, 100));
        startingGameLabel.setBounds(309, 203, 914, 166);
        sPanel.add(startingGameLabel);

        nPanel = new JPanel();
        nPanel.setBackground(new Color(255, 204, 213));
        nPanel.setLayout(null);
        nPanel.setBounds(0, 0, 1280, 720);

        JLabel nextGameLabel = new JLabel("Next Round");
        nextGameLabel.setFont(new Font("Tahoma", Font.PLAIN, 100));
        nextGameLabel.setBounds(375, 199, 796, 166);
        nPanel.add(nextGameLabel);
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

            clip.loop(Clip.LOOP_CONTINUOUSLY);

            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    private JLabel createBadgeLabel(int x, int y, String iconPath) {
        ImageIcon icon = createScaledIcon(iconPath, 30, 20);
        JLabel label = new JLabel(icon);
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
    public WindowListener getWindowListener() {
        return windowListener;
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

    public JLabel getWaitingTimeLabel() {
        return waitingTimeLabel;
    }

    public JButton getLeaveButton() {
        return leaveButton;
    }
    public JButton getexitLobbyButton(){return exitLobbyButton;}

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
    public JPanel getRankingPanel() {
        return rankingPanel;
    }
    public JPanel getLeaderboardsPanel() {
        return leaderboardsPanel;
    }
    public JLabel getLfirstUsername() {
        return lfirstUsername;
    }
    public JLabel getLsecondUsername() {
        return lsecondUsername;
    }
    public JLabel getLthirdUsername() {
        return lthirdUsername;
    }
    public JLabel getLfourthUsername() {
        return lfourthUsername;
    }
    public JLabel getLfifthUsername() {
        return lfifthUsername;
    }
    public JLabel getLpoint1() {
        return lpoint1;
    }
    public JLabel getLpoint2() {
        return lpoint2;
    }
    public JLabel getLpoint3() {
        return lpoint3;
    }
    public JLabel getLpoint4() {
        return lpoint4;
    }
    public JLabel getLpoint5() {
        return lpoint5;
    }


    public JLabel getPlayer1Ready() {
        return player1Ready;
    }

    public void setPlayer1Ready(JLabel player1Ready) {
        this.player1Ready = player1Ready;
    }

    public JLabel getPlayer2Ready() {
        return player2Ready;
    }

    public void setPlayer2Ready(JLabel player2Ready) {
        this.player2Ready = player2Ready;
    }

    public JLabel getPlayer3Ready() {
        return player3Ready;
    }

    public void setPlayer3Ready(JLabel player3Ready) {
        this.player3Ready = player3Ready;
    }

    public JLabel getPlayer4Ready() {
        return player4Ready;
    }

    public void setPlayer4Ready(JLabel player4Ready) {
        this.player4Ready = player4Ready;
    }

    public JLabel getLobbyTimerLabel() {
        return lobbyTimerLabel;
    }

    public void setLobbyTimerLabel(JLabel lobbyTimerLabel) {
        this.lobbyTimerLabel = lobbyTimerLabel;
    }

    public void setTimerLabel(JLabel timerLabel) {
        this.timerLabel = timerLabel;
    }

    public JLabel getInputJLabel() {
        return inputJLabel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public void setUsernameLabel(JLabel usernameLabel) {
        this.usernameLabel = usernameLabel;
    }

    public JLabel getRoundsWonLabel() {
        return roundsWonLabel;
    }

    public JLabel getPlayer1roundsWon() {
        return player1roundsWon;
    }

    public JLabel getPlayer2roundsWon() {
        return player2roundsWon;
    }

    public JLabel getPlayer3roundsWon() {
        return player3roundsWon;
    }

    public JLabel getPlayer4roundsWon() {
        return player4roundsWon;
    }
    
    
    public JLabel getPlayer1RoundsWonInGame() {
        return player1RoundsWonInGame;
    }

    public JLabel getPlayer2RoundsWonInGame() {
        return player2RoundsWonInGame;
    }

    public JLabel getPlayer3RoundsWonInGame() {
        return player3RoundsWonInGame;
    }

    public JLabel getPlayer4RoundsWonInGame() {
        return player4RoundsWonInGame;
    }

    public void setPlayer1RoundsWonInGame(JLabel player1RoundsWonInGame) {
        this.player1RoundsWonInGame = player1RoundsWonInGame;
    }

    public void setPlayer2RoundsWonInGame(JLabel player2RoundsWonInGame) {
        this.player2RoundsWonInGame = player2RoundsWonInGame;
    }

    public void setPlayer3RoundsWonInGame(JLabel player3RoundsWonInGame) {
        this.player3RoundsWonInGame = player3RoundsWonInGame;
    }

    public void setPlayer4RoundsWonInGame(JLabel player4RoundsWonInGame) {
        this.player4RoundsWonInGame = player4RoundsWonInGame;
    }
    
}


