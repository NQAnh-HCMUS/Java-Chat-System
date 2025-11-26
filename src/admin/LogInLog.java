import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LOG_FONT = new Font("Consolas", Font.PLAIN, 12);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    public static void List() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Login Activity Log - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600);
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
        });
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

        JPanel onlinePanel = createStatItem("image/icon/user-account.png", "Online Now:", "156");
        JPanel todayPanel = createStatItem("image/icon/bar-graph.png", "Today:", "1,234");
        JPanel totalPanel = createStatItem("image/icon/login.png", "Total Logins:", "45,678");

        statsPanel.add(onlinePanel);
        statsPanel.add(todayPanel);
        statsPanel.add(totalPanel);

        return statsPanel;
    }

    private static JPanel createStatItem(String iconPath, String label, String value) {
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
        statPanel.setBackground(DARK_BLUE);
        statPanel.setOpaque(false);

        JLabel iconLabel = createIconLabel(iconPath);
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

    private static JLabel createIconLabel(String iconPath) {
        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            // Load icon image và resize
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            // Fallback
            System.err.println("Could not load icon: " + iconPath);
            iconLabel.setText("X");
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            iconLabel.setForeground(LIGHT_TEAL);
        }
        
        return iconLabel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Log content panel
        JPanel logPanel = createLogPanel();
        
        // Control panel
        JPanel controlPanel = createControlPanel();

        mainPanel.add(logPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }

    private static JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(WHITE);
        logPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel logTitle = new JLabel("Login Activity Records");
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logTitle.setForeground(DARK_BLUE);
        logTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Log text area
        JTextArea logArea = new JTextArea();
        logArea.setFont(LOG_FONT);
        logArea.setBackground(new Color(0x2C3E50));
        logArea.setForeground(new Color(0xECF0F1));
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        // Sample log data
        StringBuilder logContent = new StringBuilder();
        logContent.append("=== LOGIN ACTIVITY LOG ===\n\n");
        logContent.append("2024-01-15 08:30:00 - john_doe logged in from 192.168.1.100 (Success)\n");
        logContent.append("2024-01-15 09:15:00 - jane_smith logged in from 192.168.1.101 (Success)\n");
        logContent.append("2024-01-15 10:20:00 - mike_wilson logged in from 192.168.1.102 (Success)\n");
        logContent.append("2024-01-15 11:05:00 - sarah_johnson logged in from 192.168.1.103 (Success)\n");
        logContent.append("2024-01-15 13:45:00 - david_brown logged in from 192.168.1.104 (Success)\n");
        logContent.append("2024-01-15 14:30:00 - Failed login attempt for unknown_user from 192.168.1.105\n");
        logContent.append("2024-01-15 15:20:00 - emily_davis logged in from 192.168.1.106 (Success)\n");
        logContent.append("2024-01-15 16:10:00 - admin logged in from 192.168.1.107 (Success)\n");
        logContent.append("2024-01-16 08:25:00 - john_doe logged in from 192.168.1.100 (Success)\n");
        logContent.append("2024-01-16 09:10:00 - jane_smith logged in from 192.168.1.101 (Success)\n");
        logContent.append("2024-01-16 10:05:00 - mike_wilson logged in from 192.168.1.102 (Success)\n");
        
        logArea.setText(logContent.toString());

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x34495E), 1));
        scrollPane.getViewport().setBackground(new Color(0x2C3E50));

        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        return logPanel;
    }

    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(LIGHT_GRAY);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Search field
        JTextField searchField = new JTextField();
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Search login records by username, IP address, or status...");

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(LIGHT_GRAY);
        
        JButton searchBtn = createModernButton("Search", TEAL);
        JButton reloadBtn = createModernButton("Reload", BLUE);
        JButton exportBtn = createModernButton("Export Log", GREEN);
        JButton clearBtn = createModernButton("Clear Filter", ORANGE);

        buttonsPanel.add(searchBtn);
        buttonsPanel.add(reloadBtn);
        buttonsPanel.add(exportBtn);
        buttonsPanel.add(clearBtn);

        // Add action listener for Enter key in search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBtn.doClick();
            }
        });

        controlPanel.add(searchField, BorderLayout.CENTER);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);

        return controlPanel;
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

        // Add action listeners
        button.addActionListener(e -> handleButtonAction(text));

        return button;
    }

    private static void handleButtonAction(String action) {
        switch (action) {
            case "Search":
                JOptionPane.showMessageDialog(null, "Searching login records...", "Search", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Reload":
                JOptionPane.showMessageDialog(null, "Reloading login data...", "Reload", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Export Log":
                JOptionPane.showMessageDialog(null, "Exporting login log to file...", "Export", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Clear Filter":
                JOptionPane.showMessageDialog(null, "Clearing search filters...", "Clear", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel statusLabel = new JLabel("Last updated: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(BLUE);
        
        JLabel recordLabel = new JLabel("Displaying 50 most recent records");
        recordLabel.setFont(LABEL_FONT);
        recordLabel.setForeground(BLUE);
        
        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(recordLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
}