import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FriendNetwork {
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
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    private static JTable userTable;
    private static DefaultTableModel tableModel;

    public static void showFriendNetwork() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Friend Network Analysis - Admin Panel");
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
        });
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        headerPanel.setPreferredSize(new Dimension(100, 65));
        
        // Title
        JLabel titleLabel = new JLabel("Friend Network Analysis");
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

        JPanel totalUsersPanel = createStatItem("Total Users:", "1,234");
        JPanel avgFriendsPanel = createStatItem("Avg Friends:", "45");
        JPanel maxFriendsPanel = createStatItem("Max Friends:", "289");

        statsPanel.add(totalUsersPanel);
        statsPanel.add(avgFriendsPanel);
        statsPanel.add(maxFriendsPanel);

        return statsPanel;
    }

    private static JPanel createStatItem(String label, String value) {
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
        statPanel.setBackground(DARK_BLUE);
        statPanel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(label);
        descLabel.setFont(LABEL_FONT);
        descLabel.setForeground(LIGHT_TEAL);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statPanel.add(valueLabel);
        statPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        statPanel.add(descLabel);

        return statPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Control panel
        JPanel controlPanel = createControlPanel();
        
        // Table panel
        JPanel tablePanel = createTablePanel();

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        return mainPanel;
    }

    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(LIGHT_GRAY);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Left controls - Search and filters
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(LIGHT_GRAY);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Search by username or full name...");

        // Sort combo
        JComboBox<String> sortCombo = new JComboBox<>(new String[]{"Sort by Name", "Sort by Join Date", "Sort by Direct Friends", "Sort by Total Friends"});
        sortCombo.setFont(LABEL_FONT);

        // Friend count filter
        JComboBox<String> filterTypeCombo = new JComboBox<>(new String[]{"Equals", "Greater than", "Less than"});
        filterTypeCombo.setFont(LABEL_FONT);
        
        JTextField friendCountField = new JTextField(5);
        friendCountField.setFont(LABEL_FONT);
        friendCountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        leftPanel.add(new JLabel("Search:"));
        leftPanel.add(searchField);
        leftPanel.add(new JLabel("Sort:"));
        leftPanel.add(sortCombo);
        leftPanel.add(new JLabel("Direct Friends:"));
        leftPanel.add(filterTypeCombo);
        leftPanel.add(friendCountField);

        // Right controls - Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(LIGHT_GRAY);
        
        JButton searchBtn = createModernButton("Search", TEAL);
        JButton reloadBtn = createModernButton("Reload", BLUE);
        JButton exportBtn = createModernButton("Export", GREEN);
        JButton clearBtn = createModernButton("Clear", ORANGE);

        rightPanel.add(searchBtn);
        rightPanel.add(reloadBtn);
        rightPanel.add(exportBtn);
        rightPanel.add(clearBtn);

        controlPanel.add(leftPanel, BorderLayout.CENTER);
        controlPanel.add(rightPanel, BorderLayout.EAST);

        return controlPanel;
    }

    private static JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel tableTitle = new JLabel("User Friend Network Statistics");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(DARK_BLUE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create table model
        String[] columns = {"Username", "Full Name", "Join Date", "Direct Friends", "Total Network", "Network Level"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Sample data
        addSampleData();

        userTable = new JTable(tableModel);
        userTable.setFont(TABLE_FONT);
        userTable.setRowHeight(25);
        userTable.setSelectionBackground(TEAL);
        userTable.setSelectionForeground(WHITE);
        userTable.setGridColor(new Color(0xE1E8ED));
        
        // Center align numeric columns
        userTable.getColumnModel().getColumn(3).setCellRenderer(new CenterAlignedRenderer());
        userTable.getColumnModel().getColumn(4).setCellRenderer(new CenterAlignedRenderer());
        userTable.getColumnModel().getColumn(5).setCellRenderer(new CenterAlignedRenderer());

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE1E8ED), 1));
        scrollPane.getViewport().setBackground(WHITE);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private static void addSampleData() {
        // Add sample user data with friend network statistics
        tableModel.addRow(new Object[]{"john_doe", "John Doe", "2023-01-15", 45, 289, "High"});
        tableModel.addRow(new Object[]{"jane_smith", "Jane Smith", "2023-02-20", 67, 512, "Very High"});
        tableModel.addRow(new Object[]{"mike_wilson", "Mike Wilson", "2023-03-10", 23, 89, "Medium"});
        tableModel.addRow(new Object[]{"sarah_johnson", "Sarah Johnson", "2023-01-05", 89, 678, "Very High"});
        tableModel.addRow(new Object[]{"david_brown", "David Brown", "2023-04-15", 12, 34, "Low"});
        tableModel.addRow(new Object[]{"emily_davis", "Emily Davis", "2023-02-28", 56, 234, "High"});
        tableModel.addRow(new Object[]{"robert_miller", "Robert Miller", "2023-05-10", 34, 123, "Medium"});
        tableModel.addRow(new Object[]{"lisa_anderson", "Lisa Anderson", "2023-03-22", 78, 456, "High"});
        tableModel.addRow(new Object[]{"admin", "System Admin", "2023-01-01", 5, 15, "Low"});
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

        button.addActionListener(e -> handleButtonAction(text));

        return button;
    }

    private static void handleButtonAction(String action) {
        switch (action) {
            case "Search":
                JOptionPane.showMessageDialog(null, "Searching user friend networks...", "Search", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Reload":
                JOptionPane.showMessageDialog(null, "Reloading friend network data...", "Reload", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Export":
                JOptionPane.showMessageDialog(null, "Exporting friend network data...", "Export", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Clear":
                JOptionPane.showMessageDialog(null, "Clearing all filters...", "Clear", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel statusLabel = new JLabel("Total users displayed: " + tableModel.getRowCount());
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(BLUE);
        
        JLabel recordLabel = new JLabel("Last updated: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        recordLabel.setFont(LABEL_FONT);
        recordLabel.setForeground(BLUE);
        
        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(recordLabel, BorderLayout.EAST);
        
        return footerPanel;
    }

    // Custom cell renderer for center alignment
    static class CenterAlignedRenderer extends DefaultTableCellRenderer {
        public CenterAlignedRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
    }
}