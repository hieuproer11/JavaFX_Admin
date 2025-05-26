package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.EtudiantDAO;
import fsiAdministration.DAO.SectionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller « Modifier étudiant » : le ComboBox des sections est *toujours à jour*.
 */
public class ModifierEtudiantController extends MenuController implements Initializable {

    /* ---------------- Composants FXML ---------------- */
    @FXML private TextField        tfNomEtud;
    @FXML private TextField        tfPrenomEtud;
    @FXML private DatePicker       dpDateNaiss;
    @FXML private ComboBox<Section> cbSection;
    @FXML private Button           bSauvegarder;
    @FXML private Button           bRetour;

    /* ---------------- Données ------------------------ */
    private final SectionDAO sectionDAO = new SectionDAO();
    private final ObservableList<Section> sections = FXCollections.observableArrayList();
    private Etudiant etudiantCourant;

    /* ---------------- Initialisation ----------------- */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // affiche uniquement le libellé dans la combo
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

    /** Recharge la liste des sections depuis la base de données. */
    private void refreshSections() {
        sections.setAll(sectionDAO.findAll());
        cbSection.setItems(sections);
    }

    /* -------------- Injection étudiant --------------- */
    public void setEtudiant(Etudiant etu) {
        this.etudiantCourant = etu;
        if (etu == null) return;

        tfNomEtud.setText(etu.getNomEtudiant());
        tfPrenomEtud.setText(etu.getPrenomEtudiant());
        dpDateNaiss.setValue(etu.getDateNaissance());
        refreshSections();                     // s'assurer que la section existe dans la liste
        cbSection.setValue(etu.getSection());  // présélection
    }

    /* ------------------- Actions --------------------- */
    @FXML
    private void bSauvegarderClick(ActionEvent event) {
        if (etudiantCourant == null) {
            alert(Alert.AlertType.ERROR, "Aucun étudiant sélectionné.");
            return;
        }

        String nom   = tfNomEtud.getText().trim();
        String prenom= tfPrenomEtud.getText().trim();
        LocalDate date= dpDateNaiss.getValue();
        Section sec  = cbSection.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || date == null || sec == null) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return;
        }

        etudiantCourant.setNomEtudiant(nom);
        etudiantCourant.setPrenomEtudiant(prenom);
        etudiantCourant.setDateNaissance(date);
        etudiantCourant.setSection(sec);

        boolean ok = new EtudiantDAO().update(etudiantCourant);
        alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                ok ? "Modifications enregistrées !" : "Erreur lors de la mise à jour.");
        if (ok) closeWindow();
    }

    @FXML private void bRetourClick(ActionEvent e) { closeWindow(); }

    /* ---------------- Utilitaires -------------------- */
    private void alert(Alert.AlertType t, String msg) {
        new Alert(t, msg, ButtonType.OK).showAndWait();
    }
    private void closeWindow() { ((Stage) bRetour.getScene().getWindow()).close(); }
}
