package fsiAdministration.controllers;

import fsiAdministration.BO.Utilisateur;
import fsiAdministration.DAO.BDDManager;
import fsiAdministration.DAO.UtilisateurDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static java.awt.SystemColor.text;

public class ConnexionController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private TextField tfLogin;
    @FXML
    private TextField tfMDP;
    @FXML
    private Button bConnexion;
    @FXML
    public void bConnexionClick(ActionEvent event) throws IOException {
        String login = tfLogin.getText();
        String mdp = tfMDP.getText();

        UtilisateurDAO userDAO = new UtilisateurDAO();
        Utilisateur user = userDAO.find(login, mdp);

        if (login.isEmpty() || mdp.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        if (user == null) {  // Si aucun utilisateur n'est trouvé
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Login ou Mot de passe incorrect.");
            alert.showAndWait();
        } else {
            // Charger le fichier FXML pour la pop-up
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) bConnexion.getScene().getWindow();

            // Obtenir le contrôleur de la nouvelle fenetre
            AccueilController ac = fxmlLoader.getController();
            ac.setUtilisateur(user);

            // Créer une nouvelle fenêtre (Stage)
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Accueil FSI ADMINISTRATION");

            // Afficher la fenêtre et attendre qu'elle se ferme
            currentStage.show();
        }

    }


}
