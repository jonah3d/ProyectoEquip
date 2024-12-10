/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.joe.application.initialize;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.joe.application.controllers.LoginScreenController;
import org.joe.application.views.LoginScreen;
import org.joe.application.views.PlayerManagementScreen;
import org.joe.gestion.model.persistence.EquipDataInterface;

/**
 *
 * @author Usuari
 */
public class Application {
    private LoginScreenController screenController;

    private static String nomClassePersistencia = null;
    private EquipDataInterface dbad = null;

    public Application() {
        // Launch the login screen and connect to the database asynchronously
        screenController = new LoginScreenController(nomClassePersistencia);
        
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (args.length == 0) {
            System.out.println("Please provide the persistence class name as the first argument.");
            System.exit(0);
        }
        nomClassePersistencia = args[0];
     // new Application();
        
     new PlayerManagementScreen();
    }
}

