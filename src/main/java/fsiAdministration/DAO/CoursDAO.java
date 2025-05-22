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

    /*                               DELETE                               */
    @Override
    public boolean delete(Cours c) {
        return deleteById(c.getIdCours());
    }

    public boolean deleteById(int idCours) {
        try (Connection cnx = BDDManager.getConnection();
             PreparedStatement ps = cnx.prepareStatement("DELETE FROM cours WHERE idCours = ?")) {
            ps.setInt(1, idCours);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
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
