package fsiAdministration.DAO;

import fsiAdministration.BO.Etudiant;
import fsiAdministration.BO.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO extends DAO<Etudiant>{

    public BDDManager db;
    @Override
    public boolean create(Etudiant obj) {
        boolean controle = false;
        try{
            int id = lastId();
            id++;
            obj.setIdEtudiant(id);
            Class.forName("org.postgresql.Driver");
            Connection connect = db.getConnection();
            String sql = "Insert into Etudiant(idEtudiant, nomEtudiant, prenomEtudiant, idSection) values (?,?,?,?);";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1,obj.getIdEtudiant());
            statement.setString(2,obj.getNomEtudiant());
            statement.setString(3,obj.getPrenomEtudiant());
            statement.setInt(4,1);

            int rowsInserer = statement.executeUpdate();
            if (rowsInserer > 0) {
                controle= true;
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return controle;
    }

    public int lastId(){
        int controle = 1;

        try {
            Class.forName("org.postgresql.Driver");
            Connection connect = db.getConnection();

            ResultSet result = connect.createStatement().executeQuery("select max(idEtudiant) from Etudiant ");
            if(result.next()){
                controle = result.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public boolean delete(Etudiant obj) {
        return false;
    }

    @Override
    public boolean update(Etudiant obj) {
        return false;
    }

    @Override
    public Etudiant find(int id) {
        return null;
    }

    @Override
    public List findAll() {
        List<Etudiant> mesEtud = new ArrayList<>();
        Etudiant etud;

        try {
            Class.forName("org.postgresql.Driver");
            Connection connect = db.getConnection();

            String sql = "SELECT * FROM etudiant";
            Statement ps = connect.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()) {
                etud = new Etudiant(
                        rs.getInt("idEtudiant"),
                        rs.getString ("nomEtudiant"),
                        rs.getString("prenomEtudiant")
                        );
                mesEtud.add(etud);
            }

        } catch (SQLException e) {
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mesEtud;
    }
}
