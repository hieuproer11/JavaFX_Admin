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
 * Controller chargé de l'ajout d'un étudiant.
 * <p>
 *  ◦ Conserve un <strong>ListView&lt;String&gt;</strong> pour l'interface, plus simple à l'usage côté FXML.
 *  ◦ Ajoute un {@link DatePicker} pour la date de naissance (champ obligatoire du modèle).
 *  ◦ Valide proprement les champs et affiche une alerte adaptée.
 *  ◦ Retourne facilement à la page d'accueil via le bouton "Retour".
 */
public class AjouterEtudiantController extends MenuController implements Initializable {

    /* ---------------------------------------------------------------------- */
    /*                         Composants FXML                                */
    /* ---------------------------------------------------------------------- */
    @FXML private TextField    tfNomEtud;
    @FXML private TextField    tfPrenomEtud;
    @FXML private DatePicker   dpDateNaiss;
    @FXML private Button       bRetour;
    @FXML private ComboBox<String> cbSection;
    private final ObservableList<String> sections = FXCollections.observableArrayList(
            "Licence developpeur et infrastructure",
            "Management de projet"
    );
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSection.setItems(sections);
    }

    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String nom       = tfNomEtud.getText().trim();
        String prenom    = tfPrenomEtud.getText().trim();
        LocalDate dateNaiss = dpDateNaiss.getValue();
        String selLabel  = cbSection.getSelectionModel().getSelectedItem();

        // Validation
        if (nom.isEmpty() || prenom.isEmpty() || dateNaiss == null || selLabel == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Veuillez remplir tous les champs et selectionner une section.",
                    ButtonType.OK).showAndWait();
            return;
        }


        Section section;
        switch (selLabel) {
            case "Licence developpeur et infrastructure" -> section = new Section(1, selLabel);
            case "Management de projet" -> section = new Section(2, selLabel);
            default -> {
                new Alert(Alert.AlertType.ERROR, "Section inconnue.").showAndWait();
                return;
            }
        }

        Etudiant etu = new Etudiant(0, nom, prenom, dateNaiss, section);
        boolean ok   = new EtudiantDAO().create(etu);

        Alert.AlertType type = ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        String message       = ok ? "L'etudiant a ete ajoute avec succes !"
                : "Echec de l'ajout de l'etudiant.";
        new Alert(type, message, ButtonType.OK).showAndWait();

        if (ok) clearForm();
    }

    private void clearForm() {
        tfNomEtud.clear();
        tfPrenomEtud.clear();
        dpDateNaiss.setValue(null);
        cbSection.getSelectionModel().clearSelection();
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
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

}
