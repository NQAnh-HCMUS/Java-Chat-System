import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpamLog {
    public static void List() {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("[Admin] User Manager");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 450);
            frame.setLocationRelativeTo(null);

            // Root panel
            JPanel root = new JPanel(new BorderLayout(8,8));

            // Top: searchbar
            JPanel topBar = new JPanel(new BorderLayout(6,6));
            JTextField searchBar = new JTextField();
            // Search button (not functional yet)
            JButton searchBtn = new JButton("Search");
            Dimension searchBtnSize = new Dimension(100, 6);
            searchBtn.setPreferredSize(searchBtnSize);
            searchBtn.setMinimumSize(searchBtnSize);
            searchBtn.setMaximumSize(searchBtnSize);
            searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            topBar.add(new JLabel("Find:"), BorderLayout.WEST);
            topBar.add(searchBar, BorderLayout.CENTER);
            topBar.add(searchBtn, BorderLayout.EAST);
            root.add(topBar, BorderLayout.NORTH);

            // Center: userlist
            DefaultListModel<String> ListTable = new DefaultListModel<>();
            List<String> spamlog = new ArrayList<>();

            // Userlist
            JList<String> list = new JList<>(ListTable);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scroll = new JScrollPane(list);
            root.add(scroll, BorderLayout.CENTER);

            // Right: buttons list
            JPanel actions = new JPanel();
            actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
            JButton timeSortBtn = new JButton("Sort by Time");
            JButton nameSortBtn = new JButton("Sort by Username");
            JButton timeFilterBtn = new JButton("Filter by Time");
            JButton nameFilterBtn = new JButton("Filter by Username");
            JButton lockBtn = new JButton("Lock/Unlock Account");
            
            Dimension buttonSize = new Dimension(160, 30);
            for (JButton b : new JButton[]{timeSortBtn, nameSortBtn, timeFilterBtn, nameFilterBtn, lockBtn}) {
                b.setMaximumSize(buttonSize);
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                actions.add(b);
                actions.add(Box.createVerticalStrut(8));
            }

            root.add(actions, BorderLayout.EAST);

            frame.setContentPane(root);
            frame.setVisible(true);
        });
    }
}	