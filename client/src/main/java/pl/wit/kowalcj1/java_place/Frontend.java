package pl.wit.kowalcj1.java_place;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Frontend extends JFrame implements FrontendAPI {
    private static final Logger log = LogManager.getLogger(Frontend.class);

    private static final int GRID_SIZE = 10;

    private JLabel connectionStatus;

    private final transient Backend backend;

    public Frontend() {
        backend = new Backend(this);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel connectionStatusPanel = new JPanel(new FlowLayout());
        JLabel connStatusLabel = new JLabel("Connection status:");
        connectionStatus = new JLabel("Disconnected");

        connectionStatusPanel.add(connStatusLabel);
        connectionStatusPanel.add(connectionStatus);
        connectionStatusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        Grid grid = new Grid(GRID_SIZE);
        grid.setPreferredSize(new Dimension(1000, 1000));

        this.add(connectionStatusPanel);
        this.add(grid);
    }

    public void initConnection() {
        backend.startConnection("localhost", 2137);
        backend.startListening();
    }

    @Override
    public void setConnectionStatus(String status) {
        SwingUtilities.invokeLater(() -> this.connectionStatus.setText(status));
    }

    @Override
    public void newChatMessage(Message message) {
        // SwingUtilities.invokeLater(() -> chat.addMessage(message));
    }
}
