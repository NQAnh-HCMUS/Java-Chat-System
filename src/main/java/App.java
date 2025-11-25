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
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenuItem openAdmin = new JMenuItem("Open Admin");
        openAdmin.addActionListener(e -> SwingUtilities.invokeLater(AdminInterface::AdminUI));
        options.add(openAdmin);

        JMenuItem openUser = new JMenuItem("Open User");
        openUser.addActionListener(e -> SwingUtilities.invokeLater(UserInterface::UserUI));
        options.add(openUser);
        
        menuBar.add(options);
        frame.setJMenuBar(menuBar);

        JPanel root = new JPanel(new BorderLayout(6,6));

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
