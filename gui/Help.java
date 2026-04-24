
package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Help extends JPanel {

    private final Mainframe mainframe;

    // Color scheme matching the application
    private static final Color HELP_BG = new Color(250, 248, 242);
    private static final Color SECTION_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(128, 1, 31);
    private static final Color TIP_BG = new Color(255, 255, 255);
    private static final Color SELECTED_PINK = new Color(249, 206, 231);

    public Help(Mainframe frame) {
        this.mainframe = frame;

        UIManager.put("TabbedPane.selected", SELECTED_PINK);
        UIManager.put("TabbedPane.contentAreaColor", SECTION_BG);
        UIManager.put("TabbedPane.background", HELP_BG);

        setLayout(new BorderLayout());
        setBackground(Mainframe.BG_DARK);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Mainframe.BG_DARK);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("User Guide");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Mainframe.TEXT_LIGHT);

        JButton backButton = new JButton("← Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(128, 1, 31));
        backButton.setForeground(Mainframe.TEXT_LIGHT);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.addActionListener(e -> mainframe.showCard("MENU"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(HELP_BG);
        mainContent.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create tabbed pane for organized help sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(HELP_BG);

        // Add all help sections as tabs
        tabbedPane.addTab("How it Works", createHowToPlayPanel());
        tabbedPane.addTab("Disk Scheduling Algorithms", createAlgorithmsPanel());
        tabbedPane.addTab("Input Guide", createInputGuidePanel());
        tabbedPane.addTab("Tips & Tricks", createTipsPanel());
        tabbedPane.addTab("FAQ", createFAQPanel());

        mainContent.add(tabbedPane, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createHowToPlayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HELP_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create scrollable content
        JTextArea content = new JTextArea();
        content.setEditable(false);
        content.setFont(new Font("Arial", Font.PLAIN, 14));
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setBackground(HELP_BG);

        content.setText(
                """
                        WELCOME TO Disk-o-Matic (Disk Scheduling Algorithm)

                        This simulator visualizes how Operating Systems manage disk drive requests.

                        STEP-BY-STEP GUIDE:

                        1. START THE SIMULATION
                           • From the main menu, click the "START" button
                           • You'll be taken to the disk configuration screen

                        2. CONFIGURE YOUR DISK
                           • You'll see a text field for the length of queue, cylinders in queue, and head of queue.
                           • Length of Queue: Maximum 40.
                           • Cylinders in Queue: Must be on 0 to 199.
                           • Head of Queue: Must be on 0 to 199.
            
                        3. USE THE ACTION BUTTONS (right side)
                           • Random: Generate random length of queue and its cylinders, head of queue, and disk scheduling algorithm to simulate.
                           • Import: Load inputs from a file (.txt, .csv)

                        4. SELECT AN ALGORITHM
                           • Choose from 6 different disk scheduling algorithms

                        5. RUN SIMULATION
                           • Click the "Simulate" button at the bottom
                           • Watch the simulation run in real-time!

                        6. SIMULATION CONTROLS
                           • Speed Adjustment: Use the timer slider to speed up or slow down the simulation.
                           • Visual Cues: The current scheduling process per queue will be highlighted as it goes along. 
                           • Direction: Toggle between left and right firection as you see the difference it can make to the algorithms.

                        7. Saving RESULTS
                           • You can save all algorithm outputs as a PDF or Image file.
                           • Filename format: (mmddyy_hhmmss_PG).
                           
                        That's it! Experiment with different algorithms to see how they affect performance!
                        """);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAlgorithmsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(HELP_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[][] algorithms = {
                { "FCFS (First-Come, First-Served)",
                "Processes requests in the exact order they arrive in the queue. It often results in wild, inefficient swings across the disk." },

                { "SSTF (Shortest Seek Time First)",
                "Always chooses the pending request closest to the current head position. Very efficient for total movement, but can cause 'starvation' for requests further away." },

                { "SCAN (Elevator Algorithm)",
                "The head moves continuously in one direction, servicing requests until it hits the absolute edge of the disk (0 or 199), then reverses direction." },

                { "C-SCAN (Circular SCAN)",
                "Moves in one direction to service requests until it hits the edge, jumps all the way back to the opposite edge WITHOUT servicing requests, and then repeats. Provides more uniform wait times." },

                { "LOOK",
                "Enhanced version of SCAN. The head moves in one direction but stops at the last request in that direction, reversing immediately as it does not go all the way to the disk edge" },

                { "C-LOOK",
                "Enhanced version of C-SCAN. It moves in one direction, then jumps back to the absolute first or last request in the queue rather than traveling all the way to the absolute edges of the disk." }
        };

        for (String[] algo : algorithms) {
            panel.add(createAlgorithmCard(algo[0], algo[1]));
            panel.add(Box.createVerticalStrut(15));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(HELP_BG);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createAlgorithmCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SECTION_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(ACCENT_COLOR);

        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setBackground(SECTION_BG);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);

        return card;
    }

    private JPanel createInputGuidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HELP_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(HELP_BG);

        // Input Fields Guide
        contentPanel.add(createSectionTitle("Input Fields Guide"));
        contentPanel.add(Box.createVerticalStrut(15));

        String[][] fields = {
                { "Length of Queue", "Maximum is 40 numbers." },
                { "Cylinders in Queue", "Number/s must be on 0 to 199." },
                { "Head of Queue", "Number must be on 0 to 199." }
        };

        for (String[] field : fields) {
            contentPanel.add(createFieldGuideRow(field[0], field[1]));
            contentPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.add(Box.createVerticalStrut(20));

        // File Import Guide
        contentPanel.add(createSectionTitle("File Import Format"));
        contentPanel.add(Box.createVerticalStrut(15));

        JPanel fileFormatPanel = new JPanel(new BorderLayout());
        fileFormatPanel.setBackground(SECTION_BG);
        fileFormatPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        fileFormatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea fileFormat = new JTextArea();
        fileFormat.setEditable(false);
        fileFormat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        fileFormat.setBackground(SECTION_BG);
        fileFormat.setText("""
                TXT/CSV Format (space or comma separated):
                ----------------------------------------
                The file should contain a single number for length of queue, then a line of integers for its cylinders.
                It can be separated by using spaces or commas.
                The third line should contain the number of Queue Head.

                Ex.
                Length of Queue
                Cylinders of Queue
                Queue Head

                Ex of imported file:
                6
                58 40 178 31 199 32
                67

                Or
                6
                58, 40, 178, 31, 199, 32
                67

                """);
        
        fileFormatPanel.add(fileFormat, BorderLayout.CENTER);
        contentPanel.add(fileFormatPanel);

        contentPanel.add(Box.createVerticalStrut(20));

        // Example
        contentPanel.add(createSectionTitle("Example Input"));
        contentPanel.add(Box.createVerticalStrut(15));

        JPanel examplePanel = new JPanel(new BorderLayout());
        examplePanel.setBackground(SECTION_BG);
        examplePanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextArea example = new JTextArea();
        example.setEditable(false);
        example.setFont(new Font("Monospaced", Font.PLAIN, 12));
        example.setBackground(SECTION_BG);
        example.setText("""
                            Length of Queue      
                                   6
                           Cylinders of Queue
                           58 40 178 31 199 32
                             Head of Queue
                                   67
                """);

        examplePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        examplePanel.add(example, BorderLayout.CENTER);
        contentPanel.add(examplePanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HELP_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(HELP_BG);

        // Beginner Tips
        contentPanel.add(createSectionTitle("Beginner Tips"));
        contentPanel.add(Box.createVerticalStrut(15));

        String[] beginnerTips = {
                "Total Head Movement is your score. A lower total seek time means that hte algorithm is more efficient",
                "Start with a small queue of 5 to 7 requests to easily trace the lines on the graph.",
                "Keep an eye on the direction changing this can alters how SSTF, C-/SCAN, and C-/LOOK behaves"
        };

        for (String tip : beginnerTips) {
            contentPanel.add(createTipBox(tip, "Tip:"));
            contentPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.add(Box.createVerticalStrut(20));

        // Advanced Tips
        contentPanel.add(createSectionTitle("Advanced Tips"));
        contentPanel.add(Box.createVerticalStrut(15));

        String[] advancedTips = {
                "SSTF is widely considered as the fastest disk scheduling algorithm.",
                "Notice that SCAN and C-SCAN touches the edges even if there are no requests there.",
                "C-SCAN is best for high-load, heavy, or complicated disk I/O workloads."
        };

        for (String tip : advancedTips) {
            contentPanel.add(createTipBox(tip, "Note:"));
            contentPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.add(Box.createVerticalStrut(20));

        // Common Patterns
        contentPanel.add(createSectionTitle("Common Patterns to Try"));
        contentPanel.add(Box.createVerticalStrut(15));

        String[][] patterns = {
                { "The Elevator Style", "Using SCAN and LOOK on any queue shows how it operates like an elevator." },
                { "The Starvation Trap", "Input '20 21 22 23 26 27 28 199' to see how SSTF acts and takes 199 only at the end." },
                { "The Edge Difference", "Notice how SCAN and C-SCAN needs to touch the edge while LOOK and C-LOOK only goes for the max or min limit of the queue." },
        };

        for (String[] pattern : patterns) {
            JPanel patternCard = new JPanel(new BorderLayout());
            patternCard.setBackground(SECTION_BG);
            patternCard.setBorder(new EmptyBorder(10, 15, 10, 15));

            patternCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel title = new JLabel(pattern[0]);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setForeground(ACCENT_COLOR);

            JLabel desc = new JLabel(pattern[1]);
            desc.setFont(new Font("Arial", Font.PLAIN, 12));

            patternCard.add(title, BorderLayout.NORTH);
            patternCard.add(desc, BorderLayout.CENTER);

            contentPanel.add(patternCard);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFAQPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HELP_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(HELP_BG);

        String[][] faqs = {
                { "What does 'Total Head Movement' mean?", "It is the total number of tracks the read/write head crossed to complete all requests." },
                { "Why does the graph jump straight to the edge in C-SCAN or min/max queue in C-LOOK?", "Those algorithms only service requests in one direction." },
                { "How do I save my work?", "Use the export button to export results as a PDF or Image following the (mmddyy_hhmmss_PG) format." },
                { "Why is the process highlighted at times?", "The highlight helps identify the process and queues met in real-time." }
        };

        for (String[] faq : faqs) {
            contentPanel.add(createFAQItem(faq[0], faq[1]));
            contentPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Helper methods for creating styled components
    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createFieldGuideRow(String field, String description) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(HELP_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel fieldLabel = new JLabel(field + ":");
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldLabel.setPreferredSize(new Dimension(120, 25));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        row.add(fieldLabel, BorderLayout.WEST);
        row.add(descLabel, BorderLayout.CENTER);

        return row;
    }

    private JPanel createTipBox(String tip, String prefix) {
        JPanel tipPanel = new JPanel(new BorderLayout(10, 0));
        tipPanel.setBackground(TIP_BG);
        tipPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 180, 0)),
                new EmptyBorder(8, 12, 8, 12)));
        tipPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel prefixLabel = new JLabel(prefix);
        prefixLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel tipLabel = new JLabel(tip);
        tipLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        tipPanel.add(prefixLabel, BorderLayout.WEST);
        tipPanel.add(tipLabel, BorderLayout.CENTER);

        return tipPanel;
    }

    private JPanel createFAQItem(String question, String answer) {
        JPanel faqPanel = new JPanel(new BorderLayout(10, 5));
        faqPanel.setBackground(SECTION_BG);
        faqPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        faqPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel questionLabel = new JLabel("Q: " + question);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 13));
        questionLabel.setForeground(ACCENT_COLOR);

        JLabel answerLabel = new JLabel("A: " + answer);
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        faqPanel.add(questionLabel, BorderLayout.NORTH);
        faqPanel.add(answerLabel, BorderLayout.CENTER);

        return faqPanel;
    }
}
