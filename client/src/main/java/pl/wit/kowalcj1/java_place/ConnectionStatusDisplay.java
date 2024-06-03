package pl.wit.kowalcj1.java_place;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectionStatusDisplay extends JPanel {
    private JLabel currentStatus;

    public ConnectionStatusDisplay() {
        this.setLayout(new FlowLayout());

        JLabel text = new JLabel("Connection status:");
        currentStatus = new JLabel("Disconnected");

        this.add(text);
        this.add(currentStatus);

        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
    }

    public void setStatus(String newStatus) {
        currentStatus.setText(newStatus);
    }
}
