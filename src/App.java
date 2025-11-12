package com.example.chat;

import javax.swing.*;
import java.awt.*;

/**
 * Minimal Swing application entry point for Java-Chat-System.
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Java Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

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
