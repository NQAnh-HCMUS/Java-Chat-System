// This is main.

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Java Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
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
        JButton loginBtn = new JButton("Log In");
        
        // Get input & validate
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (checkInfo(username, password)) {
                // Record login to log
                recordLogin(username);
                
                // Route to admin or user UI
                if (username.equalsIgnoreCase("admin")) {
                    SwingUtilities.invokeLater(AdminInterface::AdminUI);
                } else {
                    SwingUtilities.invokeLater(UserInterface::UserUI);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });
        
        buttons.add(loginBtn);
        buttons.add(signupBtn);
        root.add(buttons, BorderLayout.SOUTH);

        // Press Enter in userField -> moves to passField
        userField.addActionListener(e -> passField.requestFocus());
        // Press Enter in passField == Login button
        passField.addActionListener(e -> loginBtn.doClick());

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

    // Record login to log
    private static void recordLogin(String username) {
        try {
            Path logFile = Paths.get("script").resolve("login.log");
            if (logFile.getParent() != null) {
                Files.createDirectories(logFile.getParent());
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String ipAddr = "IPADDRESS";
            try {
                ipAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (IOException ex) {
                System.out.println("Could not retrieve IP address: " + ex.getMessage());
            }
            
            String logEntry = System.lineSeparator() + timestamp + " " + username + " " + ipAddr;
            Files.writeString(logFile, logEntry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Failed to record login: " + ex.getMessage());
        }
    }
}
