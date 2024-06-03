package pl.wit.kowalcj1.java_place;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * ClientMain
 */
public class ClientMain {
    public static void main(String[] args) {
        Configurator.setRootLevel(Level.INFO);

        App app = new App();
        app.start();
    }
}