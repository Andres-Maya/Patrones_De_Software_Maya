package com.wargame.ui;

import com.wargame.model.Battalion;
import com.wargame.model.Soldier;
import com.wargame.util.AppController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel that displays all created battalions and lets the user inspect their soldiers.
 */
public class BattalionOverviewPanel extends JPanel {

    private final AppController controller;

    private JTable battalionTable;
    private DefaultTableModel battalionModel;
    private JTable soldierTable;
    private DefaultTableModel soldierModel;
    private JLabel battalionDetailLabel;
    private JLabel statsLabel;

    public BattalionOverviewPanel(AppController controller) {
        this.controller = controller;
        buildUi();
    }

    private void buildUi() {
        setBackground(Theme.BG_PANEL);
        setLayout(new BorderLayout(0, 0));

        // Header
        JLabel header = new JLabel("  🛡  CENTRO DE MANDO DE BATALLONES", JLabel.LEFT);
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.ACCENT_BLUE);
        header.setBackground(Theme.BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        add(header, BorderLayout.NORTH);

        // Split pane
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBackground(Theme.BG_PANEL);
        split.setDividerLocation(210);
        split.setDividerSize(4);

        // ── Top: Battalion list ──
        JPanel topPanel = new JPanel(new BorderLayout(0, 6));
        topPanel.setBackground(Theme.BG_PANEL);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 4, 16));

        JLabel topLabel = new JLabel("BATALLONES");
        topLabel.setFont(Theme.FONT_HEAD);
        topLabel.setForeground(Theme.ACCENT_AMBER);
        topPanel.add(topLabel, BorderLayout.NORTH);

        String[] bCols = {"ID", "Nombre", "Soldados", "Unidades Élite", "Poder Promedio", "Poder Total"};
        battalionModel = new DefaultTableModel(bCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        battalionTable = new JTable(battalionModel);
        styleTable(battalionTable);

        battalionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showBattalionDetail();
        });

        JScrollPane bScroll = new JScrollPane(battalionTable);
        bScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        bScroll.getViewport().setBackground(Theme.BG_CARD);
        topPanel.add(bScroll, BorderLayout.CENTER);

        // Actions
        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topActions.setBackground(Theme.BG_PANEL);
        StyledButton deleteBtn = new StyledButton("🗑 ELIMINAR BATALLÓN", StyledButton.Style.DANGER);
        deleteBtn.addActionListener(e -> deleteBattalion());
        topActions.add(deleteBtn);
        topPanel.add(topActions, BorderLayout.SOUTH);

        split.setTopComponent(topPanel);

        // ── Bottom: Soldier detail ──
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 6));
        bottomPanel.setBackground(Theme.BG_PANEL);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 16, 10, 16));

        battalionDetailLabel = new JLabel("SELECCIONA UN BATALLÓN PARA VER SUS SOLDADOS");
        battalionDetailLabel.setFont(Theme.FONT_HEAD);
        battalionDetailLabel.setForeground(Theme.ACCENT_AMBER);
        bottomPanel.add(battalionDetailLabel, BorderLayout.NORTH);

        String[] sCols = {"ID", "Nombre", "Rango", "Facción", "VP", "ARM", "VEL", "Arma Principal", "Casco", "Habilidades", "Élite", "Poder"};
        soldierModel = new DefaultTableModel(sCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        soldierTable = new JTable(soldierModel);
        styleTable(soldierTable);

        JScrollPane sScroll = new JScrollPane(soldierTable);
        sScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sScroll.getViewport().setBackground(Theme.BG_CARD);
        bottomPanel.add(sScroll, BorderLayout.CENTER);

        statsLabel = new JLabel("—");
        statsLabel.setFont(Theme.FONT_SMALL);
        statsLabel.setForeground(Theme.TEXT_SECONDARY);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        bottomPanel.add(statsLabel, BorderLayout.SOUTH);

        split.setBottomComponent(bottomPanel);
        add(split, BorderLayout.CENTER);
    }

    // ─── Data Ops ─────────────────────────────────────────────────────────────

    public void refresh() {
        battalionModel.setRowCount(0);
        soldierModel.setRowCount(0);
        battalionDetailLabel.setText("SELECCIONA UN BATALLÓN PARA VER SUS SOLDADOS");
        statsLabel.setText("—");

        for (Battalion b : controller.getBattalions()) {
            battalionModel.addRow(new Object[]{
                    b.getId(),
                    b.getName(),
                    b.getSize(),
                    b.getEliteCount(),
                    b.getAverageCombatPower(),
                    b.getTotalCombatPower()
            });
        }
    }

    private void showBattalionDetail() {
        int row = battalionTable.getSelectedRow();
        if (row < 0 || row >= controller.getBattalions().size()) return;

        Battalion b = controller.getBattalions().get(row);
        battalionDetailLabel.setText("SOLDADOS — " + b.getName().toUpperCase() + "  [" + b.getId() + "]");

        soldierModel.setRowCount(0);
        for (Soldier s : b.getSoldiers()) {
            soldierModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getRank() != null ? s.getRank().getTitle() : "-",
                    s.getFaction(),
                    s.getHealth(),
                    s.getArmor(),
                    s.getSpeed(),
                    s.getPrimaryWeapon() != null ? s.getPrimaryWeapon().getDisplayName() : "-",
                    s.getHelmet() != null ? s.getHelmet().getDisplayName() : "-",
                    s.getSkills().size(),
                    s.isElite() ? "★" : "—",                    s.calculateCombatPower()
            });
        }

        statsLabel.setText(String.format(
                "Total soldados: %d  |  Élite: %d  |  Poder promedio: %d  |  Poder total: %,d",
                b.getSize(), b.getEliteCount(), b.getAverageCombatPower(), b.getTotalCombatPower()));
    }

    private void deleteBattalion() {
        int row = battalionTable.getSelectedRow();
        if (row < 0) return;
        Battalion b = controller.getBattalions().get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el batallón '" + b.getName() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.removeBattalion(b);
            refresh();
        }
    }

    // ─── Styling ──────────────────────────────────────────────────────────────

    private void styleTable(JTable table) {
        table.setBackground(Theme.BG_CARD);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setFont(Theme.FONT_SMALL);
        table.setRowHeight(26);
        table.setGridColor(Theme.BORDER);
        table.setSelectionBackground(new Color(20, 80, 160));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader th = table.getTableHeader();
        th.setBackground(Theme.BG_HEADER);
        th.setForeground(Theme.ACCENT_AMBER);
        th.setFont(Theme.FONT_LABEL);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.BORDER_BRIGHT));
    }
}
