package TestingGrounds.View;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;

public class AdminGUIFrame extends JFrame {

    private JPanel contentPane, homePanel, editPanel, hPanel, ePanel, accountPanel, aPanel, lobbiesPanel;
    private JTable table, table1;
    private JTextField textField;
    private JButton editTimerButton, deleteTimerButton, viewTimerButton, viewPlayersButton, editPlayersButton, editRoundsButton, editNumOfRoundsButton;
    private JButton viewUserButton, banUserButton, unbanUserButton;
    private JLabel lobbiesResult, rwonResults, rplayersResult;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
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

        Image icon = Toolkit.getDefaultToolkit().getImage("src/Icons/logo.png");
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
        
        editPanel = new JPanel();
        editPanel.setBackground(new Color(206, 212, 218));
        editPanel.setBounds(0, 0, 234, 47);;
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
        editLabel.setIcon(new ImageIcon("src/Icons/Edit.png"));
        editLabel.setHorizontalAlignment(SwingConstants.LEFT);
        editLabel.setFont(new Font("Tahoma", Font.BOLD, 19));

        ePanel = new JPanel();
        ePanel.setBackground(new Color(255, 255, 255));
        layeredPane.add(ePanel);
        
        ePanel = new JPanel();
        ePanel.setBackground(Color.WHITE);
        ePanel.setLayout(new BorderLayout());
        layeredPane.add(ePanel);

        JPanel buttonWrapperPanel = new JPanel(new BorderLayout());
        buttonWrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonWrapperPanel.setBackground(Color.WHITE);
        ePanel.add(buttonWrapperPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonWrapperPanel.add(buttonPanel, BorderLayout.CENTER);

        editTimerButton = new JButton("Edit Waiting Time");
        editTimerButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        buttonPanel.add(editTimerButton);

        editRoundsButton = new JButton("Edit Round Time");
        editRoundsButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        buttonPanel.add(editRoundsButton);

        editNumOfRoundsButton = new JButton("Edit No. of Rounds");
        editNumOfRoundsButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        buttonPanel.add(editNumOfRoundsButton);

        viewPlayersButton = new JButton("View Players");
        viewPlayersButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        buttonPanel.add(viewPlayersButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Lobby No.", "No. of Players", "WAITING TIME", "Timer", "Rounds", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        ePanel.add(tablePanel, BorderLayout.CENTER);
    }

    public JButton getEditTimerButton() {
        return editTimerButton;
    }

    public void setEditTimerButton(JButton editTimerButton) {
        this.editTimerButton = editTimerButton;
    }

    public JButton getEditPlayersButton() {
        return editPlayersButton;
    }

    public void setEditPlayersButton(JButton editPlayersButton) {
        this.editPlayersButton = editPlayersButton;
    }

    public JButton getDeleteTimerButton() {
        return deleteTimerButton;
    }

    public void setDeleteTimerButton(JButton deleteTimerButton) {
        this.deleteTimerButton = deleteTimerButton;
    }

    public JButton getViewTimerButton() {
        return viewTimerButton;
    }

    public void setViewTimerButton(JButton viewTimerButton) {
        this.viewTimerButton = viewTimerButton;
    }

    public JButton getEditRoundsButton() {
        return editRoundsButton;
    }

    public void setEditRoundsButton(JButton editRoundsButton) {
        this.editRoundsButton = editRoundsButton;
    }

    public JButton getViewPlayersButton() {
        return viewPlayersButton;
    }

    public void setViewPlayersButton(JButton viewPlayersButton) {
        this.viewPlayersButton = viewPlayersButton;
    }

    public JButton getViewUserButton() {
        return viewUserButton;
    }

    public void setViewUserButton(JButton viewUserButton) {
        this.viewUserButton = viewUserButton;
    }

    public JButton getBanUserButton() {
        return banUserButton;
    }

    public void setBanUserButton(JButton banUserButton) {
        this.banUserButton = banUserButton;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JTable getTable1() {
        return table1;
    }

    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    public JButton getEditNumOfRoundsButton() {
        return editNumOfRoundsButton;
    }

    public JButton getNumOfRoundsButton() {
        return getNumOfRoundsButton();
    }

    public JButton getNumOfPlayersButton() {
        return getNumOfPlayersButton();
    }

    public JButton getNumOfUsersButton() {
        return getNumOfUsersButton();
    }

    public JButton getUnbanUserButton() {
        return unbanUserButton;
    }

    public JLabel getLobbiesResult() {
        return lobbiesResult;
    }

    public JLabel getRwonResults() {
        return rwonResults;
    }

    public JLabel getRplayersResult() {
        return rplayersResult;
    }
}