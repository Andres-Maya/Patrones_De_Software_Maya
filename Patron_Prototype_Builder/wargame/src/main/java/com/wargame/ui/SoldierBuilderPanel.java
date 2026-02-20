package com.wargame.ui;

import com.wargame.enums.*;
import com.wargame.model.Soldier;
import com.wargame.util.AppController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UI panel for configuring and building soldiers using the Builder pattern.
 * Split view: form on the left, saved soldiers table on the right.
 */
public class SoldierBuilderPanel extends JPanel {

    private final AppController controller;
    private final Runnable onSoldierCreated;

    // ─── Form fields ──────────────────────────────────────────────────────────
    private JTextField nameField;
    private JComboBox<Rank> rankCombo;
    private JSpinner healthSpinner;
    private JSpinner armorSpinner;
    private JSpinner speedSpinner;
    private JComboBox<WeaponType> primaryCombo;
    private JComboBox<WeaponType> secondaryCombo;
    private JComboBox<HelmetType> helmetCombo;
    private JList<SkillType> skillList;
    private JTextField factionField;
    private JCheckBox eliteCheck;
    private JTextField protoKeyField;
    private JLabel statusLabel;

    // ─── Saved soldiers table ─────────────────────────────────────────────────
    private JTable savedTable;
    private DefaultTableModel savedModel;

    public SoldierBuilderPanel(AppController controller, Runnable onSoldierCreated) {
        this.controller = controller;
        this.onSoldierCreated = onSoldierCreated;
        buildUi();
    }

    private void buildUi() {
        setBackground(Theme.BG_PANEL);
        setLayout(new BorderLayout(0, 0));

        // Header
        JLabel header = new JLabel("  ⚙  CONSTRUCTOR DE SOLDADO", JLabel.LEFT);
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.ACCENT_AMBER);
        header.setBackground(Theme.BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        add(header, BorderLayout.NORTH);

        // Split pane: form left | table right
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildFormPanel(), buildSavedPanel());
        split.setDividerLocation(420);
        split.setDividerSize(4);
        split.setBackground(Theme.BG_PANEL);
        add(split, BorderLayout.CENTER);

        // Status bar at the bottom
        statusLabel = new JLabel("  Completa el formulario y pulsa GUARDAR SOLDADO.");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.TEXT_SECONDARY);
        statusLabel.setBackground(Theme.BG_HEADER);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        add(statusLabel, BorderLayout.SOUTH);
    }

    // ─── Left: Builder Form ───────────────────────────────────────────────────

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG_PANEL);

        JLabel sectionLabel = new JLabel("  CONFIGURAR SOLDADO");
        sectionLabel.setFont(Theme.FONT_HEAD);
        sectionLabel.setForeground(Theme.ACCENT_AMBER);
        sectionLabel.setBackground(Theme.BG_CARD);
        sectionLabel.setOpaque(true);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        wrapper.add(sectionLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BG_PANEL);
        formPanel.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        GridBagConstraints lc = labelConstraints();
        GridBagConstraints fc = fieldConstraints();
        int row = 0;

        // Name
        addLabel(formPanel, "Nombre:", lc, row);
        nameField = styledTextField("Alpha-One");
        fc.gridy = row++; formPanel.add(nameField, fc);

        // Rank
        addLabel(formPanel, "Rango:", lc, row);
        rankCombo = styledCombo(Rank.values());
        fc.gridy = row++; formPanel.add(rankCombo, fc);

        // Stats
        addLabel(formPanel, "Vida / Armadura / Velocidad:", lc, row);
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        statsRow.setBackground(Theme.BG_PANEL);
        healthSpinner = styledSpinner(100, 1, 500);
        armorSpinner  = styledSpinner(50,  0, 200);
        speedSpinner  = styledSpinner(60,  1, 150);
        statsRow.add(healthSpinner);
        statsRow.add(separator());
        statsRow.add(armorSpinner);
        statsRow.add(separator());
        statsRow.add(speedSpinner);
        fc.gridy = row++; formPanel.add(statsRow, fc);

        // Primary weapon
        addLabel(formPanel, "Arma Principal:", lc, row);
        primaryCombo = styledCombo(WeaponType.values());
        fc.gridy = row++; formPanel.add(primaryCombo, fc);

        // Secondary weapon
        addLabel(formPanel, "Arma Secundaria:", lc, row);
        secondaryCombo = styledCombo(WeaponType.values());
        secondaryCombo.setSelectedItem(WeaponType.PISTOL);
        fc.gridy = row++; formPanel.add(secondaryCombo, fc);

        // Helmet
        addLabel(formPanel, "Casco:", lc, row);
        helmetCombo = styledCombo(HelmetType.values());
        fc.gridy = row++; formPanel.add(helmetCombo, fc);

        // Skills
        addLabel(formPanel, "Habilidades (Ctrl+Clic):", lc, row);
        skillList = new JList<>(SkillType.values());
        skillList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        skillList.setBackground(Theme.BG_CARD);
        skillList.setForeground(Theme.TEXT_PRIMARY);
        skillList.setFont(Theme.FONT_SMALL);
        skillList.setSelectionBackground(new Color(20, 80, 160));
        skillList.setSelectionForeground(Color.WHITE);
        JScrollPane skillScroll = new JScrollPane(skillList);
        skillScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        skillScroll.setPreferredSize(new Dimension(240, 95));
        skillScroll.getViewport().setBackground(Theme.BG_CARD);
        fc.gridy = row++; formPanel.add(skillScroll, fc);

        // Faction
        addLabel(formPanel, "Facción:", lc, row);
        factionField = styledTextField("Escuadrón Fantasma");
        fc.gridy = row++; formPanel.add(factionField, fc);

        // Elite
        addLabel(formPanel, "Élite:", lc, row);
        eliteCheck = new JCheckBox("Marcar como Unidad Élite");
        eliteCheck.setBackground(Theme.BG_PANEL);
        eliteCheck.setForeground(Theme.ACCENT_AMBER);
        eliteCheck.setFont(Theme.FONT_BODY);
        fc.gridy = row++; formPanel.add(eliteCheck, fc);

        // Prototype key
        addLabel(formPanel, "Guardar como Prototipo:", lc, row);
        protoKeyField = styledTextField("");
        protoKeyField.setToolTipText("Opcional: nombre para guardar como plantilla reutilizable");
        fc.gridy = row++; formPanel.add(protoKeyField, fc);

        // Vertical spacer
        GridBagConstraints sc = new GridBagConstraints();
        sc.gridy = row; sc.gridx = 0; sc.gridwidth = 2;
        sc.weighty = 1.0; sc.fill = GridBagConstraints.VERTICAL;
        formPanel.add(Box.createVerticalGlue(), sc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.BG_PANEL);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        // Save button
        StyledButton saveBtn = new StyledButton("💾  GUARDAR SOLDADO", StyledButton.Style.SUCCESS);
        saveBtn.addActionListener(e -> saveSoldier());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnPanel.setBackground(Theme.BG_PANEL);
        btnPanel.add(saveBtn);
        wrapper.add(btnPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    // ─── Right: Saved Soldiers Table ──────────────────────────────────────────

    private JPanel buildSavedPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Theme.BG_PANEL);

        JLabel sectionLabel = new JLabel("  SOLDADOS GUARDADOS");
        sectionLabel.setFont(Theme.FONT_HEAD);
        sectionLabel.setForeground(Theme.ACCENT_GREEN);
        sectionLabel.setBackground(Theme.BG_CARD);
        sectionLabel.setOpaque(true);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        panel.add(sectionLabel, BorderLayout.NORTH);

        String[] cols = {"ID", "Nombre", "Rango", "Facción", "VP", "ARM", "VEL", "Arma", "Habilidades", "Élite", "Poder"};
        savedModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        savedTable = new JTable(savedModel);
        styleTable(savedTable);

        JScrollPane scroll = new JScrollPane(savedTable);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        scroll.getViewport().setBackground(Theme.BG_CARD);
        panel.add(scroll, BorderLayout.CENTER);

        // Action buttons below table
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actionsPanel.setBackground(Theme.BG_PANEL);

        StyledButton saveProtoBtn = new StyledButton("🧬 GUARDAR COMO PROTOTIPO", StyledButton.Style.PRIMARY);
        saveProtoBtn.addActionListener(e -> saveSelectedAsPrototype());
        actionsPanel.add(saveProtoBtn);

        StyledButton deleteBtn = new StyledButton("🗑 ELIMINAR", StyledButton.Style.DANGER);
        deleteBtn.addActionListener(e -> deleteSelected());
        actionsPanel.add(deleteBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Actions ──────────────────────────────────────────────────────────────

    private void saveSoldier() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { showError("El nombre del soldado no puede estar vacío."); return; }

            List<SkillType> skills = skillList.getSelectedValuesList();
            Soldier soldier = controller.buildSoldier(
                    name,
                    (Rank) rankCombo.getSelectedItem(),
                    (int) healthSpinner.getValue(),
                    (int) armorSpinner.getValue(),
                    (int) speedSpinner.getValue(),
                    (WeaponType) primaryCombo.getSelectedItem(),
                    (WeaponType) secondaryCombo.getSelectedItem(),
                    (HelmetType) helmetCombo.getSelectedItem(),
                    new ArrayList<>(skills),
                    factionField.getText().trim(),
                    eliteCheck.isSelected()
            );

            // Always persist to the soldiers list
            controller.saveSoldier(soldier);

            // Optionally also register as prototype
            String protoKey = protoKeyField.getText().trim();
            if (!protoKey.isEmpty()) {
                controller.registerAsPrototype(protoKey, soldier);
                showSuccess("✓ Soldado [" + soldier.getId() + "] guardado y registrado como prototipo: \"" + protoKey + "\"");
            } else {
                showSuccess("✓ Soldado [" + soldier.getId() + "] guardado — PODER: " + soldier.calculateCombatPower());
            }

            refreshTable();
            onSoldierCreated.run();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void saveSelectedAsPrototype() {
        int row = savedTable.getSelectedRow();
        if (row < 0) { showError("Selecciona un soldado de la tabla primero."); return; }

        Soldier soldier = controller.getSavedSoldiers().get(row);
        String key = JOptionPane.showInputDialog(this,
                "Escribe un nombre para el prototipo:", soldier.getName());
        if (key == null || key.isBlank()) return;

        controller.registerAsPrototype(key.trim(), soldier);
        showSuccess("✓ Prototipo \"" + key.trim() + "\" guardado en el registro.");
        onSoldierCreated.run();
    }

    private void deleteSelected() {
        int row = savedTable.getSelectedRow();
        if (row < 0) { showError("Selecciona un soldado para eliminar."); return; }

        Soldier soldier = controller.getSavedSoldiers().get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al soldado \"" + soldier.getName() + "\" [" + soldier.getId() + "]?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.removeSoldier(soldier);
            refreshTable();
            showSuccess("✓ Soldado eliminado correctamente.");
            onSoldierCreated.run();
        }
    }

    // ─── Table refresh ────────────────────────────────────────────────────────

    public void refreshTable() {
        savedModel.setRowCount(0);
        for (Soldier s : controller.getSavedSoldiers()) {
            savedModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getRank() != null ? s.getRank().getTitle() : "-",
                    s.getFaction(),
                    s.getHealth(),
                    s.getArmor(),
                    s.getSpeed(),
                    s.getPrimaryWeapon() != null ? s.getPrimaryWeapon().getDisplayName() : "-",
                    s.getSkills().size() + " hab.",
                    s.isElite() ? "★" : "—",
                    s.calculateCombatPower()
            });
        }
    }

    // ─── UI Helpers ───────────────────────────────────────────────────────────

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

    private void addLabel(JPanel panel, String text, GridBagConstraints lc, int row) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_LABEL);
        label.setForeground(Theme.TEXT_SECONDARY);
        lc.gridy = row;
        panel.add(label, lc);
    }

    private <T> JComboBox<T> styledCombo(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setBackground(Theme.BG_CARD);
        combo.setForeground(Theme.TEXT_PRIMARY);
        combo.setFont(Theme.FONT_BODY);
        combo.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return combo;
    }

    private JTextField styledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder, 20);
        field.setBackground(Theme.BG_CARD);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setFont(Theme.FONT_BODY);
        field.setCaretColor(Theme.ACCENT_GREEN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return field;
    }

    private JSpinner styledSpinner(int value, int min, int max) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 5));
        spinner.setPreferredSize(new Dimension(75, 28));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(Theme.BG_CARD);
            de.getTextField().setForeground(Theme.TEXT_PRIMARY);
            de.getTextField().setFont(Theme.FONT_BODY);
            de.getTextField().setCaretColor(Theme.ACCENT_GREEN);
        }
        return spinner;
    }

    private JLabel separator() {
        JLabel sep = new JLabel("  /  ");
        sep.setForeground(Theme.TEXT_MUTED);
        sep.setFont(Theme.FONT_BODY);
        return sep;
    }

    private GridBagConstraints labelConstraints() {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0;
        lc.anchor = GridBagConstraints.NORTHWEST;
        lc.insets = new Insets(8, 0, 0, 12);
        lc.fill = GridBagConstraints.NONE;
        return lc;
    }

    private GridBagConstraints fieldConstraints() {
        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 1;
        fc.anchor = GridBagConstraints.NORTHWEST;
        fc.insets = new Insets(6, 0, 0, 0);
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        return fc;
    }

    private void showSuccess(String msg) {
        statusLabel.setForeground(Theme.ACCENT_GREEN);
        statusLabel.setText("  " + msg);
    }

    private void showError(String msg) {
        statusLabel.setForeground(Theme.ACCENT_RED);
        statusLabel.setText("  ✗ Error: " + msg);
    }
}
