package fsiAdministration.controllers;

import fsiAdministration.BO.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController extends MenuController implements Initializable {

    /* ---------- FXML ---------- */
    @FXML private Label lbBienvenue;   // relie le Label du FXML
    private Utilisateur util;

    /** Appelé automatiquement après injection des champs FXML */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // si le nom d’utilisateur a déjà été passé, on l’affiche
        if (util != null) mettreAJourTexte();
    }

    /* ---------- API publique ---------- */
    /** Injecte le nom de l'utilisateur (depuis l'écran de login, par ex.) */


    /* ---------- Utilitaire interne ---------- */
    private void mettreAJourTexte() {
        util = new Utilisateur();
        lbBienvenue.setText("BIENVENUE " + util.getLoginUtilisateur().toUpperCase());
    }
}
