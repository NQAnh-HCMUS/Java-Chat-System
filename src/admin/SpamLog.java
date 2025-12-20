import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SpamLog {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color RED = new Color(0xE74C3C);
    private static final Color ORANGE = new Color(0xF39C12);
    private static final Color PURPLE = new Color(0x9B59B6);
    private static final Color YELLOW = new Color(0xF1C40F);
    private static final Color GREEN = new Color(0x2ECC71);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    private static DefaultListModel<String> listModel;
    private static JList<String> spamList;
    private static JLabel footerLabel;
    private static List<String> allReports;
    private static List<String> filteredReports;

    public static void List() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main frame
            JFrame frame = new JFrame("Spam Reports - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

            // Initialize data
            initializeData();

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
        });
    }

    private static void initializeData() {
        allReports = new ArrayList<>();
        filteredReports = new ArrayList<>();
        
        // Sample spam report data
        allReports.add("2024-01-15 14:30:00 - spam_user reported by john_doe");
        allReports.add("2024-01-15 16:45:00 - fake_account reported by jane_smith");
        allReports.add("2024-01-16 09:20:00 - malicious_bot reported by mike_wilson");
        allReports.add("2024-01-16 11:15:00 - phishing_scam reported by sarah_johnson");
        allReports.add("2024-01-17 08:30:00 - spammer_123 reported by david_brown");
        allReports.add("2024-01-17 13:45:00 - fake_profile reported by emily_davis");
        allReports.add("2024-01-18 10:20:00 - bot_account reported by admin");
        allReports.add("2024-01-18 15:30:00 - suspicious_user reported by john_doe");
        allReports.add("2024-01-19 08:45:00 - hacker_alert reported by robert_miller");
        allReports.add("2024-01-19 11:30:00 - scammer_xyz reported by lisa_anderson");
        allReports.add("2024-01-20 14:15:00 - fake_news reported by john_doe");
        allReports.add("2024-01-20 16:00:00 - spam_bot reported by jane_smith");
        
        filteredReports.addAll(allReports);
    }

    private static void updateListModel() {
        listModel.clear();
        for (String report : filteredReports) {
            listModel.addElement(" " + report);
        }
        updateFooter();
    }

    private static void updateFooter() {
        int total = allReports.size();
        int pending = (int) allReports.stream()
            .filter(report -> !report.contains("[RESOLVED]"))
            .count();
        int resolved = total - pending;
        
        footerLabel.setText(String.format(
            "Spam Report System • Total Reports: %d • Pending: %d • Resolved: %d • Showing: %d",
            total, pending, resolved, filteredReports.size()
        ));
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("Spam Reports Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(WHITE);
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(DARK_BLUE);
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(LABEL_FONT);
        searchLabel.setForeground(LIGHT_TEAL);

        JTextField searchField = new JTextField(20);
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        searchField.setToolTipText("Search by username, reporter, or keyword...");

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        
        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                // Reset to show all reports
                filteredReports.clear();
                filteredReports.addAll(allReports);
            } else {
                // Filter reports by search text
                filteredReports.clear();
                for (String report : allReports) {
                    if (report.toLowerCase().contains(searchText.toLowerCase())) {
                        filteredReports.add(report);
                    }
                }
            }
            updateListModel();
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        return searchPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Spam list panel
        JPanel listPanel = createSpamListPanel();
        
        // Action buttons panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createSpamListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel listTitle = new JLabel("Spam Reports");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Spam list
        listModel = new DefaultListModel<>();
        
        for (String report : filteredReports) {
            listModel.addElement(" " + report);
        }

        spamList = new JList<>(listModel);
        spamList.setFont(LIST_FONT);
        spamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spamList.setBackground(WHITE);
        spamList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));
        
        // Custom cell renderer for coloring
        spamList.setCellRenderer(new javax.swing.ListCellRenderer<String>() {
            private JLabel label = new JLabel();
            
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, 
                    String value, int index, boolean isSelected, boolean cellHasFocus) {
                
                label.setText(value);
                label.setFont(LIST_FONT);
                label.setOpaque(true);
                
                if (isSelected) {
                    label.setBackground(TEAL);
                    label.setForeground(WHITE);
                } else {
                    label.setBackground(WHITE);
                    
                    // Color code based on content
                    if (value.contains("[RESOLVED]")) {
                        label.setForeground(GREEN);
                        label.setText("✓ " + value.replace("[RESOLVED] ", ""));
                    } else if (value.contains("reported by admin")) {
                        label.setForeground(PURPLE);
                    } else if (value.contains("john_doe") || value.contains("jane_smith")) {
                        label.setForeground(BLUE);
                    } else {
                        label.setForeground(Color.BLACK);
                    }
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(spamList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));
        scrollPane.getViewport().setBackground(WHITE);

        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        return listPanel;
    }

    private static JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(LIGHT_GRAY);
        actionPanel.setPreferredSize(new Dimension(180, 0));

        // Action buttons
        JButton timeSortBtn = createModernButton("Sort by Time", BLUE);
        JButton nameSortBtn = createModernButton("Sort by Username", TEAL);
        JButton timeFilterBtn = createModernButton("Filter by Time", PURPLE);
        JButton nameFilterBtn = createModernButton("Filter by Username", YELLOW);
        JButton lockBtn = createModernButton("Lock/Unlock Account", RED);
        JButton resolveBtn = createModernButton("Mark Resolved", GREEN);
        JButton exportBtn = createModernButton("Export Reports", ORANGE);
        JButton refreshBtn = createModernButton("Refresh", DARK_BLUE);
        JButton clearBtn = createModernButton("Clear Filters", new Color(0x7F8C8D));

        // Add buttons to panel
        JButton[] buttons = {timeSortBtn, nameSortBtn, timeFilterBtn, nameFilterBtn, 
                            lockBtn, resolveBtn, exportBtn, refreshBtn, clearBtn};
        
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(160, 40));
            actionPanel.add(button);
            actionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        actionPanel.add(Box.createVerticalGlue());

        return actionPanel;
    }

    private static JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(LIGHT_TEAL);
        button.setForeground(DARK_BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
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

        // Add action listeners
        button.addActionListener(e -> handleButtonAction(text));

        return button;
    }

    private static void handleButtonAction(String action) {
        switch (action) {
            case "Sort by Time":
                sortByTime();
                break;
            case "Sort by Username":
                sortByUsername();
                break;
            case "Filter by Time":
                filterByTime();
                break;
            case "Filter by Username":
                filterByUsername();
                break;
            case "Lock/Unlock Account":
                lockUnlockAccount();
                break;
            case "Mark Resolved":
                markResolved();
                break;
            case "Export Reports":
                exportReports();
                break;
            case "Refresh":
                refreshData();
                break;
            case "Clear Filters":
                clearFilters();
                break;
        }
    }

    private static void sortByTime() {
        // Sort by date/time (ascending)
        filteredReports.sort((r1, r2) -> {
            String date1 = r1.substring(0, 19); // Extract timestamp
            String date2 = r2.substring(0, 19);
            return date1.compareTo(date2);
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Reports sorted by time (oldest first)", 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void sortByUsername() {
        // Sort by reported username
        filteredReports.sort((r1, r2) -> {
            // Extract username after timestamp (format: "timestamp - username reported by reporter")
            String user1 = extractReportedUsername(r1);
            String user2 = extractReportedUsername(r2);
            return user1.compareToIgnoreCase(user2);
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Reports sorted by username (A-Z)", 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static String extractReportedUsername(String report) {
        // Extract the username being reported
        // Format: "timestamp - username reported by reporter"
        String[] parts = report.split(" - ");
        if (parts.length > 1) {
            String middlePart = parts[1];
            String[] middleParts = middlePart.split(" reported by ");
            return middleParts[0].trim();
        }
        return "";
    }

    private static void filterByTime() {
        String input = JOptionPane.showInputDialog(null,
            "Enter date range (e.g., '2024-01-15' or '2024-01-15 to 2024-01-18'):",
            "Filter by Time",
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            String[] dates = input.split(" to ");
            String startDate = dates[0].trim();
            String endDate = dates.length > 1 ? dates[1].trim() : startDate;
            
            List<String> filtered = new ArrayList<>();
            for (String report : allReports) {
                String reportDate = report.substring(0, 10); // Extract YYYY-MM-DD
                if (reportDate.compareTo(startDate) >= 0 && reportDate.compareTo(endDate) <= 0) {
                    filtered.add(report);
                }
            }
            
            filteredReports.clear();
            filteredReports.addAll(filtered);
            updateListModel();
            
            JOptionPane.showMessageDialog(null,
                String.format("Showing %d reports from %s to %s", 
                    filtered.size(), startDate, endDate),
                "Filter Applied",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void filterByUsername() {
        String username = JOptionPane.showInputDialog(null,
            "Enter username to filter by:",
            "Filter by Username",
            JOptionPane.QUESTION_MESSAGE);
        
        if (username != null && !username.trim().isEmpty()) {
            List<String> filtered = new ArrayList<>();
            String searchUsername = username.trim().toLowerCase();
            
            for (String report : allReports) {
                String reportedUser = extractReportedUsername(report).toLowerCase();
                String reporter = extractReporter(report).toLowerCase();
                
                if (reportedUser.contains(searchUsername) || reporter.contains(searchUsername)) {
                    filtered.add(report);
                }
            }
            
            filteredReports.clear();
            filteredReports.addAll(filtered);
            updateListModel();
            
            JOptionPane.showMessageDialog(null,
                String.format("Found %d reports for username '%s'", 
                    filtered.size(), username),
                "Filter Applied",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static String extractReporter(String report) {
        // Extract the reporter username
        // Format: "timestamp - username reported by reporter"
        String[] parts = report.split(" reported by ");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return "";
    }

    private static void lockUnlockAccount() {
        int selectedIndex = spamList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null,
                "Please select a report first",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedReport = filteredReports.get(selectedIndex);
        String reportedUser = extractReportedUsername(selectedReport);
        
        int response = JOptionPane.showConfirmDialog(null,
            String.format("Do you want to LOCK the account '%s'?\n\nReport: %s", 
                reportedUser, selectedReport),
            "Confirm Account Lock",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            // In a real system, you would call a method to lock the account
            // For now, just show a message
            JOptionPane.showMessageDialog(null,
                String.format("Account '%s' has been locked.\nUser will not be able to login until unlocked.", 
                    reportedUser),
                "Account Locked",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Mark the report with [LOCKED] tag
            if (!selectedReport.contains("[LOCKED]")) {
                filteredReports.set(selectedIndex, selectedReport + " [LOCKED]");
                updateListModel();
            }
        }
    }

    private static void markResolved() {
        int selectedIndex = spamList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null,
                "Please select a report first",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedReport = filteredReports.get(selectedIndex);
        
        if (selectedReport.contains("[RESOLVED]")) {
            JOptionPane.showMessageDialog(null,
                "This report is already marked as resolved",
                "Already Resolved",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Update the report to mark as resolved
        String resolvedReport = "[RESOLVED] " + selectedReport;
        filteredReports.set(selectedIndex, resolvedReport);
        
        // Also update in allReports
        for (int i = 0; i < allReports.size(); i++) {
            if (allReports.get(i).equals(selectedReport)) {
                allReports.set(i, resolvedReport);
                break;
            }
        }
        
        updateListModel();
        
        JOptionPane.showMessageDialog(null,
            "Report marked as resolved",
            "Report Resolved",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void exportReports() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Spam Reports");
        fileChooser.setSelectedFile(new File("spam_reports_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                writer.write("Timestamp,Reported Username,Reporter,Status\n");
                
                // Write data
                for (String report : allReports) {
                    String timestamp = report.substring(0, 19);
                    String reportedUser = extractReportedUsername(report);
                    String reporter = extractReporter(report);
                    String status = report.contains("[RESOLVED]") ? "RESOLVED" : 
                                   report.contains("[LOCKED]") ? "LOCKED" : "PENDING";
                    
                    writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        timestamp, reportedUser, reporter, status));
                }
                
                JOptionPane.showMessageDialog(null,
                    String.format("Successfully exported %d reports to:\n%s",
                        allReports.size(), fileToSave.getAbsolutePath()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                    "Error exporting reports: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void refreshData() {
        // Reset to show all reports
        filteredReports.clear();
        filteredReports.addAll(allReports);
        updateListModel();
        
        JOptionPane.showMessageDialog(null,
            "Data refreshed successfully",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void clearFilters() {
        // Reset filtered reports to show all
        filteredReports.clear();
        filteredReports.addAll(allReports);
        updateListModel();
        
        JOptionPane.showMessageDialog(null,
            "All filters cleared\nShowing all " + allReports.size() + " reports",
            "Filters Cleared",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        footerLabel = new JLabel("Spam Report System • Total Reports: 12 • Pending: 12 • Resolved: 0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        updateFooter();
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
}