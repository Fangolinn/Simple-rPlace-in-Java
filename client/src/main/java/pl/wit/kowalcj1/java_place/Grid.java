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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

    private JPanel cellsContainer;

    private JButton[][] cells;
    private Point origin;

    private ColorSelector colorSelector;
    private Backend backend;

    public Grid(int gridSize, ColorSelector colorSelector, Backend backend) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.colorSelector = colorSelector;
        this.backend = backend;

        cellsContainer = new JPanel(new GridLayout(gridSize, gridSize));
        cellsContainer.setBorder(new LineBorder(Color.GRAY, 20));

        this.getViewport().add(cellsContainer);

        this.setWheelScrollingEnabled(false);

        cells = new GridCell[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                GridCell cell = new GridCell(i, j, Color.WHITE);

                cells[i][j] = cell;
                cellsContainer.add(cell);
            }
        }

        cellsContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    origin = e.getPoint();
                }
            }
        });

        cellsContainer.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, cellsContainer);
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

        cellsContainer.addMouseWheelListener(e -> {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int notches = -e.getWheelRotation();
                Dimension size = cellsContainer.getPreferredSize();
                int newSize = Math.max(size.width + notches * 80, gridSize * 10);
                this.setPreferredSize(new Dimension(newSize, newSize));
                cellsContainer.revalidate();
            }
        });
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        cellsContainer.setPreferredSize(preferredSize);
        super.setPreferredSize(preferredSize);
    }

    public void updateCell(CellInfo cellInfo) {
        cells[cellInfo.getiCoord()][cellInfo.getjCoord()].setBackground(cellInfo.getColor());
    }

    private class GridCell extends JButton {
        private CellInfo cellInfo;

        public CellInfo getCellInfo() {
            return cellInfo;
        }

        public GridCell(int iCoord, int jCoord, Color color) {
            this.cellInfo = new CellInfo(iCoord, jCoord, color);

            this.setOpaque(true);
            this.setBackground(color);
            this.setBorder(null);

            this.addMouseListener(new MouseAdapter() {
                private Color borderColor = Color.BLACK;
                private LineBorder highlightedBorder = new LineBorder(borderColor, 3);
                private LineBorder defaultBorder = null;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        Color selectedColor = colorSelector.getSelectedColor();
                        if (!backend.isConnectionActive()){
                            JOptionPane.showMessageDialog(SwingUtilities.getAncestorOfClass(JFrame.class, Grid.this), "Not connected!");
                        } else if (!getBackground().equals(selectedColor)) {
                            GridCell.this.setBackground(selectedColor);
                            backend.sendUpdate(GridCell.this.getCellInfo());
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        MouseEvent newEvent = new MouseEvent(cellsContainer,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        cellsContainer.dispatchEvent(newEvent);

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
                        MouseEvent newEvent = new MouseEvent(cellsContainer,
                                e.getID(),
                                System.currentTimeMillis(),
                                e.getModifiersEx(),
                                e.getX(),
                                e.getY(),
                                e.getClickCount(),
                                e.isPopupTrigger(),
                                e.getButton());

                        cellsContainer.dispatchEvent(newEvent);
                    }
                }
            });
        }

        @Override
        public void setBackground(Color bg) {
            if (getCellInfo() != null)
                getCellInfo().setColor(bg);

            super.setBackground(bg);
        }
    }
}