package pl.wit.kowalcj1.java_place;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConnection {
    private static final Logger log = LogManager.getLogger(ServerConnection.class);

    private final Set<ClientHandler> connectedClients = Collections.synchronizedSet(new HashSet<>());
    private Set<CellInfo> boardState = Collections.synchronizedSet(new HashSet<>());

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(2137)) {
            log.info("Server started on port 2137");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                connectedClients.add(clientHandler);
                new Thread(clientHandler).start();
                log.info("New client connected: " + clientSocket.getInetAddress());
            }
        } catch (Exception e) {
            log.error("Error starting server: ", e);
        }
    }

    private class ClientHandler implements Runnable {
        private static final Logger log = LogManager.getLogger(ClientHandler.class);

        private final Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                this.output = new ObjectOutputStream(clientSocket.getOutputStream());
                this.output.flush();
                this.input = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception e) {
                log.error("Error creating stream objects: ", e);
            }
        }

        @Override
        public void run() {
            log.debug("Starting ClientHandler for client: {}", clientSocket.getInetAddress());

            sendBoard();

            try {
                CellInfo incomingUpdate;
                while ((incomingUpdate = (CellInfo) input.readObject()) != null) {
                    log.debug("Received update: '{}'", incomingUpdate);
                    if (!boardState.add(incomingUpdate)){
                        boardState.remove(incomingUpdate);
                        boardState.add(incomingUpdate);
                    }
                    broadcastMessage(incomingUpdate);
                }
            } catch (EOFException e) {
                log.info("Connection closed.");
            } catch (Exception e) {
                log.error("Error in client handler: ", e);
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    log.error("Error closing client socket: ", e);
                }
                connectedClients.remove(this);
                log.info("Client disconnected: {}", clientSocket.getInetAddress());
            }
        }

        private void sendBoard() {
            for (CellInfo cellInfo : boardState) {
                try {
                    output.writeObject(cellInfo);
                } catch (IOException e) {
                    log.error("Error sending CellInfo to client: {}", cellInfo, e);
                }
            }
        }

        private void broadcastMessage(CellInfo incomingUpdate) {
            synchronized (connectedClients) {
                log.debug("Broadcasting update: '{}'", incomingUpdate);
                for (ClientHandler clientHandler : connectedClients) {
                    try {
                        clientHandler.output.writeObject(incomingUpdate);
                        clientHandler.output.reset();
                    } catch (Exception e) {
                        log.error("Error broadcasting update to client: ", e);
                    }
                }
            }
        }
    }
}