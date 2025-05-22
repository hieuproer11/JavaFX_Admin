package fsiAdministration.controllers;

import fsiAdministration.BO.Cours;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.CoursDAO;
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
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la vue page_ajout_cours.fxml.
 */
public class AjouterCoursController extends MenuController implements Initializable {

    /* ------------------------- FXML champs --------------------------- */
    @FXML private TextField tfLibelle;
    @FXML private TextField tfDescription;
    @FXML private ComboBox<Section> cbSection;
    @FXML private Button    bEnregistrer;
    @FXML private Button    bRetour;

    /* ------------------------- DAO & données ------------------------- */
    private final SectionDAO sectionDAO = new SectionDAO();
    private final CoursDAO   coursDAO   = new CoursDAO();
    private ObservableList<Section> sections;

    /* ------------------------- Initialisation ------------------------ */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sections = FXCollections.observableArrayList(sectionDAO.findAll());
        cbSection.setItems(sections);

        // affiche le libellé plutôt que toString()
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
    }

    /* --------------------------- Actions ----------------------------- */
    @FXML
    private void bEnregistrerClick(ActionEvent e) {
        String lib  = tfLibelle.getText().trim();
        String desc = tfDescription.getText().trim();
        Section sec = cbSection.getValue();

        if (lib.isEmpty() || desc.isEmpty() || sec == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.").showAndWait();
            return;
        }

        Cours c = new Cours(0, lib, desc, sec);
        boolean ok = coursDAO.create(c);

        Alert.AlertType type = ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        String msg = ok ? "Cours ajoute !" : "Erreur à l'ajout.";
        new Alert(type, msg, ButtonType.OK).showAndWait();

        if (ok) {
            tfLibelle.clear();
            tfDescription.clear();
            cbSection.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void bRetourClick(ActionEvent e) {
        Stage st = (Stage) bRetour.getScene().getWindow();
        st.close();
    }
}
