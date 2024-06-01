package pl.wit.kowalcj1.java_place;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Old {
    private static final Logger log = LogManager.getLogger(Old.class);

    private static final int GRID_SIZE = 10; // Define grid size
    private static final Color DEFAULT_COLOR = Color.WHITE;

    public static void main(String[] args) {
        Frontend window = new Frontend();
        Connection conn = new Connection();

        conn.connect(null, 2137);

        window.setVisible(true);
    }

    public static void main01(String[] args) {
        JFrame frame = new JFrame("RPlace Example");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridPanel.setPreferredSize(new Dimension(500, 500));
        frame.add(gridPanel, BorderLayout.CENTER);

        // frame.addComponentListener(new ComponentAdapter() {
        //     @Override
        //     public void componentResized(ComponentEvent e) {
        //         enforceAspectRatio(frame, ASPECT_RATIO);
        //     }
        // });

        JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(DEFAULT_COLOR);
                button.setOpaque(true);
                button.setBorder(new LineBorder(Color.BLACK));
                // button.setBorderPainted(false);

                // Add mouse listener for hover effect
                button.addMouseListener(new MouseAdapter() {
                    private Color borderColor = Color.BLACK;
                    private LineBorder highlightedBorder = new LineBorder(borderColor, 3);
                    private LineBorder defaultBorder = new LineBorder(borderColor, 1);

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBorder(highlightedBorder);
                        // button.setBorderPainted(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBorder(defaultBorder);
                        // button.setBorderPainted(false);
                    }
                });

                // Add action listener for click event
                button.addActionListener(e -> {
                    // Color selectedColor = JColorChooser.showDialog(frame, "Choose a color", button.getBackground());
                    // if (selectedColor != null) {
                    //     // Simulate sending color to the server and getting response
                    //     // In a real application, this would involve server communication
                    //     button.setBackground(selectedColor);
                    // }
                });

                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        JPanel anotherPanel = new JPanel();
        frame.add(anotherPanel, BorderLayout.SOUTH);
        anotherPanel.setPreferredSize(new Dimension(200, 100));
        anotherPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        frame.pack();
        // frame.setSize(800, 800);
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static void enforceAspectRatio(JFrame frame, double targetAspectRatio) {
        Dimension size = frame.getSize();

        int min = (int) Math.min(size.getWidth(), size.getHeight());
        frame.setSize(min, min);
    }

    private void handleCommunication(Socket socket) {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (Exception e) {
            log.error("Error during communication", e);
        }
    }
}
