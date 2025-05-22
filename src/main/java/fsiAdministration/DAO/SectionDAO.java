package fsiAdministration.DAO;

import fsiAdministration.BO.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO extends DAO<Section> {

    @Override
    public boolean create(Section section) {
        if (section == null) return false;

        String sql = "INSERT INTO section (idSection, libelleSection) VALUES (?, ?)";
        try (PreparedStatement ps =
                     BDDManager.getConnection().prepareStatement(sql)) {

            int id = lastId() + 1;
            section.setIdSection(id);

            ps.setInt(1, id);
            ps.setString(2, section.getLibelleSection());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ----------------- 2. SUPPRESSION ------------------------------
    /* Suppression à partir de l‘objet (méthode imposée par DAO) */
    @Override
    public boolean delete(Section section) {
        if (section == null) return false;
        return deleteById(section.getIdSection());
    }

    /* Suppression directe par identifiant */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM section WHERE idSection = ?";
        try (PreparedStatement ps =
                     BDDManager.getConnection().prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean update(Section section) {
        if (section == null) return false;

        String sql = "UPDATE section SET libelleSection = ? WHERE idSection = ?";
        try (PreparedStatement ps =
                     BDDManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, section.getLibelleSection());
            ps.setInt(2, section.getIdSection());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Section find(int id) {
        String sql = "SELECT idSection, libelleSection FROM section WHERE idSection = ?";
        try (PreparedStatement ps =
                     BDDManager.getConnection().prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Section(
                        rs.getInt("idSection"),
                        rs.getString("libelleSection")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;   // non trouvé
    }

    @Override
    public List<Section> findAll() {
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT idSection, libelleSection FROM section ORDER BY idSection";

        try (Statement st = BDDManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                sections.add(new Section(
                        rs.getInt("idSection"),
                        rs.getString("libelleSection")
                ));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sections;
    }

    public int lastId() {
        String sql = "SELECT COALESCE(MAX(idSection), 0) AS maxid FROM section";
        try (Statement st = BDDManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) return rs.getInt("maxid");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
