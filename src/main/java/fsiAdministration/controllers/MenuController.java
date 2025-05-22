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
    protected MenuItem bListeEtud, bAjouterEtud, bListeSection, bAjouterSection, bListeCours, bAjouterCours, bQuitter, bAccueil;



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
            currentStage.setTitle("Ajouter un Etudiant");
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_liste_section.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();


            // Obtenir le contrôleur de la nouvelle fenetre
            ListeSectionController lsc = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Liste section");
            currentStage.setScene(new Scene(root));

            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bAjouterSectionClick(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_ajout_section.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();


            // Obtenir le contrôleur de la nouvelle fenetre
            AjouterSectionController asc = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Ajouter une section");
            currentStage.setScene(new Scene(root));


            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void bListeCoursClick(ActionEvent event){
        try {
            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_liste_cours.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();


            // Obtenir le contrôleur de la nouvelle fenetre
            ListeCoursController lcc = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Liste Cours");
            currentStage.setScene(new Scene(root));


            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bAjouterCoursClick(ActionEvent event){
        try {
            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_ajout_cours.fxml"));
            Parent root = fxmlLoader.load();

            // Récupère le Stage actuel depuis l'événement
            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();


            // Obtenir le contrôleur de la nouvelle fenetre
            AjouterCoursController acc = fxmlLoader.getController();

            // Remplacer la scène actuelle par la nouvelle
            currentStage.setTitle("Ajouter un cours");
            currentStage.setScene(new Scene(root));


            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
