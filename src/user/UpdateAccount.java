import javax.swing.*;
import java.awt.*;

public class UpdateAccount {
    public static void getInfo() {
        JFrame frame = new JFrame("Update Account Info - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JLabel oldUN = new JLabel("Old Username:");
        JTextField oldUsernameField = new JTextField();
        oldUsernameField.setEditable(false);
        
        JLabel newUN = new JLabel("New Username:");
        JTextField newUsernameField = new JTextField();

        JLabel email = new JLabel("Email:");
        JTextField emailField = new JTextField();
        emailField.setEditable(false);

        JLabel oldPassword = new JLabel("Old Password:");
        JPasswordField oldPasswordField = new JPasswordField();
        
        JLabel newPassword = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        
        JLabel confirmPassword = new JLabel("Confirm New Password:");
        JPasswordField confirmPasswordField = new JPasswordField();

        form.add(oldUN);
        form.add(oldUsernameField);
        form.add(newUN);
        form.add(newUsernameField);
        form.add(email);
        form.add(emailField);
        form.add(oldPassword);
        form.add(oldPasswordField);
        form.add(newPassword);
        form.add(newPasswordField);
        form.add(confirmPassword);
        form.add(confirmPasswordField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton confirm = new JButton("Update");
        buttons.add(confirm);
        root.add(buttons, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
