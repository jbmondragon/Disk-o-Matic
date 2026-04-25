package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Result extends JPanel {

    private static final int CYLINDER_MIN = 0;
    private static final int CYLINDER_MAX = 199;
    private static final Color CANVAS_BG = new Color(241, 226, 198);
    private static final Color PLOT_BG = new Color(248, 217, 160);
    private static final Color MAROON = new Color(128, 1, 31);
    private static final Color SOFT_SHADOW = new Color(0, 0, 0, 28);
    private static final DateTimeFormatter EXPORT_TIMESTAMP = DateTimeFormatter.ofPattern("MMddyy_HHmmss'_PG'");

    public static final class SimulationSummary {
        private final String algorithmName;
        private final List<Integer> serviceSequence;
        private final int totalSeekTime;
        private final double averageSeekTime;

        public SimulationSummary(String algorithmName, List<Integer> serviceSequence, int totalSeekTime,
                double averageSeekTime) {
            this.algorithmName = algorithmName;
            this.serviceSequence = List.copyOf(serviceSequence);
            this.totalSeekTime = totalSeekTime;
            this.averageSeekTime = averageSeekTime;
        }

        public String getAlgorithmName() {
            return algorithmName;
        }

        public List<Integer> getServiceSequence() {
            return serviceSequence;
        }

        public int getTotalSeekTime() {
            return totalSeekTime;
        }

        public double getAverageSeekTime() {
            return averageSeekTime;
        }
    }

    private final Mainframe mainframe;
    private final RoundedPanel exportPanel;
    private final JLabel algorithmTitleLabel;
    private final JLabel headLabel;
    private final JLabel queueLabel;
    private final JLabel totalSeekLabel;
    private final JLabel timerLabel;
    private final JLabel directionBadge;
    private final JSlider speedSlider;
    private final JButton playButton;
    private final JButton exportButton;
    private final JComboBox<String> algorithmSelector;
    private final SeekGraphPanel graphPanel;
    private final Timer animationTimer;
    private final DecimalFormat timerFormat = new DecimalFormat("00.00");
    private Map<String, SimulationSummary> currentResults = Collections.emptyMap();
    private List<Integer> currentQueue = List.of();
    private SimulationSummary currentSummary;
    private int currentHeadPosition;
    private String currentDirection = "RIGHT";
    private int animationStep;
    private double elapsedSeconds;
    private boolean updatingSelector;

    public Result(Mainframe frame) {
        this.mainframe = frame;

        setLayout(new BorderLayout());
        setBackground(Mainframe.BG_DARK);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 18, 0));

        JButton returnButton = new JButton("Return");
        returnButton.setFocusPainted(false);
        returnButton.setFont(new Font("Arial", Font.BOLD, 12));
        returnButton.setBackground(Color.WHITE);
        returnButton.setForeground(new Color(80, 80, 80));
        returnButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(4, 12, 4, 12)));
        returnButton.addActionListener(e -> mainframe.showCard("SCHEDULE"));
        topBar.add(returnButton, BorderLayout.EAST);

        exportPanel = new RoundedPanel(CANVAS_BG, 24, true);
        exportPanel.setLayout(new BorderLayout());
        exportPanel.setBorder(new EmptyBorder(24, 28, 22, 28));

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout(24, 0));
        header.setOpaque(false);

        algorithmTitleLabel = new JLabel("Algorithm: --");
        algorithmTitleLabel.setForeground(MAROON);
        algorithmTitleLabel.setFont(new Font("Arial", Font.BOLD, 66));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        headLabel = createInfoLabel("Head of Queue: --");
        queueLabel = createInfoLabel("Queue: --");

        RoundedPanel divider = new RoundedPanel(new Color(181, 120, 130), 8, false);
        divider.setPreferredSize(new Dimension(470, 6));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));

        algorithmSelector = new JComboBox<>();
        algorithmSelector.setVisible(false);
        algorithmSelector.setMaximumSize(new Dimension(220, 28));
        algorithmSelector.setFont(new Font("Arial", Font.PLAIN, 13));
        algorithmSelector.addActionListener(e -> {
            if (!updatingSelector) {
                Object selected = algorithmSelector.getSelectedItem();
                if (selected instanceof String algorithmName) {
                    loadSummary(algorithmName, true);
                }
            }
        });

        infoPanel.add(headLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(queueLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(divider);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(algorithmSelector);

        header.add(algorithmTitleLabel, BorderLayout.WEST);
        header.add(infoPanel, BorderLayout.CENTER);

        graphPanel = new SeekGraphPanel();

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel leftFooter = new JPanel();
        leftFooter.setOpaque(false);
        leftFooter.setLayout(new BoxLayout(leftFooter, BoxLayout.Y_AXIS));

        JPanel statsLine = new JPanel();
        statsLine.setOpaque(false);
        statsLine.setLayout(new BoxLayout(statsLine, BoxLayout.X_AXIS));

        totalSeekLabel = new JLabel("Total Seek Time: --");
        totalSeekLabel.setForeground(MAROON);
        totalSeekLabel.setFont(new Font("Arial", Font.BOLD, 18));

        timerLabel = new JLabel("Timer: 00.00");
        timerLabel.setForeground(MAROON);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));

        statsLine.add(totalSeekLabel);
        statsLine.add(Box.createHorizontalStrut(44));
        statsLine.add(timerLabel);

        JPanel speedLine = new JPanel();
        speedLine.setOpaque(false);
        speedLine.setLayout(new BoxLayout(speedLine, BoxLayout.X_AXIS));

        JLabel speedText = new JLabel("Timer Speed");
        speedText.setForeground(MAROON);
        speedText.setFont(new Font("Arial", Font.BOLD, 16));

        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setOpaque(false);
        speedSlider.setFocusable(false);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);

        speedLine.add(speedText);
        speedLine.add(Box.createHorizontalStrut(16));
        speedLine.add(speedSlider);
        speedLine.add(Box.createHorizontalStrut(18));

        playButton = new JButton("Simulate");
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Arial", Font.PLAIN, 15));
        playButton.setBackground(Color.WHITE);
        playButton.setForeground(Color.BLACK);
        playButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(8, 18, 8, 18)));
        playButton.addActionListener(e -> toggleAnimation());

        speedLine.add(playButton);

        leftFooter.add(statsLine);
        leftFooter.add(Box.createVerticalStrut(10));
        leftFooter.add(speedLine);

        JPanel rightFooter = new JPanel();
        rightFooter.setOpaque(false);
        rightFooter.setLayout(new BoxLayout(rightFooter, BoxLayout.X_AXIS));

        JLabel directionLabel = new JLabel("Direction:");
        directionLabel.setForeground(MAROON);
        directionLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        directionBadge = new JLabel("RIGHT", SwingConstants.CENTER);
        directionBadge.setOpaque(true);
        directionBadge.setBackground(Color.WHITE);
        directionBadge.setForeground(MAROON);
        directionBadge.setFont(new Font("Arial", Font.BOLD, 14));
        directionBadge.setPreferredSize(new Dimension(84, 42));
        directionBadge.setMaximumSize(new Dimension(84, 42));
        directionBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(6, 12, 6, 12)));

        exportButton = new JButton("Export");
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exportButton.setBackground(Color.WHITE);
        exportButton.setForeground(Color.BLACK);
        exportButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 24, 10, 24)));
        exportButton.addActionListener(e -> exportResults());

        rightFooter.add(directionLabel);
        rightFooter.add(Box.createHorizontalStrut(10));
        rightFooter.add(directionBadge);
        rightFooter.add(Box.createHorizontalStrut(20));
        rightFooter.add(exportButton);

        footer.add(leftFooter, BorderLayout.WEST);
        footer.add(rightFooter, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);
        content.add(graphPanel, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);

        exportPanel.add(content, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(exportPanel, BorderLayout.CENTER);

        animationTimer = new Timer(getAnimationDelay(), e -> advanceAnimation());
        speedSlider.addChangeListener(e -> animationTimer.setDelay(getAnimationDelay()));

        setEmptyState();
    }

    public void startSimulation(Map<String, SimulationSummary> results, int headPosition, List<Integer> queue,
            String direction) {
        animationTimer.stop();
        currentResults = new LinkedHashMap<>(results);
        currentHeadPosition = headPosition;
        currentQueue = List.copyOf(queue);
        currentDirection = direction.toUpperCase();

        List<String> algorithmNames = new ArrayList<>(currentResults.keySet());
        updatingSelector = true;
        algorithmSelector.setModel(new DefaultComboBoxModel<>(algorithmNames.toArray(String[]::new)));
        updatingSelector = false;
        algorithmSelector.setVisible(algorithmNames.size() > 1);

        if (algorithmNames.isEmpty()) {
            setEmptyState();
            return;
        }

        loadSummary(algorithmNames.get(0), true);
    }

    private void setEmptyState() {
        currentSummary = null;
        currentResults = Collections.emptyMap();
        currentQueue = List.of();
        currentHeadPosition = 0;
        currentDirection = "RIGHT";
        animationStep = 0;
        elapsedSeconds = 0.0;
        algorithmTitleLabel.setText("Algorithm: --");
        headLabel.setText("Head of Queue: --");
        queueLabel.setText("Queue: --");
        totalSeekLabel.setText("Total Seek Time: --");
        timerLabel.setText("Timer: 00.00");
        directionBadge.setText("RIGHT");
        graphPanel.setSummary(null);
        graphPanel.setAnimationStep(0);
        speedSlider.setEnabled(false);
        playButton.setEnabled(false);
        exportButton.setEnabled(false);
        algorithmSelector.setVisible(false);
    }

    private void loadSummary(String algorithmName, boolean autoStart) {
        SimulationSummary summary = currentResults.get(algorithmName);
        if (summary == null) {
            return;
        }

        currentSummary = summary;
        algorithmTitleLabel.setText("Algorithm: " + summary.getAlgorithmName());
        headLabel.setText("Head of Queue: " + currentHeadPosition);
        queueLabel.setText("Queue: " + buildQueueText(currentQueue));
        totalSeekLabel.setText("Total Seek Time: " + summary.getTotalSeekTime());
        directionBadge.setText(currentDirection);
        graphPanel.setSummary(summary);
        speedSlider.setEnabled(true);
        playButton.setEnabled(summary.getServiceSequence().size() > 1);
        exportButton.setEnabled(true);

        restartAnimation(autoStart);
    }

    private void restartAnimation(boolean autoStart) {
        animationTimer.stop();
        animationStep = 0;
        elapsedSeconds = 0.0;
        timerLabel.setText("Timer: " + timerFormat.format(elapsedSeconds));
        graphPanel.setAnimationStep(0);
        playButton.setText("Simulate");

        if (autoStart && currentSummary != null && currentSummary.getServiceSequence().size() > 1) {
            animationTimer.setDelay(getAnimationDelay());
            animationTimer.start();
            playButton.setText("Pause");
        }
    }

    private void toggleAnimation() {
        if (currentSummary == null || currentSummary.getServiceSequence().size() <= 1) {
            return;
        }

        if (animationTimer.isRunning()) {
            animationTimer.stop();
            playButton.setText("Simulate");
            return;
        }

        int finalStep = Math.max(0, currentSummary.getServiceSequence().size() - 1);
        if (animationStep >= finalStep) {
            animationStep = 0;
            elapsedSeconds = 0.0;
            timerLabel.setText("Timer: " + timerFormat.format(elapsedSeconds));
            graphPanel.setAnimationStep(animationStep);
        }

        animationTimer.setDelay(getAnimationDelay());
        animationTimer.start();
        playButton.setText("Pause");
    }

    private void advanceAnimation() {
        if (currentSummary == null) {
            animationTimer.stop();
            return;
        }

        int finalStep = Math.max(0, currentSummary.getServiceSequence().size() - 1);
        if (animationStep >= finalStep) {
            animationTimer.stop();
            playButton.setText("Simulate");
            return;
        }

        animationStep++;
        elapsedSeconds += getAnimationDelay() / 1000.0;
        timerLabel.setText("Timer: " + timerFormat.format(elapsedSeconds));
        graphPanel.setAnimationStep(animationStep);
    }

    private int getAnimationDelay() {
        return 1200 - (speedSlider.getValue() * 100);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MAROON);
        label.setFont(new Font("Arial", Font.PLAIN, 22));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private String buildQueueText(List<Integer> queue) {
        if (queue.isEmpty()) {
            return "--";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < queue.size(); index++) {
            if (index > 0) {
                builder.append(' ');
            }
            builder.append(queue.get(index));
        }
        return builder.toString();
    }

    private void exportResults() {
        if (currentSummary == null) {
            JOptionPane.showMessageDialog(this, "Run a simulation before exporting results.", "Nothing to Export",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Object choice = JOptionPane.showInputDialog(
                this,
                "Choose an export format:",
                "Export Results",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] { "PNG Image", "PDF Document" },
                "PNG Image");

        if (!(choice instanceof String formatChoice)) {
            return;
        }

        String extension = formatChoice.startsWith("PNG") ? "png" : "pdf";
        String description = formatChoice.startsWith("PNG") ? "PNG Images" : "PDF Documents";

        try {
            File target = chooseExportFile(extension, description, extension);
            if (target == null) {
                return;
            }

            BufferedImage snapshot = createResultsSnapshot();
            if ("png".equals(extension)) {
                ImageIO.write(snapshot, "png", target);
            } else {
                ByteArrayOutputStream jpgBuffer = new ByteArrayOutputStream();
                ImageIO.write(snapshot, "jpg", jpgBuffer);
                writePdfFromJpeg(target, snapshot.getWidth(), snapshot.getHeight(), jpgBuffer.toByteArray());
            }

            JOptionPane.showMessageDialog(this, "Saved results to:\n" + target.getAbsolutePath(), "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not export results: " + ex.getMessage(), "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private File chooseExportFile(String extension, String description, String... allowedExtensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Results");
        fileChooser.setSelectedFile(new File(LocalDateTime.now().format(EXPORT_TIMESTAMP) + "." + extension));
        fileChooser.setFileFilter(new FileNameExtensionFilter(description, allowedExtensions));
        int selection = fileChooser.showSaveDialog(this);
        if (selection != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = fileChooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith("." + extension)) {
            file = new File(file.getParentFile(), file.getName() + "." + extension);
        }
        return file;
    }

    private BufferedImage createResultsSnapshot() {
        int width = Math.max(1220, exportPanel.getWidth());
        int height = Math.max(610, exportPanel.getHeight());
        exportPanel.setSize(width, height);
        layoutRecursively(exportPanel);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Mainframe.BG_DARK);
        graphics.fillRect(0, 0, width, height);
        exportPanel.printAll(graphics);
        graphics.dispose();
        return image;
    }

    private void layoutRecursively(Component component) {
        if (component instanceof JPanel panel) {
            panel.doLayout();
            for (Component child : panel.getComponents()) {
                layoutRecursively(child);
            }
        }
    }

    private void writePdfFromJpeg(File target, int width, int height, byte[] jpegBytes) throws IOException {
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();

        writeAscii(pdf, "%PDF-1.4\n%");
        pdf.write(new byte[] { (byte) 0xE2, (byte) 0xE3, (byte) 0xCF, (byte) 0xD3 });
        writeAscii(pdf, "\n");

        offsets.add(pdf.size());
        writeAscii(pdf, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        offsets.add(pdf.size());
        writeAscii(pdf, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        offsets.add(pdf.size());
        writeAscii(pdf,
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + width + ' ' + height
                        + "] /Resources << /ProcSet [/PDF /ImageC] /XObject << /Im0 4 0 R >> >> /Contents 5 0 R >>\nendobj\n");

        offsets.add(pdf.size());
        writeAscii(pdf,
                "4 0 obj\n<< /Type /XObject /Subtype /Image /Width " + width + " /Height " + height
                        + " /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length "
                        + jpegBytes.length + " >>\nstream\n");
        pdf.write(jpegBytes);
        writeAscii(pdf, "\nendstream\nendobj\n");

        byte[] contentBytes = ("q\n" + width + " 0 0 " + height + " 0 0 cm\n/Im0 Do\nQ\n")
                .getBytes(StandardCharsets.US_ASCII);
        offsets.add(pdf.size());
        writeAscii(pdf, "5 0 obj\n<< /Length " + contentBytes.length + " >>\nstream\n");
        pdf.write(contentBytes);
        writeAscii(pdf, "endstream\nendobj\n");

        int xrefOffset = pdf.size();
        writeAscii(pdf, "xref\n0 6\n0000000000 65535 f \n");
        for (int offset : offsets) {
            writeAscii(pdf, String.format("%010d 00000 n \n", offset));
        }
        writeAscii(pdf, "trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n" + xrefOffset + "\n%%EOF");

        try (FileOutputStream output = new FileOutputStream(target)) {
            pdf.writeTo(output);
        }
    }

    private void writeAscii(ByteArrayOutputStream output, String text) {
        output.writeBytes(text.getBytes(StandardCharsets.US_ASCII));
    }

    private final class SeekGraphPanel extends RoundedPanel {
        private SimulationSummary summary;
        private int currentStep;

        private SeekGraphPanel() {
            super(PLOT_BG, 26, false);
            setPreferredSize(new Dimension(1120, 356));
            setMinimumSize(new Dimension(760, 356));
        }

        private void setSummary(SimulationSummary summary) {
            this.summary = summary;
            repaint();
        }

        private void setAnimationStep(int step) {
            currentStep = step;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (summary == null || summary.getServiceSequence().isEmpty()) {
                g2.dispose();
                return;
            }

            int left = 40;
            int right = getWidth() - 36;
            int top = 70;
            int bottom = getHeight() - 32;
            int width = Math.max(1, right - left);
            int height = Math.max(1, bottom - top);
            int axisY = top - 16;

            g2.setColor(new Color(0, 0, 0, 30));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(left, axisY, right, axisY);

            Set<Integer> markers = new LinkedHashSet<>();
            markers.add(CYLINDER_MIN);
            markers.addAll(currentQueue);
            markers.add(currentHeadPosition);
            markers.add(CYLINDER_MAX);

            List<Integer> sequence = summary.getServiceSequence();
            int finalIndex = Math.max(0, sequence.size() - 1);
            int visibleIndex = Math.min(currentStep, finalIndex);

            List<Integer> sortedMarkers = new ArrayList<>(markers);
            sortedMarkers.sort(Integer::compareTo);
            List<AxisLabelPlacement> placements = computeAxisLabelPlacements(g2, sortedMarkers, left, right, width);
            int currentCylinder = sequence.get(visibleIndex);

            for (AxisLabelPlacement placement : placements) {
                boolean current = placement.value() == currentCylinder;
                g2.setColor(current ? MAROON : Color.BLACK);
                g2.fillOval(placement.centerX() - (current ? 5 : 3), axisY - (current ? 5 : 3), current ? 10 : 6,
                        current ? 10 : 6);

                g2.setFont(new Font("Arial", current ? Font.BOLD : Font.PLAIN, current ? 12 : 11));
                g2.drawString(String.valueOf(placement.value()), placement.leftX(), placement.baseY());
            }

            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(Color.BLACK);
            for (int index = 1; index <= visibleIndex; index++) {
                int fromX = mapCylinder(sequence.get(index - 1), left, width);
                int fromY = mapStep(index - 1, finalIndex, top, height);
                int toX = mapCylinder(sequence.get(index), left, width);
                int toY = mapStep(index, finalIndex, top, height);
                g2.drawLine(fromX, fromY, toX, toY);
            }

            for (int index = 0; index < sequence.size(); index++) {
                if (index == finalIndex) {
                    continue;
                }
                int x = mapCylinder(sequence.get(index), left, width);
                int y = mapStep(index, finalIndex, top, height);
                if (index < visibleIndex) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x - 4, y - 4, 8, 8);
                } else if (index == visibleIndex) {
                    g2.setColor(new Color(255, 225, 112));
                    g2.fillOval(x - 8, y - 8, 16, 16);
                    g2.setColor(MAROON);
                    g2.fillOval(x - 5, y - 5, 10, 10);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(x - 8, y - 8, 16, 16);
                } else {
                    g2.setColor(new Color(125, 125, 125, 90));
                    g2.drawOval(x - 4, y - 4, 8, 8);
                }
            }

            int markerIndex = visibleIndex;
            int markerX = mapCylinder(sequence.get(markerIndex), left, width);
            int markerY = mapStep(markerIndex, finalIndex, top, height);
            if (markerIndex > 0) {
                int prevX = mapCylinder(sequence.get(markerIndex - 1), left, width);
                int prevY = mapStep(markerIndex - 1, finalIndex, top, height);
                drawArrowHead(g2, prevX, prevY, markerX, markerY);
            } else {
                g2.setColor(MAROON);
                g2.fillOval(markerX - 6, markerY - 6, 12, 12);
                g2.setColor(Color.BLACK);
                g2.drawOval(markerX - 6, markerY - 6, 12, 12);
            }

            g2.dispose();
        }

        private void drawArrowHead(Graphics2D g2, int fromX, int fromY, int toX, int toY) {
            double angle = Math.atan2(toY - fromY, toX - fromX);
            int size = 16;

            Path2D arrow = new Path2D.Double();
            arrow.moveTo(toX, toY);
            arrow.lineTo(toX - (size * Math.cos(angle - Math.PI / 7)), toY - (size * Math.sin(angle - Math.PI / 7)));
            arrow.lineTo(toX - (size * 0.7 * Math.cos(angle)), toY - (size * 0.7 * Math.sin(angle)));
            arrow.lineTo(toX - (size * Math.cos(angle + Math.PI / 7)), toY - (size * Math.sin(angle + Math.PI / 7)));
            arrow.closePath();

            g2.setColor(MAROON);
            g2.fill(arrow);
            g2.setColor(Color.BLACK);
            g2.draw(arrow);
        }

        private int mapCylinder(int cylinder, int left, int width) {
            double ratio = (double) (cylinder - CYLINDER_MIN) / (CYLINDER_MAX - CYLINDER_MIN);
            return left + (int) Math.round(ratio * width);
        }

        private int mapStep(int step, int finalIndex, int top, int height) {
            if (finalIndex <= 0) {
                return top + (height / 2);
            }
            double ratio = (double) step / finalIndex;
            return top + (int) Math.round(ratio * height);
        }

        private List<AxisLabelPlacement> computeAxisLabelPlacements(Graphics2D g2, List<Integer> markers, int left,
                int right,
                int width) {
            List<AxisLabelPlacement> placements = new ArrayList<>();
            int minGap = 4;
            int[] rowBaseY = new int[] { 28, 42, 56 };
            int[] rowNextLeft = new int[] { left, left, left };
            Font baseFont = new Font("Arial", Font.PLAIN, 11);
            g2.setFont(baseFont);

            for (int marker : markers) {
                String text = String.valueOf(marker);
                int textWidth = g2.getFontMetrics(baseFont).stringWidth(text);
                int centerX = mapCylinder(marker, left, width);
                int preferredLeft = centerX - (textWidth / 2);
                int clampedLeft = Math.max(left, Math.min(preferredLeft, right - textWidth));

                int row = 0;
                while (row < rowNextLeft.length - 1 && clampedLeft < rowNextLeft[row]) {
                    row++;
                }
                if (clampedLeft < rowNextLeft[row]) {
                    clampedLeft = rowNextLeft[row];
                }
                if (clampedLeft + textWidth > right) {
                    row = 0;
                    int bestLeft = clampedLeft;
                    int smallestOverflow = Integer.MAX_VALUE;
                    for (int candidateRow = 0; candidateRow < rowNextLeft.length; candidateRow++) {
                        int candidateLeft = Math.max(left, Math.min(right - textWidth, rowNextLeft[candidateRow]));
                        int overflow = Math.max(0, (candidateLeft + textWidth) - right);
                        if (overflow < smallestOverflow) {
                            smallestOverflow = overflow;
                            row = candidateRow;
                            bestLeft = candidateLeft;
                        }
                    }
                    clampedLeft = bestLeft;
                }

                rowNextLeft[row] = clampedLeft + textWidth + minGap;
                placements.add(new AxisLabelPlacement(marker, centerX, clampedLeft, rowBaseY[row]));
            }
            return placements;
        }
    }

    private record AxisLabelPlacement(int value, int centerX, int leftX, int baseY) {
    }

    private static class RoundedPanel extends JPanel {
        private final Color fillColor;
        private final int radius;
        private final boolean shadow;

        private RoundedPanel(Color fillColor, int radius, boolean shadow) {
            this.fillColor = fillColor;
            this.radius = radius;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (shadow) {
                g2.setColor(SOFT_SHADOW);
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, radius, radius);
            }
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, radius, radius);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}