package com.wargame.ui;

import com.wargame.model.Battalion;
import com.wargame.util.AppController;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panel showing high-level war room statistics.
 */
public class DashboardPanel extends JPanel {

    private final AppController controller;

    private JLabel prototypesValue;
    private JLabel battalionsValue;
    private JLabel soldiersValue;
    private JLabel totalPowerValue;
    private JTextArea logArea;

    public DashboardPanel(AppController controller) {
        this.controller = controller;
        buildUi();
    }

    private void buildUi() {
        setBackground(Theme.BG_PANEL);
        setLayout(new BorderLayout(0, 0));

        // Header
        JLabel header = new JLabel("  ☠  SALA DE GUERRA — VISIÓN GENERAL", JLabel.LEFT);
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.ACCENT_RED);
        header.setBackground(Theme.BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        add(header, BorderLayout.NORTH);

        // Stats cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsPanel.setBackground(Theme.BG_PANEL);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        prototypesValue = new JLabel("0");
        battalionsValue = new JLabel("0");
        soldiersValue   = new JLabel("0");
        totalPowerValue = new JLabel("0");

        cardsPanel.add(statCard("Prototipos",       prototypesValue, Theme.ACCENT_GREEN));
        cardsPanel.add(statCard("Batallones",        battalionsValue, Theme.ACCENT_BLUE));
        cardsPanel.add(statCard("Total Soldados",    soldiersValue,   Theme.ACCENT_AMBER));
        cardsPanel.add(statCard("Poder Total",       totalPowerValue, Theme.ACCENT_RED));

        add(cardsPanel, BorderLayout.CENTER);

        // Log / info area
        JPanel logPanel = new JPanel(new BorderLayout(0, 6));
        logPanel.setBackground(Theme.BG_PANEL);
        logPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 20, 24));

        JLabel logLabel = new JLabel("INFORMACIÓN DE PATRONES DE DISEÑO");
        logLabel.setFont(Theme.FONT_HEAD);
        logLabel.setForeground(Theme.ACCENT_AMBER);
        logPanel.add(logLabel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Theme.BG_CARD);
        logArea.setForeground(Theme.TEXT_SECONDARY);
        logArea.setFont(Theme.FONT_SMALL);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        logArea.setText(getPatternInfo());

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        logScroll.getViewport().setBackground(Theme.BG_CARD);
        logScroll.setPreferredSize(new Dimension(0, 220));
        logPanel.add(logScroll, BorderLayout.CENTER);

        add(logPanel, BorderLayout.SOUTH);
    }

    private JPanel statCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(accent);
                g2.fillRect(0, 0, 4, getHeight());
            }
        };
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Courier New", Font.BOLD, 38));
        valueLabel.setForeground(accent);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        int prototypes = controller.getRegistry().getPrototypeCount();
        int battalions = controller.getBattalions().size();
        int soldiers   = controller.getSavedSoldiers().size()
                       + controller.getBattalions().stream().mapToInt(Battalion::getSize).sum();
        int power      = controller.getBattalions().stream().mapToInt(Battalion::getTotalCombatPower).sum();

        prototypesValue.setText(String.valueOf(prototypes));
        battalionsValue.setText(String.valueOf(battalions));
        soldiersValue.setText(String.valueOf(soldiers));
        totalPowerValue.setText(String.format("%,d", power));
    }

    private String getPatternInfo() {
        return """
                ═══════════════════════════════════════════════════════════════════════════
                 PATRÓN BUILDER  (Pestaña 2 — Constructor de Soldado)
                ─────────────────────────────────────────────────────────────────────────
                 Propósito : Construir un objeto Soldier complejo paso a paso usando una
                             API fluida, sin exponer constructores internos ni sobrecargas.
                 Cómo funciona: SoldierBuilder acumula la configuración (arma, casco,
                             habilidades, estadísticas) y llama a build() para producir
                             un Soldier final y validado.
                 Beneficio : Evita constructores sobrecargados, centraliza la validación
                             y hace el código legible (cadena de llamadas .withXxx()).

                ═══════════════════════════════════════════════════════════════════════════
                 PATRÓN PROTOTYPE  (Pestaña 3 — Registro de Prototipos)
                ─────────────────────────────────────────────────────────────────────────
                 Propósito : Clonar un Soldier ya configurado para producir muchas copias
                             idénticas sin volver a ejecutar el builder desde cero.
                 Cómo funciona: Soldier implementa Cloneable<Soldier>. SoldierRegistry
                             almacena plantillas con nombre. BattalionFactory llama a
                             clonePrototype(key) N veces para llenar un batallón al instante.
                 Beneficio : Gran ahorro de tiempo al desplegar ejércitos enteros. El
                             soldado "plantilla" original permanece intacto; los clones
                             pueden divergir de forma independiente.
                ═══════════════════════════════════════════════════════════════════════════
                """;
    }
}
