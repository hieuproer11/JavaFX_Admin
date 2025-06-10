package fsiAdministration.DAO;

import fsiAdministration.BO.Cours;
import fsiAdministration.BO.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO extends DAO<Cours> {

    /*                               CREATE                               */
    @Override
    public boolean create(Cours c) {
        final String sql = "INSERT INTO cours (libelleCours, descriptionCours, idSection) " +
                "VALUES (?,?,?) RETURNING idCours";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, c.getLibelleCours());
            ps.setString(2, c.getDescriptionCours());
            ps.setInt   (3, c.getSection().getIdSection());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setIdCours(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(Cours obj) {
        return false;
    }

    /*                               UPDATE                               */
    @Override
    public boolean update(Cours c) {
        final String sql = "UPDATE cours SET libelleCours = ?, descriptionCours = ?, idSection = ? " +
                "WHERE idCours = ?";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, c.getLibelleCours());
            ps.setString(2, c.getDescriptionCours());
            ps.setInt   (3, c.getSection().getIdSection());
            ps.setInt   (4, c.getIdCours());

            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
    public boolean deleteById(int idCours) {
        final String countSql =
                "SELECT COUNT(*) " +
                        "FROM etudiant e " +
                        "JOIN cours c ON e.idSection = c.idSection " +
                        "WHERE c.idCours = ?";
        final String delSql = "DELETE FROM cours WHERE idCours = ?";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement psCount = cnx.prepareStatement(countSql);
             PreparedStatement psDel   = cnx.prepareStatement(delSql)) {
            // 1) Vérification préventive
            psCount.setInt(1, idCours);
            try (ResultSet rs = psCount.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Des étudiants sont rattachés à la même section : suppression interdite
                    return false;
                }
            }
            // 2) Suppression
            psDel.setInt(1, idCours);
            return psDel.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*                                READ                                */
    @Override
    public Cours find(int id) {
        final String sql = baseQuery() + " WHERE c.idCours = ?";
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    @Override
    public List<Cours> findAll() {
        final String sql = baseQuery() + " ORDER BY c.libelleCours";
        List<Cours> list = new ArrayList<>();
        try (Connection cnx = BDDManager.getConnection();
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public List<Cours> findBySectionId(int idSection) {
        final String sql = baseQuery() + " WHERE c.idSection = ?";
        List<Cours> list = new ArrayList<>();
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idSection);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    /*                              Helpers                               */
    private String baseQuery() {
        return "SELECT c.*, s.libelleSection FROM cours c " +
                "JOIN section s ON c.idSection = s.idSection";
    }

    private Cours map(ResultSet rs) throws SQLException {
        Section sec = new Section(rs.getInt("idSection"), rs.getString("libelleSection"));
        return new Cours(
                rs.getInt("idCours"),
                rs.getString("libelleCours"),
                rs.getString("descriptionCours"),
                sec
        );
    }
}
