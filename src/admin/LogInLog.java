import javax.swing.*;
import java.awt.*;

public class LogInLog {
    public static void List() {
        JFrame frame = new JFrame("LogIn Log - Admin Only");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(6,6));

        // Scrollable log
        JTextArea listArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(listArea);
        root.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(6,6));
        JTextField inputField = new JTextField();
        JButton sendBtn = new JButton("Search");
        JButton reloadBtn = new JButton("Reload");
        inputPanel.add(inputField, BorderLayout.CENTER);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.add(sendBtn);
        right.add(reloadBtn);
        inputPanel.add(right, BorderLayout.EAST);

        inputField.addActionListener(e -> sendBtn.doClick());

        root.add(inputPanel, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}