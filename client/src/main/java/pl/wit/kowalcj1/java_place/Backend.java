package pl.wit.kowalcj1.java_place;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Backend {
    private static final Logger log = LogManager.getLogger(Backend.class);

    private Connection connection;
    private FrontendAPI frontendAPI;

    public Backend(FrontendAPI frontendAPI) {
        this.connection = new Connection();
        this.frontendAPI = frontendAPI;
    }

    public void startConnection(String host, int port) {
        connection.connect(host, port);
        String status = connection.isConnected() ? "Connected" : "Connection Failed";
        frontendAPI.setConnectionStatus(status);
        log.info(status);
    }

    public void stopConnection() {
        connection.close();
        frontendAPI.setConnectionStatus("Disconnected");
        log.info("Connection successfully stopped");
    }

    public boolean isConnectionActive() {
        return connection.isConnected();
    }

    public void sendMessage(Message message){
        connection.sendMessage(message);
    }

    public void startListening(){
        connection.startListening(frontendAPI);
        log.info("Listening for incoming messages...");
    }
}
