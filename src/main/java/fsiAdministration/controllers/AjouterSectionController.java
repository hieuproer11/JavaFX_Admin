package fsiAdministration.controllers;

import fsiAdministration.BO.Section;
import fsiAdministration.DAO.SectionDAO;
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
import java.util.ResourceBundle;

/**
 * Controller pour la vue « page_ajout_section.fxml ».
 * <p>
 * Permet d'enregistrer une nouvelle section (libellé obligatoire).
 */
public class AjouterSectionController extends MenuController implements Initializable {

    /* ----------------------- Composants FXML ------------------------- */
    @FXML private TextField tfNomSect;
    @FXML private Button    bEnregistrer;
    @FXML private Button    bRetour;

    /* ----------------------- DAO ------------------------------------ */
    private final SectionDAO sectionDAO = new SectionDAO();

    /* ----------------------- Initialisation ------------------------- */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String libelle = tfNomSect.getText().trim();
        if (libelle.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Le libelle ne peut pas être vide.").showAndWait();
            return;
        }

        Section s = new Section(0, libelle);
        boolean ok = sectionDAO.create(s);

        Alert.AlertType type = ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        String message = ok ? "Section ajoutee avec succes !" : "Echec de l'ajout de la section.";
        new Alert(type, message, ButtonType.OK).showAndWait();

        if (ok) tfNomSect.clear();
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


    public static void afficherDepuis(MenuController parent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AjouterSectionController.class.getResource("/fsiAdministration/views/page_ajout_section.fxml"));
            Parent root = loader.load();
            Stage st = new Stage();
            st.setScene(new Scene(root));
            st.setTitle("Ajouter une section");
            st.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la fenêtre d'ajout de section.").show();
        }
    }
}

