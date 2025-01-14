/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.joe.application.views;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import org.joe.application.constants.ManagemetConstants;
import org.joe.application.controllers.PlayerManagementController;
import org.joe.application.controllers.TeamManagementController;
import org.joe.gestion.model.persistence.EquipDataInterface;

/**
 *
 * @author jonah
 */
public class Management extends JFrame {

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu season;
    JMenu dataMenu;
    /*DATA MENU ITEMS*/
    //Xml
    JMenu exportXmlMenu;
    JMenu jugadoresxml;
    JMenu teamxml;
    JMenuItem todjugadores;
    JMenuItem jugador;

    JMenu todequipo;
    JMenuItem equipo;
    JMenuItem temporadaitem;
    JMenuItem categoriaitem;

    //Jasper
    JMenuItem exportJsperMenuItem;

    //CSV
    JMenu exportCSVMenuItem;
    JMenuItem exportJugadoresItem;
    JMenuItem exportarSeasonTeamsItem;

    JMenu aboutMenu;
    JMenu helpMenu;

    JMenuItem contactMenuItem;
    JMenuItem reportBugItem;

    JMenuItem sobreme;

    JMenuItem closeApp;

    JMenuItem createseason;
    JPanel mngmtypePanel;
    JButton playermngmtBTN;
    JButton teammngmtBTN;
    JTabbedPane managementTyTabbedPane;
    JProgressBar progressBar;
    private EquipDataInterface edi;

    public Management(EquipDataInterface edi) {
        this.edi = edi;
        initializeComponents();
    }

    public void initializeComponents() {

        setSize(1280, 720);
        setLayout(null);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("Archivo");
        dataMenu = new JMenu("Data");
        helpMenu = new JMenu("Ayuda");
        aboutMenu = new JMenu("Sobre");
        season = new JMenu("Temporada");
        createseason = new JMenuItem("Crear Temporada");
        playermngmtBTN = new JButton("Gestión De Jugadores");
        teammngmtBTN = new JButton("Gestión De Equipos");
        managementTyTabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        progressBar = new JProgressBar();
        contactMenuItem = new JMenuItem("Contacta Me");
        reportBugItem = new JMenuItem("Informar de un problema");
        sobreme = new JMenuItem("Sobre la aplicación");
        closeApp = new JMenuItem("Tancar la aplicación");

        exportXmlMenu = new JMenu("Exportar Xml");
        jugadoresxml = new JMenu("Jugadores");
        teamxml = new JMenu("Equipos");
        todjugadores = new JMenuItem("Todo Jugadores");
        jugador = new JMenuItem("Jugador");

        todequipo = new JMenu("Todo Equipos");
        equipo = new JMenuItem("Equipo");
        temporadaitem = new JMenuItem("Temporada");
        categoriaitem = new JMenuItem("Categoria");

        exportJugadoresItem = new JMenuItem("Todo Jugadores");
        exportarSeasonTeamsItem = new JMenuItem("Todo Equipos");

        exportJsperMenuItem = new JMenuItem("Exportar Report");
        exportCSVMenuItem = new JMenu("Exportar Csv");
        exportCSVMenuItem.add(exportJugadoresItem);
        exportCSVMenuItem.add(exportarSeasonTeamsItem);

        mngmtypePanel = new JPanel(null);
        mngmtypePanel.setBounds(0, 0, 1280, 48);
        //mngmtypePanel.setBackground(Color.red);
        playermngmtBTN.setBounds(466, 4, 170, 40);
        teammngmtBTN.setBounds(640, 4, 170, 40);
        playermngmtBTN.setIcon(new FlatSVGIcon(ManagemetConstants.sideicon_path + "icon_football.svg", 0.7f));
        playermngmtBTN.putClientProperty(FlatClientProperties.BUTTON_TYPE, "borderless");
        teammngmtBTN.setIcon(new FlatSVGIcon(ManagemetConstants.sideicon_path + "icon_person.svg", 0.7f));
        teammngmtBTN.putClientProperty(FlatClientProperties.BUTTON_TYPE, "borderless");

        progressBar.setValue(0);
        progressBar.setBounds(1090, 665, 160, 10);
        progressBar.setStringPainted(true);

        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.CYAN);
        panel1.add(new JLabel("This is Panel 1"));
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.MAGENTA);
        panel2.add(new JLabel("This is Panel 2"));

        PlayerManagementController pms = new PlayerManagementController(edi);
        TeamManagementController tms = new TeamManagementController(edi);

        managementTyTabbedPane.setBounds(0, 10, 1280, 631);
        managementTyTabbedPane.add("Panel1", pms.getPlayerManagementScreen());
        managementTyTabbedPane.add("Panel2", tms.getTeamManagementScreen());
        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        menuBar.add(season);
        season.add(createseason);
        menuBar.add(helpMenu);
        menuBar.add(aboutMenu);

        dataMenu.add(exportXmlMenu);
        dataMenu.add(exportJsperMenuItem);
        dataMenu.add(exportCSVMenuItem);

        exportXmlMenu.add(jugadoresxml);
        jugadoresxml.add(todjugadores);
        jugadoresxml.add(jugador);

        helpMenu.add(contactMenuItem);
        helpMenu.add(reportBugItem);

        aboutMenu.add(sobreme);

        fileMenu.add(closeApp);

        exportXmlMenu.add(teamxml);
        teamxml.add(todequipo);
        todequipo.add(temporadaitem);
        todequipo.add(categoriaitem);
        teamxml.add(equipo);
        progressBar.setVisible(false);

        mngmtypePanel.add(playermngmtBTN);
        mngmtypePanel.add(teammngmtBTN);

        setJMenuBar(menuBar);
        add(mngmtypePanel);
        add(managementTyTabbedPane);
        add(progressBar);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void TeamManagementBTN_OnClick(ActionListener listener) {
        teammngmtBTN.addActionListener(listener);
    }

    public void PlayerMangementBTN_Onclick(ActionListener listener) {
        playermngmtBTN.addActionListener(listener);
    }

    public void CreateTemp_OnClick(ActionListener listener) {
        createseason.addActionListener(listener);
    }

    public JButton getPlayermngmtBTN() {
        return playermngmtBTN;
    }

    public JButton getTeammngmtBTN() {
        return teammngmtBTN;
    }

    public JTabbedPane getManagementTyTabbedPane() {
        return managementTyTabbedPane;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenu getSeason() {
        return season;
    }

    public JMenu getDataMenu() {
        return dataMenu;
    }

    public JMenu getAboutMenu() {
        return aboutMenu;
    }

    public JMenu getHelpMenu() {
        return helpMenu;
    }

    public JMenuItem getCreateseason() {
        return createseason;
    }

    public JPanel getMngmtypePanel() {
        return mngmtypePanel;
    }

    public EquipDataInterface getEdi() {
        return edi;
    }

    public JMenu getExportXmlMenu() {
        return exportXmlMenu;
    }

    public JMenu getJugadoresxml() {
        return jugadoresxml;
    }

    public JMenu getTeamxml() {
        return teamxml;
    }

    public JMenuItem getTodjugadores() {
        return todjugadores;
    }

    public JMenuItem getJugador() {
        return jugador;
    }

    public JMenu getTodequipo() {
        return todequipo;
    }

    public JMenuItem getEquipo() {
        return equipo;
    }

    public JMenuItem getTemporadaitem() {
        return temporadaitem;
    }

    public JMenuItem getCategoriaitem() {
        return categoriaitem;
    }

    public JMenuItem getExportJsperMenuItem() {
        return exportJsperMenuItem;
    }

    public JMenu getExportCSVMenuItem() {
        return exportCSVMenuItem;
    }

    public void ExportarTodJugadores_OnClicK(ActionListener listener) {
        this.todjugadores.addActionListener(listener);
    }

    public void ExportarJugador_OnClick(ActionListener listener) {
        this.jugador.addActionListener(listener);
    }

    public void ExportarEquipo_OnClick(ActionListener listener) {
        this.equipo.addActionListener(listener);
    }

    public void ExportarEquipoTemp_OnClick(ActionListener listener) {
        this.temporadaitem.addActionListener(listener);
    }

    public void ExportarEquipoCat_OnClick(ActionListener listener) {
        this.categoriaitem.addActionListener(listener);
    }

    public void ExportarEquiposCSV_OnClick(ActionListener listener) {
        this.exportarSeasonTeamsItem.addActionListener(listener);
    }

    public void closeApp_OnClick(ActionListener listener) {
        this.closeApp.addActionListener(listener);
    }

    public void ExportarJugadoresCSV_onClick(ActionListener listener) {
        this.exportJugadoresItem.addActionListener(listener);
    }

    public void ExportarJasper_onClick(ActionListener listener) {
        this.exportJsperMenuItem.addActionListener(listener);
    }

    public void contactMe_OnClick(ActionListener listener) {
        this.contactMenuItem.addActionListener(listener);
    }

    public void reportBug_OnClick(ActionListener listener) {
        this.reportBugItem.addActionListener(listener);
    }

    public JMenuItem getExportJugadoresItem() {
        return exportJugadoresItem;
    }

    public JMenuItem getExportarSeasonTeamsItem() {
        return exportarSeasonTeamsItem;
    }

    public void sobreMe_OnClick(ActionListener listener) {
        this.sobreme.addActionListener(listener);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JMenuItem getContactMenuItem() {
        return contactMenuItem;
    }

    public JMenuItem getReportBugItem() {
        return reportBugItem;
    }

    public JMenuItem getSobreme() {
        return sobreme;
    }

    public JMenuItem getCloseApp() {
        return closeApp;
    }

}
