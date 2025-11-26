import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ActivityGraph {
    // Modern color palette
    private static final Color DARK_BLUE = new Color(0x001C44);
    private static final Color BLUE = new Color(0x0C5776);
    private static final Color TEAL = new Color(0x2D99AE);
    private static final Color LIGHT_TEAL = new Color(0xBCFEFE);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(0xF8F9FA);
    private static final Color CHART_GREEN = new Color(0x27AE60);
    private static final Color CHART_HOVER = new Color(0x229954);
    
    // Modern fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font CHART_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font CHART_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public static void chooseYear() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("User Activity Analytics - Admin Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

            // Header Panel
            JPanel headerPanel = createHeaderPanel();
            
            // Main Content Panel
            JPanel mainPanel = createMainPanel();
            
            // Footer Panel
            JPanel footerPanel = createFooterPanel();

            // Add panels to frame
            frame.add(headerPanel, BorderLayout.NORTH);
            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(footerPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        headerPanel.setPreferredSize(new Dimension(100, 70));
        
        // Title
        JLabel titleLabel = new JLabel("User Activity Analytics");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(WHITE);
        
        // Controls panel
        JPanel controlsPanel = createControlsPanel();
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private static JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(DARK_BLUE);
        controlsPanel.setOpaque(false);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearLabel.setForeground(LIGHT_TEAL);

        JTextField yearField = new JTextField(String.valueOf(LocalDateTime.now().getYear()), 6);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEAL, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JButton showBtn = createModernButton("Show Chart", TEAL);
        JButton reloadBtn = createModernButton("Reload Data", BLUE);

        controlsPanel.add(yearLabel);
        controlsPanel.add(yearField);
        controlsPanel.add(showBtn);
        controlsPanel.add(reloadBtn);

        return controlsPanel;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Chart panel
        BarChartPanel chartPanel = new BarChartPanel();
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE1E8ED), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        chartPanel.setBackground(WHITE);

        mainPanel.add(chartPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }

    private static JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(LIGHT_TEAL);
        button.setForeground(DARK_BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(WHITE);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_TEAL);
                button.setForeground(DARK_BLUE);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("Activity Analytics • Tracking: User Engagement • Updated: Real-time");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BLUE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    // Bar chart panel 
    private static class BarChartPanel extends JPanel {
        private int[] monthlyData = {1250, 1340, 1280, 1450, 1670, 1890, 2100, 1980, 1760, 1540, 1420, 1300}; // Sample activity data

        BarChartPanel() {
            setBackground(WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int margin = 60;
            int chartWidth = width - margin * 2;
            int chartHeight = height - margin * 2;

            // Draw background grid
            g2d.setColor(new Color(0xF8F9FA));
            int gridLines = 10;
            for (int i = 0; i <= gridLines; i++) {
                int y = margin + (i * chartHeight / gridLines);
                g2d.drawLine(margin, y, margin + chartWidth, y);
            }

            // Draw axes
            g2d.setColor(new Color(0xBDC3C7));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawLine(margin, margin, margin, margin + chartHeight);
            g2d.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight);

            // Find maxY với padding
            int maxY = 1;
            for (int data : monthlyData) {
                if (data > maxY) maxY = data;
            }
            maxY = (int)(maxY * 1.1); // Add 10% padding

            // Draw Y axis labels
            g2d.setFont(CHART_FONT);
            g2d.setColor(BLUE);
            int yTicks = Math.min(maxY, 10);
            for (int i = 0; i <= yTicks; i++) {
                int y = margin + chartHeight - (i * chartHeight / yTicks);
                int value = i * maxY / yTicks;
                
                // Grid line
                g2d.setColor(new Color(0xECF0F1));
                g2d.drawLine(margin, y, margin + chartWidth, y);
                
                // Value label
                g2d.setColor(BLUE);
                g2d.drawString(String.valueOf(value), margin - 35, y + 4);
            }

            // Draw bars và labels
            int barSpace = chartWidth / 12;
            int currentYear = LocalDateTime.now().getYear();
            
            for (int i = 0; i < 12; i++) {
                int x = margin + i * barSpace + 10;
                int barWidth = Math.max(20, barSpace - 20);
                int barHeight = (int) ((double) monthlyData[i] / (double) maxY * chartHeight);
                int y = margin + chartHeight - barHeight;
                
                // Gradient fill for bars (màu xanh lá cho activity)
                GradientPaint gradient = new GradientPaint(
                    x, y, CHART_GREEN,
                    x, y + barHeight, CHART_HOVER
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                
                // Bar border
                g2d.setColor(new Color(0x2C3E50));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(x, y, barWidth, barHeight, 8, 8);

                // Month labels
                g2d.setColor(DARK_BLUE);
                String monthLabel = LocalDateTime.of(currentYear, i + 1, 1, 0, 0)
                    .getMonth().toString().substring(0, 3);
                g2d.drawString(monthLabel, x + barWidth/2 - 10, margin + chartHeight + 20);

                // Value labels on bars
                g2d.setColor(DARK_BLUE);
                g2d.setFont(CHART_FONT.deriveFont(Font.BOLD));
                String valueStr = String.valueOf(monthlyData[i]);
                int textWidth = g2d.getFontMetrics().stringWidth(valueStr);
                g2d.drawString(valueStr, x + barWidth/2 - textWidth/2, y - 8);
            }

            // Chart title
            g2d.setFont(CHART_TITLE_FONT);
            g2d.setColor(DARK_BLUE);
            String title = "USER ACTIVITY TRENDS - " + currentYear;
            int titleWidth = g2d.getFontMetrics().stringWidth(title);
            g2d.drawString(title, width/2 - titleWidth/2, margin - 20);

            // Y axis label
            g2d.rotate(-Math.PI / 2);
            g2d.drawString("Active Users", -height/2 - 30, margin - 45);
            g2d.rotate(Math.PI / 2);

            // X axis label
            g2d.drawString("Months", width/2 - 25, height - 15);

            g2d.dispose();
        }
    }
}