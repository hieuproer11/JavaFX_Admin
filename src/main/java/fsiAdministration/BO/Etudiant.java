package fsiAdministration.BO;

import javafx.beans.property.SimpleStringProperty;

public class Etudiant {

    private int idEtudiant;
    private SimpleStringProperty nomEtudiant;
    private SimpleStringProperty prenomEtudiant;
    private Section section;

    public Etudiant(int idEtudiant, String nomEtudiant, String prenomEtudiant, Section section) {
        this.idEtudiant = idEtudiant;
        this.nomEtudiant = new SimpleStringProperty(nomEtudiant);
        this.prenomEtudiant = new SimpleStringProperty(prenomEtudiant);
        this.section = section;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getNomEtudiant() {
        return nomEtudiant.get();
    }

    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant.set(nomEtudiant);
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant.get();
    }

    public void setPrenomEtudiant(String prenomEtudiant) {
        this.prenomEtudiant.set(prenomEtudiant);
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public SimpleStringProperty nomEtudiantProperty() {
        return nomEtudiant;
    }

    public SimpleStringProperty prenomEtudiantProperty() {
        return prenomEtudiant;
    }

    public SimpleStringProperty libelleSectionProperty() {
        return new SimpleStringProperty(section.getLibelleSection());
    }
}
