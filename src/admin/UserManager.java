import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    
    // Database connection
    private static Connection connection;
    private static JFrame mainFrame;
    private static JTextField searchField;
    private static JTable userTable;
    private static DefaultTableModel tableModel;
    
    // Database configuration
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ChatSystem;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";

    public static void showUser() {
        SwingUtilities.invokeLater(() -> {
            // Initialize database connection
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connected to database successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Database connection failed: " + e.getMessage(), 
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            // Main frame
            mainFrame = new JFrame("User Management - Admin Panel");
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainFrame.setSize(1000, 700);
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
            
            // Load initial data
            refreshUserTable();
        });
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("User Management - ChatSystem");
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
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchLabel.setForeground(LIGHT_TEAL);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        
        // Add search functionality
        searchBtn.addActionListener(e -> searchUsers());
        searchField.addActionListener(e -> searchUsers());

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        return searchPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // User table panel
        JPanel tablePanel = createUserTablePanel();
        
        // Action buttons panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createUserTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel tableTitle = new JLabel("User List");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(DARK_BLUE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // User table
        String[] columnNames = {"ID", "Username", "Full Name", "Email", "Status", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTable.setRowHeight(25);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(DARK_BLUE);
        userTable.getTableHeader().setForeground(WHITE);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private static JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(LIGHT_GRAY);
        actionPanel.setPreferredSize(new Dimension(180, 0));

        // Action buttons
        JButton refreshBtn = createModernButton("Refresh", TEAL);
        JButton addBtn = createModernButton("Add User", GREEN);
        JButton updateBtn = createModernButton("Update User", BLUE);
        JButton detailsBtn = createModernButton("View Details", new Color(0x3498DB));
        JButton lockBtn = createModernButton("Lock/Unlock", new Color(0xF39C12));
        JButton passwdBtn = createModernButton("Change Password", new Color(0x9B59B6));
        JButton historyBtn = createModernButton("Login History", new Color(0x34495E));
        JButton friendlistBtn = createModernButton("Friend List", new Color(0x16A085));
        JButton deleteBtn = createModernButton("Delete User", RED);

        // Add action listeners
        refreshBtn.addActionListener(e -> refreshUserTable());
        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        detailsBtn.addActionListener(e -> viewDetails());
        lockBtn.addActionListener(e -> lockUnlockUser());
        passwdBtn.addActionListener(e -> changePassword());
        historyBtn.addActionListener(e -> showLoginHistory());
        friendlistBtn.addActionListener(e -> showFriendList());
        deleteBtn.addActionListener(e -> deleteUser());

        // Add buttons to panel
        JButton[] buttons = {refreshBtn, addBtn, updateBtn, detailsBtn, lockBtn, 
                            passwdBtn, historyBtn, friendlistBtn, deleteBtn};
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(160, 40));
            actionPanel.add(button);
            actionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        actionPanel.add(Box.createVerticalGlue());

        return actionPanel;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("ChatSystem Database • Total Users: 0 • Active: 0 • Locked: 0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        footerLabel.setName("statsLabel");
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private static void refreshUserTable() {
        try {
            tableModel.setRowCount(0);
            
            String sql = "SELECT user_id, username, full_name, email, " +
                        "CASE WHEN is_active = 1 THEN 'Active' ELSE 'Inactive' END as status, " +
                        "created_at FROM users ORDER BY user_id";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            int totalUsers = 0;
            int activeUsers = 0;
            int lockedUsers = 0;
            
            while (rs.next()) {
                totalUsers++;
                String status = rs.getString("status");
                if ("Active".equals(status)) activeUsers++;
                if ("Locked".equals(rs.getString("is_locked"))) lockedUsers++;
                
                Object[] row = {
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    status,
                    rs.getTimestamp("created_at")
                };
                tableModel.addRow(row);
            }
            
            rs.close();
            stmt.close();
            
            // Update footer stats
            updateFooterStats(totalUsers, activeUsers, lockedUsers);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error loading users: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void searchUsers() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshUserTable();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            
            String sql = "SELECT user_id, username, full_name, email, " +
                        "CASE WHEN is_active = 1 THEN 'Active' ELSE 'Inactive' END as status, " +
                        "created_at FROM users " +
                        "WHERE username LIKE ? OR full_name LIKE ? OR email LIKE ? " +
                        "ORDER BY user_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            int resultCount = 0;
            while (rs.next()) {
                resultCount++;
                Object[] row = {
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                };
                tableModel.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            
            if (resultCount == 0) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "No users found matching: " + query, 
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Search error: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void addUser() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField usernameField = new JTextField();
        JTextField fullnameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullnameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Add New User", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String fullname = fullnameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String gender = (String) genderCombo.getSelectedItem();
            
            if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                String sql = "INSERT INTO users (username, password, full_name, email, gender) " +
                           "VALUES (?, ?, ?, ?, ?)";
                
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password); // In real app, hash the password
                pstmt.setString(3, fullname);
                pstmt.setString(4, email);
                pstmt.setString(5, gender);
                
                int rows = pstmt.executeUpdate();
                pstmt.close();
                
                if (rows > 0) {
                    JOptionPane.showMessageDialog(mainFrame, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserTable();
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Error adding user: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                JTextField fullnameField = new JTextField(rs.getString("full_name"));
                JTextField addressField = new JTextField(rs.getString("address"));
                JTextField dobField = new JTextField(rs.getDate("date_of_birth") != null ? 
                    rs.getDate("date_of_birth").toString() : "");
                JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
                genderCombo.setSelectedItem(rs.getString("gender"));
                
                panel.add(new JLabel("Full Name:"));
                panel.add(fullnameField);
                panel.add(new JLabel("Address:"));
                panel.add(addressField);
                panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
                panel.add(dobField);
                panel.add(new JLabel("Gender:"));
                panel.add(genderCombo);

                int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Update User", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    String sqlUpdate = "UPDATE users SET full_name = ?, address = ?, " +
                                     "date_of_birth = ?, gender = ?, updated_at = GETDATE() " +
                                     "WHERE user_id = ?";
                    
                    PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
                    pstmtUpdate.setString(1, fullnameField.getText().trim());
                    pstmtUpdate.setString(2, addressField.getText().trim());
                    pstmtUpdate.setString(3, dobField.getText().trim());
                    pstmtUpdate.setString(4, (String) genderCombo.getSelectedItem());
                    pstmtUpdate.setInt(5, userId);
                    
                    int rows = pstmtUpdate.executeUpdate();
                    pstmtUpdate.close();
                    
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(mainFrame, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshUserTable();
                    }
                }
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error updating user: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void viewDetails() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                StringBuilder details = new StringBuilder();
                details.append("<html><body style='width: 350px'>");
                details.append("<h3>User Details</h3>");
                details.append("<b>User ID:</b> ").append(rs.getInt("user_id")).append("<br>");
                details.append("<b>Username:</b> ").append(rs.getString("username")).append("<br>");
                details.append("<b>Full Name:</b> ").append(rs.getString("full_name")).append("<br>");
                details.append("<b>Email:</b> ").append(rs.getString("email")).append("<br>");
                details.append("<b>Address:</b> ").append(rs.getString("address") != null ? rs.getString("address") : "N/A").append("<br>");
                details.append("<b>Date of Birth:</b> ").append(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth") : "N/A").append("<br>");
                details.append("<b>Gender:</b> ").append(rs.getString("gender")).append("<br>");
                details.append("<b>Status:</b> ").append(rs.getBoolean("is_active") ? "Active" : "Inactive").append("<br>");
                details.append("<b>Locked:</b> ").append(rs.getBoolean("is_locked") ? "Yes" : "No").append("<br>");
                details.append("<b>Created At:</b> ").append(rs.getTimestamp("created_at")).append("<br>");
                details.append("<b>Last Updated:</b> ").append(rs.getTimestamp("updated_at")).append("<br>");
                details.append("</body></html>");
                
                JOptionPane.showMessageDialog(mainFrame, details.toString(), "User Details", JOptionPane.INFORMATION_MESSAGE);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error loading user details: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void lockUnlockUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            // Get current lock status
            String sqlCheck = "SELECT is_locked FROM users WHERE user_id = ?";
            PreparedStatement pstmtCheck = connection.prepareStatement(sqlCheck);
            pstmtCheck.setInt(1, userId);
            ResultSet rs = pstmtCheck.executeQuery();
            
            boolean isLocked = false;
            if (rs.next()) {
                isLocked = rs.getBoolean("is_locked");
            }
            rs.close();
            pstmtCheck.close();
            
            String action = isLocked ? "unlock" : "lock";
            int confirm = JOptionPane.showConfirmDialog(mainFrame, 
                String.format("Are you sure you want to %s user: %s?", action, username),
                "Confirm " + action, JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String sqlUpdate = "UPDATE users SET is_locked = ?, updated_at = GETDATE() WHERE user_id = ?";
                PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
                pstmtUpdate.setBoolean(1, !isLocked);
                pstmtUpdate.setInt(2, userId);
                
                int rows = pstmtUpdate.executeUpdate();
                pstmtUpdate.close();
                
                if (rows > 0) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        String.format("User %s has been %sed successfully!", username, action),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserTable();
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error updating user: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void changePassword() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Change Password for " + username, 
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
            
            try {
                String sql = "UPDATE users SET password = ?, updated_at = GETDATE() WHERE user_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, password); // In real app, hash the password
                pstmt.setInt(2, userId);
                
                int rows = pstmt.executeUpdate();
                pstmt.close();
                
                if (rows > 0) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Password changed successfully for user: " + username,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Error changing password: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showLoginHistory() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            String sql = "SELECT login_time, ip_address FROM login_history " +
                       "WHERE user_id = ? ORDER BY login_time DESC";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            StringBuilder history = new StringBuilder();
            history.append("<html><body style='width: 400px'>");
            history.append("<h3>Login History for ").append(username).append("</h3>");
            history.append("<table border='0' cellpadding='5'>");
            history.append("<tr><th>Login Time</th><th>IP Address</th></tr>");
            
            int count = 0;
            while (rs.next() && count < 10) { // Show last 10 logins
                history.append("<tr>");
                history.append("<td>").append(rs.getTimestamp("login_time")).append("</td>");
                history.append("<td>").append(rs.getString("ip_address")).append("</td>");
                history.append("</tr>");
                count++;
            }
            
            history.append("</table>");
            history.append("<p>Total logins found: ").append(count).append("</p>");
            history.append("</body></html>");
            
            rs.close();
            pstmt.close();
            
            JOptionPane.showMessageDialog(mainFrame, history.toString(), "Login History", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error loading login history: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showFriendList() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            String sql = "SELECT u.username, u.full_name, f.status, f.created_at " +
                       "FROM friendships f " +
                       "JOIN users u ON (u.user_id = f.user_id2) " +
                       "WHERE f.user_id1 = ? AND f.status = 'accepted' " +
                       "UNION " +
                       "SELECT u.username, u.full_name, f.status, f.created_at " +
                       "FROM friendships f " +
                       "JOIN users u ON (u.user_id = f.user_id1) " +
                       "WHERE f.user_id2 = ? AND f.status = 'accepted' " +
                       "ORDER BY created_at DESC";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            
            StringBuilder friendList = new StringBuilder();
            friendList.append("<html><body style='width: 400px'>");
            friendList.append("<h3>Friends of ").append(username).append("</h3>");
            friendList.append("<table border='0' cellpadding='5'>");
            friendList.append("<tr><th>Username</th><th>Full Name</th><th>Friends Since</th></tr>");
            
            int count = 0;
            while (rs.next()) {
                friendList.append("<tr>");
                friendList.append("<td>").append(rs.getString("username")).append("</td>");
                friendList.append("<td>").append(rs.getString("full_name")).append("</td>");
                friendList.append("<td>").append(rs.getDate("created_at")).append("</td>");
                friendList.append("</tr>");
                count++;
            }
            
            friendList.append("</table>");
            friendList.append("<p>Total friends: ").append(count).append("</p>");
            friendList.append("</body></html>");
            
            rs.close();
            pstmt.close();
            
            JOptionPane.showMessageDialog(mainFrame, friendList.toString(), "Friend List", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error loading friend list: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(mainFrame, 
            "WARNING: This will permanently delete user: " + username + 
            "\nand all associated data (messages, friends, etc.)." +
            "\n\nAre you absolutely sure?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Use the stored procedure to delete user
                CallableStatement cstmt = connection.prepareCall("{call DeleteUser(?)}");
                cstmt.setInt(1, userId);
                cstmt.execute();
                cstmt.close();
                
                JOptionPane.showMessageDialog(mainFrame, 
                    "User " + username + " has been deleted successfully!",
                    "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
                refreshUserTable();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Error deleting user: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void updateFooterStats(int totalUsers, int activeUsers, int lockedUsers) {
        Component[] components = mainFrame.getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel) {
                        JLabel label = (JLabel) subComp;
                        if (label.getName() != null && label.getName().equals("statsLabel")) {
                            label.setText(String.format("ChatSystem Database • Total Users: %d • Active: %d • Locked: %d", 
                                totalUsers, activeUsers, lockedUsers));
                            return;
                        }
                    }
                }
            }
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
}