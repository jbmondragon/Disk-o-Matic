package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Page extends JPanel {

    private Mainframe mainframe;

    private JTextField queueLengthField;
    private JTextField cylindersField;
    private JTextField headField;

    private JRadioButton leftDirBtn;
    private JRadioButton rightDirBtn;

    private JComboBox<String> algoCombo;

    private static final int ICON_SIZE = 26;

    private static final String[] ALGORITHMS = {
            "FCFS", "SSTF", "SCAN", "C-SCAN", "LOOK", "C-LOOK"
    };

    public Page(Mainframe frame) {
        this.mainframe = frame;
        setLayout(new BorderLayout());
        setBackground(Mainframe.BG_DARK);

        ImageIcon randomIcon = loadIcon("img/random.png", ICON_SIZE);
        ImageIcon importIcon = loadIcon("img/import.png", ICON_SIZE);

        // ── Top header bar ────────────────────────────────────────────────
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Mainframe.BG_DARK);
        topHeader.setBorder(new EmptyBorder(8, 14, 6, 14));

        JLabel returnLbl = new JLabel("Return");
        returnLbl.setForeground(Mainframe.TEXT_LIGHT);
        returnLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        returnLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnLbl.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { mainframe.showCard("MENU"); }
            @Override public void mouseEntered(MouseEvent e) { returnLbl.setText("<html><u>Return</u></html>"); }
            @Override public void mouseExited(MouseEvent e)  { returnLbl.setText("Return"); }
        });

        JLabel titleLbl = new JLabel("Disk-o-Matic", SwingConstants.CENTER);
        titleLbl.setForeground(Mainframe.TEXT_LIGHT);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel spacer = new JLabel("Return");
        spacer.setFont(returnLbl.getFont());
        spacer.setVisible(false);
        spacer.setPreferredSize(returnLbl.getPreferredSize());

        topHeader.add(returnLbl, BorderLayout.WEST);
        topHeader.add(titleLbl,  BorderLayout.CENTER);
        topHeader.add(spacer,    BorderLayout.EAST);
        add(topHeader, BorderLayout.NORTH);

        // ── Body ──────────────────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout(12, 0));
        body.setBackground(Mainframe.BG_DARK);
        body.setBorder(new EmptyBorder(4, 8, 8, 8));

        // ── Left card (input form) ────────────────────────────────────────
        JPanel leftCard = new JPanel(new BorderLayout());
        leftCard.setBackground(Color.WHITE);
        leftCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 185, 185), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 14, 0)));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(new Color(128, 1, 31));
        cardHeader.setBorder(new EmptyBorder(11, 16, 11, 16));

        JPanel cardTitleBox = new JPanel();
        cardTitleBox.setLayout(new BoxLayout(cardTitleBox, BoxLayout.Y_AXIS));
        cardTitleBox.setBackground(new Color(128, 1, 31));

        JLabel cardTitle = new JLabel("Disk Scheduling Algorithm");
        cardTitle.setFont(new Font("Arial", Font.BOLD, 16));
        cardTitle.setForeground(Color.WHITE);

        JLabel cardSub = new JLabel("Configure and run a simulation");
        cardSub.setFont(new Font("Arial", Font.PLAIN, 11));
        cardSub.setForeground(Color.WHITE);

        cardTitleBox.add(cardTitle);
        cardTitleBox.add(cardSub);
        cardHeader.add(cardTitleBox, BorderLayout.WEST);
        leftCard.add(cardHeader, BorderLayout.NORTH);

        // ── Input fields ──────────────────────────────────────────────────
        JPanel inputFields = new JPanel();
        inputFields.setLayout(new BoxLayout(inputFields, BoxLayout.Y_AXIS));
        inputFields.setBackground(Color.WHITE);
        inputFields.setBorder(new EmptyBorder(22, 40, 22, 40));

        queueLengthField = styledField(140, 46);
        cylindersField   = styledField(Integer.MAX_VALUE, 46);
        headField        = styledField(140, 46);

        JPanel dirRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        dirRow.setBackground(Color.WHITE);
        dirRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup dirGroup = new ButtonGroup();
        leftDirBtn  = styledRadio("Left",  Color.WHITE);
        rightDirBtn = styledRadio("Right", Color.WHITE);
        rightDirBtn.setSelected(true);
        dirGroup.add(leftDirBtn);
        dirGroup.add(rightDirBtn);
        dirRow.add(leftDirBtn);
        dirRow.add(rightDirBtn);

        inputFields.add(formLabel("Length of Queue  (max 40)"));
        inputFields.add(Box.createVerticalStrut(8));
        inputFields.add(queueLengthField);
        inputFields.add(Box.createVerticalStrut(20));
        inputFields.add(formLabel("Cylinders in Queue  (0 \u2013 199, space or comma separated)"));
        inputFields.add(Box.createVerticalStrut(8));
        inputFields.add(cylindersField);
        inputFields.add(Box.createVerticalStrut(20));
        inputFields.add(formLabel("R/W Head Starting Position  (0 \u2013 199)"));
        inputFields.add(Box.createVerticalStrut(8));
        inputFields.add(headField);
        inputFields.add(Box.createVerticalStrut(20));
        inputFields.add(formLabel("Initial Direction  (relevant for SCAN / C-SCAN / LOOK / C-LOOK)"));
        inputFields.add(Box.createVerticalStrut(8));
        inputFields.add(dirRow);
        inputFields.add(Box.createVerticalGlue());

        JScrollPane inputScroll = new JScrollPane(inputFields);
        inputScroll.setBorder(null);
        inputScroll.getVerticalScrollBar().setUnitIncrement(12);

        leftCard.add(inputScroll, BorderLayout.CENTER);

        // ── Right sidebar ─────────────────────────────────────────────────
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Mainframe.BG_DARK);
        rightPanel.setPreferredSize(new Dimension(200, 0));

        JPanel logoBlock = new JPanel();
        logoBlock.setBackground(new Color(128, 1, 31));
        logoBlock.setMinimumSize(new Dimension(200, 68));
        logoBlock.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        logoBlock.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(logoBlock);
        rightPanel.add(Box.createVerticalStrut(12));

        rightPanel.add(makeActionRow(randomIcon, "Random",      e -> doRandom()));
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(makeActionRow(importIcon, "Import File", e -> doImport()));
        rightPanel.add(Box.createVerticalStrut(14));

        JPanel algoBox = new JPanel();
        algoBox.setLayout(new BoxLayout(algoBox, BoxLayout.Y_AXIS));
        algoBox.setBackground(new Color(245, 248, 252));
        algoBox.setBorder(new EmptyBorder(10, 10, 12, 10));
        algoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        algoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel algoLbl = new JLabel("<html>Select a Disk Scheduling Algorithm:</html>");
        algoLbl.setForeground(new Color(81, 97, 113));
        algoLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        algoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        algoCombo = new JComboBox<>(ALGORITHMS);
        algoCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        algoCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        algoCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        algoBox.add(algoLbl);
        algoBox.add(Box.createVerticalStrut(7));
        algoBox.add(algoCombo);

        rightPanel.add(algoBox);
        rightPanel.add(Box.createVerticalGlue());

        JButton simulateBtn = new JButton("Simulate");
        simulateBtn.setBackground(new Color(247, 231, 206));
        simulateBtn.setForeground(Color.BLACK);
        simulateBtn.setFont(new Font("Arial", Font.BOLD, 15));
        simulateBtn.setFocusPainted(false);
        simulateBtn.setOpaque(true);
        simulateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        simulateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        simulateBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(8, 0, 8, 0)));
        simulateBtn.addActionListener(e -> runSimulation());
        rightPanel.add(simulateBtn);

        body.add(leftCard,   BorderLayout.CENTER);
        body.add(rightPanel, BorderLayout.EAST);
        add(body, BorderLayout.CENTER);
    }

    // ── Action handlers ───────────────────────────────────────────────────

    private void doRandom() {
        Random rng = new Random();
        int len  = 5 + rng.nextInt(16);
        int head = rng.nextInt(200);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(rng.nextInt(200));
            if (i < len - 1) sb.append(" ");
        }

        queueLengthField.setText(String.valueOf(len));
        cylindersField.setText(sb.toString());
        headField.setText(String.valueOf(head));
        if (rng.nextBoolean()) rightDirBtn.setSelected(true);
        else                   leftDirBtn.setSelected(true);
        algoCombo.setSelectedIndex(rng.nextInt(ALGORITHMS.length));
    }

    private void doImport() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Import Disk Queue (.txt)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files", "txt"));
        if (fc.showOpenDialog(SwingUtilities.getWindowAncestor(this)) != JFileChooser.APPROVE_OPTION)
            return;

        File file = fc.getSelectedFile();
        try {
            java.util.List<String> lines = new ArrayList<>();
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String ln = sc.nextLine().trim();
                    if (!ln.isEmpty()) lines.add(ln);
                }
            }

            if (lines.isEmpty()) { error("File is empty."); return; }

            String cylindersText = "";
            String headText      = "";
            String direction     = "right";

            boolean labelled = lines.stream().anyMatch(l -> l.toLowerCase().startsWith("queue:"));

            if (labelled) {
                for (String line : lines) {
                    String low = line.toLowerCase();
                    if (low.startsWith("queue:")) {
                        cylindersText = line.substring(line.indexOf(':') + 1).trim()
                                .replace(",", " ").replaceAll("\\s+", " ");
                    } else if (low.startsWith("head starts at:")) {
                        headText = line.substring(line.indexOf(':') + 1).trim();
                    } else if (low.startsWith("direction:")) {
                        direction = line.substring(line.indexOf(':') + 1).trim().toLowerCase();
                    }
                }
            } else {
                if (lines.size() < 3) {
                    error("File must have at least 3 lines: queue length, cylinders, head position.");
                    return;
                }
                cylindersText = lines.get(1).trim().replace(",", " ").replaceAll("\\s+", " ");
                headText      = lines.get(2).trim();
                if (lines.size() >= 4) direction = lines.get(3).trim().toLowerCase();
            }

            if (cylindersText.isEmpty()) { error("No cylinder data found in file."); return; }
            if (headText.isEmpty())      { error("No head position found in file."); return; }

            String[] tokens = cylindersText.split("\\s+");
            if (tokens.length > 40) { error("Queue length cannot exceed 40 requests."); return; }

            for (String t : tokens) {
                try {
                    int v = Integer.parseInt(t);
                    if (v < 0 || v > 199) { error("Cylinder value " + v + " is out of range (0-199)."); return; }
                } catch (NumberFormatException ex) {
                    error("Non-integer cylinder value in file: " + t); return;
                }
            }

            int headVal;
            try {
                headVal = Integer.parseInt(headText);
            } catch (NumberFormatException ex) {
                error("Head position in file is not an integer."); return;
            }
            if (headVal < 0 || headVal > 199) {
                error("Head position " + headVal + " is out of range (0-199)."); return;
            }

            queueLengthField.setText(String.valueOf(tokens.length));
            cylindersField.setText(cylindersText);
            headField.setText(headText);

            if (direction.contains("left")) leftDirBtn.setSelected(true);
            else                            rightDirBtn.setSelected(true);

        } catch (Exception ex) {
            error("Failed to read file: " + ex.getMessage());
        }
    }

    private void runSimulation() {
        int headPos;
        try {
            headPos = Integer.parseInt(headField.getText().trim());
        } catch (NumberFormatException ex) {
            error("Head starting position must be an integer (0-199)."); return;
        }
        if (headPos < 0 || headPos > 199) {
            error("Head starting position must be between 0 and 199."); return;
        }

        String raw = cylindersField.getText().trim();
        if (raw.isEmpty()) { error("Please enter at least one cylinder request."); return; }

        String[] parts = raw.replace(",", " ").trim().split("\\s+");
        if (parts.length > 40) { error("Queue length cannot exceed 40 requests."); return; }
        if (parts.length == 0) { error("Queue cannot be empty."); return; }

        int[] cylinders = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                cylinders[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException ex) {
                error("All queue entries must be integers."); return;
            }
            if (cylinders[i] < 0 || cylinders[i] > 199) {
                error("Cylinder " + cylinders[i] + " is out of range (0-199)."); return;
            }
        }

        queueLengthField.setText(String.valueOf(cylinders.length));

        String direction    = rightDirBtn.isSelected() ? "right" : "left";
        String selectedAlgo = (String) algoCombo.getSelectedItem();
        if (selectedAlgo == null) { error("Please select an algorithm."); return; }

        mainframe.getResultPanel().startSimulation(selectedAlgo, headPos, cylinders, direction);
        mainframe.showCard("RESULT");
    }

    // ── Helper methods ────────────────────────────────────────────────────

    private JLabel formLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(81, 97, 113));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField(int maxWidth, int height) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(maxWidth, height));
        f.setMaximumSize(new Dimension(maxWidth, height));
        f.setFont(new Font("Arial", Font.PLAIN, 14));
        f.setForeground(new Color(50, 50, 60));
        f.setBackground(new Color(245, 248, 252));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 185, 185), 1, true),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        return f;
    }

    private JRadioButton styledRadio(String text, Color bg) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Arial", Font.PLAIN, 13));
        rb.setBackground(bg);
        rb.setForeground(new Color(50, 50, 60));
        rb.setFocusPainted(false);
        return rb;
    }

    private JPanel makeActionRow(ImageIcon icon, String label, ActionListener action) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setBackground(Mainframe.BG_DARK);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton iconBtn = new JButton();
        iconBtn.setPreferredSize(new Dimension(44, 44));
        iconBtn.setBackground(Color.WHITE);
        iconBtn.setFocusPainted(false);
        iconBtn.setOpaque(true);
        iconBtn.setBorder(BorderFactory.createLineBorder(new Color(81, 97, 113), 1));
        if (icon != null) iconBtn.setIcon(icon);
        else { iconBtn.setText(label.substring(0, 1)); iconBtn.setFont(new Font("Arial", Font.BOLD, 16)); }
        iconBtn.addActionListener(action);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));

        row.add(iconBtn);
        row.add(lbl);
        return row;
    }

    private ImageIcon loadIcon(String path, int targetSize) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            BufferedImage img;
            if (url != null) img = ImageIO.read(url);
            else { File f = new File(path); if (!f.exists()) return null; img = ImageIO.read(f); }
            if (img == null) return null;
            return new ImageIcon(img.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
        } catch (Exception ex) { return null; }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }
}