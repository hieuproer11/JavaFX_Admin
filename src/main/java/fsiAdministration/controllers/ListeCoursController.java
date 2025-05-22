package fsiAdministration.controllers;

import fsiAdministration.BO.Cours;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.CoursDAO;
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

/**
 * ListeCoursController : affiche, modifie et supprime les cours.
 */
public class ListeCoursController extends MenuController implements Initializable {

    /* --------------------------- FXML ----------------------------------- */
    @FXML private TableView<Cours>           tvCours;
    @FXML private TableColumn<Cours,Integer> tcIdCours;
    @FXML private TableColumn<Cours,String>  tcLibelleCours;
    @FXML private TableColumn<Cours,String>  tcDescription;
    @FXML private TableColumn<Cours,String>  tcLibelleSection;
    @FXML private TableColumn<Cours,Void>    tcModifier;
    @FXML private TableColumn<Cours,Void>    tcSupprimer;

    /* --------------------------- Données -------------------------------- */
    private final CoursDAO dao = new CoursDAO();
    private ObservableList<Cours> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Remplissage initial
        List<Cours> list = dao.findAll();
        data = FXCollections.observableArrayList(list);

        tcIdCours.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCours()).asObject());
        tcLibelleCours.setCellValueFactory(c -> c.getValue().libelleCoursProperty());
        tcDescription.setCellValueFactory(c -> c.getValue().descriptionCoursProperty());
        tcLibelleSection.setCellValueFactory(c -> c.getValue().getSection().libelleSectionProperty());

        tvCours.setItems(data);

        configBoutons();
    }

    /* ------------------------- Boutons ligne --------------------------- */
    private void configBoutons() {
        // ---- MODIFIER ----
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

        // ---- SUPPRIMER ----
        tcSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(e -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    if (c == null) return;
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                            "Supprimer le cours « " + c.getLibelleCours() + " » ?",
                            ButtonType.YES, ButtonType.NO);
                    conf.showAndWait().ifPresent(b -> {
                        if (b == ButtonType.YES && dao.delete(c)) {
                            data.remove(c);
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

    /* --------------------------- Edition ------------------------------- */
    private void afficherDialogEdition(Cours c) {
        Dialog<Cours> dlg = new Dialog<>();
        dlg.setTitle("Modifier cours");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // --- champs ---
        TextField tfLibelle = new TextField(c.getLibelleCours());
        TextField tfDesc    = new TextField(c.getDescriptionCours());

        VBox box = new VBox(10,
                new Label("Libelle:"), tfLibelle,
                new Label("Description:"), tfDesc);
        dlg.getDialogPane().setContent(box);
        dlg.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dlg.setResultConverter(bt -> bt == ButtonType.OK ? c : null);

        dlg.showAndWait().ifPresent(co -> {
            String lib = tfLibelle.getText().trim();
            String des = tfDesc.getText().trim();
            if (lib.isEmpty() || des.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Champs requis.").showAndWait();
                return;
            }
            c.setLibelleCours(lib);
            c.setDescriptionCours(des);
            if (dao.update(c)) {
                tvCours.refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Échec de la mise à jour.").showAndWait();
            }
        });
    }



}
