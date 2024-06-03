package pl.wit.kowalcj1.java_place;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger log = LogManager.getLogger(App.class);

    private Frontend frontend;

    public void start() {
        SwingUtilities.invokeLater(() -> {
            frontend = new Frontend();
            frontend.setVisible(true);
        });

        SwingUtilities.invokeLater(() -> frontend.initConnection());
    }
}
