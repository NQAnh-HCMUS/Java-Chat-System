import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendList {
    public static void showFriendList() {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("Friendlist - JCS");
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

            // Center: friendlist
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

            // Searchbar: if user not in friendlist but in database, allow request
            searchBar.addActionListener(e -> {
                // Get input
                String username = searchBar.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter an username to search.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // First, check friendlist
                boolean foundInFriendlist = false;
                for (int i = 0; i < ListTable.getSize(); i++) {
                    String item = ListTable.getElementAt(i);
                    if (item.equalsIgnoreCase(username) || item.toLowerCase().contains(username.toLowerCase())) {
                        foundInFriendlist = true;
                        break;
                    }
                }
                if (foundInFriendlist) {
                    JOptionPane.showMessageDialog(frame, username + " already a friend.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // If not in friendlist, check user database
                Path dataSql = Paths.get("script").resolve("data.sql");
                boolean existsInDatabase = false;
                try {
                    if (Files.exists(dataSql)) {
                        String content = Files.readString(dataSql);
                        String needle = "'" + username.toLowerCase() + "'";
                        if (content.toLowerCase().contains(needle)) existsInDatabase = true;
                    }
                } catch (IOException ex) {
                    System.out.println("Cannot read user database: " + ex.getMessage());
                }
                if (!existsInDatabase) {
                    JOptionPane.showMessageDialog(frame, "Cannot find user: " + username, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // If in database but not friendlist: can send request
                int ok = JOptionPane.showConfirmDialog(frame, username + " isn't a friend. Send friend request?", "Request sent!", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    // Append to friend_requests.log 
                    Path reqFile = Paths.get("script").resolve("friend_requests.log");
                    try {
                        if (reqFile.getParent() != null) Files.createDirectories(reqFile.getParent()); // 
                        String time = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        // Format: time, username, request_sent
                        String entry = time + " " + username + " request_sent" + System.lineSeparator();
                        Files.writeString(reqFile, entry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        JOptionPane.showMessageDialog(frame, "Friend request sent to " + username + ".", "Request Sent", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to send request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Right: buttons list
            JPanel actions = new JPanel();
            actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
            JButton filterBtn = new JButton("Filter Online/Offline");
            JButton removeBtn = new JButton("Remove");
            JButton detailsBtn = new JButton("Details");
            JButton messageBtn = new JButton("Message");
            JButton groupBtn = new JButton("Create Group");
            JButton reportBtn = new JButton("Report Spam");
            JButton blockBtn = new JButton("Block");
            
            Dimension buttonSize = new Dimension(150, 30);
            for (JButton btn : new JButton[]{filterBtn, detailsBtn, messageBtn, groupBtn, reportBtn, blockBtn, removeBtn}) {
                btn.setMaximumSize(buttonSize);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                actions.add(btn);
                actions.add(Box.createVerticalStrut(8));
            }

            // Search/Filter by name
            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                private void update() {
                    String username = searchBar.getText().trim().toLowerCase();
                    ListTable.clear();
                    for (String f : allFriends) {
                        if (username.isEmpty() || f.toLowerCase().contains(username)) ListTable.addElement(f);
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) { update(); }

                @Override
                public void removeUpdate(DocumentEvent e) { update(); }

                @Override
                public void changedUpdate(DocumentEvent e) { update(); }
            });

            // Press Enter in search bar == Search button
            searchBtn.addActionListener(e -> searchBar.postActionEvent());

            // Remove friend
            removeBtn.addActionListener(e -> {
                String friend = list.getSelectedValue();
                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "Select a friend to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int ok = JOptionPane.showConfirmDialog(frame, "Remove " + friend + " from friends?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    allFriends.remove(friend);
                    ListTable.removeElement(friend);
                    try { saveFriends(allFriends); } 
                    catch (IOException ex) { 
                        System.out.println("Cannot remove friend: " + ex.getMessage()); }
                }
            });

            // See friend's details
            detailsBtn.addActionListener(e -> {
                String friend = list.getSelectedValue();
                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "Select a friend to view.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Check database
                Path dataSql = Paths.get("script").resolve("data.sql");
                if (!Files.exists(dataSql)) {
                    JOptionPane.showMessageDialog(frame, "Details not found for: " + friend, "Friend Details", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    String content = Files.readString(dataSql, StandardCharsets.UTF_8);

                    // Look for table w/friend
                    String name = "'" + friend.replace("'", "''") + "'";
                    String[] table = content.split(";");
                    boolean found = false;
                    StringBuilder details = new StringBuilder();

                    for (String line : table) {
                        if (line.trim().isEmpty()) continue;
                        // If line has username
                        if (line.toLowerCase().contains(name.toLowerCase())) {
                            // Get column list and values list of INSERT INTO
                            int valuesIdx = line.toLowerCase().indexOf("values");
                            if (valuesIdx >= 0) {
                                // Get column values (first line)
                                int openCols = line.lastIndexOf('(', valuesIdx);
                                int closeCols = -1;
                                if (openCols >= 0) closeCols = line.indexOf(')', openCols);
                                String colsPart = (openCols >= 0 && closeCols > openCols) ? line.substring(openCols + 1, closeCols) : "";

                                // Get values (lines after VALUES)
                                int openVals = line.indexOf('(', valuesIdx);
                                int closeVals = -1;
                                if (openVals >= 0) closeVals = matchParenthesis(line, openVals);
                                String valsPart = (openVals >= 0 && closeVals > openVals) ? line.substring(openVals + 1, closeVals) : "";

                                List<String> cols = readSQLdata(colsPart);
                                List<String> vals = readSQLdata(valsPart);

                                if (!cols.isEmpty() && !vals.isEmpty() && cols.size() == vals.size()) {
                                    for (int i = 0; i < cols.size(); i++) {
                                        String c = cols.get(i).trim().replaceAll("[" + "`\"" + "]", "").trim();
                                        String v = vals.get(i).trim();
                                        if (v.startsWith("'") && v.endsWith("'") && v.length() >= 2) {
                                            v = v.substring(1, v.length() - 1).replace("''", "'");
                                        }
                                        details.append(c).append(": ").append(v).append("\n");
                                    }
                                } else {
                                    // Show raw data if parsing failed
                                    details.append(line.trim());
                                }
                            } else {
                                details.append(line.trim());
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(frame, "No details found of " + friend, "Friend Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, details.toString(), "Friend Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to read database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });


            // Message: open SingleChatUI w/selected friend
            messageBtn.addActionListener(e -> {
                String friend = list.getSelectedValue();
                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "Select a friend to message.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                SingleChatUI.Chat(friend);
            });

            // Block friend
            blockBtn.addActionListener(e -> {
                String friend = list.getSelectedValue();
                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "Select a friend to block.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int ok = JOptionPane.showConfirmDialog(frame, "Block " + friend + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    allFriends.remove(friend);
                    ListTable.removeElement(friend);
                    try { saveFriends(allFriends); } 
                    catch (IOException ex) { System.out.println("Cannot block friend: " + ex.getMessage()); }
                }
            });

            // Create group
            groupBtn.addActionListener(e -> {
                String groupName = JOptionPane.showInputDialog(frame, "Enter group name:", "Create Group", JOptionPane.PLAIN_MESSAGE);
                if (groupName != null) {
                    groupName = groupName.trim();
                    if (!groupName.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Group '" + groupName + "' created!\n(Functionality not implemented)", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            root.add(actions, BorderLayout.EAST);

            frame.setContentPane(root);
            frame.setVisible(true);
        });
    }

    private static List<String> loadFriends() throws IOException {
        Path fList = Paths.get("script").resolve("friends.txt");
        if (!Files.exists(fList)) { // if not, create parent dir & sample file
            if (fList.getParent() != null) Files.createDirectories(fList.getParent());
            List<String> sample = List.of("Friend A", "Friend B", "Friend C");
            Files.write(fList, sample, StandardCharsets.UTF_8);
            return new ArrayList<>(sample);
        }
        List<String> lines = Files.readAllLines(fList, StandardCharsets.UTF_8);
        return lines.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }

    private static void saveFriends(List<String> friends) throws IOException {
        Path f = Paths.get("script").resolve("friends.txt");
        if (f.getParent() != null) Files.createDirectories(f.getParent());
        Files.write(f, friends, StandardCharsets.UTF_8);
    }

    // Match ')' w/ '(' at pos openPos
    private static int matchParenthesis(String str, int openPos) {
        if (str == null || 
            openPos < 0 || 
            openPos >= str.length() || 
            str.charAt(openPos) != '(') 
            return -1;

        int depth = 0;
        for (int i = openPos; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '(') depth++;
            else if (ch == ')') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    // Read SQL data
    private static List<String> readSQLdata(String str) {
        List<String> res = new ArrayList<>();
        if (str == null) return res;

        StringBuilder curr = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // If single quote
            if (c == '\'') {
                if (inQuote && i + 1 < str.length() && str.charAt(i + 1) == '\'') {
                    curr.append('\'');
                    i++;
                } else {
                    inQuote = !inQuote;
                    curr.append('\'');
                }
            // Else if new value
            } else if (c == ',' && !inQuote) {
                res.add(curr.toString().trim());
                curr.setLength(0);
            } else {
                curr.append(c);
            }
        }

        if (curr.length() > 0) res.add(curr.toString().trim());
        return res;
    }
}	