package com.wargame.ui;

import com.wargame.model.Soldier;
import com.wargame.util.AppController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel for viewing and interacting with the Prototype registry.
 * Allows cloning individual soldiers or creating full battalions from templates.
 */
public class PrototypeRegistryPanel extends JPanel {

    private final AppController controller;
    private final Runnable onChanged;

    private JTable protoTable;
    private DefaultTableModel tableModel;
    private JSpinner cloneCountSpinner;
    private JTextField battalionNameField;
    private JLabel statusLabel;

    public PrototypeRegistryPanel(AppController controller, Runnable onChanged) {
        this.controller = controller;
        this.onChanged  = onChanged;
        buildUi();
    }

    private void buildUi() {
        setBackground(Theme.BG_PANEL);
        setLayout(new BorderLayout(0, 8));

        // Header
        JLabel header = new JLabel("  🧬  REGISTRO DE PROTOTIPOS", JLabel.LEFT);
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.ACCENT_GREEN);
        header.setBackground(Theme.BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Clave Prototipo", "Nombre", "Rango", "Arma Principal", "Casco", "Habilidades", "Élite", "Poder"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        protoTable = new JTable(tableModel);
        styleTable(protoTable);

        JScrollPane scroll = new JScrollPane(protoTable);
        scroll.setBackground(Theme.BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        scroll.getViewport().setBackground(Theme.BG_CARD);
        add(scroll, BorderLayout.CENTER);

        // Actions panel
        JPanel actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBackground(Theme.BG_HEADER);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.anchor = GridBagConstraints.WEST;

        // Row 1 — Battalion creation
        gc.gridy = 0; gc.gridx = 0;
        actionsPanel.add(labelOf("Nombre del Batallón:"), gc);
        gc.gridx = 1;
        battalionNameField = darkTextField("Batallón Alpha");
        actionsPanel.add(battalionNameField, gc);
        gc.gridx = 2;
        actionsPanel.add(labelOf("Cantidad de Clones:"), gc);
        gc.gridx = 3;
        cloneCountSpinner = darkSpinner(5, 1, 200);
        actionsPanel.add(cloneCountSpinner, gc);

        gc.gridx = 4;
        StyledButton createBtn = new StyledButton("🚀 CREAR BATALLÓN", StyledButton.Style.PRIMARY);
        createBtn.addActionListener(e -> createBattalion());
        actionsPanel.add(createBtn, gc);

        // Row 2 — Single clone + delete
        gc.gridy = 1; gc.gridx = 0;
        StyledButton cloneOneBtn = new StyledButton("📋 CLONAR SELECCIONADO", StyledButton.Style.SECONDARY);
        cloneOneBtn.addActionListener(e -> cloneSelected());
        actionsPanel.add(cloneOneBtn, gc);

        gc.gridx = 1;
        StyledButton deleteBtn = new StyledButton("🗑  ELIMINAR PROTOTIPO", StyledButton.Style.DANGER);
        deleteBtn.addActionListener(e -> removeSelected());
        actionsPanel.add(deleteBtn, gc);

        gc.gridx = 2; gc.gridwidth = 3;
        statusLabel = new JLabel("Selecciona un prototipo de la tabla para operar.");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.TEXT_SECONDARY);
        actionsPanel.add(statusLabel, gc);

        add(actionsPanel, BorderLayout.SOUTH);

        refresh();
    }

    // ─── Data refresh ─────────────────────────────────────────────────────────

    public void refresh() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Soldier> entry : controller.getRegistry().getAllPrototypes().entrySet()) {
            Soldier s = entry.getValue();
            tableModel.addRow(new Object[]{
                    entry.getKey(),
                    s.getName(),
                    s.getRank() != null ? s.getRank().getTitle() : "-",
                    s.getPrimaryWeapon() != null ? s.getPrimaryWeapon().getDisplayName() : "-",
                    s.getHelmet() != null ? s.getHelmet().getDisplayName() : "-",
                    s.getSkills().size() + " habilidades",
                    s.isElite() ? "★ ELITE" : "—",
                    s.calculateCombatPower()
            });
        }
    }

    // ─── Actions ──────────────────────────────────────────────────────────────

    private void createBattalion() {
        String key = getSelectedKey();
        if (key == null) return;

        String name  = battalionNameField.getText().trim();
        int    count = (int) cloneCountSpinner.getValue();

        if (name.isEmpty()) { showError("Ingresa un nombre para el batallón."); return; }

        controller.createBattalion(name, key, count);
        setStatus("✓ Batallón '" + name + "' creado con " + count + " clones de [" + key + "]", Theme.ACCENT_GREEN);
        onChanged.run();
    }

    private void cloneSelected() {
        String key = getSelectedKey();
        if (key == null) return;

        Soldier clone = controller.cloneSoldier(key);
        // Register the clone as a new prototype with a modified key
        String newKey = key + " #clone-" + clone.getId();
        controller.registerAsPrototype(newKey, clone);
        refresh();
        setStatus("✓ Clonado → guardado como prototipo: " + newKey, Theme.ACCENT_GREEN);
        onChanged.run();
    }

    private void removeSelected() {
        String key = getSelectedKey();
        if (key == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el prototipo '" + key + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.removePrototype(key);
            refresh();
            setStatus("✓ Prototipo eliminado: " + key, Theme.ACCENT_AMBER);
            onChanged.run();
        }
    }

    private String getSelectedKey() {
        int row = protoTable.getSelectedRow();
        if (row < 0) { showError("Por favor selecciona un prototipo de la tabla."); return null; }
        return (String) tableModel.getValueAt(row, 0);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void styleTable(JTable table) {
        table.setBackground(Theme.BG_CARD);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(28);
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

    private JLabel labelOf(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JTextField darkTextField(String text) {
        JTextField f = new JTextField(text, 14);
        f.setBackground(Theme.BG_CARD);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setFont(Theme.FONT_BODY);
        f.setCaretColor(Theme.ACCENT_GREEN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return f;
    }

    private JSpinner darkSpinner(int value, int min, int max) {
        JSpinner s = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        s.setPreferredSize(new Dimension(70, 28));
        JComponent editor = s.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(Theme.BG_CARD);
            de.getTextField().setForeground(Theme.TEXT_PRIMARY);
            de.getTextField().setFont(Theme.FONT_BODY);
        }
        return s;
    }

    private void setStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    private void showError(String msg) {
        setStatus("✗ " + msg, Theme.ACCENT_RED);
    }
}
