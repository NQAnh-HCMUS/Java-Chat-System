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
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    

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

        JTextField searchField = new JTextField(20);
        searchField.setFont(LABEL_FONT);
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
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        // List<String> allFriends = new ArrayList<>();
        // try {
        //     allFriends.addAll(loadFriends());
        // } catch (IOException ex) {
        //     System.out.println("IOException caught: " + ex.getMessage());
        // }
        // allFriends.forEach(listModel::addElement);
        
        // Sample data for demo
        listModel.addElement("john_doe - 2024-01-15 08:30:00 - Active");
        listModel.addElement("jane_smith - 2024-01-15 09:15:00 - Active");
        listModel.addElement("mike_wilson - 2024-01-15 10:20:00 - Active");
        listModel.addElement("sarah_johnson - 2024-01-16 08:25:00 - Active");
        listModel.addElement("david_brown - 2024-01-16 14:30:00 - Locked");
        listModel.addElement("emily_davis - 2024-01-17 11:45:00 - Active");

        JList<String> logList = new JList<>(listModel);
        logList.setFont(LIST_FONT);
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logList.setBackground(WHITE);
        logList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

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
        actionPanel.setPreferredSize(new Dimension(180, 0));

        // Time period filter
        JPanel timeFilterPanel = createTimeFilterPanel();
        actionPanel.add(timeFilterPanel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action buttons
        JButton nameSortBtn = createModernButton("Sort by Name", BLUE);
        JButton dateSortBtn = createModernButton("Sort by Date", TEAL);
        JButton exportBtn = createModernButton("Export Log", PURPLE);
        JButton refreshBtn = createModernButton("Refresh", ORANGE);

        // Add buttons to panel
        JButton[] buttons = {nameSortBtn, dateSortBtn, exportBtn, refreshBtn};
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(160, 40));
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

        JLabel startLabel = new JLabel("From:");
        startLabel.setFont(LABEL_FONT);
        startLabel.setForeground(DARK_BLUE);
        startLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField startField = new JTextField();
        startField.setFont(LABEL_FONT);
        startField.setMaximumSize(new Dimension(160, 25));
        startField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDC3C7), 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JLabel endLabel = new JLabel("To:");
        endLabel.setFont(LABEL_FONT);
        endLabel.setForeground(DARK_BLUE);
        endLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField endField = new JTextField();
        endField.setFont(LABEL_FONT);
        endField.setMaximumSize(new Dimension(160, 25));
        endField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDC3C7), 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JButton applyFilterBtn = createModernButton("Apply Filter", GREEN);
        applyFilterBtn.setMaximumSize(new Dimension(160, 30));
        applyFilterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        timePanel.add(startLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        timePanel.add(startField);
        timePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        timePanel.add(endLabel);
        timePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        timePanel.add(endField);
        timePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        timePanel.add(applyFilterBtn);

        return timePanel;
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
                JOptionPane.showMessageDialog(null, "Sorting by name - Under development");
                break;
            case "Sort by Date":
                JOptionPane.showMessageDialog(null, "Sorting by date - Under development");
                break;
            case "Export Log":
                JOptionPane.showMessageDialog(null, "Exporting log - Under development");
                break;
            case "Refresh":
                JOptionPane.showMessageDialog(null, "Refreshing data - Under development");
                break;
            case "Apply Filter":
                JOptionPane.showMessageDialog(null, "Applying time filter - Under development");
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("Sign-up Log System • Total Records: 1,567 • Today: 23");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    // private static List<String> loadFriends() throws IOException {
    //     Path fList = Paths.get("script").resolve("friends.txt");
    //     if (!Files.exists(fList)) { // if not, create parent dir & sample file
    //         if (fList.getParent() != null) Files.createDirectories(fList.getParent());
    //         List<String> sample = List.of("Friend A", "Friend B", "Friend C");
    //         Files.write(fList, sample, StandardCharsets.UTF_8);
    //         return new ArrayList<>(sample);
    //     }
    //     List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
    //     return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    // }
}