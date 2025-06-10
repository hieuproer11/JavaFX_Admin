package fsiAdministration.DAO;

import fsiAdministration.BO.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SectionDAO extends DAO<Section> {

    @Override
    public boolean create(Section section) {
        if (section == null) return false;
        String sql = "INSERT INTO section (libelleSection) VALUES (?) RETURNING idSection";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, section.getLibelleSection());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                section.setIdSection(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Section section) {
        if (section == null) return false;
        String sql = "UPDATE section SET libelleSection = ? WHERE idSection = ?";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, section.getLibelleSection());
            ps.setInt(2, section.getIdSection());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Section section) {
        if (section == null) return false;
        return deleteById(section.getIdSection());
    }

    /**
     * Supprime une section uniquement si aucun étudiant n'y est rattaché.
     */
    public boolean deleteById(int idSection) {
        final String countSql = "SELECT COUNT(*) FROM etudiant WHERE idSection = ?";
        final String delSql   = "DELETE FROM section WHERE idSection = ?";

        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement psCount = cnx.prepareStatement(countSql);
             PreparedStatement psDel   = cnx.prepareStatement(delSql)) {
            // 1) Vérification préventive
            psCount.setInt(1, idSection);
            try (ResultSet rs = psCount.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Des étudiants sont rattachés → suppression interdite
                    return false;
                }
            }
            // 2) Suppression
            psDel.setInt(1, idSection);
            return psDel.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Section find(int id) {
        String sql = "SELECT idSection, libelleSection FROM section WHERE idSection = ?";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
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
        return null;
    }

    @Override
    public List<Section> findAll() {
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT idSection, libelleSection FROM section ORDER BY idSection";
        try (Connection cnx = BDDManager.getConnection();
             Statement st = cnx.createStatement();
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

    /**
     * Récupère le dernier idSection existant.
     */
    public int lastId() {
        String sql = "SELECT COALESCE(MAX(idSection), 0) AS maxid FROM section";
        try (Connection cnx = BDDManager.getConnection();
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("maxid");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
