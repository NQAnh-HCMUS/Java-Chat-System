import javax.swing.*;
import java.awt.*;

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
        JButton signUpBtn = new JButton("Sign Up");
        JButton loginBtn = new JButton("Login");
        JButton friendListBtn = new JButton("Friend List");
        JButton updateAccBtn = new JButton("Update Account");
        JButton chatBtn = new JButton("Chat");

        Dimension buttonSize = new Dimension(140, 30);
        for (JButton b : new JButton[]{loginBtn, signUpBtn, updateAccBtn, friendListBtn, chatBtn}) {
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            actions.add(b);
            actions.add(Box.createVerticalStrut(8)); // Gap between buttons
        }

        // Wire buttons to existing UIs
        loginBtn.addActionListener(e -> SwingUtilities.invokeLater(LogIn::getInfo));
        friendListBtn.addActionListener(e -> SwingUtilities.invokeLater(FriendList::showFriendList));
        signUpBtn.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));
        updateAccBtn.addActionListener(e -> SwingUtilities.invokeLater(UpdateAccount::getInfo));
        chatBtn.addActionListener(e -> SwingUtilities.invokeLater(ChatUI::Chat));

        root.add(actions, BorderLayout.WEST);


        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
