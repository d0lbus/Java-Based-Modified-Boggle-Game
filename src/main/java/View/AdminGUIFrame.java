package View;

//import com.formdev.flatlaf.themes.FlatMacLightLaf;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;

public class AdminGUIFrame extends JFrame {

    private JPanel contentPane;
    private JPanel homePanel;
    private JPanel viewPanel;
    private JPanel editPanel;
    private JPanel hPanel;
    private JPanel vPanel;
    private JPanel ePanel;
    private JPanel accountPanel;
    private JPanel aPanel;
    private JTable table;
    private JTextField textField;


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
                    AdminGUIFrame frame = new AdminGUIFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AdminGUIFrame() {
        setTitle("Boggled Admin");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1002, 769);
        setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/java/Icons/logo.png");
        setIconImage(icon);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(234, 0, 752, 730);
        contentPane.add(layeredPane);
        layeredPane.setLayout(new CardLayout(0, 0));

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(206, 212, 218));
        menuPanel.setBounds(0, 0, 234, 730);
        contentPane.add(menuPanel);
        menuPanel.setLayout(null);

        homePanel = new JPanel();
        homePanel.setBackground(new Color(206, 212, 218));
        homePanel.setBounds(0, 0, 234, 47);
        menuPanel.add(homePanel);
        homePanel.setLayout(null);

        JLabel homeLabel = new JLabel("Home");
        homeLabel.setBounds(10, 0, 224, 47);
        homePanel.add(homeLabel);
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(hPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                homePanel.setBackground(new Color(233, 236, 239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                homePanel.setBackground(new Color(206, 212, 218));
            }
        });
        homeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        homeLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
        homeLabel.setIcon(new ImageIcon("src/main/java/Icons/Home.png"));

        viewPanel = new JPanel();
        viewPanel.setBackground(new Color(206, 212, 218));
        viewPanel.setBounds(0, 92, 234, 47);
        menuPanel.add(viewPanel);
        viewPanel.setLayout(null);

        JLabel viewLabel = new JLabel("View Players");
        viewLabel.setBounds(10, 0, 224, 47);
        viewPanel.add(viewLabel);
        viewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(vPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                viewPanel.setBackground(new Color(233, 236, 239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                viewPanel.setBackground(new Color(206, 212, 218));
            }
        });
        viewLabel.setIcon(new ImageIcon("src/main/java/Icons/Binoculars.png"));
        viewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        viewLabel.setFont(new Font("Tahoma", Font.BOLD, 19));

        editPanel = new JPanel();
        editPanel.setBackground(new Color(206, 212, 218));
        editPanel.setBounds(0, 139, 234, 47);
        menuPanel.add(editPanel);
        editPanel.setLayout(null);

        JLabel editLabel = new JLabel("Edit");
        editLabel.setBounds(10, 0, 224, 47);
        editPanel.add(editLabel);
        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(ePanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                editPanel.setBackground(new Color(233, 236, 239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                editPanel.setBackground(new Color(206, 212, 218));
            }
        });
        editLabel.setIcon(new ImageIcon("src/main/java/Icons/Edit.png"));
        editLabel.setHorizontalAlignment(SwingConstants.LEFT);
        editLabel.setFont(new Font("Tahoma", Font.BOLD, 19));

        ePanel = new JPanel();
        ePanel.setBackground(new Color(255, 255, 255));
        layeredPane.add(ePanel);

        accountPanel = new JPanel();
        accountPanel.setBackground(new Color(206, 212, 218));
        accountPanel.setBounds(0, 45, 234, 47);
        menuPanel.add(accountPanel);
        accountPanel.setLayout(null);

        JLabel accountLabel = new JLabel("Accounts");
        accountLabel.setBounds(10, 0, 224, 47);
        accountPanel.add(accountLabel);
        accountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(aPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                accountPanel.setBackground(new Color(233, 236, 239));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                accountPanel.setBackground(new Color(206, 212, 218));
            }
        });
        accountLabel.setIcon(new ImageIcon("src/main/java/Icons/Contacts.png"));
        accountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        accountLabel.setFont(new Font("Tahoma", Font.BOLD, 19));


        hPanel = new JPanel();
        hPanel.setBackground(Color.WHITE);
        layeredPane.add(hPanel);
        hPanel.setLayout(null);

        ePanel = new JPanel();
        ePanel.setBackground(Color.BLACK);
        layeredPane.add(ePanel);

        vPanel = new JPanel();
        vPanel.setBackground(Color.PINK);
        layeredPane.add(vPanel);

        JLabel viewPlayersLabel = new JLabel("PLAYERS");
        viewPlayersLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        viewPlayersLabel.setBounds(305, 84, 200, 20);
        vPanel.add(viewPlayersLabel);
        vPanel.setLayout(null);

        table = new JTable();
        table.setSelectionBackground(new Color(243, 50, 118));
        table.setBackground(new Color(255, 111, 139));
        table.setBorder(null);
        table.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                },
                new String[] {
                        "Player Name", ""
                }

        ));
        table.setBounds(41, 125, 668, 548);
        vPanel.add(table);



        aPanel = new JPanel();
        aPanel.setBackground(Color.WHITE);
        layeredPane.add(aPanel);
        aPanel.setLayout(null);

        table = new JTable();
        table.setSelectionBackground(new Color(104, 215, 211));
        table.setBackground(new Color(240, 240, 240));
        table.setBorder(null);
        table.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                },
                new String[] {
                        "User ID", "First Name", "Last Name", "Username", "Password"
                }

        ));
        table.setBounds(41, 125, 668, 548);
        aPanel.add(table);

        JLabel lblNewLabel = new JLabel("Search User");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel.setBounds(41, 84, 77, 16);
        aPanel.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(118, 83, 181, 20);
        aPanel.add(textField);
        textField.setColumns(10);
    }
}

