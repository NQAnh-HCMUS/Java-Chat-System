import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

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

public class SpamLog {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color RED = new Color(0xE74C3C);
    private static final Color ORANGE = new Color(0xF39C12);
    private static final Color PURPLE = new Color(0x9B59B6);
    private static final Color YELLOW = new Color(0xF1C40F);
    private static final Color GREEN = new Color(0x2ECC71);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font LIST_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    public static void List() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main frame
            JFrame frame = new JFrame("Spam Reports - Admin Panel");
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
        JLabel titleLabel = new JLabel("Spam Reports Management");
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

        // Spam list panel
        JPanel listPanel = createSpamListPanel();
        
        // Action buttons panel
        JPanel actionPanel = createActionPanel();

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        return mainPanel;
    }

    private static JPanel createSpamListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel title
        JLabel listTitle = new JLabel("Spam Reports");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(DARK_BLUE);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Spam list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<String> spamlog = new ArrayList<>();

        // Sample spam report data
        listModel.addElement(" 2024-01-15 14:30:00 - spam_user reported by john_doe");
        listModel.addElement(" 2024-01-15 16:45:00 - fake_account reported by jane_smith");
        listModel.addElement(" 2024-01-16 09:20:00 - malicious_bot reported by mike_wilson");
        listModel.addElement(" 2024-01-16 11:15:00 - phishing_scam reported by sarah_johnson");
        listModel.addElement(" 2024-01-17 08:30:00 - spammer_123 reported by david_brown");
        listModel.addElement(" 2024-01-17 13:45:00 - fake_profile reported by emily_davis");
        listModel.addElement(" 2024-01-18 10:20:00 - bot_account reported by admin");
        listModel.addElement(" 2024-01-18 15:30:00 - suspicious_user reported by john_doe");

        JList<String> spamList = new JList<>(listModel);
        spamList.setFont(LIST_FONT);
        spamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spamList.setBackground(WHITE);
        spamList.setBorder(BorderFactory.createLineBorder(new Color(0xECF0F1), 1));

        JScrollPane scrollPane = new JScrollPane(spamList);
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
        JButton timeSortBtn = createModernButton("Sort by Time", BLUE);
        JButton nameSortBtn = createModernButton("Sort by Username", TEAL);
        JButton timeFilterBtn = createModernButton("Filter by Time", PURPLE);
        JButton nameFilterBtn = createModernButton("Filter by Username", YELLOW);
        JButton lockBtn = createModernButton("Lock/Unlock Account", RED);
        JButton resolveBtn = createModernButton("Mark Resolved", GREEN);
        JButton exportBtn = createModernButton("Export Reports", ORANGE);

        // Add buttons to panel
        JButton[] buttons = {timeSortBtn, nameSortBtn, timeFilterBtn, nameFilterBtn, lockBtn, resolveBtn, exportBtn};
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
            case "Sort by Time":
                JOptionPane.showMessageDialog(null, "Sorting spam reports by time - Under development");
                break;
            case "Sort by Username":
                JOptionPane.showMessageDialog(null, "Sorting spam reports by username - Under development");
                break;
            case "Filter by Time":
                JOptionPane.showMessageDialog(null, "Filtering spam reports by time range - Under development");
                break;
            case "Filter by Username":
                JOptionPane.showMessageDialog(null, "Filtering spam reports by username - Under development");
                break;
            case "Lock/Unlock Account":
                JOptionPane.showMessageDialog(null, "Locking/Unlocking reported account - Under development");
                break;
            case "Mark Resolved":
                JOptionPane.showMessageDialog(null, "Marking spam report as resolved - Under development");
                break;
            case "Export Reports":
                JOptionPane.showMessageDialog(null, "Exporting spam reports to file - Under development");
                break;
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("Spam Report System • Total Reports: 156 • Pending: 23 • Resolved: 133");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
}