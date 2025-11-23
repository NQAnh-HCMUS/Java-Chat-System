import javax.swing.*;
import java.awt.*;

public class SignUp {
    public static void getInfo() {
        JFrame frame = new JFrame("Sign Up - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel confirmLabel = new JLabel("Confirm Password:");
        JPasswordField confirmField = new JPasswordField();

        form.add(userLabel);
        form.add(userField);
        form.add(emailLabel);
        form.add(emailField);
        form.add(passLabel);
        form.add(passField);
        form.add(confirmLabel);
        form.add(confirmField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton signupBtn = new JButton("Sign Up");
        JButton signinBtn = new JButton("Sign In");
        buttons.add(signinBtn);
        buttons.add(signupBtn);
        root.add(buttons, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
