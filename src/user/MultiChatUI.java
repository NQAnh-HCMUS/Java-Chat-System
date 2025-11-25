import javax.swing.*;
import java.awt.*;

public class MultiChatUI {
    public static void Chat () {
        JFrame frame = new JFrame("JCS - Chatting with ____,____,____");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        // JMenuItem openAdmin = new JMenuItem("Open Admin");
        // openAdmin.addActionListener(e -> SwingUtilities.invokeLater(AdminInterface::AdminUI));
        // options.add(openAdmin);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        


        JPanel root = new JPanel(new BorderLayout(6,6));

        // Live chat log
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(chatArea);
        root.add(scroll, BorderLayout.CENTER);

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
            }
        });

        inputField.addActionListener(e -> sendBtn.doClick());

        root.add(inputPanel, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
