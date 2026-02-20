package com.wargame.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom styled button with military-grade aesthetics.
 */
public class StyledButton extends JButton {

    public enum Style { PRIMARY, DANGER, SECONDARY, SUCCESS }

    private final Style style;
    private boolean hovered = false;

    public StyledButton(String text, Style style) {
        super(text);
        this.style = style;
        applyStyle();
    }

    private void applyStyle() {
        setFont(Theme.FONT_LABEL);
        setForeground(Theme.TEXT_PRIMARY);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(true);
        setBackground(getBaseColor());
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hovered = true;
                setBackground(getHoverColor());
            }
            @Override public void mouseExited(MouseEvent e) {
                hovered = false;
                setBackground(getBaseColor());
            }
        });
    }

    private Color getBaseColor() {
        return switch (style) {
            case PRIMARY   -> new Color(20, 80, 160);
            case DANGER    -> new Color(140, 30, 30);
            case SUCCESS   -> new Color(20, 110, 60);
            case SECONDARY -> new Color(40, 55, 70);
        };
    }

    private Color getHoverColor() {
        return switch (style) {
            case PRIMARY   -> new Color(30, 110, 220);
            case DANGER    -> new Color(200, 50, 50);
            case SUCCESS   -> new Color(30, 160, 90);
            case SECONDARY -> new Color(60, 80, 100);
        };
    }
}
