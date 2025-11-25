import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class Graph {
    public static void chooseYear() {
        JFrame frame = new JFrame("[Admin] New Account Graph - JCS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(8,8));

        // Top controls: year input + buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JLabel yearLabel = new JLabel("Year:");
        JTextField yearField = new JTextField(String.valueOf(LocalDateTime.now().getYear()), 6);
        JButton showBtn = new JButton("Show");
        JButton reloadBtn = new JButton("Reload Data");
        top.add(yearLabel);
        top.add(yearField);
        top.add(showBtn);
        top.add(reloadBtn);

        root.add(top, BorderLayout.NORTH);

        // Chart panel
        BarChartPanel chart = new BarChartPanel(yearField);
        root.add(chart, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // Bar chart panel
    private static class BarChartPanel extends JPanel {
        private int[] monthlyData = new int[12];
        private final JTextField yearField;

        BarChartPanel(JTextField yearField) {
            this.yearField = yearField;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D timeGraph = (Graphics2D) g.create();
            timeGraph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int margin = 50;
            int chartWidth = width - margin*2;
            int chartHeight = height - margin*2;

            // Draw axes
            timeGraph.drawLine(margin, margin, margin, margin + chartHeight);
            timeGraph.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight);

            // Find maxY
            int maxY = 1;
            for (int data : monthlyData) if (data > maxY) maxY = data;

            // Draw Y marks
            int yTicks = Math.min(maxY, 10);
            for (int i = 0; i <= yTicks; i++) {
                int y = margin + chartHeight - (i * chartHeight / yTicks);
                int value = i * maxY / yTicks;
                timeGraph.drawLine(margin - 4, y, margin, y);
                timeGraph.drawString(String.valueOf(value), 6, y+4);
            }

            // Get year
            int defaultYear = LocalDateTime.now().getYear(); // Default current year
            try {
                String input = yearField.getText().trim();
                if (!input.isEmpty()) defaultYear = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {}

            // Draw X marks & bars
            int barSpace = chartWidth / 12;
            for (int i = 0; i < 12; i++) {
                int x = margin + i * barSpace + 4;
                int barWidth = Math.max(8, barSpace - 8);
                int barHeight = (int) ((double) monthlyData[i] / (double) maxY * (chartHeight - 20));
                int y = margin + chartHeight - barHeight;
                timeGraph.setColor(new Color(100, 150, 220));
                timeGraph.fillRect(x, y, barWidth, barHeight);
                timeGraph.setColor(Color.BLACK);
                timeGraph.drawRect(x, y, barWidth, barHeight);
                // Month labels
                String mLabel = LocalDateTime.of(defaultYear, i+1, 1,0,0).getMonth().toString().substring(0,3);
                timeGraph.drawString(mLabel, x, margin + chartHeight + 15);
                // Value label
                timeGraph.drawString(String.valueOf(monthlyData[i]), x, y - 4);
            }

            // Chart title
            timeGraph.setFont(timeGraph.getFont().deriveFont(Font.BOLD, 14f));
            timeGraph.drawString("NEW SIGNUPS IN: " + defaultYear, width/2 - 60, margin/2 + 6);

            timeGraph.dispose();
        }
    }
}
