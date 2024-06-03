package pl.wit.kowalcj1.java_place;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ColorSelector extends JPanel {
    private static final int NO_OF_COLORS = 16;

    private Color selectedColor;
    private JButton[] colorButtons;

    public ColorSelector(Color[] colors) {
        if (colors.length != NO_OF_COLORS)
            throw new IllegalArgumentException(String.format("Expected %d colors, got %d.", NO_OF_COLORS, colors.length));

        this.setLayout(new GridLayout(colors.length / 2, 2));
        colorButtons = new JButton[colors.length];

        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorder(new LineBorder(Color.BLACK));
            colorButton.addActionListener(new ColorButtonListener(color));
            colorButtons[i] = colorButton;
            this.add(colorButton);
        }

        setSelectedColor(colors[0]);
        colorButtons[0].setBorder(new LineBorder(Color.RED, 3));
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    private void setSelectedColor(Color color) {
        this.selectedColor = color;
    }

    private class ColorButtonListener implements ActionListener {
        private Color color;

        public ColorButtonListener(Color color) {
            this.color = color;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setSelectedColor(color);
            updateButtonBorders();
        }

        private void updateButtonBorders() {
            for (JButton button : colorButtons) {
                if (button.getBackground().equals(selectedColor)) {
                    button.setBorder(new LineBorder(Color.RED, 3));
                } else {
                    button.setBorder(new LineBorder(Color.BLACK));
                }
            }
        }
    }
}
