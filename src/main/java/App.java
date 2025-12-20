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
    private static final Path DATABASE = Paths.get("script").resolve("data.sql");
    private static final Path LOG = Paths.get("script").resolve("login.log");
    private static final DateTimeFormatter TIMEFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGui);
    }


    private static void createAndShowGui() {
        JFrame frame = new JFrame("egwrifuyasbfhkjhasdbfasdkhfjbgsahjfbd");
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

        // Bottom area: left (Regen Pass), right (Login & Sign Up)
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton regenBtn = new JButton("Regen Pass");
        regenBtn.addActionListener(e -> SwingUtilities.invokeLater(RegenPass::getInfo));
        leftButtons.add(regenBtn);
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));
        
        
        // Get input & validate
        JButton loginBtn = new JButton("Log In");
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
                
                // Close screen
                frame.dispose();
                
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

            // Record shutdown to log
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                recordShutdown(username);
            }));
        });
        


        rightButtons.add(loginBtn);
        rightButtons.add(signupBtn);
        bottom.add(leftButtons, BorderLayout.WEST);
        bottom.add(rightButtons, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        // Press Enter in userField -> moves to passField
        userField.addActionListener(e -> passField.requestFocus());
        // Press Enter in passField == Login button
        passField.addActionListener(e -> loginBtn.doClick());

        frame.setContentPane(root);
        frame.setVisible(true);        
    }

    // Check username & password existence
    private static boolean checkInfo(String username, String password) {
        if (!Files.exists(DATABASE)) {
            System.out.println("Database not found.");
            return false;
        }

        try {
            String data = Files.readString(DATABASE, StandardCharsets.UTF_8);
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
            if (LOG.getParent() != null) {
                Files.createDirectories(LOG.getParent());
            }
            
            String timestamp = LocalDateTime.now().format(TIMEFORMAT);
            String ipAddress = "unknown";
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (IOException ex) {
                System.out.println("Could not retrieve IP address: " + ex.getMessage());
            }
            
            String logEntry = System.lineSeparator() + timestamp + " LOGIN " + username + " " + ipAddress;
            Files.writeString(LOG, logEntry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Failed to record login: " + ex.getMessage());
        }
    }

    // Record shutdown to log
    private static void recordShutdown(String username) {
        try {
            if (LOG.getParent() != null) {
                Files.createDirectories(LOG.getParent());
            }
            
            String timestamp = LocalDateTime.now().format(TIMEFORMAT);
            String ipAddress = "unknown";
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (IOException ex) {
                System.out.println("Could not retrieve IP address: " + ex.getMessage());
            }
            
            String logEntry = System.lineSeparator() + timestamp + " LOGOUT " + username + " " + ipAddress;
            Files.writeString(LOG, logEntry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Failed to record shutdown: " + ex.getMessage());
        }
    }
}
