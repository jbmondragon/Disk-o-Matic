package gui;

import java.awt.*;
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

        JLabel algoTitle = new JLabel("Page Replacement Algorithms:");
        algoTitle.setFont(new Font("Arial", Font.BOLD, 14));
        algoTitle.setBorder(new EmptyBorder(0, 0, 10, 0)); // Space below title
        algoPanel.add(algoTitle, BorderLayout.NORTH);

        JList<String> algoList = new JList<>(algorithms);
        algoList.setFont(new Font("Arial", Font.BOLD, 13));
        algoList.setBackground(Mainframe.BG_LIGHT_GRAY);
        algoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Explicitly tell the list to size itself for exactly 6 items
        algoList.setVisibleRowCount(6);
        algoList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
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
        // Set rows to roughly match the height of the list panel
        descriptionArea.setRows(6);

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(null);
        descScroll.setBackground(Mainframe.BG_LIGHT_GRAY);
        descPanel.add(descScroll, BorderLayout.CENTER);

        rightTop.add(algoPanel);
        rightTop.add(descPanel);

        // WEIGHTY = 0.0 -> Tells layout to make this panel EXACTLY as tall as its
        // content needs, no taller.
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weighty = 0.0;
        gbcRight.insets = new Insets(0, 0, 10, 0);
        rightContainer.add(rightTop, gbcRight);

        // --- Right Bottom Panel (Image Placeholder) ---
        JPanel rightBottom = new JPanel(new BorderLayout());
        rightBottom.setBackground(Mainframe.BG_LIGHT_GRAY);
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        // Show cover image by default, but scale it dynamically like the others
        ImageIcon coverIcon = new ImageIcon(getClass().getResource("/img/cover.png"));
        // Use a default size for initial display
        int defaultWidth = 400;
        int defaultHeight = 300;
        Image scaledCover = coverIcon.getImage().getScaledInstance(defaultWidth, defaultHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledCover));

        // Ensure the cover image always matches the size of the algorithm images
        rightBottom.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = imageLabel.getWidth();
                int height = imageLabel.getHeight();
                if (width > 0 && height > 0 && algoList.getSelectedIndex() == -1) {
                    Image scaled = coverIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaled));
                }
            }
        });
        rightBottom.add(imageLabel, BorderLayout.CENTER);

        // WEIGHTY = 1.0 -> Tells layout to give all remaining vertical space to this
        // image panel.
        gbcRight.gridx = 0;
        gbcRight.gridy = 1;
        gbcRight.weighty = 1.0;
        gbcRight.insets = new Insets(5, 0, 0, 0);
        rightContainer.add(rightBottom, gbcRight);

        // === Add Interaction Logic ===
        algoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = algoList.getSelectedIndex();
                if (selectedIndex != -1) {
                    descriptionArea.setText("What does it do?:\n\n" + descriptions[selectedIndex]);
                    var url = getClass().getResource(imageFiles[selectedIndex]);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        int width = imageLabel.getWidth();
                        int height = imageLabel.getHeight();
                        if (width > 0 && height > 0) {
                            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                        } else {
                            Image scaledImage = icon.getImage().getScaledInstance(defaultWidth, defaultHeight,
                                    Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                        }
                    }
                } else {
                    // No selection: revert to cover image and scale to current label size
                    int width = imageLabel.getWidth();
                    int height = imageLabel.getHeight();
                    if (width > 0 && height > 0) {
                        Image scaledCover2 = coverIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledCover2));
                    } else {
                        Image scaledCover2 = coverIcon.getImage().getScaledInstance(defaultWidth, defaultHeight,
                                Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledCover2));
                    }
                    descriptionArea
                            .setText("What does it do?:\n\nSelect an algorithm from the list to see its description.");
                }
            }
        });

        // Add Right Container to Main Layout
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.weightx = 0.62;
        gbcMain.insets = new Insets(0, 5, 0, 0);
        add(rightContainer, gbcMain);
    }

}