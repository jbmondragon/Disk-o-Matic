import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DiscOMaticSimulator simulator = new DiscOMaticSimulator();
            simulator.setVisible(true);
        });
    }
}
