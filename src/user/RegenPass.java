import javax.swing.*;
import java.awt.*;

public class RegenPass {
    public static void getInfo() {
        JFrame frame = new JFrame("Regenerate Password - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 120);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Form fields
        JPanel form = new JPanel(new GridLayout(0, 2));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        
        form.add(emailLabel);
        form.add(emailField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton sendBtn = new JButton("Randomize");
        buttons.add(sendBtn);
        root.add(buttons, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
