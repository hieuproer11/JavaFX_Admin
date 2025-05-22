package fsiAdministration.controllers;

import fsiAdministration.BO.Cours;
import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;
import fsiAdministration.DAO.CoursDAO;
import fsiAdministration.DAO.EtudiantDAO;
import fsiAdministration.DAO.SectionDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeSectionController extends MenuController implements Initializable {

    /* ---------- FXML ---------- */
    @FXML private TableView<SectionRow>         tvSections;
    @FXML private TableColumn<SectionRow, Void> tcVoirCours;
    @FXML private TableColumn<SectionRow,Integer> tcIdSection;
    @FXML private TableColumn<SectionRow,String>  tcLibelleSection;
    @FXML private TableColumn<SectionRow,Integer> tcNbEtudiants;
    @FXML private TableColumn<SectionRow,Void>   tcModifier, tcSupprimer, tcConsulter;

    /* ---------- DAO & données ---------- */
    private final SectionDAO  sectionDAO = new SectionDAO();
    private final EtudiantDAO etudDAO    = new EtudiantDAO();
    private final ObservableList<SectionRow> data = FXCollections.observableArrayList();
    private final CoursDAO coursDAO = new CoursDAO();
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerDonnees();
        configColonnes();
        tvSections.setItems(data);
    }

    /** Charge toutes les sections + nb étudiants */
    private void chargerDonnees() {
        data.clear();
        for (Section s : sectionDAO.findAll()) {
            int nb = etudDAO.countBySectionId(s.getIdSection());
            data.add(new SectionRow(s, nb));
        }
    }

    /** Lie les colonnes et fabrique les boutons */
    private void configColonnes() {
        tcIdSection.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getSection().getIdSection()).asObject());
        tcLibelleSection.setCellValueFactory(c ->
                c.getValue().getSection().libelleSectionProperty());
        tcNbEtudiants.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getNbEtudiants()).asObject());

        /* -------- bouton MODIFIER -------- */
        tcModifier.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(e -> {
                    SectionRow row = getTableView().getItems().get(getIndex());
                    TextInputDialog d = new TextInputDialog(row.getSection().getLibelleSection());
                    d.setHeaderText("Modifier le libelle");
                    d.setContentText("Nouveau libelle :");
                    d.showAndWait().ifPresent(val -> {
                        row.getSection().setLibelleSection(val);
                        if (sectionDAO.update(row.getSection())) {
                            getTableView().refresh();
                        } else {
                            new Alert(Alert.AlertType.ERROR,"Echec mise à jour").showAndWait();
                        }
                    });
                });
            }
            @Override protected void updateItem(Void it, boolean empty) {
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });

        /* -------- bouton SUPPRIMER -------- */
        tcSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(e -> {
                    SectionRow row = getTableView().getItems().get(getIndex());
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                            "Supprimer la section ?", ButtonType.YES, ButtonType.NO);
                    conf.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            if (sectionDAO.delete(row.getSection())) {
                                data.remove(row);
                            } else {
                                new Alert(Alert.AlertType.ERROR,"Echec suppression").showAndWait();
                            }
                        }
                    });
                });
            }
            @Override protected void updateItem(Void it, boolean empty) {
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });

        /* -------- bouton VOIR COURS -------- */
        tcVoirCours.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Voir Cours");
            {
                btn.setOnAction(e -> {
                    SectionRow row = getTableView().getItems().get(getIndex());
                    List<Cours> cours = coursDAO.findBySectionId(
                            row.getSection().getIdSection());

                    if (cours.isEmpty()) {
                        new Alert(Alert.AlertType.INFORMATION,
                                "Aucun cours pour cette section.").showAndWait();
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    for (Cours c : cours) {
                        sb.append(" ").append(c.getLibelleCours())
                                .append(" -- ").append(c.getDescriptionCours())
                                .append('\n');
                    }

                    Alert dlg = new Alert(Alert.AlertType.INFORMATION);
                    dlg.setHeaderText("Cours de "
                            + row.getSection().getLibelleSection() + "  ");
                    dlg.setContentText(sb.toString());
                    dlg.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    dlg.showAndWait();
                });
            }
            @Override protected void updateItem(Void it, boolean empty) {
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });

        /* -------- bouton CONSULTER ÉTUDIANTS -------- */
        tcConsulter.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Voir details");
            {
                btn.setOnAction(e -> {
                    SectionRow row = getTableView().getItems().get(getIndex());
                    List<Etudiant> etuds = etudDAO.findBySectionId(row.getSection().getIdSection());

                    StringBuilder sb = new StringBuilder();
                    for (Etudiant et : etuds) {
                        sb.append(et.getNomEtudiant())
                                .append(" ")
                                .append(et.getPrenomEtudiant())
                                .append("\n");
                    }
                    Alert dlg = new Alert(Alert.AlertType.INFORMATION);
                    dlg.setHeaderText("Etudiants de  " + row.getSection().getLibelleSection() + " ");
                    dlg.setContentText(sb.length()==0 ?
                            "Aucun etudiant inscrit." : sb.toString());
                    dlg.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    dlg.showAndWait();
                });
            }
            @Override protected void updateItem(Void it, boolean empty) {
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    /* ---------------------------------------------------------------------- */
    /** Petit wrapper pour stocker section + effectif */
    public static class SectionRow {
        private final Section section;
        private final int nbEtudiants;
        public SectionRow(Section s, int nb) { this.section=s; this.nbEtudiants=nb; }
        public Section getSection()      { return section; }
        public int     getNbEtudiants()  { return nbEtudiants; }
    }
}
