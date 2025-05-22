package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.EtudiantDAO;
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
 * Controller « Modifier étudiant ».
 * <p>
 * 1. Affiche dans le formulaire les données de l'étudiant à modifier.<br>
 * 2. Permet de les éditer puis de les sauvegarder via {@link EtudiantDAO#update(fsiAdministration.BO.Etudiant)}.<br>
 * 3. Affiche un message de succès / erreur et ferme la fenêtre (ou revient) si tout est OK.
 */
public class ModifierEtudiantController extends MenuController implements Initializable {

    /* ---------------------------------------------------------------------- */
    /*                       Composants FXML                                  */
    /* ---------------------------------------------------------------------- */
    @FXML private TextField   tfNomEtud;
    @FXML private TextField   tfPrenomEtud;
    @FXML private DatePicker  dpDateNaiss;
    @FXML private ComboBox<String> cbSection;
    @FXML private Button      bSauvegarder;
    @FXML private Button      bRetour;

    /* ---------------------------------------------------------------------- */
    /*                             Données                                     */
    /* ---------------------------------------------------------------------- */
    private final ObservableList<String> sections = FXCollections.observableArrayList(
            "Licence developpeur et infrastructure",
                    "Management de projet"
    );

    /** Étudiant actuellement en cours d'édition */
    private Etudiant etudiantCourant;

    /* ---------------------------------------------------------------------- */
    /*                       Initialisation                                    */
    /* ---------------------------------------------------------------------- */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSection.setItems(sections);
    }

    /* ---------------------------------------------------------------------- */
    /*                 Injection depuis le contrôleur appelant                 */
    /* ---------------------------------------------------------------------- */
    public void setEtudiant(Etudiant etu) {
        this.etudiantCourant = etu;
        if (etu == null) return;

        tfNomEtud.setText(etu.getNomEtudiant());
        tfPrenomEtud.setText(etu.getPrenomEtudiant());
        dpDateNaiss.setValue(etu.getDateNaissance());
        cbSection.setValue(etu.getSection().getLibelleSection());
    }

    /* ---------------------------------------------------------------------- */
    /*                             Actions UI                                 */
    /* ---------------------------------------------------------------------- */
    @FXML
    private void bSauvegarderClick(ActionEvent event) {
        if (etudiantCourant == null) {
            new Alert(Alert.AlertType.ERROR, "Aucun etudiant selectionne.").showAndWait();
            return;
        }

        String    nom    = tfNomEtud.getText().trim();
        String    prenom = tfPrenomEtud.getText().trim();
        LocalDate date   = dpDateNaiss.getValue();
        String    label  = cbSection.getValue();

        // Validation minimale
        if (nom.isEmpty() || prenom.isEmpty() || date == null || label == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.").showAndWait();
            return;
        }

        // Libellé → Section
        Section section = switch (label) {
            case "Licence developpeur et infrastructure" -> new Section(1, label);
            case "Management de projet"                 -> new Section(2, label);
            default -> null;
        };
        if (section == null) {
            new Alert(Alert.AlertType.ERROR, "Section inconnue").showAndWait();
            return;
        }

        // MAJ de l'objet puis persistance
        etudiantCourant.setNomEtudiant(nom);
        etudiantCourant.setPrenomEtudiant(prenom);
        etudiantCourant.setDateNaissance(date);
        etudiantCourant.setSection(section);

        boolean ok = new EtudiantDAO().update(etudiantCourant);
        Alert.AlertType t = ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        String msg        = ok ? "Modifications enregistrees !" : "Erreur lors de la mise a jour.";
        new Alert(t, msg, ButtonType.OK).showAndWait();

        if (ok) closeWindow();
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        closeWindow();
    }


    private void closeWindow() {
        Stage st = (Stage) bRetour.getScene().getWindow();
        st.close();
    }
}
