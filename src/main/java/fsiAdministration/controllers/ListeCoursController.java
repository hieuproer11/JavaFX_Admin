package fsiAdministration.controllers;

import fsiAdministration.BO.Cours;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.CoursDAO;
import fsiAdministration.DAO.SectionDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeCoursController extends MenuController implements Initializable {

    /* --------------------------- FXML ----------------------------------- */
    @FXML private TableView<Cours>           tvCours;
    @FXML private TableColumn<Cours,Integer> tcIdCours;
    @FXML private TableColumn<Cours,String>  tcLibelleCours;
    @FXML private TableColumn<Cours,String>  tcDescription;
    @FXML private TableColumn<Cours,String>  tcLibelleSection;
    @FXML private TableColumn<Cours,Void>    tcModifier;
    @FXML private TableColumn<Cours,Void>    tcSupprimer;

    /* --------------------------- DAO & données -------------------------- */
    private final CoursDAO   coursDAO   = new CoursDAO();
    private final SectionDAO sectionDAO = new SectionDAO();

    private ObservableList<Cours>   data;
    private ObservableList<Section> sections;

    /* ---------------------------- Init ---------------------------------- */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data     = FXCollections.observableArrayList(coursDAO.findAll());
        sections = FXCollections.observableArrayList(sectionDAO.findAll());

        tcIdCours.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCours()).asObject());
        tcLibelleCours.setCellValueFactory(c -> c.getValue().libelleCoursProperty());
        tcDescription.setCellValueFactory(c -> c.getValue().descriptionCoursProperty());
        tcLibelleSection.setCellValueFactory(c -> c.getValue().getSection().libelleSectionProperty());
        tvCours.setItems(data);

        configBoutons();
    }

    /* ------------------------- Boutons de ligne ------------------------- */
    private void configBoutons() {
        // MODIFIER ------------------------------------------------------
        tcModifier.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(e -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    if (c != null) afficherDialogEdition(c);
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // SUPPRIMER -----------------------------------------------------
        tcSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(e -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    if (c == null) return;
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                            "Supprimer le cours « " + c.getLibelleCours() + "  ?",
                            ButtonType.YES, ButtonType.NO);
                    conf.showAndWait().ifPresent(b -> {
                        if (b == ButtonType.YES) {
                            boolean ok = coursDAO.deleteById(c.getIdCours());
                            if (ok) {
                                getTableView().getItems().remove(c);
                            } else {
                                new Alert(Alert.AlertType.ERROR,
                                        "Impossible de supprimer : des etudiants sont rattaches a ce cours.",
                                        ButtonType.OK).showAndWait();
                            }
                        }
                    });
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    /* ------------------------ Dialog d'édition -------------------------- */
    private void afficherDialogEdition(Cours c) {
        Dialog<Cours> dlg = new Dialog<>();
        dlg.setTitle("Modifier cours");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Champs de saisie
        TextField tfLib = new TextField(c.getLibelleCours());
        TextField tfDes = new TextField(c.getDescriptionCours());

        ComboBox<Section> cbSec = new ComboBox<>(sections);
        cbSec.setValue(c.getSection());
        cbSec.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Section s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "" : s.getLibelleSection());
            }
        });
        cbSec.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Section s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "" : s.getLibelleSection());
            }
        });

        VBox box = new VBox(10,
                new Label("Libelle:"), tfLib,
                new Label("Description:"), tfDes,
                new Label("Section:"), cbSec);
        dlg.getDialogPane().setContent(box);
        dlg.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dlg.setResultConverter(bt -> bt == ButtonType.OK ? c : null);

        dlg.showAndWait().ifPresent(co -> {
            String lib = tfLib.getText().trim();
            String des = tfDes.getText().trim();
            Section sec = cbSec.getValue();

            if (lib.isEmpty() || des.isEmpty() || sec == null) {
                new Alert(Alert.AlertType.WARNING, "Champs requis.").showAndWait();
                return;
            }

            c.setLibelleCours(lib);
            c.setDescriptionCours(des);
            c.setSection(sec);

            if (coursDAO.update(c)) {
                tvCours.refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Echec de la mise à jour.").showAndWait();
            }
        });
    }


}
