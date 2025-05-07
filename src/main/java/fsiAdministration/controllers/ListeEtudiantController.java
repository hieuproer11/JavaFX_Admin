package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.EtudiantDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeEtudiantController extends MenuController implements Initializable {
    @FXML
    private TableView<Etudiant> tvEtudiants;

    @FXML
    private TableColumn<Etudiant, String> tcNomEtud;

    @FXML
    private TableColumn<Etudiant, String> tcPrenomEtud;

    @FXML
    private TableColumn<Etudiant, String> tcLibelleSection;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tvEtudiants.getItems().isEmpty()) { // Assure-toi de ne charger qu'une fois
            EtudiantDAO etudDAO = new EtudiantDAO();
            List<Etudiant> mesEtud = etudDAO.findAll();
            ObservableList<Etudiant> mesEtudOL = FXCollections.observableArrayList(mesEtud);

            // Configuration des colonnes de la TableView
            tcNomEtud.setCellValueFactory(cellData -> cellData.getValue().nomEtudiantProperty());
            tcPrenomEtud.setCellValueFactory(cellData -> cellData.getValue().prenomEtudiantProperty());
            tcLibelleSection.setCellValueFactory(cellData -> cellData.getValue().getSection().libelleSectionProperty());


            // Affecter les données à la TableView UNE SEULE FOIS
            tvEtudiants.setItems(mesEtudOL);
        }
    }




}
