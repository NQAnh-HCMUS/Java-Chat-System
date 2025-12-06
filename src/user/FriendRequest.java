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

public class FriendRequest {
    public static void showRequest() {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("Friend Request - JCS");
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

            // Center: riendlist frame
            DefaultListModel<String> ListTable = new DefaultListModel<>();
            List<String> allFriends = new ArrayList<>();
            try {
                allFriends.addAll(loadFriends());
            } catch (IOException ex) {
                System.out.println("IOException caught: " + ex.getMessage());
            }
            allFriends.forEach(ListTable::addElement);

            // Friendlist
            JList<String> list = new JList<>(ListTable);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scroll = new JScrollPane(list);
            root.add(scroll, BorderLayout.CENTER);

            // Right: buttons list
            JPanel actions = new JPanel();
            actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
            JButton addBtn = new JButton("Accept");
            JButton detailsBtn = new JButton("Details");
            JButton messageBtn = new JButton("Message");
            JButton removeBtn = new JButton("Remove");

            Dimension buttonSize = new Dimension(120, 30);
            for (JButton b : new JButton[]{addBtn, detailsBtn, messageBtn, removeBtn}) {
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

    private static List<String> loadFriends() throws IOException {
        Path fList = Paths.get("script").resolve("friend_requests.txt");
        if (!Files.exists(fList)) { // if not, create parent dir & sample file
            if (fList.getParent() != null) Files.createDirectories(fList.getParent());
            List<String> sample = List.of("Friend A", "Friend B", "Friend C");
            Files.write(fList, sample, StandardCharsets.UTF_8);
            return new ArrayList<>(sample);
        }
        List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
        return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }
}	