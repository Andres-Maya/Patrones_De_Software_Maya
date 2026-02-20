package com.wargame.ui;

import java.awt.*;

/**
 * Centralized UI theme constants — military / dark-ops aesthetic.
 */
public final class Theme {

    private Theme() {}

    // ─── Colors ───────────────────────────────────────────────────────────────

    public static final Color BG_DARK        = new Color(10,  14,  20);
    public static final Color BG_PANEL       = new Color(18,  24,  32);
    public static final Color BG_CARD        = new Color(26,  35,  45);
    public static final Color BG_HEADER      = new Color(15,  20,  28);

    public static final Color ACCENT_GREEN   = new Color(0,  220, 100);
    public static final Color ACCENT_AMBER   = new Color(255, 190,  30);
    public static final Color ACCENT_RED     = new Color(220,  50,  50);
    public static final Color ACCENT_BLUE    = new Color( 30, 150, 255);

    public static final Color TEXT_PRIMARY   = new Color(220, 230, 240);
    public static final Color TEXT_SECONDARY = new Color(130, 150, 170);
    public static final Color TEXT_MUTED     = new Color( 70,  85, 100);

    public static final Color BORDER         = new Color(40,  55,  70);
    public static final Color BORDER_BRIGHT  = new Color(60,  80, 100);

    // ─── Fonts ────────────────────────────────────────────────────────────────

    public static final Font FONT_TITLE  = new Font("Courier New", Font.BOLD, 22);
    public static final Font FONT_HEAD   = new Font("Courier New", Font.BOLD, 14);
    public static final Font FONT_BODY   = new Font("Courier New", Font.PLAIN, 12);
    public static final Font FONT_SMALL  = new Font("Courier New", Font.PLAIN, 11);
    public static final Font FONT_LABEL  = new Font("Courier New", Font.BOLD, 12);

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Generates a green-to-red color based on a 0–100 score.
     */
    public static Color scoreColor(int score, int max) {
        float ratio = Math.min(1f, (float) score / max);
        if (ratio > 0.6f) return ACCENT_GREEN;
        if (ratio > 0.35f) return ACCENT_AMBER;
        return ACCENT_RED;
    }
}
