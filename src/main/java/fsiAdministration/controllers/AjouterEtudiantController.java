package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.EtudiantDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterEtudiantController extends MenuController implements Initializable {

    @FXML
    private TextField tfNomEtud, tfPrenomEtud;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<String> lvSectionEtud ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> section = FXCollections.observableArrayList("Licence developpeur et infrastructure", "Management de projet");
        lvSectionEtud.setItems(section);
    }

    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        String x = tfNomEtud.getText();
        String y = tfPrenomEtud.getText();
        Section section;
        if (lvSectionEtud.getSelectionModel().getSelectedItem().equals("Licence développeur et infrastructure")) {
            section = new Section(1, "Licence développeur et infrastructure");
        } else if (lvSectionEtud.getSelectionModel().getSelectedItem().equals("Management de projet")) {
            section = new Section(2, "Management de projet");
        } else {
            System.out.println("Veuillez sélectionner une section.");
            return;
        }

        Etudiant newEtud = new Etudiant(0, x, y, section);


        EtudiantDAO etudDAO = new EtudiantDAO();
        boolean controle = etudDAO.create(newEtud);

        if (controle) {
            System.out.println("L'étudiant a été ajouté avec succès !");
            tfNomEtud.clear();
            tfPrenomEtud.clear();
        } else {
            System.out.println("Échec de l'ajout de l'étudiant.");
        }

    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        stageP.close();
    }

}
