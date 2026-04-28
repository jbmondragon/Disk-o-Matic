package gui;

import algorithms.CLOOK;
import algorithms.CSCAN;
import algorithms.DiskSchedulingAlgorithm;
import algorithms.FCFS;
import algorithms.LOOK;
import algorithms.SCAN;
import algorithms.SSTF;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Page extends JPanel {

    private final Mainframe mainframe;
    private final JComboBox<String> algoCombo;
    private final JComboBox<String> directionCombo;
    private final JTextField lengthField;
    private final JTextField textField;
    private final JTextField frameField;
    private static final int ICON_SIZE = 26;
    private static final int CYLINDER_MIN = 0;
    private static final int CYLINDER_MAX = 199;
    private static final int MAX_QUEUE_LENGTH = 40;

    private static final String[] ALGORITHMS = {
            "FCFS",
            "SSTF",
            "SCAN",
            "C-SCAN",
            "LOOK",
            "C-LOOK",
            "All Algorithms"
    };
    private final Random random = new Random();

    public Page(Mainframe frame) {
        this.mainframe = frame;
        setLayout(new BorderLayout());
        setBackground(Mainframe.BG_DARK);

        ImageIcon randomIcon = loadIcon("img/random.png", ICON_SIZE);
        ImageIcon importIcon = loadIcon("img/import.png", ICON_SIZE);

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Mainframe.BG_DARK);
        topHeader.setBorder(new EmptyBorder(8, 14, 6, 14));

        JLabel returnLbl = new JLabel("Return");
        returnLbl.setForeground(Mainframe.TEXT_LIGHT);
        returnLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        returnLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainframe.showCard("MENU");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                returnLbl.setText("<html><u>Return</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnLbl.setText("Return");
            }
        });

        JLabel aisaLbl = new JLabel("Disk-o-Matic", SwingConstants.CENTER);
        aisaLbl.setForeground(Mainframe.TEXT_LIGHT);
        aisaLbl.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel spacer = new JLabel("Return");
        spacer.setFont(returnLbl.getFont());
        spacer.setForeground(Mainframe.BG_DARK);
        spacer.setVisible(false);
        spacer.setPreferredSize(returnLbl.getPreferredSize());

        topHeader.add(returnLbl, BorderLayout.WEST);
        topHeader.add(aisaLbl, BorderLayout.CENTER);
        topHeader.add(spacer, BorderLayout.EAST);
        add(topHeader, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(12, 0));
        body.setBackground(Mainframe.BG_DARK);
        body.setBorder(new EmptyBorder(4, 8, 8, 8));

        JPanel leftCard = new JPanel(new BorderLayout());
        leftCard.setBackground(Color.WHITE);
        leftCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 185, 185), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 14, 0)));

        JPanel procHeader = new JPanel(new BorderLayout());
        procHeader.setBackground(new Color(128, 1, 31));
        procHeader.setBorder(new EmptyBorder(11, 16, 11, 16));

        JPanel procTitleBox = new JPanel();
        procTitleBox.setLayout(new BoxLayout(procTitleBox, BoxLayout.Y_AXIS));
        procTitleBox.setBackground(new Color(128, 1, 31));

        JLabel procTitle = new JLabel("Disk Scheduling Algorithm");
        procTitle.setFont(new Font("Arial", Font.BOLD, 16));
        procTitle.setForeground(Color.WHITE);

        JLabel procSub = new JLabel("Add a queue for simulation");
        procSub.setFont(new Font("Arial", Font.PLAIN, 11));
        procSub.setForeground(Color.white);

        procTitleBox.add(procTitle);
        procTitleBox.add(procSub);
        procHeader.add(procTitleBox, BorderLayout.WEST);
        leftCard.add(procHeader, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(30, 10, 10, 10));

        JLabel textLabel = new JLabel("Cylinders in Queue");
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setForeground(new Color(81, 97, 113));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(650, 50));
        textField.setMaximumSize(new Dimension(650, 50));
        textField.setFont(new Font("Arial", Font.BOLD, 16));
        textField.setForeground(new Color(81, 97, 113));
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setBackground(new Color(245, 248, 252));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 185, 185), 1, true),
                BorderFactory.createEmptyBorder(0, 20, 0, 20)));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                syncLengthField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                syncLengthField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                syncLengthField();
            }
        });

        JLabel lengthLabel = new JLabel("Length of Queue");
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lengthLabel.setForeground(new Color(81, 97, 113));
        lengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lengthField = new JTextField();
        lengthField.setPreferredSize(new Dimension(120, 50));
        lengthField.setMaximumSize(new Dimension(120, 50));
        lengthField.setFont(new Font("Arial", Font.BOLD, 16));
        lengthField.setForeground(new Color(81, 97, 113));
        lengthField.setHorizontalAlignment(JTextField.CENTER);
        lengthField.setAlignmentX(Component.CENTER_ALIGNMENT);
        lengthField.setBackground(new Color(245, 248, 252));
        lengthField.setBorder(textField.getBorder());
        lengthField.setEditable(false);
        lengthField.setFocusable(false);
        lengthField.setText("0");

        JLabel frameLabel = new JLabel("Head of Queue");
        frameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frameLabel.setForeground(new Color(81, 97, 113));
        frameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        frameField = new JTextField();
        frameField.setPreferredSize(new Dimension(120, 50));
        frameField.setMaximumSize(new Dimension(120, 50));
        frameField.setFont(new Font("Arial", Font.BOLD, 16));
        frameField.setForeground(new Color(81, 97, 113));
        frameField.setHorizontalAlignment(JTextField.CENTER);
        frameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        frameField.setBackground(new Color(245, 248, 252));
        frameField.setBorder(textField.getBorder());

        inputPanel.add(lengthLabel);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(lengthField);
        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(textLabel);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(textField);
        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(frameLabel);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(frameField);
        inputPanel.add(Box.createVerticalGlue());

        leftCard.add(inputPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Mainframe.BG_DARK);
        rightPanel.setPreferredSize(new Dimension(190, 0));

        JPanel logoBlock = new JPanel();
        logoBlock.setBackground(new Color(128, 1, 31));
        logoBlock.setMinimumSize(new Dimension(190, 68));
        logoBlock.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        logoBlock.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(logoBlock);
        rightPanel.add(Box.createVerticalStrut(12));

        rightPanel.add(makeActionRow(randomIcon, "Random", e -> randomFill()));
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(makeActionRow(importIcon, "Import", e -> importFile()));
        rightPanel.add(Box.createVerticalStrut(14));

        JPanel algoBox = new JPanel();
        algoBox.setLayout(new BoxLayout(algoBox, BoxLayout.Y_AXIS));
        algoBox.setBackground(new Color(245, 248, 252));
        algoBox.setBorder(new EmptyBorder(10, 10, 12, 10));
        algoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        algoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel algoLbl = new JLabel("<html>Select A Disk Scheduling Algorithm To Simulate:</html>");
        algoLbl.setForeground(new Color(81, 97, 113));
        algoLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        algoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        algoCombo = new JComboBox<>(ALGORITHMS);
        algoCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        algoCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        algoCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel directionLbl = new JLabel("Initial Head Direction:");
        directionLbl.setForeground(new Color(81, 97, 113));
        directionLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        directionLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        directionCombo = new JComboBox<>(new String[] { "Right", "Left" });
        directionCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        directionCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        directionCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        algoBox.add(algoLbl);
        algoBox.add(Box.createVerticalStrut(7));
        algoBox.add(algoCombo);
        algoBox.add(Box.createVerticalStrut(6));
        algoBox.add(directionLbl);
        algoBox.add(Box.createVerticalStrut(7));
        algoBox.add(directionCombo);
        algoBox.add(Box.createVerticalStrut(6));

        rightPanel.add(algoBox);
        rightPanel.add(Box.createVerticalGlue());

        JButton submitBtn = new JButton("Simulate");
        submitBtn.setBackground(new Color(247, 231, 206));
        submitBtn.setForeground(Color.black);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 15));
        submitBtn.setFocusPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(8, 0, 8, 0)));
        submitBtn.addActionListener(e -> runSimulation());
        rightPanel.add(submitBtn);

        body.add(leftCard, BorderLayout.CENTER);
        body.add(rightPanel, BorderLayout.EAST);
        add(body, BorderLayout.CENTER);
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

        if (icon != null) {
            iconBtn.setIcon(icon);
        } else {
            iconBtn.setText(label.substring(0, 1));
            iconBtn.setFont(new Font("Arial", Font.BOLD, 16));
        }

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
            if (url != null) {
                img = ImageIO.read(url);
            } else {
                File f = new File(path);
                if (!f.exists())
                    return null;
                img = ImageIO.read(f);
            }
            if (img == null)
                return null;
            Image scaled = img.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (java.io.IOException | RuntimeException ex) {
            System.err.println("Could not load icon: " + path + " - " + ex.getMessage());
            return null;
        }
    }

    private void randomFill() {
        int queueLength = random.nextInt(11) + 5;
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < queueLength; index++) {
            if (index > 0) {
                sb.append(' ');
            }
            sb.append(random.nextInt(CYLINDER_MAX + 1));
        }
        textField.setText(sb.toString());
        frameField.setText(String.valueOf(random.nextInt(CYLINDER_MAX + 1)));
        // Do not randomize direction or algorithm selection
    }

    private void importFile() {
        JFileChooser fc = new JFileChooser();
        File datasetDir = resolveDatasetDirectory();
        if (datasetDir != null) {
            fc.setCurrentDirectory(datasetDir);
        }
        fc.setDialogTitle("Import Disk Queue (.txt/.csv/.xlsx)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text/CSV/XLSX files", "txt", "csv", "xlsx"));
        if (fc.showOpenDialog(SwingUtilities.getWindowAncestor(this)) != JFileChooser.APPROVE_OPTION)
            return;
        File file = fc.getSelectedFile();
        String name = file.getName().toLowerCase();
        try {
            if (name.endsWith(".txt") || name.endsWith(".csv")) {
                java.util.List<String> lines = new java.util.ArrayList<>();
                try (java.util.Scanner sc = new java.util.Scanner(file)) {
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine().trim();
                        if (!line.isEmpty())
                            lines.add(line);
                    }
                }
                if (lines.isEmpty()) {
                    error("File is empty.");
                    return;
                }
                applyImportedRows(lines);
            } else if (name.endsWith(".xlsx")) {
                List<String[]> rows = readXlsx(file);
                if (rows.isEmpty()) {
                    error("Excel file is empty.");
                    return;
                }
                List<String> lines = new ArrayList<>();
                for (String[] row : rows) {
                    String rowText = String.join(",", row).trim();
                    if (!rowText.isEmpty()) {
                        lines.add(rowText);
                    }
                }
                applyImportedRows(lines);
            } else {
                error("Unsupported file type.");
            }
        } catch (Exception ex) {
            error("Failed to import: " + ex.getMessage());
        }
    }

    private File resolveDatasetDirectory() {
        File[] candidates = {
                new File(System.getProperty("user.dir"), "dataset"),
                new File("dataset")
        };
        for (File candidate : candidates) {
            if (candidate.exists() && candidate.isDirectory()) {
                return candidate;
            }
        }
        return null;
    }

    private void applyImportedRows(List<String> lines) {
        int queueLineIndex = 0;
        int headLineIndex = 1;
        int directionLineIndex = 2;

        if (lines.size() >= 2 && looksLikeSingleNumber(lines.get(0)) && !looksLikeSingleNumber(lines.get(1))) {
            queueLineIndex = 1;
            headLineIndex = 2;
            directionLineIndex = 3;
        }

        String queueLine = normalizeQueueInput(lines.get(queueLineIndex));
        if (queueLine.isBlank()) {
            throw new IllegalArgumentException("Queue row is empty.");
        }
        textField.setText(queueLine);

        if (lines.size() <= headLineIndex) {
            throw new IllegalArgumentException("Head row is missing.");
        }

        String headValue = lines.get(headLineIndex).trim().split("[,\\s]+")[0];
        frameField.setText(headValue);

        if (lines.size() > directionLineIndex) {
            String direction = lines.get(directionLineIndex).trim().toLowerCase();
            if (direction.startsWith("l")) {
                directionCombo.setSelectedItem("Left");
            } else if (direction.startsWith("r")) {
                directionCombo.setSelectedItem("Right");
            }
        }
    }

    private boolean looksLikeSingleNumber(String value) {
        return value.trim().matches("\\d+");
    }

    private void runSimulation() {
        final List<Integer> queue;
        try {
            queue = parseQueue(textField.getText());
        } catch (NumberFormatException ex) {
            error("Cylinders in Queue must contain only integers from 0 to 199.");
            return;
        }

        if (queue.isEmpty()) {
            error("Enter at least one cylinder request.");
            return;
        }

        if (queue.size() > MAX_QUEUE_LENGTH) {
            error("The queue can contain at most 40 cylinder requests.");
            return;
        }

        final int headPosition;
        try {
            headPosition = Integer.parseInt(frameField.getText().trim());
        } catch (NumberFormatException ex) {
            error("Head of Queue must be an integer from 0 to 199.");
            return;
        }

        if (headPosition < CYLINDER_MIN || headPosition > CYLINDER_MAX) {
            error("Head of Queue must be between 0 and 199.");
            return;
        }

        String selectedAlgo = (String) algoCombo.getSelectedItem();
        if (selectedAlgo == null) {
            error("Please select an algorithm.");
            return;
        }

        String direction = ((String) directionCombo.getSelectedItem()).toLowerCase();
        Map<String, Result.SimulationSummary> results = new LinkedHashMap<>();

        if ("All Algorithms".equals(selectedAlgo)) {
            for (String algorithmName : ALGORITHMS) {
                if (!"All Algorithms".equals(algorithmName)) {
                    results.put(algorithmName, executeAlgorithm(algorithmName, headPosition, queue, direction));
                }
            }
        } else {
            results.put(selectedAlgo, executeAlgorithm(selectedAlgo, headPosition, queue, direction));
        }

        mainframe.getResultPanel().startSimulation(results, headPosition, queue, direction);

        mainframe.showCard("RESULT");
    }

    private Result.SimulationSummary executeAlgorithm(String algorithmName, int headPosition, List<Integer> queue,
            String direction) {
        DiskSchedulingAlgorithm algorithm = createAlgorithm(algorithmName);
        int totalSeekTime = algorithm.execute(headPosition, queue, direction);
        return new Result.SimulationSummary(
                algorithmName,
                new ArrayList<>(algorithm.getServiceSequence()),
                totalSeekTime,
                algorithm.getAverageSeekTime());
    }

    private DiskSchedulingAlgorithm createAlgorithm(String algorithmName) {
        return switch (algorithmName) {
            case "FCFS" -> new FCFS();
            case "SSTF" -> new SSTF();
            case "SCAN" -> new SCAN();
            case "C-SCAN" -> new CSCAN();
            case "LOOK" -> new LOOK();
            case "C-LOOK" -> new CLOOK();
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algorithmName);
        };
    }

    private String normalizeQueueInput(String input) {
        return input.trim().replace(',', ' ');
    }

    private void syncLengthField() {
        String normalized = normalizeQueueInput(textField.getText());
        if (normalized.isBlank()) {
            lengthField.setText("0");
            return;
        }

        int count = 0;
        for (String token : normalized.split("\\s+")) {
            if (!token.isBlank()) {
                count++;
            }
        }
        lengthField.setText(String.valueOf(count));
    }

    private List<Integer> parseQueue(String input) {
        String normalized = normalizeQueueInput(input);
        List<Integer> queue = new ArrayList<>();
        if (normalized.isBlank()) {
            return queue;
        }

        for (String token : normalized.split("\\s+")) {
            int value = Integer.parseInt(token);
            if (value < CYLINDER_MIN || value > CYLINDER_MAX) {
                throw new NumberFormatException("Cylinder out of range");
            }
            queue.add(value);
        }
        return queue;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }

    private List<String[]> readXlsx(File file) throws Exception {
        List<String[]> rows = new ArrayList<>();
        try (ZipFile zip = new ZipFile(file)) {
            List<String> shared = new ArrayList<>();
            ZipEntry sst = zip.getEntry("xl/sharedStrings.xml");
            if (sst != null) {
                Document sstDoc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(zip.getInputStream(sst));
                NodeList siList = sstDoc.getElementsByTagName("si");
                for (int i = 0; i < siList.getLength(); i++) {
                    shared.add(siList.item(i).getTextContent());
                }
            }

            ZipEntry sheet = zip.getEntry("xl/worksheets/sheet1.xml");
            if (sheet != null) {
                Document sheetDoc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(zip.getInputStream(sheet));
                NodeList rowList = sheetDoc.getElementsByTagName("row");
                for (int i = 0; i < rowList.getLength(); i++) {
                    Element row = (Element) rowList.item(i);
                    NodeList cellList = row.getElementsByTagName("c");
                    List<String> rowVals = new ArrayList<>();
                    for (int j = 0; j < cellList.getLength(); j++) {
                        Element c = (Element) cellList.item(j);
                        String type = c.getAttribute("t");
                        String v = "";
                        NodeList vnodes = c.getElementsByTagName("v");
                        if (vnodes.getLength() > 0) {
                            v = vnodes.item(0).getTextContent();
                            if ("s".equals(type)) {
                                try {
                                    int idx = Integer.parseInt(v);
                                    if (idx < shared.size()) {
                                        v = shared.get(idx);
                                    }
                                } catch (NumberFormatException ignored) {
                                }
                            }
                        }
                        rowVals.add(v);
                    }
                    rows.add(rowVals.toArray(String[]::new));
                }
            }
        }
        return rows;
    }
}