import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LogInLog {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color GREEN = new Color(0x2ECC71);
    private static final Color ORANGE = new Color(0xF39C12);
    private static final Color RED = new Color(0xE74C3C);
    private static final Color DARK_GRAY = new Color(0x2C3E50);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LOG_FONT = new Font("Consolas", Font.PLAIN, 12);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 13);
    
    // Data structures
    private static List<LoginRecord> loginRecords = new ArrayList<>();
    private static JTextArea logArea;
    private static JComboBox<String> sortComboBox;
    private static JComboBox<String> filterComboBox;
    private static JTextField searchField;
    private static JLabel statsLabel;
    private static DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    // Sorting and filtering options
    private static final String SORT_NEWEST = "Newest First";
    private static final String SORT_OLDEST = "Oldest First";
    private static final String FILTER_ALL = "All Records";
    private static final String FILTER_SUCCESS = "Success Only";
    private static final String FILTER_FAILED = "Failed Only";
    private static final String FILTER_TODAY = "Today Only";
    
    // Statistics
    private static int totalLogins = 0;
    private static int successfulLogins = 0;
    private static int failedLogins = 0;
    private static int todayLogins = 0;

    public static void List() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Initialize sample data
            initializeSampleData();
            
            JFrame frame = new JFrame("Login Activity Log - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

            // Header Panel
            JPanel headerPanel = createHeaderPanel();
            
            // Main Content Panel
            JPanel mainPanel = createMainPanel();
            
            // Footer Panel
            JPanel footerPanel = createFooterPanel();

            // Add panels to frame
            frame.add(headerPanel, BorderLayout.NORTH);
            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(footerPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
            
            // Refresh display
            refreshLogDisplay();
        });
    }

    private static void initializeSampleData() {
        // Clear existing data
        loginRecords.clear();
        
        // Create sample login records with varied dates and times
        LocalDateTime baseTime = LocalDateTime.now();
        Random random = new Random();
        
        // Generate records for the last 30 days
        for (int i = 0; i < 100; i++) {
            int daysAgo = random.nextInt(30);
            int hoursAgo = random.nextInt(24);
            int minutesAgo = random.nextInt(60);
            
            LocalDateTime timestamp = baseTime
                .minusDays(daysAgo)
                .minusHours(hoursAgo)
                .minusMinutes(minutesAgo);
            
            // Random user from list
            String[] users = {
                "john_doe", "jane_smith", "mike_wilson", "sarah_johnson", 
                "david_brown", "emily_davis", "admin", "guest_user",
                "alice_walker", "bob_miller", "charlie_nguyen", "diana_clark"
            };
            String username = users[random.nextInt(users.length)];
            
            // Random IP
            String ip = String.format("192.168.%d.%d", 
                random.nextInt(256), random.nextInt(256));
            
            // Random status (80% success, 20% failed)
            boolean success = random.nextDouble() < 0.8;
            String status = success ? "Success" : "Failed";
            
            // Optional failure reason
            String details = "";
            if (!success) {
                String[] reasons = {
                    "Wrong password", "Account locked", "Invalid username",
                    "Too many attempts", "Session expired", "IP blocked"
                };
                details = " - " + reasons[random.nextInt(reasons.length)];
            }
            
            loginRecords.add(new LoginRecord(timestamp, username, ip, status, details));
        }
        
        // Update statistics
        updateStatistics();
    }

    private static void updateStatistics() {
        totalLogins = loginRecords.size();
        successfulLogins = (int) loginRecords.stream()
            .filter(r -> r.status.equals("Success"))
            .count();
        failedLogins = totalLogins - successfulLogins;
        
        LocalDateTime today = LocalDateTime.now();
        todayLogins = (int) loginRecords.stream()
            .filter(r -> r.timestamp.toLocalDate().equals(today.toLocalDate()))
            .count();
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        headerPanel.setPreferredSize(new Dimension(100, 65));
        
        // Title
        JLabel titleLabel = new JLabel("Login Activity Log");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(WHITE);
        
        // Stats panel
        JPanel statsPanel = createStatsPanel();
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private static JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(DARK_BLUE);
        statsPanel.setOpaque(false);

        JPanel totalPanel = createStatItem("📊", "Total:", String.valueOf(totalLogins));
        JPanel successPanel = createStatItem("✅", "Success:", String.valueOf(successfulLogins));
        JPanel failedPanel = createStatItem("❌", "Failed:", String.valueOf(failedLogins));
        JPanel todayPanel = createStatItem("📅", "Today:", String.valueOf(todayLogins));

        statsPanel.add(totalPanel);
        statsPanel.add(successPanel);
        statsPanel.add(failedPanel);
        statsPanel.add(todayPanel);

        return statsPanel;
    }

    private static JPanel createStatItem(String icon, String label, String value) {
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
        statPanel.setBackground(DARK_BLUE);
        statPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        iconLabel.setForeground(LIGHT_TEAL);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(label);
        descLabel.setFont(LABEL_FONT);
        descLabel.setForeground(LIGHT_TEAL);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statPanel.add(iconLabel);
        statPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        statPanel.add(valueLabel);
        statPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        statPanel.add(descLabel);

        return statPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter and sort panel
        JPanel filterSortPanel = createFilterSortPanel();
        
        // Log content panel
        JPanel logPanel = createLogPanel();
        
        // Control panel
        JPanel controlPanel = createControlPanel();

        mainPanel.add(filterSortPanel, BorderLayout.NORTH);
        mainPanel.add(logPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }

    private static JPanel createFilterSortPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Sort label and combo
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(HEADER_FONT);
        sortLabel.setForeground(DARK_BLUE);

        String[] sortOptions = {SORT_NEWEST, SORT_OLDEST};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setFont(LABEL_FONT);
        sortComboBox.setBackground(WHITE);
        sortComboBox.setPreferredSize(new Dimension(120, 30));
        sortComboBox.addActionListener(e -> refreshLogDisplay());

        // Filter label and combo
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(HEADER_FONT);
        filterLabel.setForeground(DARK_BLUE);

        String[] filterOptions = {FILTER_ALL, FILTER_SUCCESS, FILTER_FAILED, FILTER_TODAY};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(LABEL_FONT);
        filterComboBox.setBackground(WHITE);
        filterComboBox.setPreferredSize(new Dimension(120, 30));
        filterComboBox.addActionListener(e -> refreshLogDisplay());

        panel.add(sortLabel);
        panel.add(sortComboBox);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(filterLabel);
        panel.add(filterComboBox);

        return panel;
    }

    private static JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(WHITE);
        logPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);
        
        JLabel logTitle = new JLabel("Login Activity Records");
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logTitle.setForeground(DARK_BLUE);
        
        statsLabel = new JLabel();
        statsLabel.setFont(LABEL_FONT);
        statsLabel.setForeground(BLUE);
        
        titlePanel.add(logTitle, BorderLayout.WEST);
        titlePanel.add(statsLabel, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Log text area
        logArea = new JTextArea();
        logArea.setFont(LOG_FONT);
        logArea.setBackground(DARK_GRAY);
        logArea.setForeground(new Color(0xECF0F1));
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setCaretPosition(0); // Start at top

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x34495E), 1));
        scrollPane.getViewport().setBackground(DARK_GRAY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        logPanel.add(titlePanel, BorderLayout.NORTH);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        return logPanel;
    }

    private static void refreshLogDisplay() {
        // Get current filter and sort options
        String sortOption = (String) sortComboBox.getSelectedItem();
        String filterOption = (String) filterComboBox.getSelectedItem();
        String searchText = searchField != null ? searchField.getText().toLowerCase() : "";
        
        // Filter and sort records
        List<LoginRecord> filteredRecords = loginRecords.stream()
            .filter(record -> {
                // Apply filter
                switch (filterOption) {
                    case FILTER_SUCCESS:
                        if (!record.status.equals("Success")) return false;
                        break;
                    case FILTER_FAILED:
                        if (!record.status.equals("Failed")) return false;
                        break;
                    case FILTER_TODAY:
                        if (!record.timestamp.toLocalDate().equals(LocalDateTime.now().toLocalDate())) 
                            return false;
                        break;
                }
                
                // Apply search
                if (!searchText.isEmpty()) {
                    return record.username.toLowerCase().contains(searchText) ||
                           record.ip.contains(searchText) ||
                           record.status.toLowerCase().contains(searchText) ||
                           record.details.toLowerCase().contains(searchText);
                }
                
                return true;
            })
            .sorted((r1, r2) -> {
                // Apply sort
                if (sortOption.equals(SORT_NEWEST)) {
                    return r2.timestamp.compareTo(r1.timestamp); // Newest first
                } else {
                    return r1.timestamp.compareTo(r2.timestamp); // Oldest first
                }
            })
            .collect(java.util.stream.Collectors.toList());
        
        // Build display text
        StringBuilder logContent = new StringBuilder();
        logContent.append("=== LOGIN ACTIVITY LOG ===\n");
        logContent.append("Sort: ").append(sortOption);
        logContent.append(" | Filter: ").append(filterOption);
        if (!searchText.isEmpty()) {
            logContent.append(" | Search: ").append(searchText);
        }
        logContent.append("\n");
        logContent.append("=".repeat(80)).append("\n\n");
        
        // Color coding for status
        for (LoginRecord record : filteredRecords) {
            String timestamp = record.timestamp.format(displayFormatter);
            String statusColor = record.status.equals("Success") ? "✓" : "✗";
            String statusDisplay = record.status.equals("Success") ? 
                "✓ Success" : "✗ Failed";
            
            logContent.append(String.format("%-20s | %-15s | %-15s | %-10s%s\n",
                timestamp, record.username, record.ip, statusDisplay, record.details));
        }
        
        // Update text area
        logArea.setText(logContent.toString());
        logArea.setCaretPosition(0); // Scroll to top
        
        // Update statistics label
        statsLabel.setText(String.format("Showing %d of %d records", 
            filteredRecords.size(), loginRecords.size()));
    }

    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(LIGHT_GRAY);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Search field
        searchField = new JTextField();
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Search by username, IP address, status...");
        searchField.addActionListener(e -> refreshLogDisplay());

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(LIGHT_GRAY);
        
        JButton searchBtn = createModernButton("Search", TEAL);
        JButton reloadBtn = createModernButton("Refresh", BLUE);
        JButton exportBtn = createModernButton("Export Log", GREEN);
        JButton clearBtn = createModernButton("Clear All", RED);
        JButton addTestBtn = createModernButton("Add Test Data", ORANGE);

        // Add action listeners
        searchBtn.addActionListener(e -> refreshLogDisplay());
        reloadBtn.addActionListener(e -> {
            initializeSampleData();
            refreshLogDisplay();
            JOptionPane.showMessageDialog(null, 
                "Login data refreshed with new sample records!", 
                "Refresh Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        exportBtn.addActionListener(e -> exportLogToFile());
        clearBtn.addActionListener(e -> clearSearchAndFilter());
        addTestBtn.addActionListener(e -> addTestRecord());

        buttonsPanel.add(searchBtn);
        buttonsPanel.add(reloadBtn);
        buttonsPanel.add(exportBtn);
        buttonsPanel.add(clearBtn);
        buttonsPanel.add(addTestBtn);

        controlPanel.add(searchField, BorderLayout.CENTER);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);

        return controlPanel;
    }

    private static void exportLogToFile() {
        try {
            String timestamp = LocalDateTime.now().format(fileFormatter);
            String filename = "login_log_" + timestamp + ".txt";
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("=== LOGIN ACTIVITY LOG EXPORT ===\n");
                writer.write("Export Date: " + LocalDateTime.now().format(displayFormatter) + "\n");
                writer.write("Total Records: " + loginRecords.size() + "\n");
                writer.write("Successful: " + successfulLogins + "\n");
                writer.write("Failed: " + failedLogins + "\n");
                writer.write("=".repeat(80) + "\n\n");
                
                // Sort by newest first for export
                loginRecords.stream()
                    .sorted((r1, r2) -> r2.timestamp.compareTo(r1.timestamp))
                    .forEach(record -> {
                        try {
                            writer.write(String.format("%s | %s | %s | %s%s\n",
                                record.timestamp.format(displayFormatter),
                                record.username,
                                record.ip,
                                record.status,
                                record.details));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
            
            JOptionPane.showMessageDialog(null, 
                "Login log exported successfully to:\n" + filename, 
                "Export Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting log: " + e.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void clearSearchAndFilter() {
        searchField.setText("");
        filterComboBox.setSelectedItem(FILTER_ALL);
        sortComboBox.setSelectedItem(SORT_NEWEST);
        refreshLogDisplay();
        
        JOptionPane.showMessageDialog(null, 
            "All filters and search cleared!", 
            "Clear Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void addTestRecord() {
        // Add a new test record with current timestamp
        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();
        
        String[] users = {"test_user", "demo_account", "system_admin", "guest_login"};
        String username = users[random.nextInt(users.length)];
        
        String ip = String.format("10.0.%d.%d", random.nextInt(256), random.nextInt(256));
        boolean success = random.nextBoolean();
        String status = success ? "Success" : "Failed";
        
        String details = "";
        if (!success) {
            String[] reasons = {"Test failure", "Connection timeout", "Auth error"};
            details = " - " + reasons[random.nextInt(reasons.length)];
        }
        
        LoginRecord newRecord = new LoginRecord(now, username, ip, status, details);
        loginRecords.add(newRecord);
        updateStatistics();
        refreshLogDisplay();
        
        JOptionPane.showMessageDialog(null, 
            "Test login record added:\n" + 
            "User: " + username + "\n" +
            "IP: " + ip + "\n" +
            "Status: " + status, 
            "Test Record Added", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(LIGHT_TEAL);
        button.setForeground(DARK_BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(WHITE);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_TEAL);
                button.setForeground(DARK_BLUE);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel statusLabel = new JLabel("Last updated: " + 
            LocalDateTime.now().format(displayFormatter));
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(BLUE);
        
        JLabel recordLabel = new JLabel("Total records in database: " + loginRecords.size());
        recordLabel.setFont(LABEL_FONT);
        recordLabel.setForeground(BLUE);
        
        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(recordLabel, BorderLayout.EAST);
        
        return footerPanel;
    }

    // Login Record class
    static class LoginRecord {
        LocalDateTime timestamp;
        String username;
        String ip;
        String status;
        String details;
        
        public LoginRecord(LocalDateTime timestamp, String username, String ip, String status, String details) {
            this.timestamp = timestamp;
            this.username = username;
            this.ip = ip;
            this.status = status;
            this.details = details;
        }
    }
}