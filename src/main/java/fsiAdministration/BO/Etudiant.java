package fsiAdministration.BO;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Etudiant {

    /* ---------- Champs ---------- */
    private final IntegerProperty idEtudiant = new SimpleIntegerProperty();
    private final StringProperty nomEtudiant = new SimpleStringProperty();
    private final StringProperty prenomEtudiant = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateNaissance = new SimpleObjectProperty<>();
    private Section section;


    public Etudiant(int id, String nom, String prenom,
                    LocalDate dateNaiss, Section section) {
        this.idEtudiant.set(id);
        this.nomEtudiant.set(nom);
        this.prenomEtudiant.set(prenom);
        this.dateNaissance.set(dateNaiss);
        this.section = section;
    }

    public Etudiant() {
    }


    public int getIdEtudiant() {
        return idEtudiant.get();
    }

    public void setIdEtudiant(int id) {
        this.idEtudiant.set(id);
    }

    public String getNomEtudiant() {
        return nomEtudiant.get();
    }

    public void setNomEtudiant(String nom) {
        this.nomEtudiant.set(nom);
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant.get();
    }

    public void setPrenomEtudiant(String pr) {
        this.prenomEtudiant.set(pr);
    }

    public LocalDate getDateNaissance() {
        return dateNaissance.get();
    }

    public void setDateNaissance(LocalDate d) {
        this.dateNaissance.set(d);
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section s) {
        this.section = s;
    }

    /* ---------- Properties pour JavaFX ---------- */
    public IntegerProperty idEtudiantProperty() {
        return idEtudiant;
    }

    public StringProperty nomEtudiantProperty() {
        return nomEtudiant;
    }

    public StringProperty prenomEtudiantProperty() {
        return prenomEtudiant;
    }

    public ObjectProperty<LocalDate> dateNaissanceProperty() {
        return dateNaissance;
    }
}


