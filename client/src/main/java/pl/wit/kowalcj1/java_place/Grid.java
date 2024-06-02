package pl.wit.kowalcj1.java_place;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Grid extends JPanel {
    private static final Logger log = LogManager.getLogger(Grid.class);

    private JButton[][] buttons;
    private Point origin;

    public Grid(int gridSize) {
        this.setLayout(new GridLayout(gridSize, gridSize));
        buttons = new GridCell[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                GridCell button = new GridCell();
                button.setBackground(Color.WHITE);
                button.setBorder(new LineBorder(Color.BLACK));

                buttons[i][j] = button;
                this.add(button);
            }
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    origin = e.getPoint();
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, Grid.this);
                    if (viewport != null && origin != null) {
                        Point current = e.getPoint();
                        Point viewPosition = viewport.getViewPosition();
                        int deltaX = origin.x - current.x;
                        int deltaY = origin.y - current.y;
                        viewPosition.translate(deltaX, deltaY);
                        ((JComponent) e.getSource())
                                .scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                    }
                }
            }
        });
    }

    private class GridCell extends JButton{
        public GridCell(){
            this.setOpaque(true);

            this.addMouseListener(new MouseAdapter() {
                private Color borderColor = Color.BLACK;
                private LineBorder highlightedBorder = new LineBorder(borderColor, 3);
                private LineBorder defaultBorder = null;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        MouseEvent newEvent = new MouseEvent(Grid.this,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        Grid.this.dispatchEvent(newEvent);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    GridCell.this.setBorder(highlightedBorder);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    GridCell.this.setBorder(defaultBorder);
                }
            });

            this.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        MouseEvent newEvent = new MouseEvent(Grid.this,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        Grid.this.dispatchEvent(newEvent);
                    }
                }
            });
        }
    }
}