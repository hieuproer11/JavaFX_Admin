package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
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
        ObservableList<String> section = FXCollections.observableArrayList("section 1", "section 2");
        lvSectionEtud.setItems(section);
    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        String x = tfNomEtud.getText();
        String y = tfPrenomEtud.getText();
        int z = 1;

        Etudiant newEtud = new Etudiant(0,x,y);
        newEtud.setIdSection(z);

        System.out.println(newEtud);
        EtudiantDAO etudDAO = new EtudiantDAO();
        boolean controle = etudDAO.create(newEtud);
        System.out.println(controle);
    }

    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        tfNomEtud.clear();
        tfPrenomEtud.clear();
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        // On fait le lien avec l'ecran actuelle
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        //on ferme l'écran
        stageP.close();
    }
}
