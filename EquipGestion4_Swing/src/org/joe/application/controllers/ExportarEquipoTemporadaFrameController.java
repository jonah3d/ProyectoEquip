/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.joe.application.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.joe.application.constants.ErrMsg;
import org.joe.application.data.xml.DataToXML;
import org.joe.application.views.ExportarEquipoTemporadaFrame;
import org.joe.gestion.model.data.Player;
import org.joe.gestion.model.data.Team;
import org.joe.gestion.model.persistence.EquipDataInterface;
import org.joe.gestion.model.persistence.EquipDataInterfaceException;

/**
 *
 * @author jonah
 */
public class ExportarEquipoTemporadaFrameController implements ActionListener {

    private ExportarEquipoTemporadaFrame eetf;
    private EquipDataInterface edi;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    List<Team> teams = null;
    Team team = null;
    List<Player> players = null;

    public ExportarEquipoTemporadaFrameController(EquipDataInterface edi) {
        this.edi = edi;
        eetf = new ExportarEquipoTemporadaFrame();
        eetf.setVisible(true);

        eetf.browsDir_onClick(this);
        eetf.exportarJug_OnClick(this);
        eetf.searhJugador_OnClick(this);
        eetf.getAddPlayers().setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == eetf.getSearchButton()) {
            String dateString = eetf.getDatePicker().getSelectedDateAsString();
            try {
                Date date = sdf.parse(dateString);

                teams = edi.getTeamsBySeason(date);
                JOptionPane.showMessageDialog(null,
                        "Equipos Encontrado",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ErrMsg.error(ex.getMessage(), ex.getCause());
            }
        }

        if (e.getSource() == eetf.getBrowseButton()) {
            int returnValue = eetf.getFilepathChooser().showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String selectedPath = eetf.getFilepathChooser().getSelectedFile().getPath();
                String fullPath = selectedPath + "\\" + eetf.getFilenameTF().getText();
                eetf.getFilepathTF().setText(fullPath);
            }
        }

        if (e.getSource() == eetf.getExportButton()) {
            String fileString = eetf.getFilepathTF().getText();
            try {

//                if (eetf.getAddPlayers().isSelected()) {
//                    try {
//                        if (teams == null || teams.isEmpty()) {
//                            JOptionPane.showMessageDialog(null, "No teams found for the given season.", "Error", JOptionPane.ERROR_MESSAGE);
//                            return;
//                        }
//
//                        String dateString = eetf.getDatePicker().getSelectedDateAsString();
//                        Date date = sdf.parse(dateString);
//                        java.sql.Date oracldate = new java.sql.Date(date.getTime());
//
//                        if (players == null) {
//                            players = new ArrayList<>();
//                        } else {
//                            players.clear();
//                        }
//
//                        for (Team team : teams) {
//                            List<Player> teamPlayers = edi.getTeamPlayers(team.getName().trim(), oracldate);
//                            if (teamPlayers != null) {
//                                teamPlayers.forEach(Player::mostrarJugDetalle);
//                                players.addAll(teamPlayers);
//                            }
//                        }
//
//                        if (players.isEmpty()) {
//                            JOptionPane.showMessageDialog(null, "No players found for the selected teams.", "Info", JOptionPane.INFORMATION_MESSAGE);
//                        } else {
//                            DataToXML.exportSeasonTeamWithPlayers(teams, players, fileString);
//                            JOptionPane.showMessageDialog(null, "Teams with players exported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                        }
//                    } catch (Exception ex) {
//                        ErrMsg.error(ex.getMessage(), ex.getCause());
//                    }
//                } else if (!eetf.getAddPlayers().isSelected()) {
                DataToXML.exportSeasonTeam(teams, fileString);
                JOptionPane.showMessageDialog(null,
                        "Equipos Exportado Existosamente",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ErrMsg.error(ex.getMessage(), ex.getCause());
            }

        }
    }

}