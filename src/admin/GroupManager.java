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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GroupManager {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color GREEN = new Color(0x2ECC71);
    private static final Color PURPLE = new Color(0x9B59B6);
    private static final Color ORANGE = new Color(0xF39C12);
    private static final Color RED = new Color(0xE74C3C);
    private static final Color YELLOW = new Color(0xF1C40F);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private static DefaultListModel<String> listModel;
    private static JList<String> groupList;
    private static JTextField searchField;
    private static JLabel footerLabel;
    private static List<GroupData> allGroups;
    private static List<GroupData> filteredGroups;

    public static void showGroup() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main frame
            JFrame frame = new JFrame("Group Management - Admin Panel");
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

    // Data class to hold group information
    private static class GroupData {
        String name;
        int memberCount;
        String creationDate;
        String admin;
        List<String> members;
        List<String> admins;
        
        GroupData(String name, int memberCount, String creationDate, String admin) {
            this.name = name;
            this.memberCount = memberCount;
            this.creationDate = creationDate;
            this.admin = admin;
            this.members = new ArrayList<>();
            this.admins = new ArrayList<>();
            this.admins.add(admin);
        }
        
        void addMember(String member) {
            if (!members.contains(member)) {
                members.add(member);
            }
        }
        
        void addAdmin(String admin) {
            if (!admins.contains(admin)) {
                admins.add(admin);
            }
        }
        
        @Override
        public String toString() {
            return name + " (" + memberCount + " members) - Created: " + creationDate;
        }
    }

    private static void initializeData() {
        allGroups = new ArrayList<>();
        filteredGroups = new ArrayList<>();
        
        // Create sample data
        createSampleData();
        
        // Initialize filtered list
        filteredGroups.addAll(allGroups);
    }

    private static void createSampleData() {
        // Create sample groups with realistic data
        GroupData group1 = new GroupData("Close Friends Group", 5, "2024-01-15", "john_doe");
        addSampleMembers(group1);
        group1.addAdmin("jane_smith");
        
        GroupData group2 = new GroupData("Work Collaboration", 12, "2024-01-10", "sarah_johnson");
        addSampleMembers(group2);
        group2.addAdmin("mike_wilson");
        
        GroupData group3 = new GroupData("Family Chat", 8, "2024-01-08", "david_brown");
        addSampleMembers(group3);
        
        GroupData group4 = new GroupData("Study Group", 6, "2024-01-12", "emily_davis");
        addSampleMembers(group4);
        group4.addAdmin("robert_miller");
        
        GroupData group5 = new GroupData("Project Team", 9, "2024-01-05", "lisa_anderson");
        addSampleMembers(group5);
        
        GroupData group6 = new GroupData("Travel Buddies", 4, "2024-01-18", "alex_turner");
        addSampleMembers(group6);
        
        GroupData group7 = new GroupData("Gaming Community", 15, "2024-01-03", "chris_lee");
        addSampleMembers(group7);
        group7.addAdmin("sophia_martinez");
        
        GroupData group8 = new GroupData("Book Club", 7, "2024-01-20", "olivia_white");
        addSampleMembers(group8);
        
        GroupData group9 = new GroupData("Fitness Group", 10, "2024-01-22", "daniel_clark");
        addSampleMembers(group9);
        group9.addAdmin("ava_robinson");
        
        GroupData group10 = new GroupData("Tech Discussion", 11, "2024-01-25", "james_walker");
        addSampleMembers(group10);
        
        allGroups.add(group1);
        allGroups.add(group2);
        allGroups.add(group3);
        allGroups.add(group4);
        allGroups.add(group5);
        allGroups.add(group6);
        allGroups.add(group7);
        allGroups.add(group8);
        allGroups.add(group9);
        allGroups.add(group10);
    }

    private static void addSampleMembers(GroupData group) {
        // Add sample members based on group size
        String[] sampleMembers = {
            "john_doe", "jane_smith", "mike_wilson", "sarah_johnson", 
            "david_brown", "emily_davis", "robert_miller", "lisa_anderson",
            "alex_turner", "sophia_martinez", "chris_lee", "olivia_white",
            "daniel_clark", "ava_robinson", "james_walker", "admin"
        };
        
        // Add creator as first member
        group.addMember(group.admin);
        
        // Add additional members up to group size
        for (int i = 0; i < group.memberCount - 1 && i < sampleMembers.length - 1; i++) {
            String member = sampleMembers[(i + 1) % sampleMembers.length];
            if (!member.equals(group.admin)) {
                group.addMember(member);
            }
        }
    }

    private static void updateListModel() {
        listModel.clear();
        for (GroupData group : filteredGroups) {
            listModel.addElement(group.toString());
        }
        updateFooter();
    }

    private static void updateFooter() {
        if (footerLabel != null) {
            int totalGroups = allGroups.size();
            int filteredCount = filteredGroups.size();
            
            String footerText = String.format(
                "Group Management System • Total Groups: %d • Showing: %d",
                totalGroups, filteredCount
            );
            footerLabel.setText(footerText);
        }
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("Group Management");
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
        searchField.setToolTipText("Search group by name...");

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        
        JButton refreshBtn = createModernButton("Refresh", BLUE);
        refreshBtn.setPreferredSize(new Dimension(80, 35));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchBtn);
        buttonPanel.add(refreshBtn);

        // Search button action
        searchBtn.addActionListener(e -> performSearch());
        
        // Refresh button action
        refreshBtn.addActionListener(e -> refreshData());

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private static void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            // Show all groups
            filteredGroups.clear();
            filteredGroups.addAll(allGroups);
        } else {
            // Filter groups by name
            filteredGroups.clear();
            for (GroupData group : allGroups) {
                if (group.name.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredGroups.add(group);
                }
            }
        }
        
        updateListModel();
        
        if (filteredGroups.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No groups found for: " + searchText, 
                "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void refreshData() {
        // Clear search
        searchField.setText("");
        
        // Reload data
        filteredGroups.clear();
        filteredGroups.addAll(allGroups);
        updateListModel();
        
        JOptionPane.showMessageDialog(null, 
            "Data refreshed successfully!\nTotal groups: " + allGroups.size(), 
            "Refresh Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Group list panel
        JPanel listPanel = createGroupListPanel();
        
        // Action buttons panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createGroupListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel listTitle = new JLabel("Group List - Select a group to view details");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Group list
        listModel = new DefaultListModel<>();
        
        for (GroupData group : filteredGroups) {
            listModel.addElement(group.toString());
        }

        groupList = new JList<>(listModel);
        groupList.setFont(LIST_FONT);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBackground(WHITE);
        groupList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

        JScrollPane scrollPane = new JScrollPane(groupList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        return listPanel;
    }

    private static JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(LIGHT_GRAY);
        actionPanel.setPreferredSize(new Dimension(250, 0));
        
        // Title for action panel
        JLabel actionTitle = new JLabel("Group Actions");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionTitle.setForeground(DARK_BLUE);
        actionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        actionPanel.add(actionTitle);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sort section
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.Y_AXIS));
        sortPanel.setBackground(LIGHT_GRAY);
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sort Groups"));
        
        JButton nameAscBtn = createModernButton("Sort by Name (A-Z)", BLUE);
        nameAscBtn.setMaximumSize(new Dimension(220, 35));
        nameAscBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton dateDescBtn = createModernButton("Sort by Date (Newest)", PURPLE);
        dateDescBtn.setMaximumSize(new Dimension(220, 35));
        dateDescBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton sizeDescBtn = createModernButton("Sort by Size (Large)", GREEN);
        sizeDescBtn.setMaximumSize(new Dimension(220, 35));
        sizeDescBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sortPanel.add(nameAscBtn);
        sortPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sortPanel.add(dateDescBtn);
        sortPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sortPanel.add(sizeDescBtn);
        
        actionPanel.add(sortPanel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Main action buttons
        JLabel viewLabel = new JLabel("View Group Details");
        viewLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewLabel.setForeground(DARK_BLUE);
        viewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(viewLabel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Create buttons without icons
        JButton memberListBtn = createModernButton("View Member List", GREEN);
        memberListBtn.setMaximumSize(new Dimension(220, 45));
        memberListBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton adminListBtn = createModernButton("View Admin List", PURPLE);
        adminListBtn.setMaximumSize(new Dimension(220, 45));
        adminListBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton detailsBtn = createModernButton("Group Details", BLUE);
        detailsBtn.setMaximumSize(new Dimension(220, 45));
        detailsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        actionPanel.add(memberListBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(adminListBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(detailsBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Management buttons
        JLabel manageLabel = new JLabel("Manage Groups");
        manageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        manageLabel.setForeground(DARK_BLUE);
        manageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(manageLabel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton createGroupBtn = createModernButton("Create New Group", ORANGE);
        createGroupBtn.setMaximumSize(new Dimension(220, 45));
        createGroupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton deleteGroupBtn = createModernButton("Delete Selected Group", RED);
        deleteGroupBtn.setMaximumSize(new Dimension(220, 45));
        deleteGroupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton exportBtn = createModernButton("Export Group List", YELLOW);
        exportBtn.setMaximumSize(new Dimension(220, 45));
        exportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        actionPanel.add(createGroupBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(deleteGroupBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(exportBtn);

        actionPanel.add(Box.createVerticalGlue());
        
        return actionPanel;
    }

    private static void sortByName(boolean ascending) {
        filteredGroups.sort((g1, g2) -> {
            int result = g1.name.compareToIgnoreCase(g2.name);
            return ascending ? result : -result;
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by name " + (ascending ? "(A-Z)" : "(Z-A)"), 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void sortByDate(boolean ascending) {
        filteredGroups.sort((g1, g2) -> {
            int result = g1.creationDate.compareTo(g2.creationDate);
            return ascending ? result : -result;
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by creation date " + (ascending ? "(oldest first)" : "(newest first)"), 
            "Sort Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void sortBySize(boolean ascending) {
        filteredGroups.sort((g1, g2) -> {
            int result = Integer.compare(g1.memberCount, g2.memberCount);
            return ascending ? result : -result;
        });
        updateListModel();
        JOptionPane.showMessageDialog(null, 
            "Sorted by size " + (ascending ? "(smallest first)" : "(largest first)"), 
            "Sort Complete", 
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
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
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
            case "Sort by Name (A-Z)":
                sortByName(true);
                break;
            case "Sort by Date (Newest)":
                sortByDate(false);
                break;
            case "Sort by Size (Large)":
                sortBySize(false);
                break;
            case "View Member List":
                showMemberList();
                break;
            case "View Admin List":
                showAdminList();
                break;
            case "Group Details":
                showGroupDetails();
                break;
            case "Create New Group":
                createNewGroup();
                break;
            case "Delete Selected Group":
                deleteSelectedGroup();
                break;
            case "Export Group List":
                exportGroupList();
                break;
        }
    }

    private static void showMemberList() {
        int selectedIndex = groupList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a group first!", 
                "No Group Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        GroupData selectedGroup = filteredGroups.get(selectedIndex);
        
        // Create simple text area
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(0xF8F9FA));
        
        // Build member list - SIMPLE FORMAT
        StringBuilder memberList = new StringBuilder();
        memberList.append("MEMBER LIST\n");
        memberList.append("===========\n\n");
        memberList.append("Group: ").append(selectedGroup.name).append("\n");
        memberList.append("Total Members: ").append(selectedGroup.memberCount).append("\n");
        memberList.append("Creation Date: ").append(selectedGroup.creationDate).append("\n");
        memberList.append("Creator/Admin: ").append(selectedGroup.admin).append("\n\n");
        
        memberList.append("ALL MEMBERS:\n");
        memberList.append("------------\n\n");
        
        for (int i = 0; i < selectedGroup.members.size(); i++) {
            String member = selectedGroup.members.get(i);
            String role = selectedGroup.admins.contains(member) ? " [ADMIN]" : "";
            memberList.append(String.format("%2d. %s%s\n", i + 1, member, role));
        }
        
        textArea.setText(memberList.toString());
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scrollPane, 
            "Member List - " + selectedGroup.name, JOptionPane.PLAIN_MESSAGE);
    }

    private static void showAdminList() {
        int selectedIndex = groupList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a group first!", 
                "No Group Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        GroupData selectedGroup = filteredGroups.get(selectedIndex);
        
        // Create simple text area
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(0xF8F9FA));
        
        // Build admin list - SIMPLE FORMAT
        StringBuilder adminList = new StringBuilder();
        adminList.append("ADMIN LIST\n");
        adminList.append("==========\n\n");
        adminList.append("Group: ").append(selectedGroup.name).append("\n");
        adminList.append("Total Admins: ").append(selectedGroup.admins.size()).append("\n");
        adminList.append("Creator: ").append(selectedGroup.admin).append("\n\n");
        
        adminList.append("ADMINISTRATORS:\n");
        adminList.append("---------------\n\n");
        
        for (int i = 0; i < selectedGroup.admins.size(); i++) {
            String admin = selectedGroup.admins.get(i);
            String isCreator = admin.equals(selectedGroup.admin) ? " [CREATOR]" : "";
            adminList.append(String.format("%d. %s%s\n", i + 1, admin, isCreator));
        }
        
        textArea.setText(adminList.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(null, scrollPane, 
            "Admin List - " + selectedGroup.name, JOptionPane.PLAIN_MESSAGE);
    }

    private static void showGroupDetails() {
        int selectedIndex = groupList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a group first!", 
                "No Group Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        GroupData selectedGroup = filteredGroups.get(selectedIndex);
        
        // Create simple text area
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(0xF8F9FA));
        
        // Build detailed information - SIMPLE FORMAT
        StringBuilder details = new StringBuilder();
        details.append("GROUP DETAILS\n");
        details.append("=============\n\n");
        
        details.append("BASIC INFORMATION:\n");
        details.append("------------------\n");
        details.append("Group Name: ").append(selectedGroup.name).append("\n");
        details.append("Member Count: ").append(selectedGroup.memberCount).append("\n");
        details.append("Creation Date: ").append(selectedGroup.creationDate).append("\n");
        details.append("Creator: ").append(selectedGroup.admin).append("\n\n");
        
        details.append("MEMBER STATISTICS:\n");
        details.append("------------------\n");
        details.append("Total Members: ").append(selectedGroup.memberCount).append("\n");
        details.append("Admins: ").append(selectedGroup.admins.size()).append("\n");
        details.append("Regular Members: ").append(selectedGroup.memberCount - selectedGroup.admins.size()).append("\n\n");
        
        details.append("ADMINISTRATORS:\n");
        details.append("---------------\n");
        for (int i = 0; i < selectedGroup.admins.size(); i++) {
            String admin = selectedGroup.admins.get(i);
            String role = admin.equals(selectedGroup.admin) ? " (Creator)" : "";
            details.append(String.format("%d. %s%s\n", i + 1, admin, role));
        }
        
        textArea.setText(details.toString());
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 450));
        
        JOptionPane.showMessageDialog(null, scrollPane, 
            "Group Details - " + selectedGroup.name, JOptionPane.PLAIN_MESSAGE);
    }

    private static void createNewGroup() {
        // Create dialog for new group creation
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
        createPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JTextField adminField = new JTextField(20);
        adminField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        adminField.setText("admin");
        
        createPanel.add(new JLabel("Group Name:"));
        createPanel.add(nameField);
        createPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        createPanel.add(new JLabel("Admin Username:"));
        createPanel.add(adminField);
        
        int result = JOptionPane.showConfirmDialog(null, createPanel, 
            "Create New Group", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String groupName = nameField.getText().trim();
            String admin = adminField.getText().trim();
            
            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Group name cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (admin.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Admin username cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if group name already exists
            boolean exists = allGroups.stream()
                .anyMatch(g -> g.name.equalsIgnoreCase(groupName));
            
            if (exists) {
                JOptionPane.showMessageDialog(null, 
                    "A group with this name already exists", 
                    "Duplicate Group", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new group
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            GroupData newGroup = new GroupData(groupName, 1, currentDate, admin);
            newGroup.addMember(admin);
            
            allGroups.add(newGroup);
            filteredGroups.add(newGroup);
            updateListModel();
            
            JOptionPane.showMessageDialog(null, 
                "Group created successfully!\n\n" +
                "Name: " + groupName + "\n" +
                "Admin: " + admin + "\n" +
                "Created: " + currentDate + "\n\n" +
                "The group has been added to the list.", 
                "Group Created", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void deleteSelectedGroup() {
        int selectedIndex = groupList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select a group to delete!", 
                "No Group Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        GroupData selectedGroup = filteredGroups.get(selectedIndex);
        
        int confirm = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to delete this group?\n\n" +
            "Group: " + selectedGroup.name + "\n" +
            "Members: " + selectedGroup.memberCount + "\n" +
            "Created: " + selectedGroup.creationDate + "\n\n" +
            "This action cannot be undone!",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove from all lists
            filteredGroups.remove(selectedIndex);
            allGroups.remove(selectedGroup);
            
            // Update display
            updateListModel();
            
            JOptionPane.showMessageDialog(null,
                "Group \"" + selectedGroup.name + "\" has been deleted successfully.",
                "Group Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void exportGroupList() {
        if (filteredGroups.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No groups to export!",
                "Export Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Group List");
        fileChooser.setSelectedFile(new File("group_list_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                writer.write("Group Name,Member Count,Creation Date,Creator,Admins,Total Members\n");
                
                // Write data
                for (GroupData group : filteredGroups) {
                    String adminsStr = String.join(";", group.admins);
                    writer.write(String.format("\"%s\",%d,\"%s\",\"%s\",\"%s\",%d\n",
                        group.name,
                        group.memberCount,
                        group.creationDate,
                        group.admin,
                        adminsStr,
                        group.members.size()
                    ));
                }
                
                JOptionPane.showMessageDialog(null,
                    "Export Successful!\n\n" +
                    String.format("Exported %d groups to:\n%s", 
                        filteredGroups.size(), fileToSave.getAbsolutePath()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                    "Error exporting group list:\n" + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        footerLabel = new JLabel("Group Management System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        updateFooter();
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
}