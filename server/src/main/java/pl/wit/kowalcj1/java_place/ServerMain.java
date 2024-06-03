package pl.wit.kowalcj1.java_place;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class ServerMain {
    private static final Logger log = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {
        Configurator.setRootLevel(Level.INFO);

        ServerConnection conn = new ServerConnection();
        conn.startServer();
    }
}