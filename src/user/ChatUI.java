import javax.swing.*;
import java.awt.*;

public class ChatUI {
    public static void Chat () {
        JFrame frame = new JFrame("Java Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenuItem openAdmin = new JMenuItem("Open Admin");
        openAdmin.addActionListener(e -> SwingUtilities.invokeLater(AdminInterface::AdminUI));
        options.add(openAdmin);

        menuBar.add(options);
        frame.setJMenuBar(menuBar);

        


        JPanel root = new JPanel(new BorderLayout(6,6));

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(chatArea);
        root.add(scroll, BorderLayout.CENTER);

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
