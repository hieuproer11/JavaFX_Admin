package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.EtudiantDAO;
import fsiAdministration.DAO.SectionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller « Ajouter étudiant » avec ComboBox&lt;Section&gt; dynamique :
 * la liste se recharge depuis la base à chaque ouverture, donc toute nouvelle
 * section créée apparaît immédiatement.
 */
public class AjouterEtudiantController extends MenuController implements Initializable {

    /* ---------------- Composants FXML ---------------- */
    @FXML private TextField        tfNomEtud;
    @FXML private TextField        tfPrenomEtud;
    @FXML private DatePicker       dpDateNaiss;
    @FXML private ComboBox<Section> cbSection;
    @FXML private Button           bEnregistrer;
    @FXML private Button           bRetour;

    /* ---------------- Données ------------------------ */
    private final SectionDAO sectionDAO = new SectionDAO();
    private final ObservableList<Section> sections = FXCollections.observableArrayList();

    /* ---------------- Initialisation ----------------- */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSection.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Section s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "" : s.getLibelleSection());
            }
        });
        cbSection.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Section s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "" : s.getLibelleSection());
            }
        });
        refreshSections();
    }

    /** Recharge la ComboBox depuis la base. */
    private void refreshSections() {
        sections.setAll(sectionDAO.findAll());
        cbSection.setItems(sections);
    }

    /* ------------------- Actions --------------------- */
    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String     nom   = tfNomEtud.getText().trim();
        String     prenom= tfPrenomEtud.getText().trim();
        LocalDate  date  = dpDateNaiss.getValue();
        Section    sec   = cbSection.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || date == null || sec == null) {
            show(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return;
        }

        Etudiant etu = new Etudiant(0, nom, prenom, date, sec);
        boolean ok   = new EtudiantDAO().create(etu);
        show(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                ok ? "L'étudiant a ete ajoute avec succes !" : "Echec de l'ajout de l'etudiant.");

        if (ok) clearForm();
    }

    @FXML private void bRetourClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fsiAdministration/views/page_accueil.fxml"));
            Stage  st   = (Stage) bRetour.getScene().getWindow();
            st.setScene(new Scene(root));
            st.setTitle("Accueil FSI ADMINISTRATION");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la page d'accueil.").show();
        }
    }

    /* ---------------- Utilitaires -------------------- */
    private void clearForm() {
        tfNomEtud.clear();
        tfPrenomEtud.clear();
        dpDateNaiss.setValue(null);
        cbSection.getSelectionModel().clearSelection();
    }
    private void show(Alert.AlertType t, String msg) {
        new Alert(t, msg, ButtonType.OK).showAndWait();
    }
}
