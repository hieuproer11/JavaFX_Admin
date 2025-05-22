package fsiAdministration.BO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cours {

    /* -- Champs -------------------------------------------------------- */
    private final IntegerProperty idCours          = new SimpleIntegerProperty();
    private final StringProperty  libelleCours     = new SimpleStringProperty();
    private final StringProperty  descriptionCours = new SimpleStringProperty();

    private Section section;

    /* -- Constructeurs ------------------------------------------------- */
    public Cours(int id, String libelle, String desc, Section section) {
        this.idCours.set(id);
        this.libelleCours.set(libelle);
        this.descriptionCours.set(desc);
        this.section = section;
    }
    public Cours() {}

    /* -- Getters / Setters classiques --------------------------------- */
    public int     getIdCours()               { return idCours.get(); }
    public void    setIdCours(int id)         { idCours.set(id); }
    public String  getLibelleCours()          { return libelleCours.get(); }
    public void    setLibelleCours(String s)  { libelleCours.set(s); }
    public String  getDescriptionCours()      { return descriptionCours.get(); }
    public void    setDescriptionCours(String d){ descriptionCours.set(d); }
    public Section getSection()               { return section; }
    public void    setSection(Section s)      { section = s; }

    /* -- Properties pour TableView ------------------------------------ */
    public IntegerProperty idCoursProperty()          { return idCours; }
    public StringProperty  libelleCoursProperty()     { return libelleCours; }
    public StringProperty descriptionCoursProperty() { return descriptionCours; }
}