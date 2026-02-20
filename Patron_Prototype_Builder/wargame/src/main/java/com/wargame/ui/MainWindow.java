package com.wargame.ui;

import com.wargame.util.AppController;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * Main application window — a tabbed JFrame that hosts all panels.
 */
public class MainWindow extends JFrame {

    private final AppController controller;

    private DashboardPanel dashboardPanel;
    private SoldierBuilderPanel builderPanel;
    private PrototypeRegistryPanel registryPanel;
    private BattalionOverviewPanel battalionPanel;

    public MainWindow() {
        this.controller = new AppController();
        initWindow();
        buildUi();
        refreshAll();
    }

    private void initWindow() {
        setTitle("⚔  GUERRA — Sistema de Mando de Soldados  |  Builder + Prototype");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 780);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    private void buildUi() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(Theme.BG_DARK);
        tabs.setForeground(Theme.TEXT_PRIMARY);
        tabs.setFont(Theme.FONT_LABEL);
        tabs.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Override tab colors
        tabs.setUI(new BasicTabbedPaneUI() {
            @Override protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                                        int x, int y, int w, int h, boolean selected) {
                g.setColor(selected ? Theme.BG_CARD : Theme.BG_DARK);
                g.fillRect(x, y, w, h);
            }
            @Override protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                                    int x, int y, int w, int h, boolean selected) {
                if (selected) {
                    g.setColor(Theme.ACCENT_AMBER);
                    g.fillRect(x, y + h - 3, w, 3);
                }
            }
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
        });

        dashboardPanel  = new DashboardPanel(controller);
        builderPanel    = new SoldierBuilderPanel(controller, this::refreshAll);
        registryPanel   = new PrototypeRegistryPanel(controller, this::refreshAll);
        battalionPanel  = new BattalionOverviewPanel(controller);

        tabs.addTab("  ☠  Panel de Control  ",   dashboardPanel);
        tabs.addTab("  ⚙  Constructor de Soldado  ", builderPanel);
        tabs.addTab("  🧬  Registro de Prototipos  ", registryPanel);
        tabs.addTab("  🛡  Batallones  ",   battalionPanel);

        setContentPane(tabs);
    }

    private void refreshAll() {
        dashboardPanel.refresh();
        registryPanel.refresh();
        battalionPanel.refresh();
    }
}
