package View;

import View.Design.GradientSliderUI;
import View.Design.RoundedPanel;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClientGUIFrame extends JFrame {

    private JPanel contentPane;
    private JTextField inputTextField;
    private JPanel homePanel;
    private JPanel lobbyPanel;
    private JPanel gamePanel;
    private JPanel settingsPanel;
    private JPanel joinPanel;
    private Clip clip;
    private JSlider volumeSlider;

    private JButton createLobbyButton = new JButton("Create Lobby");
    private JButton joinButton = new JButton("Join");

    private int minutes = 0;
    private int seconds= 0;
    private Timer timer;

    public JButton getCreateLobbyButton() {
        return createLobbyButton;
    }

    public JButton getJoinButton() {
        return joinButton;
    }

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

        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/java/Icons/logo.png");
        setIconImage(icon);

        playMusic("music.wav");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1264, 681);
        contentPane.add(layeredPane);
        layeredPane.setLayout(new CardLayout(0, 0));

        homePanel = new JPanel();
        homePanel.setBackground(new Color(255, 204, 213));
        layeredPane.add(homePanel);
        homePanel.setLayout(null);

        JButton randomButton = new JButton("Random");
        randomButton.setBackground(new Color(189, 224, 254));
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        randomButton.setBounds(354, 359, 125, 38);
        homePanel.add(randomButton);

        createLobbyButton = new JButton("Create Lobby");
        createLobbyButton.setBackground(new Color(189, 224, 254));
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
        createLobbyButton.setBounds(547, 359, 125, 38);
        homePanel.add(createLobbyButton);

        joinButton = new JButton("Join Game");
        joinButton.setBackground(new Color(189, 224, 254));

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
                layeredPane.removeAll();
                layeredPane.add(joinPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
        });
        joinButton.setBounds(746, 359, 125, 38);
        homePanel.add(joinButton);

        joinPanel = new JPanel();
        joinPanel.setBackground(new Color(255, 204, 213));
        layeredPane.add(joinPanel);
        joinPanel.setLayout(null);

        JPanel joinGamePanel = new JPanel();
        joinPanel.add(joinGamePanel);
        joinGamePanel.setLayout(null);

        JLabel joinGameLabel = new JLabel("JOIN GAME");
        joinGameLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 50));
        joinGameLabel.setBounds(500, 65, 451, 129);
        joinPanel.add(joinGameLabel);

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
        backButton.setBounds(25, 15, 89, 30);
        joinPanel.add(backButton);



        JLabel lblNewLabel = new JLabel("BOGGLED");
        lblNewLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 99));
        lblNewLabel.setBounds(400, 191, 451, 129);
        homePanel.add(lblNewLabel);

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
        settingsIcon.setIcon(new ImageIcon("src/main/java/Icons/Settings.png"));
        settingsIcon.setBounds(1200, 15, 37, 38);
        homePanel.add(settingsIcon);

        lobbyPanel = new JPanel();
        lobbyPanel.setBackground(new Color(255, 204, 213));
        layeredPane.add(lobbyPanel);
        lobbyPanel.setLayout(null);

        JPanel player1pic = new JPanel();
        player1pic.setBackground(Color.GRAY);
        player1pic.setBounds(59, 233, 240, 230);
        lobbyPanel.add(player1pic);
        player1pic.setLayout(null);

        JPanel player2pic = new JPanel();
        player2pic.setBackground(Color.PINK);
        player2pic.setLayout(null);
        player2pic.setBounds(358, 233, 240, 230);
        lobbyPanel.add(player2pic);

        JPanel player3pic = new JPanel();
        player3pic.setBackground(Color.YELLOW);
        player3pic.setLayout(null);
        player3pic.setBounds(657, 233, 240, 230);
        lobbyPanel.add(player3pic);

        JPanel player4pic = new JPanel();
        player4pic.setBackground(Color.MAGENTA);
        player4pic.setLayout(null);
        player4pic.setBounds(963, 233, 240, 230);
        lobbyPanel.add(player4pic);

        JButton startButton = new JButton("Start");
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
        exitLobbyButton.setBackground(new Color(189, 224, 254));
        exitLobbyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitLobbyButton.setBackground(new Color(162, 210, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitLobbyButton.setBackground(new Color(189, 224, 254));
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
        settingsIcon1.setBounds(10, 22, 37, 38);
        lobbyPanel.add(settingsIcon1);

        JLabel player1username = new JLabel("JLabel");
        player1username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player1username.setHorizontalAlignment(SwingConstants.CENTER);
        player1username.setBounds(59, 485, 240, 32);
        lobbyPanel.add(player1username);

        JLabel player2username = new JLabel("JLabel1");
        player2username.setHorizontalAlignment(SwingConstants.CENTER);
        player2username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player2username.setBounds(358, 485, 240, 32);
        lobbyPanel.add(player2username);

        JLabel player3username = new JLabel("JLabel2");
        player3username.setHorizontalAlignment(SwingConstants.CENTER);
        player3username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player3username.setBounds(657, 485, 240, 32);
        lobbyPanel.add(player3username);

        JLabel player4username = new JLabel("JLabel3");
        player4username.setHorizontalAlignment(SwingConstants.CENTER);
        player4username.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 15));
        player4username.setBounds(963, 485, 240, 32);
        lobbyPanel.add(player4username);

        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(255, 204, 213));
        layeredPane.add(gamePanel);
        gamePanel.setLayout(null);

        JLabel timerLabel = new JLabel("00:30");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setBounds(525, 10, 150, 50);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(timerLabel);

        JPanel timerBackground = new JPanel();
        timerBackground.setBackground(new Color(144, 224, 239));
        timerBackground.setBounds(500, 5, 200, 60);
        gamePanel.add(timerBackground);
        gamePanel.setComponentZOrder(timerLabel, 0);


        JPanel player1gamePic = new JPanel();
        player1gamePic.setBackground(Color.GRAY);
        player1gamePic.setBounds(24, 30, 150, 140);
        gamePanel.add(player1gamePic);
        player1gamePic.setLayout(null);

        JPanel player2gamePic = new JPanel();
        player2gamePic.setBackground(Color.GRAY);
        player2gamePic.setLayout(null);
        player2gamePic.setBounds(24, 188, 150, 140);
        gamePanel.add(player2gamePic);

        JPanel player3gamePic = new JPanel();
        player3gamePic.setBackground(Color.GRAY);
        player3gamePic.setLayout(null);
        player3gamePic.setBounds(24, 347, 150, 140);
        gamePanel.add(player3gamePic);

        JPanel player4gamePic = new JPanel();
        player4gamePic.setBackground(Color.GRAY);
        player4gamePic.setLayout(null);
        player4gamePic.setBounds(24, 509, 150, 140);
        gamePanel.add(player4gamePic);

        JLabel player1gameUsername = new JLabel("JLabel");
        player1gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player1gameUsername.setBounds(184, 43, 123, 30);
        gamePanel.add(player1gameUsername);

        JLabel player2gameUsername = new JLabel("JLabel1");
        player2gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player2gameUsername.setBounds(184, 206, 123, 30);
        gamePanel.add(player2gameUsername);

        JLabel player3gameUsername = new JLabel("JLabel2");
        player3gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player3gameUsername.setBounds(184, 366, 123, 30);
        gamePanel.add(player3gameUsername);

        JLabel player4gameUsername = new JLabel("JLabel3");
        player4gameUsername.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        player4gameUsername.setBounds(184, 532, 123, 30);
        gamePanel.add(player4gameUsername);

        JLabel player1gamePoints = new JLabel("JLabel");
        player1gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player1gamePoints.setBounds(184, 84, 123, 30);
        gamePanel.add(player1gamePoints);

        JLabel player2gamePoints = new JLabel("JLabel1");
        player2gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player2gamePoints.setBounds(184, 247, 123, 30);
        gamePanel.add(player2gamePoints);

        JLabel player3gamePoints = new JLabel("JLabel2");
        player3gamePoints.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        player3gamePoints.setBounds(184, 407, 123, 30);
        gamePanel.add(player3gamePoints);

        JLabel player4gamePoints = new JLabel("JLabel3");
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

        JButton quitButton = new JButton("Quit");
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
            }
        });
        quitButton.setBounds(1062, 626, 89, 30);
        gamePanel.add(quitButton);

        RoundedPanel aPanel = new RoundedPanel(30);
        aPanel.setBackground(new Color(255, 238, 153));
        aPanel.setBounds(947, 43, 286, 548);
        gamePanel.add(aPanel);
        aPanel.setLayout(null);

        JTextPane announcementTextpane = new JTextPane();
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

        JButton button1 = new JButton("New button");
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

        JButton button2 = new JButton("New button");
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

        JButton button3 = new JButton("New button");
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

        JButton button4 = new JButton("New button");
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

        JButton button5 = new JButton("New button");
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

        JButton button6 = new JButton("New button");
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

        JButton button7 = new JButton("New button");
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

        JButton button8 = new JButton("New button");
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

        JButton button9 = new JButton("New button");
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

        JButton button10 = new JButton("New button");
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

        JButton button11 = new JButton("New button");
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

        JButton button12 = new JButton("New button");
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

        JButton button13 = new JButton("New button");
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

        JButton button14 = new JButton("New button");
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

        JButton button15 = new JButton("New button");
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

        JButton button16 = new JButton("New button");
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

        JButton button17 = new JButton("New button");
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

        JButton button18 = new JButton("New button");
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

        JButton button19 = new JButton("New button");
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

        JButton button20 = new JButton("New button");
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

        settingsPanel = new JPanel();
        layeredPane.add(settingsPanel);
        settingsPanel.setBackground(new Color(255, 204, 213));
        settingsPanel.setLayout(null);

        JLabel musicLabel = new JLabel("Music");
        musicLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 25));
        musicLabel.setBounds(471, 143, 80, 38);
        settingsPanel.add(musicLabel);

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 30));
        settingsLabel.setBounds(582, 30, 122, 53);
        settingsPanel.add(settingsLabel);

        backButton = new JButton("Back");
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
        volumeSlider.setValue(100);
        volumeSlider.setUI(new GradientSliderUI(volumeSlider)); //ito para sa design comment out if gusto nyo walang kulay
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider.getValue() / 100f;
                setVolume(volume);
            }
        });
        volumeSlider.setBounds(561, 143, 200, 38);
        settingsPanel.add(volumeSlider);
        startTimer(timerLabel);
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
}


