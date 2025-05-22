package fsiAdministration.controllers;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.DAO.EtudiantDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ListeEtudiantController extends MenuController implements Initializable {

    /* ---------- FXML ---------- */
    @FXML private TableView<Etudiant> tvEtudiants;
    @FXML private TableColumn<Etudiant, String> tcNomEtud;
    @FXML private TableColumn<Etudiant, String> tcPrenomEtud;
    @FXML private TableColumn<Etudiant, String> tcLibelleSection;
    @FXML private TableColumn<Etudiant, LocalDate> tcDateNaiEtud;
    @FXML private TableColumn<Etudiant, Void>   tcSupprimer;
    @FXML private TableColumn<Etudiant, Void>   tcModifier;
    /* -------------------------- */

    private final EtudiantDAO etudDAO = new EtudiantDAO();       // DAO réutilisable
    private ObservableList<Etudiant> mesEtudiantsOL;             // référence conservée

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* --- 1. Remplir la TableView si elle est vide ---------------------- */
        if (tvEtudiants.getItems().isEmpty()) {
            List<Etudiant> mesEtud = etudDAO.findAll();
            mesEtudiantsOL = FXCollections.observableArrayList(mesEtud);

            tcNomEtud.setCellValueFactory(c -> c.getValue().nomEtudiantProperty());
            tcPrenomEtud.setCellValueFactory(c -> c.getValue().prenomEtudiantProperty());
            tcDateNaiEtud.setCellValueFactory(c -> c.getValue().dateNaissanceProperty());
            tcLibelleSection.setCellValueFactory(c -> c.getValue()
                    .getSection()
                    .libelleSectionProperty());

            tvEtudiants.setItems(mesEtudiantsOL);
        }


        tcModifier.setCellFactory(col -> new TableCell<>() {
                    private final Button btn = new Button("Modifier");
                    {
                        btn.setOnAction(e -> {
                            Etudiant etu = getTableView().getItems().get(getIndex());
                            if (etu == null) return;
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fsiAdministration/views/page_modif_etudiant.fxml"));
                                Parent root = loader.load();
                                ModifierEtudiantController ctrl = loader.getController();
                                ctrl.setEtudiant(etu);

                                Stage st = new Stage();
                                st.setScene(new Scene(root));
                                st.setTitle("Modifier étudiant");
                                st.initModality(Modality.APPLICATION_MODAL);
                                st.showAndWait();

                                tvEtudiants.refresh();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                new Alert(Alert.AlertType.ERROR,
                                        "Impossible d'ouvrir la page de modification.").show();
                            }
                        });
                        btn.getStyleClass().add("primary");
                    }
                    @Override protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                });

        tcSupprimer.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(e -> {
                    Etudiant etu = getTableView().getItems().get(getIndex());
                    if (etu == null) return;
                    mesEtudiantsOL.remove(etu);
                    etudDAO.deleteById(etu.getIdEtudiant());
                });

                btn.getStyleClass().add("danger");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
}
