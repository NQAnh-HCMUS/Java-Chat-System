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
import java.util.Comparator;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
    private static final Color PURPLE = new Color(0x9B59B6);
    private static final Color YELLOW = new Color(0xF1C40F);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    private static JTable userTable;
    private static DefaultTableModel tableModel;
    private static JComboBox<String> sortCombo;
    private static JComboBox<String> filterTypeCombo;
    private static JTextField searchField;
    private static JTextField friendCountFromField;
    private static JTextField friendCountToField;
    private static JLabel statusLabel;
    private static JLabel recordLabel;
    private static TableRowSorter<DefaultTableModel> sorter;
    private static List<UserNetworkData> userDataList;
    private static JPanel statsPanelRef;
    private static List<UserNetworkData> filteredData;

    public static void showFriendNetwork() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Friend Network Analysis - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 700);
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

    // Data class to hold user network information
    private static class UserNetworkData {
        String username;
        String fullName;
        String joinDate;
        int directFriends;
        int totalNetwork;
        String networkLevel;
        
        UserNetworkData(String username, String fullName, String joinDate, 
                       int directFriends, int totalNetwork, String networkLevel) {
            this.username = username;
            this.fullName = fullName;
            this.joinDate = joinDate;
            this.directFriends = directFriends;
            this.totalNetwork = totalNetwork;
            this.networkLevel = networkLevel;
        }
    }

    private static void initializeData() {
        userDataList = new ArrayList<>();
        filteredData = new ArrayList<>();
        
        // Add sample user data with friend network statistics
        userDataList.add(new UserNetworkData("john_doe", "John Doe", "2023-01-15", 45, 289, "High"));
        userDataList.add(new UserNetworkData("jane_smith", "Jane Smith", "2023-02-20", 67, 512, "Very High"));
        userDataList.add(new UserNetworkData("mike_wilson", "Mike Wilson", "2023-03-10", 23, 89, "Medium"));
        userDataList.add(new UserNetworkData("sarah_johnson", "Sarah Johnson", "2023-01-05", 89, 678, "Very High"));
        userDataList.add(new UserNetworkData("david_brown", "David Brown", "2023-04-15", 12, 34, "Low"));
        userDataList.add(new UserNetworkData("emily_davis", "Emily Davis", "2023-02-28", 56, 234, "High"));
        userDataList.add(new UserNetworkData("robert_miller", "Robert Miller", "2023-05-10", 34, 123, "Medium"));
        userDataList.add(new UserNetworkData("lisa_anderson", "Lisa Anderson", "2023-03-22", 78, 456, "High"));
        userDataList.add(new UserNetworkData("admin", "System Admin", "2023-01-01", 5, 15, "Low"));
        userDataList.add(new UserNetworkData("alex_turner", "Alex Turner", "2023-06-15", 42, 198, "Medium"));
        userDataList.add(new UserNetworkData("sophia_martinez", "Sophia Martinez", "2023-07-22", 91, 712, "Very High"));
        userDataList.add(new UserNetworkData("chris_lee", "Chris Lee", "2023-08-10", 38, 167, "Medium"));
        userDataList.add(new UserNetworkData("olivia_white", "Olivia White", "2023-09-05", 124, 845, "Very High"));
        userDataList.add(new UserNetworkData("daniel_clark", "Daniel Clark", "2023-10-18", 27, 92, "Medium"));
        userDataList.add(new UserNetworkData("ava_robinson", "Ava Robinson", "2023-11-30", 63, 321, "High"));
        userDataList.add(new UserNetworkData("james_walker", "James Walker", "2023-12-12", 18, 45, "Low"));
        
        // Initialize filtered data with all data
        filteredData.addAll(userDataList);
    }

    private static void updateTableModel() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add filtered user data to table
        for (UserNetworkData user : filteredData) {
            tableModel.addRow(new Object[]{
                user.username,
                user.fullName,
                user.joinDate,
                user.directFriends,
                user.totalNetwork,
                user.networkLevel
            });
        }
        
        updateStatsPanel();
        updateFooter();
    }

    private static void updateFooter() {
        if (statusLabel != null && recordLabel != null) {
            int visibleRows = filteredData.size();
            int totalRows = userDataList.size();
            statusLabel.setText("Showing " + visibleRows + " of " + totalRows + " users");
            recordLabel.setText("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
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
        statsPanelRef = createStatsPanel();
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statsPanelRef, BorderLayout.EAST);
        
        return headerPanel;
    }

    private static JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(DARK_BLUE);
        statsPanel.setOpaque(false);

        // Calculate initial statistics from filtered data
        int totalUsers = filteredData.size();
        int totalDirectFriends = 0;
        int maxDirectFriends = 0;
        
        for (UserNetworkData user : filteredData) {
            totalDirectFriends += user.directFriends;
            if (user.directFriends > maxDirectFriends) {
                maxDirectFriends = user.directFriends;
            }
        }
        
        double avgFriends = totalUsers > 0 ? (double) totalDirectFriends / totalUsers : 0;

        JPanel totalUsersPanel = createStatItem("Total Users:", String.valueOf(totalUsers));
        JPanel avgFriendsPanel = createStatItem("Avg Friends:", String.format("%.1f", avgFriends));
        JPanel maxFriendsPanel = createStatItem("Max Friends:", String.valueOf(maxDirectFriends));

        statsPanel.add(totalUsersPanel);
        statsPanel.add(avgFriendsPanel);
        statsPanel.add(maxFriendsPanel);

        return statsPanel;
    }

    private static void updateStatsPanel() {
        if (statsPanelRef == null) return;
        
        // Calculate statistics from filtered data
        int totalUsers = filteredData.size();
        int totalDirectFriends = 0;
        int maxDirectFriends = 0;
        
        for (UserNetworkData user : filteredData) {
            totalDirectFriends += user.directFriends;
            if (user.directFriends > maxDirectFriends) {
                maxDirectFriends = user.directFriends;
            }
        }
        
        double avgFriends = totalUsers > 0 ? (double) totalDirectFriends / totalUsers : 0;
        
        // Update stats panel
        statsPanelRef.removeAll();
        
        JPanel totalUsersPanel = createStatItem("Total Users:", String.valueOf(totalUsers));
        JPanel avgFriendsPanel = createStatItem("Avg Friends:", String.format("%.1f", avgFriends));
        JPanel maxFriendsPanel = createStatItem("Max Friends:", String.valueOf(maxDirectFriends));
        
        statsPanelRef.add(totalUsersPanel);
        statsPanelRef.add(avgFriendsPanel);
        statsPanelRef.add(maxFriendsPanel);
        
        statsPanelRef.revalidate();
        statsPanelRef.repaint();
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
        searchField = new JTextField(20);
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Search by username or full name...");

        // Sort combo - Thêm nhiều tùy chọn sắp xếp
        sortCombo = new JComboBox<>(new String[]{
            "Default (No Sort)", 
            "Name (A → Z)", 
            "Name (Z → A)", 
            "Join Date (Newest First)", 
            "Join Date (Oldest First)",
            "Direct Friends (High → Low)", 
            "Direct Friends (Low → High)",
            "Total Network (High → Low)", 
            "Total Network (Low → High)",
            "Network Level (Very High → Low)"
        });
        sortCombo.setFont(LABEL_FONT);
        sortCombo.setPreferredSize(new Dimension(180, 25));

        // Filter panel for friend count
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setBackground(LIGHT_GRAY);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEAL, 1), "Filter by Direct Friends"
        ));
        
        filterTypeCombo = new JComboBox<>(new String[]{
            "All", 
            "Greater than", 
            "Less than", 
            "Equals",
            "Between"
        });
        filterTypeCombo.setFont(LABEL_FONT);
        filterTypeCombo.setPreferredSize(new Dimension(100, 25));
        
        friendCountFromField = new JTextField(5);
        friendCountFromField.setFont(LABEL_FONT);
        friendCountFromField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        friendCountFromField.setToolTipText("Minimum friends");
        
        JLabel toLabel = new JLabel("to");
        toLabel.setFont(LABEL_FONT);
        toLabel.setForeground(DARK_BLUE);
        
        friendCountToField = new JTextField(5);
        friendCountToField.setFont(LABEL_FONT);
        friendCountToField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        friendCountToField.setToolTipText("Maximum friends");
        friendCountToField.setEnabled(false); // Initially disabled
        
        // Update UI when filter type changes
        filterTypeCombo.addActionListener(e -> {
            String selected = (String) filterTypeCombo.getSelectedItem();
            friendCountToField.setEnabled("Between".equals(selected));
        });

        filterPanel.add(filterTypeCombo);
        filterPanel.add(friendCountFromField);
        filterPanel.add(toLabel);
        filterPanel.add(friendCountToField);

        leftPanel.add(new JLabel("Search:"));
        leftPanel.add(searchField);
        leftPanel.add(new JLabel("Sort:"));
        leftPanel.add(sortCombo);
        leftPanel.add(filterPanel);

        // Right controls - Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(LIGHT_GRAY);
        
        JButton searchBtn = createModernButton("Search & Filter", TEAL);
        JButton reloadBtn = createModernButton("Reload", BLUE);
        JButton exportBtn = createModernButton("Export", GREEN);
        JButton clearBtn = createModernButton("Clear", ORANGE);
        JButton analyzeBtn = createModernButton("Analyze", PURPLE);
        JButton visualizeBtn = createModernButton("Visualize", YELLOW);

        rightPanel.add(searchBtn);
        rightPanel.add(reloadBtn);
        rightPanel.add(exportBtn);
        rightPanel.add(clearBtn);
        rightPanel.add(analyzeBtn);
        rightPanel.add(visualizeBtn);

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
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3 || columnIndex == 4) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        // Create sorter (for table sorting only, not for filtering)
        sorter = new TableRowSorter<>(tableModel);
        
        // Add sample data
        updateTableModel();

        userTable = new JTable(tableModel);
        userTable.setRowSorter(sorter);
        userTable.setFont(TABLE_FONT);
        userTable.setRowHeight(25);
        userTable.setSelectionBackground(TEAL);
        userTable.setSelectionForeground(WHITE);
        userTable.setGridColor(new Color(0xE1E8ED));
        
        // Custom cell renderer for coloring
        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(new NetworkCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE1E8ED), 1));
        scrollPane.getViewport().setBackground(WHITE);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
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
            case "Search & Filter":
                performSearchAndFilter();
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
            case "Analyze":
                analyzeNetwork();
                break;
            case "Visualize":
                visualizeNetwork();
                break;
        }
    }

    private static void performSearchAndFilter() {
        try {
            // Get search text
            String searchText = searchField.getText().trim();
            
            // Get friend count filter
            String filterType = (String) filterTypeCombo.getSelectedItem();
            String countFromText = friendCountFromField.getText().trim();
            String countToText = friendCountToField.getText().trim();
            
            // Validate inputs for friend count filter
            int minFriends = -1;
            int maxFriends = -1;
            
            if (!filterType.equals("All")) {
                if (countFromText.isEmpty() && !filterType.equals("Between")) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a value for friend count", 
                        "Input Required", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!countFromText.isEmpty()) {
                    try {
                        minFriends = Integer.parseInt(countFromText);
                        if (minFriends < 0) {
                            JOptionPane.showMessageDialog(null, 
                                "Friend count cannot be negative", 
                                "Invalid Input", 
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, 
                            "Please enter a valid number for friend count", 
                            "Invalid Input", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                if (filterType.equals("Between")) {
                    if (countToText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Please enter maximum value for range", 
                            "Input Required", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    try {
                        maxFriends = Integer.parseInt(countToText);
                        if (maxFriends < 0) {
                            JOptionPane.showMessageDialog(null, 
                                "Friend count cannot be negative", 
                                "Invalid Input", 
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        if (minFriends > maxFriends) {
                            JOptionPane.showMessageDialog(null, 
                                "Minimum value cannot be greater than maximum value", 
                                "Invalid Range", 
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, 
                            "Please enter a valid number for maximum friend count", 
                            "Invalid Input", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            
            // Apply search and filter
            filteredData.clear();
            
            for (UserNetworkData user : userDataList) {
                boolean matchesSearch = true;
                boolean matchesFilter = true;
                
                // Apply search filter
                if (!searchText.isEmpty()) {
                    String searchLower = searchText.toLowerCase();
                    matchesSearch = user.username.toLowerCase().contains(searchLower) ||
                                   user.fullName.toLowerCase().contains(searchLower);
                }
                
                // Apply friend count filter
                if (!filterType.equals("All")) {
                    switch (filterType) {
                        case "Greater than":
                            matchesFilter = user.directFriends > minFriends;
                            break;
                        case "Less than":
                            matchesFilter = user.directFriends < minFriends;
                            break;
                        case "Equals":
                            matchesFilter = user.directFriends == minFriends;
                            break;
                        case "Between":
                            matchesFilter = user.directFriends >= minFriends && 
                                          user.directFriends <= maxFriends;
                            break;
                    }
                }
                
                if (matchesSearch && matchesFilter) {
                    filteredData.add(user);
                }
            }
            
            // Apply sorting
            applySorting();
            
            // Update table and stats
            updateTableModel();
            
            // Show results
            String message = "Search completed!\n";
            message += "Found " + filteredData.size() + " users\n";
            
            if (!searchText.isEmpty()) {
                message += "Search: '" + searchText + "'\n";
            }
            
            if (!filterType.equals("All")) {
                message += "Filter: Direct Friends " + filterType + " ";
                if (filterType.equals("Between")) {
                    message += minFriends + " to " + maxFriends;
                } else {
                    message += minFriends;
                }
                message += "\n";
            }
            
            JOptionPane.showMessageDialog(null, 
                message, 
                "Search & Filter Results", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error performing search: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void applySorting() {
        String sortOption = (String) sortCombo.getSelectedItem();
        
        if (sortOption.startsWith("Default")) {
            // No sorting
            return;
        }
        
        // Apply sorting based on selected option
        switch (sortOption) {
            case "Name (A → Z)":
                filteredData.sort(Comparator.comparing(user -> user.fullName.toLowerCase()));
                break;
            case "Name (Z → A)":
                filteredData.sort((u1, u2) -> u2.fullName.compareToIgnoreCase(u1.fullName));
                break;
            case "Join Date (Newest First)":
                filteredData.sort((u1, u2) -> u2.joinDate.compareTo(u1.joinDate));
                break;
            case "Join Date (Oldest First)":
                filteredData.sort(Comparator.comparing(user -> user.joinDate));
                break;
            case "Direct Friends (High → Low)":
                filteredData.sort((u1, u2) -> Integer.compare(u2.directFriends, u1.directFriends));
                break;
            case "Direct Friends (Low → High)":
                filteredData.sort(Comparator.comparingInt(user -> user.directFriends));
                break;
            case "Total Network (High → Low)":
                filteredData.sort((u1, u2) -> Integer.compare(u2.totalNetwork, u1.totalNetwork));
                break;
            case "Total Network (Low → High)":
                filteredData.sort(Comparator.comparingInt(user -> user.totalNetwork));
                break;
            case "Network Level (Very High → Low)":
                // Custom sorting for network levels
                filteredData.sort((u1, u2) -> {
                    String[] levels = {"Very High", "High", "Medium", "Low"};
                    int index1 = -1, index2 = -1;
                    
                    for (int i = 0; i < levels.length; i++) {
                        if (levels[i].equals(u1.networkLevel)) index1 = i;
                        if (levels[i].equals(u2.networkLevel)) index2 = i;
                    }
                    
                    return Integer.compare(index1, index2);
                });
                break;
        }
    }

    private static void reloadData() {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Reload all friend network data? This will reset all filters.", 
            "Reload Data", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear all filters
            clearFilters();
            
            // Reset filtered data to all data
            filteredData.clear();
            filteredData.addAll(userDataList);
            
            // Update table
            updateTableModel();
            
            JOptionPane.showMessageDialog(null, 
                "Friend network data reloaded successfully!\n" + 
                "Total users: " + userDataList.size(), 
                "Reload Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Friend Network Data");
        fileChooser.setSelectedFile(new File("friend_network_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                writer.write("Username,Full Name,Join Date,Direct Friends,Total Network,Network Level\n");
                
                // Write current filtered data
                for (UserNetworkData user : filteredData) {
                    writer.write(String.format("\"%s\",\"%s\",\"%s\",%d,%d,\"%s\"\n",
                        user.username,
                        user.fullName,
                        user.joinDate,
                        user.directFriends,
                        user.totalNetwork,
                        user.networkLevel
                    ));
                }
                
                JOptionPane.showMessageDialog(null, 
                    "Data exported successfully to:\n" + fileToSave.getAbsolutePath() + 
                    "\nExported records: " + filteredData.size(), 
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
        // Clear all input fields
        searchField.setText("");
        sortCombo.setSelectedIndex(0);
        filterTypeCombo.setSelectedIndex(0);
        friendCountFromField.setText("");
        friendCountToField.setText("");
        friendCountToField.setEnabled(false);
        
        // Reset filtered data to all data
        filteredData.clear();
        filteredData.addAll(userDataList);
        
        // Update table
        updateTableModel();
        
        JOptionPane.showMessageDialog(null, 
            "All filters cleared successfully!\nShowing all " + userDataList.size() + " users", 
            "Clear Filters", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void analyzeNetwork() {
        if (filteredData.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No data to analyze. Please apply filters first.", 
                "No Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Calculate network statistics from filtered data
        int totalUsers = filteredData.size();
        int totalDirectFriends = 0;
        int maxDirectFriends = 0;
        int maxNetworkSize = 0;
        double avgDirectFriends = 0;
        
        List<UserNetworkData> topUsers = new ArrayList<>();
        
        for (UserNetworkData user : filteredData) {
            totalDirectFriends += user.directFriends;
            if (user.directFriends > maxDirectFriends) {
                maxDirectFriends = user.directFriends;
            }
            if (user.totalNetwork > maxNetworkSize) {
                maxNetworkSize = user.totalNetwork;
            }
        }
        
        avgDirectFriends = totalUsers > 0 ? (double) totalDirectFriends / totalUsers : 0;
        
        // Find top influencers from filtered data
        List<UserNetworkData> sortedByFriends = new ArrayList<>(filteredData);
        sortedByFriends.sort((u1, u2) -> Integer.compare(u2.directFriends, u1.directFriends));
        int limit = Math.min(5, sortedByFriends.size());
        for (int i = 0; i < limit; i++) {
            topUsers.add(sortedByFriends.get(i));
        }
        
        // Display analysis results
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== FRIEND NETWORK ANALYSIS ===\n");
        analysis.append("(Based on currently filtered data)\n\n");
        analysis.append(String.format("Total Users Analyzed: %d\n", totalUsers));
        analysis.append(String.format("Average Direct Friends: %.1f\n", avgDirectFriends));
        analysis.append(String.format("Maximum Direct Friends: %d\n", maxDirectFriends));
        analysis.append(String.format("Maximum Network Size: %d\n\n", maxNetworkSize));
        
        analysis.append("=== NETWORK DISTRIBUTION ===\n");
        int veryHigh = 0, high = 0, medium = 0, low = 0;
        for (UserNetworkData user : filteredData) {
            switch (user.networkLevel) {
                case "Very High": veryHigh++; break;
                case "High": high++; break;
                case "Medium": medium++; break;
                case "Low": low++; break;
            }
        }
        analysis.append(String.format("Very High: %d users (%.1f%%)\n", veryHigh, (veryHigh * 100.0 / totalUsers)));
        analysis.append(String.format("High: %d users (%.1f%%)\n", high, (high * 100.0 / totalUsers)));
        analysis.append(String.format("Medium: %d users (%.1f%%)\n", medium, (medium * 100.0 / totalUsers)));
        analysis.append(String.format("Low: %d users (%.1f%%)\n\n", low, (low * 100.0 / totalUsers)));
        
        analysis.append("=== TOP INFLUENCERS ===\n");
        for (int i = 0; i < topUsers.size(); i++) {
            UserNetworkData user = topUsers.get(i);
            analysis.append(String.format("%d. %s (%s) - %d direct friends, %d total network\n", 
                i + 1, user.username, user.fullName, user.directFriends, user.totalNetwork));
        }
        
        // Show in a scrollable dialog for better viewing
        JTextArea textArea = new JTextArea(analysis.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scrollPane, 
            "Network Analysis", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void visualizeNetwork() {
        if (filteredData.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No data to visualize. Please apply filters first.", 
                "No Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create a simple visualization dialog
        JFrame visualizeFrame = new JFrame("Network Visualization");
        visualizeFrame.setSize(800, 600);
        visualizeFrame.setLocationRelativeTo(null);
        
        JPanel visualizePanel = new JPanel(new BorderLayout());
        visualizePanel.setBackground(WHITE);
        visualizePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Friend Network Visualization");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Create a simple visualization using text
        JTextArea visualizationArea = new JTextArea();
        visualizationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        visualizationArea.setEditable(false);
        visualizationArea.setBackground(new Color(0xF8F9FA));
        visualizationArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Generate visualization text from filtered data
        StringBuilder vizText = new StringBuilder();
        vizText.append("FRIEND NETWORK VISUALIZATION\n");
        vizText.append("=============================\n");
        vizText.append("(Based on currently filtered data)\n\n");
        
        // Sort users by network size for visualization
        List<UserNetworkData> sortedUsers = new ArrayList<>(filteredData);
        sortedUsers.sort((u1, u2) -> Integer.compare(u2.totalNetwork, u1.totalNetwork));
        
        int limit = Math.min(10, sortedUsers.size());
        for (int i = 0; i < limit; i++) {
            UserNetworkData user = sortedUsers.get(i);
            
            vizText.append(String.format("%d. %s\n", i + 1, user.fullName));
            vizText.append(String.format("   Username: %s\n", user.username));
            vizText.append(String.format("   Direct Friends: %d\n", user.directFriends));
            vizText.append(String.format("   Total Network: %d\n", user.totalNetwork));
            vizText.append(String.format("   Network Level: %s\n", user.networkLevel));
            
            // Create a simple bar chart for network size
            int barLength = (int) Math.min(50, (user.totalNetwork / 20.0));
            vizText.append("   Network Size: [");
            for (int j = 0; j < barLength; j++) {
                vizText.append("■");
            }
            vizText.append("]\n\n");
        }
        
        visualizationArea.setText(vizText.toString());
        visualizationArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(visualizationArea);
        
        visualizePanel.add(titleLabel, BorderLayout.NORTH);
        visualizePanel.add(scrollPane, BorderLayout.CENTER);
        
        visualizeFrame.add(visualizePanel);
        visualizeFrame.setVisible(true);
        
        JOptionPane.showMessageDialog(null, 
            "Network visualization opened in new window.\n\n" +
            "Key:\n" +
            "• Very High: Large network (>500 connections)\n" +
            "• High: Medium network (200-500 connections)\n" +
            "• Medium: Small network (50-200 connections)\n" +
            "• Low: Very small network (<50 connections)", 
            "Visualization Info", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        statusLabel = new JLabel("Total users displayed: " + userDataList.size());
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(BLUE);
        
        recordLabel = new JLabel("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        recordLabel.setFont(LABEL_FONT);
        recordLabel.setForeground(BLUE);
        
        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(recordLabel, BorderLayout.EAST);
        
        return footerPanel;
    }

    // Custom cell renderer for coloring
    static class NetworkCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                c.setBackground(WHITE);
                
                // Color code based on column
                if (column == 5) { // Network Level column
                    if (value != null) {
                        String level = value.toString();
                        switch (level) {
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
                } else if (column == 3 || column == 4) { // Numeric columns
                    c.setForeground(new Color(0x2980B9)); // Blue for numbers
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    c.setForeground(Color.BLACK);
                    setHorizontalAlignment(JLabel.LEFT);
                }
            }
            
            return c;
        }
    }
}