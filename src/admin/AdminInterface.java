import javax.swing.*;
import java.awt.*;

public class AdminInterface {
    public static void AdminUI() {
        JFrame frame = new JFrame("Admin - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(6,6));

        // Buttons list
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        JButton listBtn = new JButton("All List");
        JButton graphBtn = new JButton("Graph");
        JButton groupManagerBtn = new JButton("Group Manager");
        JButton userManagerBtn = new JButton("User Manager");
        JButton loginLogBtn = new JButton("Login Log");
        JButton spamLogBtn = new JButton("Spam Log");

        Dimension buttonSize = new Dimension(140, 30);
        for (JButton b : new JButton[]{listBtn, graphBtn, groupManagerBtn, userManagerBtn, loginLogBtn, spamLogBtn}) {
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            actions.add(b);
            actions.add(Box.createVerticalStrut(8));
        }

        // Wire buttons to existing UIs
        listBtn.addActionListener(e -> SwingUtilities.invokeLater(AllList::List));
        graphBtn.addActionListener(e -> SwingUtilities.invokeLater(Graph::chooseYear));
        groupManagerBtn.addActionListener(e -> SwingUtilities.invokeLater(GroupManager::showGroup));
        userManagerBtn.addActionListener(e -> SwingUtilities.invokeLater(UserManager::showUser));        
        loginLogBtn.addActionListener(e -> SwingUtilities.invokeLater(LogInLog::List));
        spamLogBtn.addActionListener(e -> SwingUtilities.invokeLater(SpamLog::List));
        

        root.add(actions, BorderLayout.WEST);


        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
