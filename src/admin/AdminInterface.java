
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AdminInterface {

    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color PINK = new Color(0xF8DADA);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);

    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font STAT_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public static void AdminUI() {
        try {
            // Set modern look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Admin Dashboard - Java Chat System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

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
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(100, 80));

        // Title
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(WHITE);

        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(DARK_BLUE);
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, Administrator");
        userLabel.setFont(SUBTITLE_FONT);
        userLabel.setForeground(LIGHT_TEAL);

        userPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Sidebar with buttons
        JPanel sidebar = createSidebarPanel();

        // Content area
        JPanel contentPanel = createContentPanel();

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private static JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(DARK_BLUE);
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));
        sidebar.setPreferredSize(new Dimension(220, 0));


        String[] buttonNames = {
            "User Management",
            "Group Management",
            "New User Graph",
            "Activity Graph",
            "Sign Up Log",
            "Login History",
            "Spam Reports",
            "System Settings"
        };

        Runnable[] buttonActions = {
            () -> SwingUtilities.invokeLater(UserManager::showUser),
            () -> SwingUtilities.invokeLater(GroupManager::showGroup),
            () -> SwingUtilities.invokeLater(NewUserGraph::chooseYear),
            () -> SwingUtilities.invokeLater(ActivityGraph::chooseYear),
            () -> SwingUtilities.invokeLater(SignUpLog::showList),
            () -> SwingUtilities.invokeLater(LogInLog::List),
            () -> SwingUtilities.invokeLater(SpamLog::List),
            () -> showSystemSettings()
        };

        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = createStyledButton(buttonNames[i], buttonActions[i]);
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private static JButton createStyledButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 50));
        button.setPreferredSize(new Dimension(180, 50));
        button.setFont(BUTTON_FONT);

        // Styling 
        button.setBackground(LIGHT_TEAL);  
        button.setForeground(DARK_BLUE);   
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEAL, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(TEAL);
                button.setForeground(Color.WHITE);  
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_TEAL);
                button.setForeground(DARK_BLUE);    
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        button.addActionListener(e -> action.run());

        return button;
    }

    private static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_GRAY);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome section
        JPanel welcomePanel = createWelcomePanel();

        // Stats section
        JPanel statsPanel = createStatsPanel();

        // Main content container
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_GRAY);

        mainContent.add(welcomePanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 40)));
        mainContent.add(statsPanel);
        mainContent.add(Box.createVerticalGlue());

        contentPanel.add(mainContent, BorderLayout.CENTER);

        return contentPanel;
    }

    private static JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(LIGHT_GRAY);
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Main welcome
        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(DARK_BLUE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage your chat system efficiently with powerful tools");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(BLUE);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        welcomePanel.add(subtitleLabel);

        return welcomePanel;
    }

    private static JPanel createStatsPanel() {
        // Use wrapper panel for proper centering and sizing
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        wrapperPanel.setBackground(LIGHT_GRAY);
        wrapperPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBackground(LIGHT_GRAY);
        statsPanel.setPreferredSize(new Dimension(700, 220));

        // Sample stats
        String[] stats = {"1,234", "567", "89", "12,345", "23", "456"};
        String[] titles = {"Total Users", "Active Today", "Groups", "Messages", "Reports", "Online"};
        Color[] colors = {TEAL, BLUE, DARK_BLUE, TEAL, BLUE, DARK_BLUE};

        for (int i = 0; i < stats.length; i++) {
            JPanel statCard = createModernStatCard(titles[i], stats[i], colors[i]);
            statsPanel.add(statCard);
        }

        wrapperPanel.add(statsPanel);
        return wrapperPanel;
    }

    private static JPanel createModernStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE1E8ED), 1),
                new EmptyBorder(20, 15, 20, 15)
        ));
        card.setPreferredSize(new Dimension(180, 90));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(DARK_BLUE);
        footerPanel.setBorder(new EmptyBorder(12, 30, 12, 30));
        footerPanel.setPreferredSize(new Dimension(100, 50));

        JLabel footerLabel = new JLabel("Java Chat System © 2024 - Professional Admin Panel");
        footerLabel.setFont(SUBTITLE_FONT);
        footerLabel.setForeground(LIGHT_TEAL);

        JLabel versionLabel = new JLabel("v2.1.0");
        versionLabel.setFont(SUBTITLE_FONT);
        versionLabel.setForeground(LIGHT_TEAL);

        footerPanel.add(footerLabel, BorderLayout.WEST);
        footerPanel.add(versionLabel, BorderLayout.EAST);

        return footerPanel;
    }

    private static void showSystemSettings() {
        JOptionPane.showMessageDialog(null,
                "System Settings Panel\n\n"
                + "• Database Configuration\n"
                + "• Security Settings\n"
                + "• Backup & Restore\n"
                + "• System Logs\n\n"
                + "This feature is under development.",
                "System Settings",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
