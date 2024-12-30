/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.joe.application.data.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joe.gestion.model.data.Player;
import org.joe.gestion.model.data.PlayerList;
import org.joe.gestion.model.persistence.EquipDataInterface;
import org.joe.gestion.model.persistence.EquipDataInterfaceException;

/**
 *
 * @author jonah
 */
public class DataToXML {

    public DataToXML() {

    }

    public static boolean exportAllPlayers(List<Player> players, String pathname) {
        boolean ans = false;
        if (players == null || players.isEmpty()) {
            throw new EquipDataInterfaceException("Passed a null or empty player list.");
        }

        if (pathname == null || pathname.isBlank()) {
            throw new EquipDataInterfaceException("Can't pass a null or empty pathname.");
        }

        File file = new File(pathname);
        try {

            PlayerList playerList = new PlayerList(players);

            JAXBContext jaxbContext = JAXBContext.newInstance(PlayerList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            OutputStream os = new FileOutputStream(file);
            marshaller.marshal(playerList, os);
            ans = true;

        } catch (JAXBException ex) {
            throw new EquipDataInterfaceException("JAXB Exception: " + ex.getMessage(), ex.getCause());
        } catch (FileNotFoundException ex) {
            throw new EquipDataInterfaceException("Error finding file: " + ex.getMessage(), ex.getCause());
        }
        return ans;
    }

    public static void exportPlayer(Player player, String pathname) {
        if (player == null) {
            throw new EquipDataInterfaceException("Passed a null or empty player.");
        }
        if (pathname == null || pathname.isBlank()) {
            throw new EquipDataInterfaceException("Can't pass a null or empty pathname.");
        }
        File file = new File(pathname);
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(PlayerList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            OutputStream os = new FileOutputStream(file);
            marshaller.marshal(player, os);

        } catch (JAXBException ex) {
            throw new EquipDataInterfaceException("JAXB Exception: " + ex.getMessage(), ex.getCause());
        } catch (FileNotFoundException ex) {
            throw new EquipDataInterfaceException("Error finding file: " + ex.getMessage(), ex.getCause());
        }
    }

}
