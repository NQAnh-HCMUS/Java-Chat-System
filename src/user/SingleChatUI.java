import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SingleChatUI {
    public static void Chat(String friend) {
        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame frame = new JFrame("JCS - Chatting with " + friend);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("Menu");
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

            JPanel root = new JPanel(new BorderLayout(6,6));

            // Prep log
            String safeName = friend.replaceAll("\\s",""); // Remove whitespaces
            Path logsDir = Paths.get("script", "chatlogs");
            Path logFile = logsDir.resolve(safeName + ".log");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Live chat log
            JTextArea chatArea = new JTextArea();
            chatArea.setEditable(false);
            chatArea.setLineWrap(true);
            chatArea.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(chatArea);
            root.add(scroll, BorderLayout.CENTER);

            // If log dir/file not exist, create
            try {
                if (!Files.exists(logsDir)) Files.createDirectories(logsDir);
                if (!Files.exists(logFile)) Files.createFile(logFile);
            } catch (IOException e) {
                System.out.println("Failed to create chat log file: " + e.getMessage());
            }
            // Load chat log
            try {
                if (Files.exists(logFile)) {
                    List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
                    for (String line : lines) {
                        if (line.trim().isEmpty()) continue; // Ignore empty lines
                        // Expected format: time,username,sent|received,message
                        String[] parts = line.split(",", 4);
                        if (parts.length >= 3) {
                            String time = parts[0];
                            String user = parts.length > 1 ? parts[1] : "";
                            String dir = parts.length > 2 ? parts[2] : "";
                            String msg = parts.length > 3 ? parts[3] : "";
                            chatArea.append(time + " " + user + " (" + dir + "): " + msg + "\n");
                        } else {
                            chatArea.append(line + "\n");
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Failed to load chat log: " + ex.getMessage());
            }

            // Input panel
            JPanel inputPanel = new JPanel(new BorderLayout(6,6));
            JTextField inputField = new JTextField();
            JButton sendBtn = new JButton("Send");
            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(sendBtn, BorderLayout.EAST);

            sendBtn.addActionListener(e -> {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    chatArea.append("You: " + text + "\n");
                    inputField.setText("");

                    // append to log file: time,username,sent|received,message
                    try {
                        // if log dir not exist, create
                        if (!Files.exists(logsDir)) Files.createDirectories(logsDir);
                        String time = LocalDateTime.now().format(dtf);
                        String safeMessage = text.replaceAll("[\\r\\n]+", " ");
                        String entry = time + "," + friend + ",sent," + safeMessage + System.lineSeparator();
                        Files.write(logFile, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } catch (IOException ioEx) {
                        System.out.println("Failed to write chat log: " + ioEx.getMessage());
                    }
                }
            });

            inputField.addActionListener(e -> sendBtn.doClick());

            root.add(inputPanel, BorderLayout.SOUTH);

            frame.setContentPane(root);
            frame.setVisible(true);
        });
    }
}
