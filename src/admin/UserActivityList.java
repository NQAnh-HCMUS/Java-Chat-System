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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class UserActivityList {
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

    private static JTable activityTable;
    private static DefaultTableModel tableModel;
    private static JComboBox<String> periodCombo;
    private static JComboBox<String> sortCombo;
    private static JTextField searchField;
    private static JComboBox<String> filterTypeCombo;
    private static JTextField activityCountField;
    private static TableRowSorter<DefaultTableModel> sorter;
    private static JLabel statusLabel;
    private static JLabel recordLabel;
    private static boolean isInitialized = false;

    public static void showUserActivity() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("User Activity Analysis - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 700);
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
            
            // Mark as initialized after all components are created
            isInitialized = true;
            updateFooter();
        });
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        headerPanel.setPreferredSize(new Dimension(100, 65));
        
        // Title
        JLabel titleLabel = new JLabel("User Activity Analysis");
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

        JPanel activeUsersPanel = createStatItem("Active Users:", "856");
        JPanel avgActivityPanel = createStatItem("Avg Activities:", "23");
        JPanel topUserPanel = createStatItem("Top User:", "89 activities");

        statsPanel.add(activeUsersPanel);
        statsPanel.add(avgActivityPanel);
        statsPanel.add(topUserPanel);

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

        // Left controls - Period selection and filters
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(LIGHT_GRAY);

        // Period selection
        periodCombo = new JComboBox<>(new String[]{
            "All Time", "Last 7 days", "Last 30 days", "Last 90 days", 
            "This Month", "Last Month", "Custom Range"
        });
        periodCombo.setFont(LABEL_FONT);

        // Search field
        searchField = new JTextField(15);
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Search by username or full name...");

        // Sort combo
        sortCombo = new JComboBox<>(new String[]{
            "Sort by Name", "Sort by Join Date", "Sort by App Opens", 
            "Sort by Individual Chats", "Sort by Group Chats", "Sort by Total Activity"
        });
        sortCombo.setFont(LABEL_FONT);

        // Activity count filter
        filterTypeCombo = new JComboBox<>(new String[]{"All", "Greater than", "Less than"});
        filterTypeCombo.setFont(LABEL_FONT);
        
        activityCountField = new JTextField(5);
        activityCountField.setFont(LABEL_FONT);
        activityCountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        leftPanel.add(new JLabel("Period:"));
        leftPanel.add(periodCombo);
        leftPanel.add(new JLabel("Search:"));
        leftPanel.add(searchField);
        leftPanel.add(new JLabel("Sort:"));
        leftPanel.add(sortCombo);
        leftPanel.add(new JLabel("Activity:"));
        leftPanel.add(filterTypeCombo);
        leftPanel.add(activityCountField);

        // Right controls - Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(LIGHT_GRAY);
        
        JButton applyBtn = createModernButton("Apply Filter", TEAL);
        JButton reloadBtn = createModernButton("Reload", BLUE);
        JButton exportBtn = createModernButton("Export", GREEN);
        JButton clearBtn = createModernButton("Clear", ORANGE);

        rightPanel.add(applyBtn);
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
        JLabel tableTitle = new JLabel("User Activity Statistics (Last 30 days)");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(DARK_BLUE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create table model
        String[] columns = {
            "Username", "Full Name", "Join Date", "App Opens", 
            "Individual Chats", "Group Chats", "Total Messages", "Activity Score"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 6) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        
        // Add sample data
        addSampleData();

        activityTable = new JTable(tableModel);
        activityTable.setRowSorter(sorter);
        activityTable.setFont(TABLE_FONT);
        activityTable.setRowHeight(25);
        activityTable.setSelectionBackground(TEAL);
        activityTable.setSelectionForeground(WHITE);
        activityTable.setGridColor(new Color(0xE1E8ED));
        
        // Center align numeric columns
        for (int i = 3; i < 8; i++) {
            activityTable.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE1E8ED), 1));
        scrollPane.getViewport().setBackground(WHITE);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private static void addSampleData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add sample user activity data
        tableModel.addRow(new Object[]{"john_doe", "John Doe", "2023-01-15", 45, 23, 5, 156, "High"});
        tableModel.addRow(new Object[]{"jane_smith", "Jane Smith", "2023-02-20", 67, 45, 8, 289, "Very High"});
        tableModel.addRow(new Object[]{"mike_wilson", "Mike Wilson", "2023-03-10", 23, 12, 3, 67, "Medium"});
        tableModel.addRow(new Object[]{"sarah_johnson", "Sarah Johnson", "2023-01-05", 89, 67, 12, 456, "Very High"});
        tableModel.addRow(new Object[]{"david_brown", "David Brown", "2023-04-15", 12, 8, 1, 23, "Low"});
        tableModel.addRow(new Object[]{"emily_davis", "Emily Davis", "2023-02-28", 56, 34, 6, 189, "High"});
        tableModel.addRow(new Object[]{"robert_miller", "Robert Miller", "2023-05-10", 34, 21, 4, 89, "Medium"});
        tableModel.addRow(new Object[]{"lisa_anderson", "Lisa Anderson", "2023-03-22", 78, 45, 9, 278, "High"});
        tableModel.addRow(new Object[]{"admin", "System Admin", "2023-01-01", 5, 2, 0, 8, "Low"});
        
        // Only update footer if labels are initialized
        if (isInitialized) {
            updateFooter();
        }
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
            case "Apply Filter":
                applyFilters();
                break;
            case "Reload":
                reloadData();
                break;
            case "Export":
                exportData();
                break;
            case "Clear":
                clearFilters();
                break;
        }
    }

    private static void applyFilters() {
        try {
            // Apply search filter
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 0, 1)); // Search in username and full name
            } else {
                sorter.setRowFilter(null);
            }
            
            // Apply activity count filter
            String filterType = (String) filterTypeCombo.getSelectedItem();
            String countText = activityCountField.getText().trim();
            
            if (!countText.isEmpty() && !filterType.equals("All")) {
                try {
                    final int count = Integer.parseInt(countText);
                    RowFilter<DefaultTableModel, Integer> activityFilter = new RowFilter<DefaultTableModel, Integer>() {
                        @Override
                        public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                            int row = entry.getIdentifier();
                            Integer activityCount = (Integer) entry.getModel().getValueAt(row, 6); // Total Messages column
                            
                            if (activityCount == null) {
                                return false;
                            }
                            
                            switch (filterType) {
                                case "Greater than":
                                    return activityCount > count;
                                case "Less than":
                                    return activityCount < count;
                                default:
                                    return true;
                            }
                        }
                    };
                    
                    // Combine search filter with activity filter if both exist
                    if (!searchText.isEmpty()) {
                        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
                        filters.add(RowFilter.regexFilter("(?i)" + searchText, 0, 1));
                        filters.add(activityFilter);
                        sorter.setRowFilter(RowFilter.andFilter(filters));
                    } else {
                        sorter.setRowFilter(activityFilter);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a valid number for activity count", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
            // Apply period filter (simulated)
            String period = (String) periodCombo.getSelectedItem();
            // In a real application, you would filter by date here
            
            // Apply sorting
            applySorting();
            
            // Update footer
            updateFooter();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error applying filters: " + e.getMessage(), 
                "Filter Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void applySorting() {
        String sortOption = (String) sortCombo.getSelectedItem();
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        
        switch (sortOption) {
            case "Sort by Name":
                sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING)); // Sort by Full Name
                break;
            case "Sort by Join Date":
                sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING)); // Sort by Join Date
                break;
            case "Sort by App Opens":
                sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING)); // Sort by App Opens
                break;
            case "Sort by Individual Chats":
                sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING)); // Sort by Individual Chats
                break;
            case "Sort by Group Chats":
                sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING)); // Sort by Group Chats
                break;
            case "Sort by Total Activity":
                sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING)); // Sort by Total Messages
                break;
        }
        
        if (!sortKeys.isEmpty()) {
            sorter.setSortKeys(sortKeys);
        }
    }

    private static void reloadData() {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Reload all activity data? This will reset all filters.", 
            "Reload Data", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear all filters
            clearFilters();
            
            // Simulate loading new data
            addSampleData();
            
            // Reset sorter
            sorter.setRowFilter(null);
            
            // Update footer
            updateFooter();
            
            JOptionPane.showMessageDialog(null, 
                "Activity data reloaded successfully!\n" + 
                "Total records: " + tableModel.getRowCount(), 
                "Reload Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Activity Data");
        fileChooser.setSelectedFile(new File("user_activity_" + 
            new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
                
                // Write data
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Object value = tableModel.getValueAt(row, col);
                        writer.write(value != null ? value.toString() : "");
                        if (col < tableModel.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    writer.write("\n");
                }
                
                JOptionPane.showMessageDialog(null, 
                    "Data exported successfully to:\n" + fileToSave.getAbsolutePath(), 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error exporting data: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void clearFilters() {
        // Clear search field
        searchField.setText("");
        
        // Reset combo boxes
        periodCombo.setSelectedIndex(0);
        sortCombo.setSelectedIndex(0);
        filterTypeCombo.setSelectedIndex(0);
        activityCountField.setText("");
        
        // Clear table filters
        sorter.setRowFilter(null);
        
        // Reset sorting
        sorter.setSortKeys(null);
        
        // Update footer
        updateFooter();
        
        JOptionPane.showMessageDialog(null, 
            "All filters cleared successfully!", 
            "Clear Filters", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Initialize labels
        statusLabel = new JLabel("Active users in period: " + tableModel.getRowCount());
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(BLUE);
        
        recordLabel = new JLabel("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        recordLabel.setFont(LABEL_FONT);
        recordLabel.setForeground(BLUE);
        
        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(recordLabel, BorderLayout.EAST);
        
        return footerPanel;
    }

    private static void updateFooter() {
        if (statusLabel != null && recordLabel != null && sorter != null) {
            int visibleRows = sorter.getViewRowCount();
            int totalRows = tableModel.getRowCount();
            statusLabel.setText("Showing " + visibleRows + " of " + totalRows + " users");
            recordLabel.setText("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    // Custom cell renderer for center alignment
    static class CenterAlignedRenderer extends DefaultTableCellRenderer {
        public CenterAlignedRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Color code activity scores
            if (column == 7 && value != null) {
                String score = value.toString();
                switch (score) {
                    case "Very High":
                        c.setForeground(new Color(0x27AE60)); // Green
                        break;
                    case "High":
                        c.setForeground(new Color(0x2ECC71)); // Light Green
                        break;
                    case "Medium":
                        c.setForeground(new Color(0xF39C12)); // Orange
                        break;
                    case "Low":
                        c.setForeground(new Color(0xE74C3C)); // Red
                        break;
                    default:
                        c.setForeground(Color.BLACK);
                }
                setFont(getFont().deriveFont(Font.BOLD));
            }
            
            return c;
        }
    }
}