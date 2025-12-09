import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class UserInterface {
    public static void UserUI() {
        JFrame frame = new JFrame("User - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(6,6));

        // Buttons list
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        JButton friendListBtn = new JButton("Friend List");
        JButton friendReqBtn = new JButton("Friend Requests");
        JButton multichatBtn = new JButton("Multi Chat");
        JButton singlechatBtn = new JButton("Single Chat");
        JButton signUpBtn = new JButton("Sign Up");
        JButton updateAccBtn = new JButton("Update Account");
        JButton regenpasswdButton = new JButton("Regenerate Password");

        Dimension buttonSize = new Dimension(160, 30);
        for (JButton b : new JButton[]{regenpasswdButton, signUpBtn, updateAccBtn, friendReqBtn, friendListBtn, singlechatBtn, multichatBtn}) {
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            actions.add(b);
            actions.add(Box.createVerticalStrut(8)); // Gap between buttons
        }

        // Wire buttons to existing UIs
        regenpasswdButton.addActionListener(e -> SwingUtilities.invokeLater(RegenPass::getInfo));
        friendListBtn.addActionListener(e -> SwingUtilities.invokeLater(FriendList::showFriendList));
        signUpBtn.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));
        updateAccBtn.addActionListener(e -> SwingUtilities.invokeLater(UpdateAccount::getInfo));
        singlechatBtn.addActionListener(e -> {
            String friend = JOptionPane.showInputDialog(frame, "Enter username to chat with:", "Single Chat", JOptionPane.PLAIN_MESSAGE);
            if (friend != null && !friend.trim().isEmpty()) {
                String f = friend.trim();
                SwingUtilities.invokeLater(() -> SingleChatUI.Chat(f));
            }
        });
        multichatBtn.addActionListener(e -> SwingUtilities.invokeLater(MultiChatUI::Chat));
        friendReqBtn.addActionListener(e -> SwingUtilities.invokeLater(FriendRequest::showRequest));

        root.add(actions, BorderLayout.WEST);


        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
