package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        EtudiantDAO etudDAO = new EtudiantDAO();
        List<Etudiant> mesEtud = etudDAO.findAll();
        ObservableList<Etudiant> mesEtudOL= FXCollections.observableArrayList(mesEtud);
        tcNomEtud.setCellValueFactory(cellData -> cellData.getValue().nomEtudiantProperty());
        tcPrenomEtud.setCellValueFactory(cellData -> cellData.getValue().prenomEtudiantProperty());

        tvEtudiants.setItems(mesEtudOL);



    }

/*       tcNomEtud.setCellValueFactory(new PropertyValueFactory<>("nomEtudiant"));
        tcPrenomEtud.setCellValueFactory(new PropertyValueFactory<>("prenomEtudiant"));
    ObservableList<Etudiant> data = getUserList();
        tvEtudiants.setItems(data);
    private ObservableList<Etudiant> getUserList() {

        Etudiant etud1 = new Etudiant(1,"Goudet","Magali");

        ObservableList<Etudiant> list = FXCollections.observableArrayList(etud1);
        return list;
    }*/



}
