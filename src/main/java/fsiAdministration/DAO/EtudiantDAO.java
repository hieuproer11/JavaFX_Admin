package fsiAdministration.DAO;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Section;

import java.sql.*;
import java.time.LocalDate;                // ⇦ NEW
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO extends DAO<Etudiant> {

    public BDDManager db;

    /*-----------------------------------------------------------
     *  INSERT
     *----------------------------------------------------------*/
    @Override
    public boolean create(Etudiant obj) {
        boolean controle = false;
        try {
            int id = lastId() + 1;
            obj.setIdEtudiant(id);

            Class.forName("org.postgresql.Driver");
            Connection connect = db.getConnection();

            String sql = "INSERT INTO Etudiant" +
                    "(idEtudiant, nomEtudiant, prenomEtudiant, datenaissance, idSection) " +
                    "VALUES (?,?,?,?,?);";
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setInt   (1, obj.getIdEtudiant());
            ps.setString(2, obj.getNomEtudiant());
            ps.setString(3, obj.getPrenomEtudiant());
            ps.setDate  (4, Date.valueOf(obj.getDateNaissance()));
            ps.setInt   (5, obj.getSection().getIdSection());

            controle = ps.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return controle;
    }


    @Override
    public Etudiant find(int id) {
        // (non demandé ; à compléter si besoin)
        return null;
    }


    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> result = new ArrayList<>();
        String sql = """
            SELECT e.idEtudiant,
                   e.nomEtudiant,
                   e.prenomEtudiant,
                   e.datenaissance,                                                      -- ⇦ NEW
                   e.idSection,
                   s.libelleSection
              FROM Etudiant e
              JOIN Section  s ON e.idSection = s.idSection
            """;

        try (Connection connect = db.getConnection();
             Statement st   = connect.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) {
                Section section = new Section(
                        rs.getInt   ("idSection"),
                        rs.getString("libelleSection")
                );

                Etudiant etu = new Etudiant(
                        rs.getInt   ("idEtudiant"),
                        rs.getString("nomEtudiant"),
                        rs.getString("prenomEtudiant"),
                        rs.getDate  ("datenaissance").toLocalDate(),                     // ⇦ NEW
                        section
                );
                result.add(etu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Etudiant> findBySectionId(int idSection) {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM Etudiant WHERE idSection = ?";

        try (Connection connect = db.getConnection();
             PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, idSection);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Etudiant(
                        rs.getInt   ("idEtudiant"),
                        rs.getString("nomEtudiant"),
                        rs.getString("prenomEtudiant"),
                        rs.getDate  ("datenaissance").toLocalDate(),                     // ⇦ NEW
                        null        // libre à vous de charger Section ici
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override public boolean delete(Etudiant obj) { return false; }
    @Override
    public boolean update(Etudiant obj) {

        final String sql = """
        UPDATE etudiant
           SET nomEtudiant   = ?,
               prenomEtudiant = ?,
               dateNaissance  = ?,
               idSection      = ?
         WHERE idEtudiant     = ?
    """;

        try (Connection connect = db.getConnection();
             PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, obj.getNomEtudiant());
            ps.setString(2, obj.getPrenomEtudiant());
            ps.setDate  (3, Date.valueOf(obj.getDateNaissance()));   // LocalDate → java.sql.Date
            ps.setInt   (4, obj.getSection().getIdSection());
            ps.setInt   (5, obj.getIdEtudiant());

            return ps.executeUpdate() == 1;   // true si exactement 1 ligne modifiée

        } catch (SQLException e) {
            e.printStackTrace();              // à remplacer par votre logger
            return false;
        }
    }


    public boolean deleteById(int idEtudiant) {
        String sql = "DELETE FROM Etudiant WHERE idEtudiant = ?";
        try (Connection connect = db.getConnection();
             PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, idEtudiant);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countBySectionId(int idSection) {
        String sql = "SELECT COUNT(*) FROM Etudiant WHERE idSection = ?";
        try (PreparedStatement ps = BDDManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idSection);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int lastId() {
        int id = 1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connect = db.getConnection();
            ResultSet rs = connect.createStatement()
                    .executeQuery("SELECT MAX(idEtudiant) FROM Etudiant");
            if (rs.next()) id = rs.getInt(1);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id;
    }
}
