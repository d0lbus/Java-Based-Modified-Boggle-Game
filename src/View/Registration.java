package View;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Registration extends JFrame {

    private JPanel contentPane;
    private JTextField usernameLoginTextfield;
    private JPasswordField loginPasswordField;
    private JTextField firstNameTextfield;
    private JTextField lastNameTextfield;
    private JTextField usernameRegisterTextfield;
    private JPasswordField confirmPasswordField;
    private JPasswordField signUpPasswordField;
    private JButton doneButton;
    private JButton signInButton;

    public static void main(String[] args) throws Exception {

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Registration frame = new Registration();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Registration() {
        setTitle("Register");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 583);
        setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/Icons/logo.png");
        setIconImage(icon);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 204, 213));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(411, 0, 423, 544);
        contentPane.add(layeredPane);
        layeredPane.setLayout(new CardLayout(0, 0));

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        layeredPane.add(loginPanel);
        loginPanel.setLayout(null);

        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(Color.WHITE);
        layeredPane.add(signupPanel);
        signupPanel.setLayout(null);

        JLabel loginLabel = new JLabel("Log In");
        loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        loginLabel.setBounds(164, 133, 111, 43);
        loginPanel.add(loginLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        usernameLabel.setBounds(123, 205, 88, 14);
        loginPanel.add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        passwordLabel.setBounds(123, 275, 88, 14);
        loginPanel.add(passwordLabel);

        usernameLoginTextfield = new JTextField();
        usernameLoginTextfield.setBounds(122, 230, 188, 27);
        loginPanel.add(usernameLoginTextfield);
        usernameLoginTextfield.setColumns(10);

        loginPasswordField = new JPasswordField();
        loginPasswordField.setBounds(123, 300, 187, 27);
        loginPanel.add(loginPasswordField);

        signInButton = new JButton("Sign in");
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signInButton.setBackground(new Color(206,212,218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signInButton.setBackground(new Color(233, 236, 239));
            }
        });
        signInButton.setBounds(175, 384, 89, 27);
        loginPanel.add(signInButton);

        JLabel signUpLabel = new JLabel("Don't have an account. Sign up");
        signUpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(signupPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }
            public void mouseEntered(MouseEvent e){
                signUpLabel.setForeground(Color.BLUE);
            }
            public void mouseExited(MouseEvent e){
                signUpLabel.setForeground(Color.BLACK);
            }
        });
        signUpLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        signUpLabel.setBounds(123, 434, 188, 23);
        loginPanel.add(signUpLabel);

        JCheckBox showPasswordCheckbox = new JCheckBox("Show password");
        showPasswordCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loginPasswordField.setEchoChar((char)0);
                }else {
                    loginPasswordField.setEchoChar('\u2022');
                }
            }
        });
        showPasswordCheckbox.setBackground(Color.WHITE);
        showPasswordCheckbox.setBounds(120, 333, 190, 23);
        loginPanel.add(showPasswordCheckbox);

        JLabel sLabel = new JLabel("Sign up");
        sLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        sLabel.setBounds(162, 30, 124, 47);
        signupPanel.add(sLabel);

        firstNameTextfield = new JTextField();
        firstNameTextfield.setBounds(121, 126, 187, 27);
        signupPanel.add(firstNameTextfield);
        firstNameTextfield.setColumns(10);

        lastNameTextfield = new JTextField();
        lastNameTextfield.setColumns(10);
        lastNameTextfield.setBounds(121, 197, 187, 27);
        signupPanel.add(lastNameTextfield);

        usernameRegisterTextfield = new JTextField();
        usernameRegisterTextfield.setColumns(10);
        usernameRegisterTextfield.setBounds(121, 267, 187, 27);
        signupPanel.add(usernameRegisterTextfield);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(121, 416, 187, 27);
        signupPanel.add(confirmPasswordField);

        signUpPasswordField = new JPasswordField();
        signUpPasswordField.setBounds(121, 342, 187, 27);
        signupPanel.add(signUpPasswordField);

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        firstNameLabel.setBounds(121, 101, 70, 14);
        signupPanel.add(firstNameLabel);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lastNameLabel.setBounds(121, 172, 70, 14);
        signupPanel.add(lastNameLabel);

        JLabel UsernameLabel = new JLabel("Username");
        UsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        UsernameLabel.setBounds(121, 242, 70, 14);
        signupPanel.add(UsernameLabel);

        JLabel signUpPasswordLabel = new JLabel("Password");
        signUpPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        signUpPasswordLabel.setBounds(121, 317, 70, 14);
        signupPanel.add(signUpPasswordLabel);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        confirmPasswordLabel.setBounds(121, 391, 115, 14);
        signupPanel.add(confirmPasswordLabel);

        doneButton = new JButton("Done");
        doneButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                doneButton.setBackground(new Color(206,212,218));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                doneButton.setBackground(new Color(233, 236, 239));
            }
        });
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        doneButton.setBounds(170, 466, 89, 23);
        signupPanel.add(doneButton);

        JLabel backToLoginLabel = new JLabel("Back to log in");
        backToLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layeredPane.removeAll();
                layeredPane.add(loginPanel);
                layeredPane.repaint();
                layeredPane.revalidate();
            }

            public void mouseEntered(MouseEvent e){
                backToLoginLabel.setForeground(Color.BLUE);
            }
            public void mouseExited(MouseEvent e){
                backToLoginLabel.setForeground(Color.BLACK);
            }
        });
        backToLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backToLoginLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        backToLoginLabel.setBounds(162, 499, 101, 23);
        signupPanel.add(backToLoginLabel);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("src/Icons/logoRegistration.png"));
        lblNewLabel.setBounds(0, 199, 401, 102);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("© Team Quantum Tech 2024");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_1.setBounds(109, 493, 209, 22);
        contentPane.add(lblNewLabel_1);
    }
}


