import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegenPass {
    public static void getInfo() {
        JFrame frame = new JFrame("Regenerate Password - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 120);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        
        form.add(emailLabel);
        form.add(emailField);

        root.add(form, BorderLayout.CENTER);

        // Bottom area: status on the left, button on the right
        JLabel statusLabel = new JLabel(" ");
        JPanel bottom = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton sendBtn = new JButton("Randomize");
        sendBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                statusLabel.setText("Enter your email");
                return;
            }
            // Optional: show generic success text when not empty
            statusLabel.setText("Password sent");
        });
        buttons.add(sendBtn);

        bottom.add(statusLabel, BorderLayout.WEST);
        bottom.add(buttons, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
