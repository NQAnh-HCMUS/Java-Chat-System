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
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public static void showUser() {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("User Management - Admin Panel");
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
        JLabel titleLabel = new JLabel("User Management");
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
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<String> allFriends = new ArrayList<>();
        try {
            allFriends.addAll(loadFriends());
        } catch (IOException ex) {
            System.out.println("IOException caught: " + ex.getMessage());
            // Add sample data for demo
            allFriends.add("John Doe (Active)");
            allFriends.add("Jane Smith (Active)");
            allFriends.add("Mike Wilson (Locked)");
            allFriends.add("Sarah Johnson (Active)");
        }
        allFriends.forEach(listModel::addElement);

        JList<String> userList = new JList<>(listModel);
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
        JButton detailsBtn = createModernButton("View Details", TEAL);
        JButton lockBtn = createModernButton("Lock/Unlock", new Color(0xF39C12));
        JButton passwdBtn = createModernButton("Change Password", new Color(0x9B59B6));
        JButton historyBtn = createModernButton("Login History", new Color(0x34495E));
        JButton friendlistBtn = createModernButton("Friend List", new Color(0x16A085));

        // Add buttons to panel
        JButton[] buttons = {addBtn, updateBtn, detailsBtn, lockBtn, passwdBtn, historyBtn, friendlistBtn};
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

    private static Color darkenColor(Color color, double factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }

    private static void handleButtonAction(String action) {
        switch (action) {
            case "Add User":
                JOptionPane.showMessageDialog(null, "Add User functionality - Under development");
                break;
            case "Update User":
                JOptionPane.showMessageDialog(null, "Update User functionality - Under development");
                break;
            case "View Details":
                JOptionPane.showMessageDialog(null, "View Details functionality - Under development");
                break;
            case "Lock/Unlock":
                JOptionPane.showMessageDialog(null, "Lock/Unlock functionality - Under development");
                break;
            case "Change Password":
                JOptionPane.showMessageDialog(null, "Change Password functionality - Under development");
                break;
            case "Login History":
                JOptionPane.showMessageDialog(null, "Login History functionality - Under development");
                break;
            case "Friend List":
                JOptionPane.showMessageDialog(null, "Friend List functionality - Under development");
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("User Management System • Total Users: 1,234 • Online: 456");
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
                "John Doe (Active) - john.doe@email.com",
                "Jane Smith (Active) - jane.smith@email.com", 
                "Mike Wilson (Locked) - mike.wilson@email.com",
                "Sarah Johnson (Active) - sarah.johnson@email.com",
                "David Brown (Active) - david.brown@email.com",
                "Emily Davis (Active) - emily.davis@email.com"
            );
            Files.write(fList, sample, StandardCharsets.UTF_8);
            return new ArrayList<>(sample);
        }
        List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
        return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }
}