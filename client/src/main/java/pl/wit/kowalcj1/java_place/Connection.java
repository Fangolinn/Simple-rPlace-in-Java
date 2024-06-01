package pl.wit.kowalcj1.java_place;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection {
    private static final Logger log = LogManager.getLogger(Connection.class);

    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            log.info("Connected to {} on port {}", host, port);
        } catch (Exception e) {
            log.error("Failed to connect to {} on port {}", host, port, e);
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                log.info("Connection closed");
            }
        } catch (Exception e) {
            log.error("Failed to close connection", e);
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    private void createWriter() {
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            log.debug("Writer created");
        } catch (IOException e) {
            log.error("Failed to create writer for socket", e);
        }
    }

    private void createReader() {
        try {
            reader = new ObjectInputStream(socket.getInputStream());
            log.debug("Reader created");
        } catch (IOException e) {
            log.error("Failed to create reader for socket", e);
        }
    }

    public void sendMessage(Message message) {
        if (writer == null)
            createWriter();

        try {
            writer.writeObject(message);
        } catch (IOException e) {
            log.error("Error while writing message", e);
        }
        log.info("Message sent: '{}'", message);

    }

    public void startListening(FrontendAPI frontendAPI) {
        if (reader == null)
            createReader();

        new Thread(() -> {
            Message message;
            try {
                while ((message = (Message) reader.readObject()) != null) {
                    log.info("Message received: '{}'", message);
                    frontendAPI.newChatMessage(message);
                }
            } catch (Exception e) {
                log.error("Error while reading messages", e);
            }
        }).start();
    }
}
