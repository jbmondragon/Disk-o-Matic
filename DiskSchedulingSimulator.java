import javax.swing.*;
import java.awt.*;
import algorithms.*;
import ui.*;
import utils.*;

public class DiskSchedulingSimulator extends JFrame {
    private InputPanel inputPanel;
    private OutputPanel outputPanel;
    private VisualizationPanel visualizationPanel;
    private SimulationTimer simulationTimer;
    
    public DiskSchedulingSimulator() {
        setTitle("Disk Scheduling Algorithms Simulator - CMSC 125");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        inputPanel = new InputPanel();
        outputPanel = new OutputPanel();
        visualizationPanel = new VisualizationPanel();
        simulationTimer = new SimulationTimer(visualizationPanel, outputPanel);
        
        // Set callback for simulation start
        inputPanel.setSimulationCallback((algorithm, head, queue, direction) -> {
            DiskSchedulingAlgorithm scheduler = createAlgorithm(algorithm);
            SimulationResult result = scheduler.execute(head, queue, direction);
            simulationTimer.startSimulation(result);
            outputPanel.displayResult(result);
        });
    }
    
    private DiskSchedulingAlgorithm createAlgorithm(String name) {
        switch (name) {
            case "FCFS": return new FCFS();
            case "SSTF": return new SSTF();
            case "SCAN": return new SCAN();
            case "C-SCAN": return new CSCAN();
            case "LOOK": return new LOOK();
            case "C-LOOK": return new CLOOK();
            default: return new FCFS();
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        topSplitPane.setResizeWeight(0.4);
        
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplitPane, visualizationPanel);
        mainSplitPane.setResizeWeight(0.6);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
}