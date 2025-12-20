import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class UserManager {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color RED = new Color(0xE74C3C);
    private static final Color GREEN = new Color(0x2ECC71);
    private static final Color ORANGE = new Color(0xF39C12);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    
    // Data structures
    private static DefaultListModel<String> listModel;
    private static JList<String> userList;
    private static Map<String, User> users = new HashMap<>();
    private static JFrame mainFrame;
    private static JTextField searchField;
    private static JComboBox<String> sortComboBox;
    private static JComboBox<String> sortOrderComboBox;
    
    // Sorting options
    private static final String SORT_BY_NAME = "Sort by Name";
    private static final String SORT_BY_EMAIL = "Sort by Email";
    private static final String SORT_BY_STATUS = "Sort by Status";
    private static final String SORT_BY_CREATED_DATE = "Sort by Created Date";
    private static final String SORT_BY_LAST_ACTIVE = "Sort by Last Active";
    private static final String SORT_ASCENDING = "Ascending";
    private static final String SORT_DESCENDING = "Descending";
    
    // Current sorting
    private static String currentSortBy = SORT_BY_NAME;
    private static String currentSortOrder = SORT_ASCENDING;

    public static void showUser() {
        SwingUtilities.invokeLater(() -> {
            // Initialize users data
            loadUsersData();
            
            // Main frame
            mainFrame = new JFrame("User Management - Admin Panel");
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainFrame.setSize(1000, 650);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setLayout(new BorderLayout(10, 10));

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Header Panel
            JPanel headerPanel = createHeaderPanel();
            
            // Main Content Panel
            JPanel mainPanel = createMainPanel();
            
            // Footer Panel
            JPanel footerPanel = createFooterPanel();

            // Add panels to frame
            mainFrame.add(headerPanel, BorderLayout.NORTH);
            mainFrame.add(mainPanel, BorderLayout.CENTER);
            mainFrame.add(footerPanel, BorderLayout.SOUTH);

            mainFrame.setVisible(true);
        });
    }

    private static void loadUsersData() {
        try {
            List<String> friendLines = loadFriends();
            for (String line : friendLines) {
                User user = User.fromString(line);
                users.put(user.getEmail(), user);
            }
        } catch (IOException ex) {
            System.out.println("Error loading users: " + ex.getMessage());
            // Add sample users with varied dates
            LocalDateTime now = LocalDateTime.now();
            users.put("john.doe@email.com", new User("John Doe", "john.doe@email.com", "Active", now.minusDays(5)));
            users.put("jane.smith@email.com", new User("Jane Smith", "jane.smith@email.com", "Active", now.minusDays(3)));
            users.put("mike.wilson@email.com", new User("Mike Wilson", "mike.wilson@email.com", "Locked", now.minusDays(10)));
            users.put("sarah.johnson@email.com", new User("Sarah Johnson", "sarah.johnson@email.com", "Active", now.minusDays(1)));
            users.put("alice.brown@email.com", new User("Alice Brown", "alice.brown@email.com", "Active", now.minusDays(15)));
            users.put("bob.miller@email.com", new User("Bob Miller", "bob.miller@email.com", "Active", now.minusDays(7)));
            users.put("charlie.davis@email.com", new User("Charlie Davis", "charlie.davis@email.com", "Locked", now.minusDays(20)));
            saveUsersToFile();
        }
    }

    private static void saveUsersToFile() {
        try {
            Path fList = Paths.get("script").resolve("friends.txt");
            if (fList.getParent() != null) Files.createDirectories(fList.getParent());
            
            List<String> lines = new ArrayList<>();
            for (User user : users.values()) {
                lines.add(user.toFileString());
            }
            
            Files.write(fList, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void refreshUserList() {
        listModel.clear();
        List<User> sortedUsers = getSortedUsers();
        sortedUsers.stream()
            .map(User::toString)
            .forEach(listModel::addElement);
        updateFooterStats();
    }

    private static List<User> getSortedUsers() {
        List<User> userList = new ArrayList<>(users.values());
        
        Comparator<User> comparator = null;
        
        switch (currentSortBy) {
            case SORT_BY_NAME:
                comparator = Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_EMAIL:
                comparator = Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_STATUS:
                comparator = Comparator.comparing(User::getStatus, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_CREATED_DATE:
                comparator = Comparator.comparing(User::getCreatedAt);
                break;
            case SORT_BY_LAST_ACTIVE:
                comparator = Comparator.comparing(User::getLastActive);
                break;
            default:
                comparator = Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
        }
        
        if (currentSortOrder.equals(SORT_DESCENDING)) {
            comparator = comparator.reversed();
        }
        
        return userList.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }

    private static void updateFooterStats() {
        long activeUsers = users.values().stream()
            .filter(u -> u.getStatus().equals("Active"))
            .count();
        long lockedUsers = users.values().stream()
            .filter(u -> u.getStatus().equals("Locked"))
            .count();
        
        if (mainFrame != null) {
            Component[] components = mainFrame.getContentPane().getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    Component[] subComps = ((JPanel) comp).getComponents();
                    for (Component subComp : subComps) {
                        if (subComp instanceof JLabel) {
                            JLabel label = (JLabel) subComp;
                            if (label.getText().contains("Total Users")) {
                                label.setText(String.format("User Management System • Total Users: %d • Active: %d • Locked: %d", 
                                    users.size(), activeUsers, lockedUsers));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(WHITE);
        
        // Right panel with search and sort
        JPanel rightPanel = new JPanel(new BorderLayout(15, 0));
        rightPanel.setBackground(DARK_BLUE);
        rightPanel.setOpaque(false);
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        
        // Sort panel
        JPanel sortPanel = createSortPanel();
        
        rightPanel.add(searchPanel, BorderLayout.WEST);
        rightPanel.add(sortPanel, BorderLayout.EAST);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(DARK_BLUE);
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchLabel.setForeground(LIGHT_TEAL);

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        
        // Add search functionality
        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        return searchPanel;
    }

    private static JPanel createSortPanel() {
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        sortPanel.setBackground(DARK_BLUE);
        sortPanel.setOpaque(false);

        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortLabel.setForeground(LIGHT_TEAL);

        String[] sortOptions = {SORT_BY_NAME, SORT_BY_EMAIL, SORT_BY_STATUS, 
                                SORT_BY_CREATED_DATE, SORT_BY_LAST_ACTIVE};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortComboBox.setBackground(WHITE);
        sortComboBox.setPreferredSize(new Dimension(150, 30));
        sortComboBox.addActionListener(e -> {
            currentSortBy = (String) sortComboBox.getSelectedItem();
            refreshUserList();
        });

        String[] orderOptions = {SORT_ASCENDING, SORT_DESCENDING};
        sortOrderComboBox = new JComboBox<>(orderOptions);
        sortOrderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortOrderComboBox.setBackground(WHITE);
        sortOrderComboBox.setPreferredSize(new Dimension(120, 30));
        sortOrderComboBox.addActionListener(e -> {
            currentSortOrder = (String) sortOrderComboBox.getSelectedItem();
            refreshUserList();
        });

        sortPanel.add(sortLabel);
        sortPanel.add(sortComboBox);
        sortPanel.add(sortOrderComboBox);

        return sortPanel;
    }

    private static void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            refreshUserList();
            return;
        }

        listModel.clear();
        users.values().stream()
            .filter(user -> user.getName().toLowerCase().contains(query) ||
                           user.getEmail().toLowerCase().contains(query) ||
                           user.getStatus().toLowerCase().contains(query))
            .sorted(getCurrentComparator())
            .map(User::toString)
            .forEach(listModel::addElement);
    }

    private static Comparator<User> getCurrentComparator() {
        Comparator<User> comparator = null;
        
        switch (currentSortBy) {
            case SORT_BY_NAME:
                comparator = Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_EMAIL:
                comparator = Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_STATUS:
                comparator = Comparator.comparing(User::getStatus, String.CASE_INSENSITIVE_ORDER);
                break;
            case SORT_BY_CREATED_DATE:
                comparator = Comparator.comparing(User::getCreatedAt);
                break;
            case SORT_BY_LAST_ACTIVE:
                comparator = Comparator.comparing(User::getLastActive);
                break;
            default:
                comparator = Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
        }
        
        if (currentSortOrder.equals(SORT_DESCENDING)) {
            comparator = comparator.reversed();
        }
        
        return comparator;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // User list panel
        JPanel listPanel = createUserListPanel();
        
        // Action buttons panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createUserListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel listTitle = new JLabel("User List");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // User list
        listModel = new DefaultListModel<>();
        refreshUserList();

        userList = new JList<>(listModel);
        userList.setFont(LIST_FONT);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(WHITE);
        userList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

        JScrollPane scrollPane = new JScrollPane(userList);
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
        JButton addBtn = createModernButton("Add User", GREEN);
        JButton updateBtn = createModernButton("Update User", BLUE);
        JButton deleteBtn = createModernButton("Delete User", RED);
        JButton detailsBtn = createModernButton("View Details", TEAL);
        JButton lockBtn = createModernButton("Lock/Unlock", new Color(0xF39C12));
        JButton passwdBtn = createModernButton("Change Password", new Color(0x9B59B6));
        JButton resetPwdBtn = createModernButton("Reset Password", new Color(0xE67E22));
        JButton historyBtn = createModernButton("Login History", new Color(0x34495E));
        JButton friendlistBtn = createModernButton("Friend List", new Color(0x16A085));

        // Add action listeners
        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        deleteBtn.addActionListener(e -> deleteUserWithConfirmation());
        detailsBtn.addActionListener(e -> viewDetails());
        lockBtn.addActionListener(e -> lockUnlockUser());
        passwdBtn.addActionListener(e -> changePassword());
        resetPwdBtn.addActionListener(e -> resetPassword());
        historyBtn.addActionListener(e -> showLoginHistory());
        friendlistBtn.addActionListener(e -> showFriendList());

        // Add buttons to panel
        JButton[] buttons = {addBtn, updateBtn, deleteBtn, detailsBtn, lockBtn, 
                           passwdBtn, resetPwdBtn, historyBtn, friendlistBtn};
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(160, 40));
            actionPanel.add(button);
            actionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        actionPanel.add(Box.createVerticalGlue());

        return actionPanel;
    }

    private static void deleteUserWithConfirmation() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Please select a user first!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        // Create custom confirmation dialog
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel warningIcon = new JLabel("⚠️");
        warningIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        warningIcon.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel messageLabel = new JLabel("<html><div style='width: 300px; text-align: center;'>"
                + "<b style='color: #e74c3c; font-size: 14px;'>WARNING: This action cannot be undone!</b><br><br>"
                + "Are you sure you want to delete the following user?<br><br>"
                + "<b>Name:</b> " + user.getName() + "<br>"
                + "<b>Email:</b> " + user.getEmail() + "<br>"
                + "<b>Status:</b> " + user.getStatus() + "<br><br>"
                + "This will permanently remove all user data."
                + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(warningIcon, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        // Show custom confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(mainFrame, 
            panel, 
            "Confirm User Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Ask for final confirmation
            JTextField confirmField = new JTextField();
            JPanel finalConfirmPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            finalConfirmPanel.add(new JLabel("Type 'DELETE' to confirm:"));
            finalConfirmPanel.add(confirmField);
            
            int finalConfirm = JOptionPane.showConfirmDialog(mainFrame, 
                finalConfirmPanel, 
                "Final Confirmation", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (finalConfirm == JOptionPane.OK_OPTION && 
                confirmField.getText().trim().equalsIgnoreCase("DELETE")) {
                
                // Perform deletion
                users.remove(email);
                saveUsersToFile();
                refreshUserList();
                
                JOptionPane.showMessageDialog(mainFrame, 
                    "User '" + user.getName() + "' has been deleted successfully!", 
                    "Deletion Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else if (finalConfirm == JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Confirmation text does not match. Deletion cancelled.", 
                    "Cancelled", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static void addUser() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Add New User", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (users.containsKey(email)) {
                JOptionPane.showMessageDialog(mainFrame, "User with this email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = new User(name, email, "Active", LocalDateTime.now());
            users.put(email, newUser);
            saveUsersToFile();
            refreshUserList();
            
            JOptionPane.showMessageDialog(mainFrame, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void updateUser() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(user.getName());
        
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Status:"));
        panel.add(new JLabel(user.getStatus()));

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Update User", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(user.getName())) {
                user.setName(newName);
                saveUsersToFile();
                refreshUserList();
                JOptionPane.showMessageDialog(mainFrame, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void viewDetails() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String details = String.format(
            "<html><body style='width: 350px'>" +
            "<h3 style='color: #001C44;'>User Details</h3>" +
            "<table style='width: 100%%; border-collapse: collapse;'>" +
            "<tr><td style='padding: 5px;'><b>Full Name:</b></td><td style='padding: 5px;'>%s</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Email:</b></td><td style='padding: 5px;'>%s</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Status:</b></td><td style='padding: 5px; color: %s;'>%s</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Last Active:</b></td><td style='padding: 5px;'>%s</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Account Created:</b></td><td style='padding: 5px;'>%s</td></tr>" +
            "</table>" +
            "</body></html>",
            user.getName(),
            user.getEmail(),
            user.getStatus().equals("Active") ? "green" : "red",
            user.getStatus(),
            user.getLastActive().format(formatter),
            user.getCreatedAt().format(dateFormatter)
        );
        
        JOptionPane.showMessageDialog(mainFrame, details, "User Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void lockUnlockUser() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        String currentStatus = user.getStatus();
        String newStatus = currentStatus.equals("Active") ? "Locked" : "Active";
        String action = currentStatus.equals("Active") ? "lock" : "unlock";
        String actionMessage = currentStatus.equals("Active") ? 
            "Locking will prevent this user from logging in." : 
            "Unlocking will allow this user to log in again.";
        
        JPanel confirmPanel = new JPanel(new BorderLayout(10, 10));
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel iconLabel = new JLabel(currentStatus.equals("Active") ? "🔒" : "🔓");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel messageLabel = new JLabel("<html><div style='width: 300px; text-align: center;'>"
                + "Are you sure you want to " + action + " user:<br><br>"
                + "<b>" + user.getName() + "</b><br>"
                + "(" + user.getEmail() + ")<br><br>"
                + actionMessage + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        confirmPanel.add(iconLabel, BorderLayout.NORTH);
        confirmPanel.add(messageLabel, BorderLayout.CENTER);
        
        int confirm = JOptionPane.showConfirmDialog(mainFrame, 
            confirmPanel,
            "Confirm " + (currentStatus.equals("Active") ? "Lock User" : "Unlock User"), 
            JOptionPane.YES_NO_OPTION,
            currentStatus.equals("Active") ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            user.setStatus(newStatus);
            user.setLastActive(LocalDateTime.now());
            saveUsersToFile();
            refreshUserList();
            JOptionPane.showMessageDialog(mainFrame, 
                String.format("User <b>%s</b> has been %sed successfully!", user.getName(), action),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void changePassword() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Change Password for " + user.getName(), 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(mainFrame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // In real application, you would hash the password
            JOptionPane.showMessageDialog(mainFrame, 
                "<html>Password changed successfully for user: <b>" + user.getName() + "</b></html>",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void resetPassword() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        // Generate random password
        String newPassword = generateRandomPassword(12);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel iconLabel = new JLabel("🔑");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel messageLabel = new JLabel("<html><div style='width: 350px; text-align: center;'>"
                + "<b>Reset Password for " + user.getName() + "</b><br><br>"
                + "A new password will be generated and should be sent to:<br>"
                + "<b>" + user.getEmail() + "</b><br><br>"
                + "Do you want to proceed?</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        int confirm = JOptionPane.showConfirmDialog(mainFrame, 
            panel,
            "Reset Password", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Show the new password (in real app, this would be sent via email)
            JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel resultIcon = new JLabel("✅");
            resultIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            resultIcon.setHorizontalAlignment(JLabel.CENTER);
            
            JLabel resultMessage = new JLabel("<html><div style='width: 350px; text-align: center;'>"
                    + "<b>Password Reset Successful!</b><br><br>"
                    + "New password for <b>" + user.getName() + "</b>:<br>"
                    + "<div style='background: #f8f9fa; padding: 10px; margin: 10px; border-radius: 5px; border: 1px solid #ddd; font-family: monospace;'>"
                    + newPassword
                    + "</div><br>"
                    + "Please send this password to the user via secure email.<br>"
                    + "The user should change this password immediately after login."
                    + "</div></html>");
            resultMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            resultPanel.add(resultIcon, BorderLayout.NORTH);
            resultPanel.add(resultMessage, BorderLayout.CENTER);
            
            JOptionPane.showMessageDialog(mainFrame, resultPanel, "Password Reset Complete", JOptionPane.INFORMATION_MESSAGE);
            
            // Log this action
            System.out.println("Password reset for " + user.getEmail() + " at " + LocalDateTime.now());
        }
    }

    private static String generateRandomPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+";
        
        String allChars = upper + lower + digits + special;
        Random random = new Random();
        
        // Ensure password contains at least one of each character type
        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }

    private static void showLoginHistory() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        // Simulate login history
        List<String> loginHistory = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        LocalDateTime now = LocalDateTime.now();
        loginHistory.add("• " + now.format(formatter) + " - Login successful");
        loginHistory.add("• " + now.minusHours(2).format(formatter) + " - Login successful");
        loginHistory.add("• " + now.minusDays(1).format(formatter) + " - Login successful");
        loginHistory.add("• " + now.minusDays(2).format(formatter) + " - Failed attempt (wrong password)");
        loginHistory.add("• " + now.minusDays(3).format(formatter) + " - Login successful");
        
        String historyText = String.format(
            "<html><body style='width: 350px'><h3>Login History for %s</h3>%s</body></html>",
            user.getName(),
            String.join("<br>", loginHistory)
        );
        
        JOptionPane.showMessageDialog(mainFrame, historyText, "Login History", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showFriendList() {
        String selected = userList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String email = extractEmailFromString(selected);
        User user = users.get(email);
        if (user == null) return;
        
        // Simulate friend list
        List<String> friends = users.values().stream()
            .filter(u -> !u.getEmail().equals(email))
            .map(User::getName)
            .limit(5) // Show only 5 friends as example
            .collect(Collectors.toList());
        
        if (friends.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, 
                user.getName() + " has no friends yet.", 
                "Friend List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String friendListText = String.format(
            "<html><body style='width: 300px'><h3>%s's Friends (%d)</h3><ul>%s</ul></body></html>",
            user.getName(),
            friends.size(),
            friends.stream().map(f -> "<li>" + f + "</li>").collect(Collectors.joining())
        );
        
        JOptionPane.showMessageDialog(mainFrame, friendListText, "Friend List", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String extractEmailFromString(String userString) {
        // Extract email from string like "John Doe (Active) - john.doe@email.com"
        if (userString.contains("-")) {
            return userString.split("-")[1].trim();
        }
        return "";
    }

    private static JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(LIGHT_TEAL); 
        button.setForeground(DARK_BLUE);  
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
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
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("User Management System • Total Users: 0 • Active: 0 • Locked: 0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private static List<String> loadFriends() throws IOException {
        Path fList = Paths.get("script").resolve("friends.txt");
        if (!Files.exists(fList)) {
            if (fList.getParent() != null) Files.createDirectories(fList.getParent());
            List<String> sample = List.of(
                "John Doe (Active) - john.doe@email.com - " + LocalDateTime.now().minusDays(5).toString(),
                "Jane Smith (Active) - jane.smith@email.com - " + LocalDateTime.now().minusDays(3).toString(),
                "Mike Wilson (Locked) - mike.wilson@email.com - " + LocalDateTime.now().minusDays(10).toString(),
                "Sarah Johnson (Active) - sarah.johnson@email.com - " + LocalDateTime.now().minusDays(1).toString(),
                "Alice Brown (Active) - alice.brown@email.com - " + LocalDateTime.now().minusDays(15).toString(),
                "Bob Miller (Active) - bob.miller@email.com - " + LocalDateTime.now().minusDays(7).toString(),
                "Charlie Davis (Locked) - charlie.davis@email.com - " + LocalDateTime.now().minusDays(20).toString()
            );
            Files.write(fList, sample, StandardCharsets.UTF_8);
            return new ArrayList<>(sample);
        }
        List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
        return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }

    // User class to represent user data
    static class User {
        private String name;
        private String email;
        private String status;
        private LocalDateTime lastActive;
        private LocalDateTime createdAt;

        public User(String name, String email, String status, LocalDateTime lastActive) {
            this.name = name;
            this.email = email;
            this.status = status;
            this.lastActive = lastActive;
            this.createdAt = lastActive.minusDays(30); // Simulate account creation 30 days before first activity
        }

        public static User fromString(String str) {
            try {
                // Format: "Name (Status) - email - lastActive"
                String[] parts = str.split(" - ");
                String nameStatus = parts[0];
                String email = parts[1];
                String lastActiveStr = parts.length > 2 ? parts[2] : LocalDateTime.now().toString();
                
                String name = nameStatus.substring(0, nameStatus.indexOf('(')).trim();
                String status = nameStatus.substring(nameStatus.indexOf('(') + 1, nameStatus.indexOf(')')).trim();
                
                LocalDateTime lastActive = LocalDateTime.parse(lastActiveStr);
                return new User(name, email, status, lastActive);
            } catch (Exception e) {
                return new User("Unknown", "unknown@email.com", "Active", LocalDateTime.now());
            }
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - %s", name, status, email);
        }

        public String toFileString() {
            return String.format("%s (%s) - %s - %s", name, status, email, lastActive.toString());
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public LocalDateTime getLastActive() { return lastActive; }
        public void setLastActive(LocalDateTime lastActive) { this.lastActive = lastActive; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}