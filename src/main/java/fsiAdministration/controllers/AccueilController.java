package fsiAdministration.controllers;

import fsiAdministration.BO.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController extends MenuController implements Initializable {

    @FXML private Label lbBienvenue;

    private Utilisateur utilisateur;   // l’utilisateur connecté

    /* --------------------- cycle de vie --------------------- */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // le contrôleur est instancié avant qu’on lui passe l’utilisateur ;
        // on affiche donc le texte plus tard dans setUtilisateur(...)
    }

    /* ------------------ injection depuis Login -------------- */
    /** Appelée par le contrôleur de connexion après authentification */
    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        mettreAJourTexte();
    }

    /* ----------------------- affichage ---------------------- */
    private void mettreAJourTexte() {
        if (utilisateur == null || lbBienvenue == null) return;
        String login = utilisateur.getLoginUtilisateur();
        lbBienvenue.setText("BIENVENUE " + login.toUpperCase());
    }
}
