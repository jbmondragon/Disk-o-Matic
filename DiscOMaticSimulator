import javax.swing.*;
import java.awt.*;

public class DiscOMaticSimulator extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MainMenuPanel mainMenuPanel;
    private SimulationPanel simulationPanel;
    private HelpPanel helpPanel;
    
    public DiscOMaticSimulator() {
        setTitle("Disc-o-matic - Disk Scheduling Algorithms Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1024, 768));
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(new Color(18, 18, 24));
        
        mainMenuPanel = new MainMenuPanel(this);
        simulationPanel = new SimulationPanel(this);
        helpPanel = new HelpPanel(this);
        
        mainContainer.add(mainMenuPanel, "MENU");
        mainContainer.add(simulationPanel, "SIMULATION");
        mainContainer.add(helpPanel, "HELP");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);
    }
    
    public void showMenu() {
        cardLayout.show(mainContainer, "MENU");
    }
    
    public void showSimulation() {
        cardLayout.show(mainContainer, "SIMULATION");
    }
    
    public void showHelp() {
        cardLayout.show(mainContainer, "HELP");
    }
    
    public void startSimulation(String algorithm, int headPosition, int[] queue, String direction) {
        simulationPanel.startSimulation(algorithm, headPosition, queue, direction);
        showSimulation();
    }
}
