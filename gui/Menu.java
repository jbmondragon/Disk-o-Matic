package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Menu extends JPanel {

    private Mainframe mainframe;

    // Arrays to hold the dynamic content for each algorithm
    private final String[] algorithms = {
            "1. FCFS (First-Come, First-Served)",
            "2. SSTF (Shortest Seek Time First)",
            "3. SCAN (Elevator Algorithm)",
            "4. C-SCAN (Circular SCAN)",
            "5. LOOK",
            "6. C-LOOK"
    };

    private final String[] descriptions = {
            "The simplest scheduling algorithm. It processes requests in the exact order of arrival in the queue. While it prevents starvation, it usually results in high average seek times.",
            "Selects the request that is closest to the current position and direction of the disk head. This greatly reduces the average seek time compared to FCFS, but it can cause 'starvation' for requests located further away.",
            "The disk arm starts at one end of the disk, moves toward the other end servicing requests as it goes, and then reverses direction once it hits the physical edge.",
            "Provides a more uniform wait time than SCAN. The head moves from one end of the disk to the other servicing requests, but when it reaches the edge, it immediately jumps back to the beginning without servicing requests then repeats.",
            "A smarter version of the SCAN algorithm. Instead of going all the way to the edge of the disk, the arm only goes as far as the final queue in that direction before reversing.",
            "A smarter version of the C-SCAN algorithm. The arm moves in one direction servicing requests until the last request in that direction, then immediately jumps back to the lowest-numbered pending request without traveling to the edge."
    };

    private final String[] imageFiles = {
            "/img/fcfs.png",
            "/img/sstf.png",
            "/img/scan.png",
            "/img/cscan.png",
            "/img/look.png",
            "/img/clook.png"
    };

    // ---------------------------------------------------------------
    // Null-safe image loader
    // Returns a scaled ImageIcon, or null if the resource is missing.
    // ---------------------------------------------------------------
    private ImageIcon loadImage(String resourcePath, int width, int height) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) return null;
            BufferedImage img = ImageIO.read(url);
            if (img == null) return null;
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception ex) {
            return null;
        }
    }

    // Convenience overload: scales to the current size of a label (falls back to defaults).
    private ImageIcon loadImage(String resourcePath, JLabel targetLabel,
                                int defaultWidth, int defaultHeight) {
        int w = targetLabel.getWidth()  > 0 ? targetLabel.getWidth()  : defaultWidth;
        int h = targetLabel.getHeight() > 0 ? targetLabel.getHeight() : defaultHeight;
        return loadImage(resourcePath, w, h);
    }

    public Menu(Mainframe frame) {
        this.mainframe = frame;

        // Use GridBagLayout for proportional sizing
        setLayout(new GridBagLayout());
        setBackground(Mainframe.BG_DARK);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1.0;

        // === Left Panel ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Mainframe.BG_LIGHT_GRAY);
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Disk-o-Matic");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("(Disk Scheduling Algorithm)");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Common button style
        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
        Dimension buttonSize = new Dimension(200, 60);
        Color buttonBg = Mainframe.BG_DARK_GRAY_HEADER;
        Color buttonFg = Mainframe.TEXT_LIGHT;

        // START button
        JButton startButton = new JButton("START");
        startButton.setFont(buttonFont);
        startButton.setBackground(buttonBg);
        startButton.setForeground(buttonFg);
        startButton.setFocusPainted(false);
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);
        startButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        startButton.addActionListener(e -> mainframe.showCard("SCHEDULE"));

        // HELP button
        JButton helpButton = new JButton("HELP");
        helpButton.setFont(buttonFont);
        helpButton.setBackground(buttonBg);
        helpButton.setForeground(buttonFg);
        helpButton.setFocusPainted(false);
        helpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        helpButton.setPreferredSize(buttonSize);
        helpButton.setMaximumSize(buttonSize);
        helpButton.setMinimumSize(buttonSize);
        helpButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        helpButton.addActionListener(e -> mainframe.showCard("HELP"));

        leftPanel.add(titleLabel);
        leftPanel.add(subtitleLabel);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(startButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(helpButton);
        leftPanel.add(Box.createVerticalStrut(150));

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 0.38;
        gbcMain.insets = new Insets(0, 0, 0, 10);
        add(leftPanel, gbcMain);

        // === Right Panel Container ===
        JPanel rightContainer = new JPanel(new GridBagLayout());
        rightContainer.setBackground(Mainframe.BG_DARK);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.fill = GridBagConstraints.BOTH;
        gbcRight.weightx = 1.0;

        // --- Right Top Container ---
        JPanel rightTop = new JPanel(new GridLayout(1, 2, 15, 0));
        rightTop.setBackground(Mainframe.BG_DARK);

        // Algorithms List Sub-panel
        JPanel algoPanel = new JPanel(new BorderLayout());
        algoPanel.setBackground(Mainframe.BG_LIGHT_GRAY);
        algoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel algoTitle = new JLabel("Disk Scheduling Algorithms:");
        algoTitle.setFont(new Font("Arial", Font.BOLD, 14));
        algoTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        algoPanel.add(algoTitle, BorderLayout.NORTH);

        JList<String> algoList = new JList<>(algorithms);
        algoList.setFont(new Font("Arial", Font.BOLD, 13));
        algoList.setBackground(Mainframe.BG_LIGHT_GRAY);
        algoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        algoList.setVisibleRowCount(6);
        algoList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, false);
                setBorder(new EmptyBorder(3, 5, 3, 5));
                return this;
            }
        });

        JScrollPane algoScroll = new JScrollPane(algoList);
        algoScroll.setBorder(null);
        algoScroll.setBackground(Mainframe.BG_LIGHT_GRAY);
        algoPanel.add(algoScroll, BorderLayout.CENTER);

        // Description Sub-panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(Mainframe.BG_LIGHT_GRAY);
        descPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextArea descriptionArea = new JTextArea(
                "What does it do?:\n\nSelect an algorithm from the list to see its description.");
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 15));
        descriptionArea.setBackground(Mainframe.BG_LIGHT_GRAY);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setRows(6);

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(null);
        descScroll.setBackground(Mainframe.BG_LIGHT_GRAY);
        descPanel.add(descScroll, BorderLayout.CENTER);

        rightTop.add(algoPanel);
        rightTop.add(descPanel);

        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weighty = 0.0;
        gbcRight.insets = new Insets(0, 0, 10, 0);
        rightContainer.add(rightTop, gbcRight);

        // --- Right Bottom Panel (Image area) ---
        final int defaultWidth  = 400;
        final int defaultHeight = 300;

        JPanel rightBottom = new JPanel(new BorderLayout());
        rightBottom.setBackground(Mainframe.BG_LIGHT_GRAY);

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        imageLabel.setForeground(new Color(160, 80, 80));

        // --- Load cover image (null-safe) ---
        // coverIcon may be null if /img/cover.png is not on the classpath.
        final ImageIcon[] coverIconHolder = { loadImage("/img/cover.png", defaultWidth, defaultHeight) };
        if (coverIconHolder[0] != null) {
            imageLabel.setIcon(coverIconHolder[0]);
        } else {
            // Friendly placeholder text shown instead of the cover image
            imageLabel.setText("<html><center>Select an algorithm<br>to see its diagram</center></html>");
        }

        // Resize cover image with the panel (null-safe)
        rightBottom.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (algoList.getSelectedIndex() != -1) return; // algo image is shown; don't overwrite
                if (coverIconHolder[0] == null) return;        // no cover image — nothing to scale
                ImageIcon scaled = loadImage("/img/cover.png", imageLabel, defaultWidth, defaultHeight);
                if (scaled != null) {
                    coverIconHolder[0] = scaled;               // keep holder current for deselect revert
                    imageLabel.setIcon(scaled);
                }
            }
        });

        rightBottom.add(imageLabel, BorderLayout.CENTER);

        gbcRight.gridx = 0;
        gbcRight.gridy = 1;
        gbcRight.weighty = 1.0;
        gbcRight.insets = new Insets(5, 0, 0, 0);
        rightContainer.add(rightBottom, gbcRight);

        // === Interaction: list selection → show algorithm image + description ===
        algoList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int idx = algoList.getSelectedIndex();
            if (idx != -1) {
                // Show description
                descriptionArea.setText("What does it do?:\n\n" + descriptions[idx]);

                // Load algorithm diagram (null-safe)
                ImageIcon icon = loadImage(imageFiles[idx], imageLabel, defaultWidth, defaultHeight);
                if (icon != null) {
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                } else {
                    // Image file missing — show a readable fallback instead of crashing
                    imageLabel.setIcon(null);
                    imageLabel.setText("<html><center><b>" + algorithms[idx] + "</b><br><br>"
                            + "(diagram image not found)</center></html>");
                }
            } else {
                // Nothing selected — revert to cover image or placeholder text
                descriptionArea.setText(
                        "What does it do?:\n\nSelect an algorithm from the list to see its description.");
                if (coverIconHolder[0] != null) {
                    ImageIcon scaled = loadImage("/img/cover.png", imageLabel, defaultWidth, defaultHeight);
                    imageLabel.setIcon(scaled != null ? scaled : coverIconHolder[0]);
                    imageLabel.setText("");
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("<html><center>Select an algorithm<br>to see its diagram</center></html>");
                }
            }
        });

        // === Add Right Container to Main Layout ===
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.weightx = 0.62;
        gbcMain.insets = new Insets(0, 5, 0, 0);
        add(rightContainer, gbcMain);
    }
}