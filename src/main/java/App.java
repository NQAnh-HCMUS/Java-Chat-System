import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
        JFrame frame = new JFrame("Login System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("LOGIN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 30));
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 30));
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Left buttons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftPanel.setBackground(Color.WHITE);
        JButton regenBtn = new JButton("Forgot Password");
        regenBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        regenBtn.setForeground(new Color(0, 102, 204));
        regenBtn.setContentAreaFilled(false);
        regenBtn.setBorderPainted(false);
        regenBtn.setFocusPainted(false);
        regenBtn.addActionListener(e -> SwingUtilities.invokeLater(RegenPass::getInfo));
        leftPanel.add(regenBtn);

        // Right buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);
        
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signupBtn.setBackground(new Color(240, 240, 240));
        signupBtn.setFocusPainted(false);
        signupBtn.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setBackground(new Color(0, 102, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (checkInfo(username, password)) {
                recordLogin(username);
                frame.dispose();

                if (username.equalsIgnoreCase("admin")) {
                    SwingUtilities.invokeLater(AdminInterface::AdminUI);
                } else {
                    SwingUtilities.invokeLater(UserInterface::UserUI);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                recordShutdown(username);
            }));
        });

        rightPanel.add(signupBtn);
        rightPanel.add(loginBtn);

        buttonPanel.add(leftPanel, BorderLayout.WEST);
        buttonPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Keyboard shortcuts
        userField.addActionListener(e -> passField.requestFocus());
        passField.addActionListener(e -> loginBtn.doClick());

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

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

                String firstLine = section.toLowerCase();
                if (firstLine.contains("insert into users")) {
                    if (section.toLowerCase().contains(user.toLowerCase()) &&
                        section.toLowerCase().contains(passwd.toLowerCase())) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Database read error: " + ex.getMessage());
        }

        return false;
    }

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
                System.out.println("Cannot get IP address: " + ex.getMessage());
            }

            String logEntry = System.lineSeparator() + timestamp + " LOGIN " + username + " " + ipAddress;
            Files.writeString(LOG, logEntry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Login log write error: " + ex.getMessage());
        }
    }

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
                System.out.println("Cannot get IP address: " + ex.getMessage());
            }

            String logEntry = System.lineSeparator() + timestamp + " LOGOUT " + username + " " + ipAddress;
            Files.writeString(LOG, logEntry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Logout log write error: " + ex.getMessage());
        }
    }
}