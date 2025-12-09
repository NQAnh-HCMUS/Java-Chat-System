import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LogIn {
    public static void getInfo() {
        JFrame frame = new JFrame("Log In - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JLabel userLabel = new JLabel("Username/Email:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        form.add(userLabel);
        form.add(userField);
        form.add(passLabel);
        form.add(passField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton signupBtn = new JButton("Sign Up");
        JButton signinBtn = new JButton("Sign In");
        
        // Get input & validate
        signinBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (checkInfo(username, password)) {
                JOptionPane.showMessageDialog(frame, "Welcome!" + username + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                // TODO: Open main application UI (e.g., UserInterface.userUI())
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });
        
        buttons.add(signinBtn);
        buttons.add(signupBtn);
        root.add(buttons, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // Check username & password existence
    private static boolean checkInfo(String username, String password) {
        Path dataSQL = Paths.get("script").resolve("data.sql");
        if (!Files.exists(dataSQL)) {
            System.out.println("Database not found.");
            return false;
        }

        try {
            String data = Files.readString(dataSQL, StandardCharsets.UTF_8);
            String user = "'" + username.replace("'", "''") + "'";
            String passwd = "'" + password.replace("'", "''") + "'";
            String[] sections = data.split(";");

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;
                
                // If first line has username & password
                String firstLine = section.toLowerCase();
                if (firstLine.contains("insert into users")) {
                    if (section.toLowerCase().contains(user.toLowerCase()) &&
                        section.toLowerCase().contains(passwd.toLowerCase())) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Failed to read database: " + ex.getMessage());
        }
        
        return false;
    }
}
