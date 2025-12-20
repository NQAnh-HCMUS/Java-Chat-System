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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

public class SignUpLog {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color PURPLE = new Color(0x9B59B6);
    private static final Color ORANGE = new Color(0xF39C12);
    private static final Color GREEN = new Color(0x3B8132);
    private static final Color RED = new Color(0xE74C3C);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Data structures
    private static DefaultListModel<String> listModel;
    private static JList<String> logList;
    private static JLabel footerLabel;
    private static List<String> allSignups;
    private static List<String> filteredSignups;
    private static JTextField searchField;
    private static JTextField startField;
    private static JTextField endField;
    private static JTextField startFilterField;
    private static JTextField endFilterField;

    public static void showList() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main frame
            JFrame frame = new JFrame("Sign-up Log - Admin Panel");
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
        allSignups = new ArrayList<>();
        filteredSignups = new ArrayList<>();
        
        // Generate sample sign-up data
        generateSampleData();
        
        // Initialize filtered list with all data
        filteredSignups.addAll(allSignups);
    }

    private static void generateSampleData() {
        allSignups.add("john_doe - 2024-01-15 08:30:00 - Active");
        allSignups.add("jane_smith - 2024-01-15 09:15:00 - Active");
        allSignups.add("mike_wilson - 2024-01-15 10:20:00 - Active");
        allSignups.add("sarah_johnson - 2024-01-16 08:25:00 - Active");
        allSignups.add("david_brown - 2024-01-16 14:30:00 - Locked");
        allSignups.add("emily_davis - 2024-01-17 11:45:00 - Active");
        allSignups.add("robert_miller - 2024-01-18 09:30:00 - Banned");
        allSignups.add("lisa_anderson - 2024-01-18 13:20:00 - Active");
        allSignups.add("admin_user - 2024-01-19 08:00:00 - Active");
        allSignups.add("guest_123 - 2024-01-19 15:45:00 - Inactive");
        allSignups.add("test_account - 2024-01-20 10:30:00 - Active");
        allSignups.add("demo_user - 2024-01-20 16:20:00 - Locked");
        allSignups.add("support_team - 2024-01-21 09:15:00 - Active");
        allSignups.add("moderator_1 - 2024-01-21 14:30:00 - Active");
        allSignups.add("user_alpha - 2024-01-22 11:00:00 - Banned");
        allSignups.add("user_beta - 2024-01-22 16:45:00 - Active");
        allSignups.add("gamma_user - 2024-01-23 08:30:00 - Inactive");
        allSignups.add("delta_test - 2024-01-23 13:15:00 - Active");
        allSignups.add("epsilon_01 - 2024-01-24 10:45:00 - Locked");
        allSignups.add("zeta_user - 2024-01-24 15:30:00 - Active");
    }

    private static void updateListModel() {
        listModel.clear();
        for (String signup : filteredSignups) {
            listModel.addElement(signup);
        }
        updateFooter();
    }

    private static void updateFooter() {
        int total = allSignups.size();
        int active = (int) allSignups.stream()
            .filter(s -> s.contains("Active"))
            .count();
        int locked = (int) allSignups.stream()
            .filter(s -> s.contains("Locked"))
            .count();
        int banned = (int) allSignups.stream()
            .filter(s -> s.contains("Banned"))
            .count();
        
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long todayCount = allSignups.stream()
            .filter(s -> s.contains(today))
            .count();
        
        footerLabel.setText(String.format(
            "Sign-up Log System • Total: %d • Active: %d • Locked: %d • Banned: %d • Today: %d • Showing: %d",
            total, active, locked, banned, todayCount, filteredSignups.size()
        ));
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("Sign-up Log");
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

        searchField = new JTextField(20);
        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        searchField.setToolTipText("Search by username or status...");

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        
        JButton clearBtn = createModernButton("Clear", ORANGE);
        clearBtn.setPreferredSize(new Dimension(80, 35));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchBtn);
        buttonPanel.add(clearBtn);

        // Search button action
        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter search text", 
                    "Search Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            filteredSignups.clear();
            for (String signup : allSignups) {
                if (signup.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredSignups.add(signup);
                }
            }
            
            if (filteredSignups.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No sign-ups found for: " + searchText, 
                    "No Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            updateListModel();
        });
        
        // Clear button action
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            filteredSignups.clear();
            filteredSignups.addAll(allSignups);
            updateListModel();
            JOptionPane.showMessageDialog(null, 
                "Search cleared. Showing all records.", 
                "Clear Search", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Log list panel
        JPanel listPanel = createLogListPanel();
        
        // Filter and action panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createLogListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel listTitle = new JLabel("Sign-up Records");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Log list
        listModel = new DefaultListModel<>();
        
        for (String signup : filteredSignups) {
            listModel.addElement(signup);
        }

        logList = new JList<>(listModel);
        logList.setFont(LIST_FONT);
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logList.setBackground(WHITE);
        logList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));
        
        // Custom cell renderer for coloring
        logList.setCellRenderer(new javax.swing.ListCellRenderer<String>() {
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
                    
                    // Color code based on status
                    if (value.contains("Active")) {
                        label.setForeground(GREEN);
                    } else if (value.contains("Locked")) {
                        label.setForeground(ORANGE);
                    } else if (value.contains("Banned")) {
                        label.setForeground(RED);
                    } else if (value.contains("Inactive")) {
                        label.setForeground(Color.GRAY);
                    } else {
                        label.setForeground(Color.BLACK);
                    }
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(logList);
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
        actionPanel.setPreferredSize(new Dimension(200, 0));

        // Time period filter
        JPanel timeFilterPanel = createTimeFilterPanel();
        actionPanel.add(timeFilterPanel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Status filter
        JPanel statusPanel = createStatusFilterPanel();
        actionPanel.add(statusPanel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action buttons
        JButton nameSortBtn = createModernButton("Sort by Name", BLUE);
        JButton dateSortBtn = createModernButton("Sort by Date", TEAL);
        JButton statusSortBtn = createModernButton("Sort by Status", PURPLE);
        JButton exportBtn = createModernButton("Export Log", PURPLE);
        JButton refreshBtn = createModernButton("Refresh", ORANGE);
        JButton lockBtn = createModernButton("Lock/Unlock", RED);
        JButton deleteBtn = createModernButton("Delete User", new Color(0x7F8C8D));

        // Add buttons to panel
        JButton[] buttons = {nameSortBtn, dateSortBtn, statusSortBtn, lockBtn, 
                            deleteBtn, exportBtn, refreshBtn};
        
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            actionPanel.add(button);
            actionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        actionPanel.add(Box.createVerticalGlue());

        return actionPanel;
    }

    private static JPanel createTimeFilterPanel() {
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBackground(LIGHT_GRAY);
        timePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEAL, 1), "Time Filter"
        ));

        JLabel startLabel = new JLabel("From (YYYY-MM-DD):");
        startLabel.setFont(LABEL_FONT);
        startLabel.setForeground(DARK_BLUE);
        startLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        startFilterField = new JTextField();
        startFilterField.setFont(LABEL_FONT);
        startFilterField.setMaximumSize(new Dimension(180, 25));
        startFilterField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDC3C7), 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        startFilterField.setToolTipText("Format: YYYY-MM-DD");

        JLabel endLabel = new JLabel("To (YYYY-MM-DD):");
        endLabel.setFont(LABEL_FONT);
        endLabel.setForeground(DARK_BLUE);
        endLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        endFilterField = new JTextField();
        endFilterField.setFont(LABEL_FONT);
        endFilterField.setMaximumSize(new Dimension(180, 25));
        endFilterField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDC3C7), 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        endFilterField.setToolTipText("Format: YYYY-MM-DD");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(180, 40));
        
        JButton applyFilterBtn = createModernButton("Apply", GREEN);
        applyFilterBtn.setPreferredSize(new Dimension(80, 30));
        
        JButton clearFilterBtn = createModernButton("Clear", ORANGE);
        clearFilterBtn.setPreferredSize(new Dimension(80, 30));
        
        buttonPanel.add(applyFilterBtn);
        buttonPanel.add(clearFilterBtn);

        // Apply filter action
        applyFilterBtn.addActionListener(e -> {
            String startDate = startFilterField.getText().trim();
            String endDate = endFilterField.getText().trim();
            
            if (startDate.isEmpty() && endDate.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter at least one date", 
                    "Filter Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate date format
            if (!startDate.isEmpty() && !isValidDate(startDate)) {
                JOptionPane.showMessageDialog(null, 
                    "Invalid start date format. Use YYYY-MM-DD", 
                    "Format Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!endDate.isEmpty() && !isValidDate(endDate)) {
                JOptionPane.showMessageDialog(null, 
                    "Invalid end date format. Use YYYY-MM-DD", 
                    "Format Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            filteredSignups.clear();
            for (String signup : allSignups) {
                String signupDate = extractDate(signup);
                
                boolean matches = true;
                if (!startDate.isEmpty() && signupDate.compareTo(startDate) < 0) {
                    matches = false;
                }
                if (!endDate.isEmpty() && signupDate.compareTo(endDate) > 0) {
                    matches = false;
                }
                
                if (matches) {
                    filteredSignups.add(signup);
                }
            }
            
            updateListModel();
            
            String message = String.format("Showing %d sign-ups", filteredSignups.size());
            if (!startDate.isEmpty() || !endDate.isEmpty()) {
                message += String.format(" from %s to %s", 
                    startDate.isEmpty() ? "beginning" : startDate,
                    endDate.isEmpty() ? "now" : endDate);
            }
            
            JOptionPane.showMessageDialog(null, message, "Filter Applied", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Clear filter action
        clearFilterBtn.addActionListener(e -> {
            startFilterField.setText("");
            endFilterField.setText("");
            filteredSignups.clear();
            filteredSignups.addAll(allSignups);
            updateListModel();
            JOptionPane.showMessageDialog(null, 
                "Time filter cleared", 
                "Filter Cleared", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        timePanel.add(startLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        timePanel.add(startFilterField);
        timePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        timePanel.add(endLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        timePanel.add(endFilterField);
        timePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        timePanel.add(buttonPanel);

        return timePanel;
    }

    private static JPanel createStatusFilterPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(LIGHT_GRAY);
        statusPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PURPLE, 1), "Status Filter"
        ));

        JButton activeBtn = createModernButton("Active Only", GREEN);
        activeBtn.setMaximumSize(new Dimension(180, 25));
        activeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton lockedBtn = createModernButton("Locked Only", ORANGE);
        lockedBtn.setMaximumSize(new Dimension(180, 25));
        lockedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton bannedBtn = createModernButton("Banned Only", RED);
        bannedBtn.setMaximumSize(new Dimension(180, 25));
        bannedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton allBtn = createModernButton("Show All", BLUE);
        allBtn.setMaximumSize(new Dimension(180, 25));
        allBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status filter actions
        activeBtn.addActionListener(e -> filterByStatus("Active"));
        lockedBtn.addActionListener(e -> filterByStatus("Locked"));
        bannedBtn.addActionListener(e -> filterByStatus("Banned"));
        allBtn.addActionListener(e -> {
            filteredSignups.clear();
            filteredSignups.addAll(allSignups);
            updateListModel();
            JOptionPane.showMessageDialog(null, 
                "Showing all statuses", 
                "Status Filter", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        statusPanel.add(activeBtn);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(lockedBtn);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(bannedBtn);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(allBtn);

        return statusPanel;
    }

    private static void filterByStatus(String status) {
        filteredSignups.clear();
        for (String signup : allSignups) {
            if (signup.contains(status)) {
                filteredSignups.add(signup);
            }
        }
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            String.format("Showing %d %s users", filteredSignups.size(), status), 
            "Status Filter", 
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
            case "Sort by Name":
                sortByName();
                break;
            case "Sort by Date":
                sortByDate();
                break;
            case "Sort by Status":
                sortByStatus();
                break;
            case "Export Log":
                exportLog();
                break;
            case "Refresh":
                refreshData();
                break;
            case "Lock/Unlock":
                toggleLockStatus();
                break;
            case "Delete User":
                deleteUser();
                break;
        }
    }

    private static void sortByName() {
        filteredSignups.sort((s1, s2) -> {
            String name1 = extractUsername(s1);
            String name2 = extractUsername(s2);
            return name1.compareToIgnoreCase(name2);
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by username (A-Z)", 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void sortByDate() {
        filteredSignups.sort((s1, s2) -> {
            String date1 = extractDateTime(s1);
            String date2 = extractDateTime(s2);
            return date2.compareTo(date1); // Newest first
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by date (newest first)", 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void sortByStatus() {
        filteredSignups.sort((s1, s2) -> {
            String status1 = extractStatus(s1);
            String status2 = extractStatus(s2);
            return status1.compareTo(status2);
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by status", 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void toggleLockStatus() {
        int selectedIndex = logList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a user first", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedSignup = filteredSignups.get(selectedIndex);
        String username = extractUsername(selectedSignup);
        String currentStatus = extractStatus(selectedSignup);
        
        String newStatus;
        if (currentStatus.equals("Locked")) {
            newStatus = "Active";
        } else {
            newStatus = "Locked";
        }
        
        int response = JOptionPane.showConfirmDialog(null,
            String.format("Do you want to %s the account '%s'?\nCurrent status: %s", 
                newStatus.equals("Locked") ? "LOCK" : "UNLOCK",
                username, currentStatus),
            "Confirm Account Status Change",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            // Update in filtered list
            String updatedSignup = selectedSignup.replace(currentStatus, newStatus);
            filteredSignups.set(selectedIndex, updatedSignup);
            
            // Update in all signups list
            for (int i = 0; i < allSignups.size(); i++) {
                if (allSignups.get(i).equals(selectedSignup)) {
                    allSignups.set(i, updatedSignup);
                    break;
                }
            }
            
            updateListModel();
            
            JOptionPane.showMessageDialog(null,
                String.format("Account '%s' is now %s", username, newStatus),
                "Status Updated",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void deleteUser() {
        int selectedIndex = logList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a user to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedSignup = filteredSignups.get(selectedIndex);
        String username = extractUsername(selectedSignup);
        
        int response = JOptionPane.showConfirmDialog(null,
            String.format("Are you sure you want to DELETE user '%s'?\nThis action cannot be undone!", 
                username),
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            // Remove from filtered list
            filteredSignups.remove(selectedIndex);
            
            // Remove from all signups list
            for (int i = 0; i < allSignups.size(); i++) {
                if (allSignups.get(i).equals(selectedSignup)) {
                    allSignups.remove(i);
                    break;
                }
            }
            
            updateListModel();
            
            JOptionPane.showMessageDialog(null,
                String.format("User '%s' has been deleted", username),
                "User Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void exportLog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Sign-up Log");
        fileChooser.setSelectedFile(new File("signup_log_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                writer.write("Username,Sign-up Date,Sign-up Time,Status\n");
                
                // Write data
                for (String signup : allSignups) {
                    String[] parts = signup.split(" - ");
                    if (parts.length >= 3) {
                        String username = parts[0].trim();
                        String datetime = parts[1].trim();
                        String status = parts[2].trim();
                        
                        String[] datetimeParts = datetime.split(" ");
                        String date = datetimeParts[0];
                        String time = datetimeParts.length > 1 ? datetimeParts[1] : "";
                        
                        writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                            username, date, time, status));
                    }
                }
                
                JOptionPane.showMessageDialog(null,
                    String.format("Successfully exported %d sign-ups to:\n%s",
                        allSignups.size(), fileToSave.getAbsolutePath()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                    "Error exporting log: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void refreshData() {
        // Clear all filters
        searchField.setText("");
        startFilterField.setText("");
        endFilterField.setText("");
        
        // Reset filtered list
        filteredSignups.clear();
        filteredSignups.addAll(allSignups);
        updateListModel();
        
        JOptionPane.showMessageDialog(null,
            "Data refreshed successfully\nShowing all " + allSignups.size() + " sign-ups",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper methods
    private static String extractUsername(String signup) {
        String[] parts = signup.split(" - ");
        return parts.length > 0 ? parts[0].trim() : "";
    }

    private static String extractDateTime(String signup) {
        String[] parts = signup.split(" - ");
        return parts.length > 1 ? parts[1].trim() : "";
    }

    private static String extractDate(String signup) {
        String datetime = extractDateTime(signup);
        String[] parts = datetime.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    private static String extractStatus(String signup) {
        String[] parts = signup.split(" - ");
        return parts.length > 2 ? parts[2].trim() : "";
    }

    private static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        footerLabel = new JLabel("Sign-up Log System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        updateFooter();
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
}