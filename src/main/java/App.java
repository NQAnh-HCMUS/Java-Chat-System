// This is main.
import src.admin.*;
import src.user.*;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Java Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenuItem openAdmin = new JMenuItem("Open Admin");
        openAdmin.addActionListener(e -> SwingUtilities.invokeLater(AdminInterface::AdminUI));
        options.add(openAdmin);

        JMenuItem openUser = new JMenuItem("Open User");
        openUser.addActionListener(e -> SwingUtilities.invokeLater(UserInterface::UserUI));
        options.add(openUser);


        JMenuItem userList = new JMenuItem("[Admin] All List");
        userList.addActionListener(e -> SwingUtilities.invokeLater(AllList::List));
        options.add(userList);

        JMenuItem loginLog = new JMenuItem("[Admin] Login Log");
        loginLog.addActionListener(e -> SwingUtilities.invokeLater(LogInLog::List));
        options.add(loginLog);

        JMenuItem spamLog = new JMenuItem("[Admin] Spam Log");
        spamLog.addActionListener(e -> SwingUtilities.invokeLater(SpamLog::List));
        options.add(spamLog);

/**********************************************************************************/
    
        JMenuItem chatUI = new JMenuItem("[User] Chat Interface");
        chatUI.addActionListener(e -> SwingUtilities.invokeLater(ChatUI::Chat));
        options.add(chatUI);

        JMenuItem friendList = new JMenuItem("[User] Friend List");
        friendList.addActionListener(e -> SwingUtilities.invokeLater(FriendList::showFriendList));
        options.add(friendList);
        
        JMenuItem signUp = new JMenuItem("[User] Sign Up");
        signUp.addActionListener(e -> SwingUtilities.invokeLater(SignUp::getInfo));
        options.add(signUp);
        
        JMenuItem updateAccount = new JMenuItem("[User] Update Account Info");
        updateAccount.addActionListener(e -> SwingUtilities.invokeLater(UpdateAccount::getInfo));
        options.add(updateAccount);

        
        
        
        
        menuBar.add(options);
        frame.setJMenuBar(menuBar);

        JPanel root = new JPanel(new BorderLayout(6,6));

        // JTextArea chatArea = new JTextArea();
        // chatArea.setEditable(false);
        // chatArea.setLineWrap(true);
        // chatArea.setWrapStyleWord(true);
        // JScrollPane scroll = new JScrollPane(chatArea);
        // root.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(6,6));
        JTextField inputField = new JTextField();
        JButton sendBtn = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        // sendBtn.addActionListener(e -> {
        //     String text = inputField.getText().trim();
        //     if (!text.isEmpty()) {
        //         chatArea.append("You: " + text + "\n");
        //         inputField.setText("");
        //     }
        // });

        inputField.addActionListener(e -> sendBtn.doClick());

        root.add(inputPanel, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
