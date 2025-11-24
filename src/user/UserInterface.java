import javax.swing.*;
import java.awt.*;

public class UserInterface {
    public static void UserUI() {
        JFrame frame = new JFrame("Admin - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(6,6));

        // Buttons list
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        JButton friendListBtn = new JButton("Friend List");
        JButton signUpBtn = new JButton("Sign Up");
        JButton updateAccBtn = new JButton("Update Account");
        JButton chatBtn = new JButton("Chat");

        Dimension buttonSize = new Dimension(140, 30);
        for (JButton b : new JButton[]{friendListBtn, signUpBtn, updateAccBtn, chatBtn}) {
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            actions.add(b);
            actions.add(Box.createVerticalStrut(8));
        }

        // Wire buttons to existing UIs
        friendListBtn.addActionListener(e -> SwingUtilities.invokeLater(FriendList::showFriendList));
        signUpBtn.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));
        updateAccBtn.addActionListener(e -> SwingUtilities.invokeLater(UpdateAccount::getInfo));
        chatBtn.addActionListener(e -> SwingUtilities.invokeLater(ChatUI::Chat));

        root.add(actions, BorderLayout.WEST);


        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
