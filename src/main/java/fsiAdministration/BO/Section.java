package fsiAdministration.BO;

import javafx.beans.property.SimpleStringProperty;

public class Section {

    private int idSection;
    private SimpleStringProperty libelleSection;

    public Section(int idSection, String libelleSection) {
        this.idSection = idSection;
        this.libelleSection = new SimpleStringProperty(libelleSection);
    }
    public Section(){};

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    public String getLibelleSection() {
        return libelleSection.get();
    }

    public void setLibelleSection(String libelleSection) {
        this.libelleSection.set(libelleSection);
    }

    public SimpleStringProperty libelleSectionProperty() {
        return libelleSection;
    }

    @Override
    public String toString() {
        return libelleSection.get();
    }
}
