package fsiAdministration.controllers;

import fsiAdministration.BO.Utilisateur;
import fsiAdministration.DAO.BDDManager;
import fsiAdministration.DAO.UtilisateurDAO;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.awt.SystemColor.text;

public class ConnexionController implements Initializable {

    /* ----------------------------------------------------------------- */
    /*  Constantes de sécurité                                           */
    /* ----------------------------------------------------------------- */
    private static final int   MAX_TENTATIVES      = 3;               // essais
    private static final long  DURATION_VERROUILLEE  = 5 * 60_000L;     // 5 min

    private static final Map<String, AttemptInfo> attemptsMap = new HashMap<>();

    private record AttemptInfo(int tries, long unlockAt) { }
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




        if (login.isEmpty() || mdp.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Vérifie un éventuel verrouillage
        AttemptInfo info = attemptsMap.get(login);
        long now = System.currentTimeMillis();
        if (info != null && info.unlockAt > now) {
            long restant = (info.unlockAt - now) / 1000;
            show(Alert.AlertType.ERROR,
                    "Compte verrouillé. Réessayez dans " + restant + " s.");
            disableForm(true, info.unlockAt - now);   // grise les champs
            return;
        }


        /* ------ Authentification en BD ------------------------------ */
        UtilisateurDAO dao = new UtilisateurDAO();
        Utilisateur user  = dao.find(login, mdp);

        if (user == null) {  // Si aucun utilisateur n'est trouvé
            enregistrerEchec(login, now);
            show(Alert.AlertType.ERROR, "Login ou mot de passe incorrect.");
            return;
        }

        // Succès : on efface les données d’échec
        attemptsMap.remove(login);
        openAccueil(event, user);
    }

    private void enregistrerEchec(String login, long now) {
        AttemptInfo info = attemptsMap.getOrDefault(login, new AttemptInfo(0, 0));
        int tentatives = info.tries + 1;
        long unlock = info.unlockAt;
        if (tentatives >= MAX_TENTATIVES) {
            tentatives   = 0;                          // on remet à zéro le compteur
            unlock  = now + DURATION_VERROUILLEE;     // date de déblocage
            show(Alert.AlertType.WARNING, "Trop de tentatives. Veuillez reessayer dans 5 minutes.");
            disableForm(true, DURATION_VERROUILLEE);
        }
        attemptsMap.put(login, new AttemptInfo(tentatives, unlock));
    }

    private void openAccueil(ActionEvent evt, Utilisateur user) throws IOException {
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

    /* ---------------- active / désactive les champs ------- */
    private void disableForm(boolean lock, long durationMs) {
        tfLogin.setDisable(lock);
        tfMDP.setDisable(lock);
        bConnexion.setDisable(lock);

        if (lock) {
            PauseTransition pt = new PauseTransition(Duration.millis(durationMs));
            pt.setOnFinished(e -> {
                tfLogin.setDisable(false);
                tfMDP.setDisable(false);
                bConnexion.setDisable(false);
            });
            pt.play();
        }
    }

    private void show(Alert.AlertType t, String msg) {
        new Alert(t, msg).showAndWait();
    }

}
