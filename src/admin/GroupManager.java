import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public static void showGroup() {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("Group Management - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

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
            frame.add(headerPanel, BorderLayout.NORTH);
            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(footerPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
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
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchLabel.setForeground(LIGHT_TEAL);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JButton searchBtn = createModernButton("Search", TEAL);
        searchBtn.setPreferredSize(new Dimension(80, 35));

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        return searchPanel;
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
        JLabel listTitle = new JLabel("Group List");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Group list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<String> allGroups = new ArrayList<>();
        try {
            allGroups.addAll(loadGroups());
        } catch (IOException ex) {
            System.out.println("IOException caught: " + ex.getMessage());
            // Add sample data for demo
            allGroups.add("Close Friends Group (5 members) - Created: 2024-01-15");
            allGroups.add("Work Collaboration (12 members) - Created: 2024-01-10");
            allGroups.add("Family Chat (8 members) - Created: 2024-01-08");
            allGroups.add("Study Group (6 members) - Created: 2024-01-12");
            allGroups.add("Project Team (9 members) - Created: 2024-01-05");
        }
        allGroups.forEach(listModel::addElement);

        JList<String> groupList = new JList<>(listModel);
        groupList.setFont(LIST_FONT);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBackground(WHITE);
        groupList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

        JScrollPane scrollPane = new JScrollPane(groupList);
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
        JButton nameSortBtn = createModernButton("Sort by Name", BLUE);
        JButton dateSortBtn = createModernButton("Sort by Creation Date", TEAL);
        JButton memberListBtn = createModernButton("Member List", GREEN);
        JButton adminListBtn = createModernButton("Admin List", PURPLE);
        JButton createGroupBtn = createModernButton("Create Group", ORANGE);
        JButton deleteGroupBtn = createModernButton("Delete Group", new Color(0xE74C3C));

        // Add buttons to panel
        JButton[] buttons = {nameSortBtn, dateSortBtn, memberListBtn, adminListBtn, createGroupBtn, deleteGroupBtn};
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
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);           // Nền đổi màu khi hover
                button.setForeground(WHITE);           // Chữ trắng khi hover
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_TEAL);      // Trở lại nền sáng
                button.setForeground(DARK_BLUE);       // Trở lại chữ xanh đen
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
                JOptionPane.showMessageDialog(null, "Sorting groups by name - Under development");
                break;
            case "Sort by Creation Date":
                JOptionPane.showMessageDialog(null, "Sorting groups by creation date - Under development");
                break;
            case "Member List":
                JOptionPane.showMessageDialog(null, "Viewing member list - Under development");
                break;
            case "Admin List":
                JOptionPane.showMessageDialog(null, "Viewing admin list - Under development");
                break;
            case "Create Group":
                JOptionPane.showMessageDialog(null, "Create new group - Under development");
                break;
            case "Delete Group":
                JOptionPane.showMessageDialog(null, "Delete selected group - Under development");
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("Group Management System • Total Groups: 45 • Active Today: 23");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private static List<String> loadGroups() throws IOException {
        Path fList = Paths.get("script").resolve("groups.txt");
        if (!Files.exists(fList)) {
            if (fList.getParent() != null) Files.createDirectories(fList.getParent());
            List<String> sample = List.of(
                "Close Friends Group (5 members) - Created: 2024-01-15",
                "Work Collaboration (12 members) - Created: 2024-01-10",
                "Family Chat (8 members) - Created: 2024-01-08",
                "Study Group (6 members) - Created: 2024-01-12",
                "Project Team (9 members) - Created: 2024-01-05",
                "Travel Buddies (4 members) - Created: 2024-01-18",
                "Gaming Community (15 members) - Created: 2024-01-03"
            );
            Files.write(fList, sample, StandardCharsets.UTF_8);
            return new ArrayList<>(sample);
        }
        List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
        return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }
}