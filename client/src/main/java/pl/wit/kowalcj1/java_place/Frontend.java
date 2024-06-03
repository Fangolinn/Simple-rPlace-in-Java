package pl.wit.kowalcj1.java_place;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Frontend extends JFrame implements FrontendAPI {
    private static final Logger log = LogManager.getLogger(Frontend.class);

    private static final int GRID_SIZE = 50;

    private ConnectionStatusDisplay connectionStatus;

    private final transient Backend backend;

    private Grid grid;

    public Frontend() {
        backend = new Backend(this);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        connectionStatus = new ConnectionStatusDisplay();

        JButton resetConnButton = new JButton("Reset connection");

        resetConnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backend.isConnectionActive())
                    backend.stopConnection();

                Frontend.this.initConnection();
            }
        });

        connectionStatus.add(resetConnButton);

        // https://www.rapidtables.com/web/color/RGB_Color.html
        Color[] colors = new Color[] { Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, new Color(192, 192, 192), Color.GRAY, new Color(128, 0, 0),
                new Color(128, 128, 0), new Color(0, 128, 0), new Color(128, 0, 128), new Color(0, 128, 128), new Color(0, 0, 128) };
        ColorSelector colorSelector = new ColorSelector(colors);

        grid = new Grid(GRID_SIZE, colorSelector, backend);
        grid.setPreferredSize(new Dimension(1000, 1000));

        this.add(connectionStatus);

        JPanel gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));

        JPanel colorSelectorPanel = new JPanel();
        colorSelectorPanel.setLayout(new BoxLayout(colorSelectorPanel, BoxLayout.Y_AXIS));

        JTextArea instructions = new JTextArea(String.format(
                "- Wybierz kolor z dostępnych poniżej %n- Lewy przycisk myszy - zmień kolor komórki (tylko kiedy jest aktywne połączenie do serwera) %n- Scroll - zmień aktualny zoom %n- Prawy przycisk myszy - przytrzymaj żeby przesunąć kanwę"));

        instructions.setEditable(false);
        instructions.setMaximumSize(new Dimension(500, 500));
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);

        colorSelectorPanel.add(instructions);
        colorSelectorPanel.add(colorSelector);

        colorSelectorPanel.setMinimumSize(new Dimension(500, 0));
        colorSelectorPanel.setPreferredSize(new Dimension(300, Integer.MAX_VALUE));
        colorSelectorPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        gamePanel.add(colorSelectorPanel);

        gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        gamePanel.add(grid);

        this.add(gamePanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (backend != null && backend.isConnectionActive())
                    backend.stopConnection();

                super.windowClosing(e);
            }
        });

    }

    public void initConnection() {
        backend.startConnection("localhost", 2137);

        if (isConnected())
            backend.startListening();
    }

    @Override
    public void setConnectionStatus(String status) {
        SwingUtilities.invokeLater(() -> connectionStatus.setStatus(status));
    }

    @Override
    public void updateCell(CellInfo cellInfo) {
        grid.updateCell(cellInfo);
    }

    private boolean isConnected() {
        return backend.isConnectionActive();
    }
}
