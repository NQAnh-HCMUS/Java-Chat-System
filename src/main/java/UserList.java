import javax.swing.*;
import java.awt.*;

public class UserList {
    public static void List() {
        JFrame frame = new JFrame("UserList - Admin Only");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(6,6));

        JTextArea listArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(listArea);
        root.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(6,6));
        JTextField inputField = new JTextField();
        JButton sendBtn = new JButton("Search");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        inputField.addActionListener(e -> sendBtn.doClick());

        root.add(inputPanel, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
