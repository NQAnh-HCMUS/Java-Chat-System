import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SignUp {
    private static final Path DATABASE = Paths.get("script").resolve("data.sql");

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
        buttons.add(signupBtn);
        root.add(buttons, BorderLayout.SOUTH);

        
        signupBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();

            // Prevent empty inputs
            if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Match password
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check existing account
            SignUpResult result = checkExistingAccount(username, email);
            switch (result) {
                // Conflicts
                case USERNAME_EXISTS -> JOptionPane.showMessageDialog(frame, "Username already exists!", "Exists", JOptionPane.WARNING_MESSAGE);
                case EMAIL_EXISTS -> JOptionPane.showMessageDialog(frame, "Email already exists!", "Exists", JOptionPane.WARNING_MESSAGE);
                // Available
                case AVAILABLE -> {
                    if (addAccountToDatabase(username, email, pass)) {
                        JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Close sign up and return to log in
                        frame.dispose();
                        SwingUtilities.invokeLater(() -> App.main(new String[0]));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // enum to check account existence 
    private enum SignUpResult { AVAILABLE, USERNAME_EXISTS, EMAIL_EXISTS }

    // Check database for existing username/email
    private static SignUpResult checkExistingAccount(String username, String email) {
        if (!Files.exists(DATABASE)) {
            // If no database, assume available
            return SignUpResult.AVAILABLE;
        }

        try {
            String data = Files.readString(DATABASE, StandardCharsets.UTF_8);
            String line = data.toLowerCase();
            String u = "'" + username.toLowerCase().replace("'", "''") + "'";
            String e = "'" + email.toLowerCase().replace("'", "''") + "'";
            String[] sections = data.split(";");

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;
                
                // If first line has username & password
                String firstLine = section.toLowerCase();
                if (firstLine.contains("insert into users")) {
                    if (line.contains(u))
                        return SignUpResult.USERNAME_EXISTS;
                    if (line.contains(e))
                        return SignUpResult.EMAIL_EXISTS;
                }
            }
        } catch (IOException ex) {
            System.out.println("Failed to read database: " + ex.getMessage());
        }

        return SignUpResult.AVAILABLE;
    }

    // Add new account to database
    private static boolean addAccountToDatabase(String username, String email, String password) {
        try {
            String newInsert = String.format(
                "('%s', '%s', N'%s', N'Unknown', '1990-01-01', 'Other', '%s', 1, 0),%n",
                username.replace("'", "''"),
                password.replace("'", "''"),
                username.replace("'", "''"),
                email.replace("'", "''")
            );
            Files.writeString(DATABASE, newInsert, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            return true;
        } catch (IOException ex) {
            System.out.println("Failed to add account to database: " + ex.getMessage());
            return false;
        }
    }

}
