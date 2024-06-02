package pl.wit.kowalcj1.java_place;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Grid extends JScrollPane {
    private static final Logger log = LogManager.getLogger(Grid.class);

    private JPanel cells;

    private JButton[][] buttons;
    private Point origin;

    public Grid(int gridSize) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cells = new JPanel(new GridLayout(gridSize, gridSize));

        this.getViewport().add(cells);

        this.setWheelScrollingEnabled(false);

        buttons = new GridCell[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                GridCell button = new GridCell(i, j);
                button.setBackground(Color.WHITE);
                button.setBorder(new LineBorder(Color.BLACK));

                buttons[i][j] = button;
                cells.add(button);
            }
        }

        cells.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    origin = e.getPoint();
                }
            }
        });

        cells.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, cells);
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

        cells.addMouseWheelListener(e -> {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int notches = e.getWheelRotation();
                Dimension size = cells.getPreferredSize();
                int newSize = Math.max(size.width + notches * 10, gridSize * 10); // Ensure minimum size
                this.setPreferredSize(new Dimension(newSize, newSize));
                cells.revalidate();
            }
        });
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        cells.setPreferredSize(preferredSize);
        this.setMaximumSize(preferredSize);
    }

    private class GridCell extends JButton {
        private int iCoord;
        private int jCoord;

        public int getiCoord() {
            return iCoord;
        }

        public int getjCoord() {
            return jCoord;
        }

        public GridCell(int iCoord, int jCoord) {
            this.iCoord = iCoord;
            this.jCoord = jCoord;

            this.setOpaque(true);

            this.addMouseListener(new MouseAdapter() {
                private Color borderColor = Color.BLACK;
                private LineBorder highlightedBorder = new LineBorder(borderColor, 3);
                private LineBorder defaultBorder = null;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        MouseEvent newEvent = new MouseEvent(cells,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        cells.dispatchEvent(newEvent);
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
                        MouseEvent newEvent = new MouseEvent(cells,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        cells.dispatchEvent(newEvent);
                    }
                }
            });
        }
    }
}