package View;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Font;
import javax.swing.*;

public class EnterCodeFrame extends javax.swing.JFrame {

    public EnterCodeFrame() {
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new java.awt.Color(255, 240, 233)); // Set background color
        setUIFont(new javax.swing.plaf.FontUIResource("Franklin Gothic Demi", Font.PLAIN, 14)); // Set font
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private javax.swing.JButton joinButton;
    private javax.swing.JLabel enterCodeLabel;
    private javax.swing.JTextField codeTextField;


    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EnterCodeFrame().setVisible(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        enterCodeLabel = new javax.swing.JLabel();
        codeTextField = new javax.swing.JTextField();
        joinButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enter Code");
        setResizable(false);

        enterCodeLabel.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 18)); // NOI18N
        enterCodeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enterCodeLabel.setText("ENTER CODE");

        joinButton.setText("Join");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(enterCodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(codeTextField)
                                        .addComponent(joinButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(enterCodeLabel)
                                .addGap(18, 18, 18)
                                .addComponent(codeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(joinButton)
                                .addGap(50, 50, 50))
        );

        pack();
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }


    public JTextField getCodeTextField() {
        return codeTextField;
    }
    public JButton getJoinButton() {
        return joinButton;
    }
}
