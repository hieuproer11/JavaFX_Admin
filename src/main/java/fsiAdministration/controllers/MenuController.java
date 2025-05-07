package fsiAdministration.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController {


    @FXML
    protected MenuItem bListeEtud, bAjouterEtud, bListeSection, bAjouterSection, bQuitter, bAccueil;



    @FXML
    public void bQuitterClick(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void bAccueilClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Accueil FSI ADMINISTRATION");

            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bListEtudClick(ActionEvent event) {

        try {

            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_liste_etudiant.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Obtenir le contrôleur de la nouvelle fenetre
            ListeEtudiantController listeEtudiantController = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Liste etudiant");

            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bAjouterEtudClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_ajout_etudiant.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Obtenir le contrôleur de la nouvelle fenetre
            AjouterEtudiantController ajouterEtudiantController = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Accueil FSI ADMINISTRATION");
            currentStage.setScene(new Scene(root));


            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void bListeSectionClick(ActionEvent event) {
        try {

            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_liste_etudiant.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();


            // Obtenir le contrôleur de la nouvelle fenetre
            ListeEtudiantController abc = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Liste etudiant");
            currentStage.setScene(new Scene(root));

            // Configurer la fenêtre en tant que modal
           currentStage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bAjouterSectionClick(ActionEvent event) {}
}
