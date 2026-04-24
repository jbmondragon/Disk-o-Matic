import javax.swing.SwingUtilities;
import gui.Mainframe;

public class DiscOMaticSimulator {

    private final Mainframe mainframe;

    public DiscOMaticSimulator() {
        mainframe = new Mainframe();
    }

    public void showMenu() {
        mainframe.showCard("MENU");
    }

    public void showSimulation() {
        mainframe.showCard("SCHEDULE");
    }

    public void showHelp() {
        mainframe.showCard("HELP");
    }

    public void startSimulation(String algorithm, int headPosition,
                                int[] queue, String direction) {
        mainframe.getResultPanel().startSimulation(algorithm, headPosition, queue, direction);
        mainframe.showCard("RESULT");
    }

    public void setVisible(boolean visible) {
        mainframe.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DiscOMaticSimulator app = new DiscOMaticSimulator();
            app.setVisible(true);
        });
    }
}