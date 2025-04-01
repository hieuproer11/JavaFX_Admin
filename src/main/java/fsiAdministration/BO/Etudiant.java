package fsiAdministration.BO;

import javafx.beans.property.SimpleStringProperty;

public class Etudiant {

    private int idEtudiant;
    private SimpleStringProperty nomEtudiant;
    private SimpleStringProperty prenomEtudiant;

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    private int idSection;

    public Etudiant(int idEtudiant, String nomEtudiant, String prenomEtudiant) {
        this.idEtudiant = idEtudiant;
        this.nomEtudiant = new SimpleStringProperty(nomEtudiant);
        this.prenomEtudiant = new SimpleStringProperty(prenomEtudiant);

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

    public SimpleStringProperty nomEtudiantProperty() {
        return nomEtudiant;
    }


    public SimpleStringProperty prenomEtudiantProperty() {
        return prenomEtudiant;
    }
}
