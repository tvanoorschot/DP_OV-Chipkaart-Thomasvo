package com.chipkaart.dao.reiziger;

import com.chipkaart.dao.adres.AdresDAO;
import com.chipkaart.dao.adres.AdresDAOPsql;
import com.chipkaart.dao.ovchipkaart.OVChipkaartDAO;
import com.chipkaart.dao.ovchipkaart.OVChipkaartDAOPsql;
import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection connection;
    private AdresDAO adao;
    private OVChipkaartDAO odao;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    public ReizigerDAOPsql(Connection connection, AdresDAOPsql adao , OVChipkaartDAOPsql odao) {
        this(connection);
        adao.setRdao(this);
        odao.setRdao(this);
        this.adao = adao;
        this.odao = odao;
    }

    public ReizigerDAOPsql(Connection connection, AdresDAOPsql adao) {
        this(connection);
        adao.setRdao(this);
        this.adao = adao;
    }

    public ReizigerDAOPsql(Connection connection, OVChipkaartDAOPsql odao) {
        this(connection);
        odao.setRdao(this);
        this.odao = odao;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    /**
     * Deze methode slaat een reiziger op in de database.
     */
    @Override
    public boolean save(Reiziger reiziger) {
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO reiziger VALUES (?, ?, ?, ?, ?)");

            statement.setInt(1, reiziger.getId());
            statement.setString(2, reiziger.getVoorletters());
            statement.setString(3, reiziger.getTussenvoegsel());
            statement.setString(4, reiziger.getAchternaam());
            statement.setDate(5, reiziger.getGeboortedatum());

            if(statement.executeUpdate() != 0) {
                if(reiziger.getAdres() != null) adao.save(reiziger.getAdres());
                if(reiziger.getOvChipkaarten() != null)
                    if (!reiziger.getOvChipkaarten().isEmpty())
                        reiziger.getOvChipkaarten().forEach(o -> odao.save(o));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Deze methode past een reiziger aan in de database.
     */
    @Override
    public boolean update(Reiziger reiziger) {
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");

            statement.setString(1, reiziger.getVoorletters());
            statement.setString(2, reiziger.getTussenvoegsel());
            statement.setString(3, reiziger.getAchternaam());
            statement.setDate(4, reiziger.getGeboortedatum());
            statement.setInt(5, reiziger.getId());

            if(statement.executeUpdate() != 0){
                if(reiziger.getAdres() == null) return true;
                if (!adao.update(reiziger.getAdres())) adao.save(reiziger.getAdres());
                if (odao.findByReiziger(reiziger).containsAll(reiziger.getOvChipkaarten())){
                    reiziger.getOvChipkaarten().forEach(o -> odao.update(o));
                } else {
                    List<OVChipkaart> nieuweOVChipkaarten = new ArrayList<>(reiziger.getOvChipkaarten());
                    nieuweOVChipkaarten.removeAll(odao.findByReiziger(reiziger));
                    nieuweOVChipkaarten.forEach(o -> odao.save(o));
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Deze methode verwijderd een reiziger uit de database.
     */
    @Override
    public boolean delete(Reiziger reiziger) {

        if(reiziger.getAdres() != null) {
            adao.delete(reiziger.getAdres());
        }

        if(reiziger.getOvChipkaarten() != null) {
            reiziger.getOvChipkaarten().forEach(o -> odao.delete(o));
        }

        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");

            statement.setInt(1, reiziger.getId());

            if(statement.executeUpdate() != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode zoekt een reiziger op id uit de database.
     */
    @Override
    public Reiziger findById(int id) {

        Reiziger reiziger = null;
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                reiziger = new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));

                reiziger.setAdres(adao.findByReiziger(reiziger));
                reiziger.setOvChipkaarten(odao.findByReiziger(reiziger));

            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reiziger;
    }

    /**
     * Deze methode zoekt reizigers op geboortedatum uit de database.
     */
    @Override
    public List<Reiziger> findByGbdatum(String datum) {

        List<Reiziger> reizigers = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");

            statement.setDate(1, java.sql.Date.valueOf(datum));

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Reiziger reiziger = new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));

                reizigers.add(reiziger);
                reiziger.setAdres(adao.findByReiziger(reiziger));
                reiziger.setOvChipkaarten(odao.findByReiziger(reiziger));
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reizigers;
    }

    /**
     * Deze methode haalt alle reizigers op uit de database.
     */
    @Override
    public List<Reiziger> findAll() {

        List<Reiziger> reizigers = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reiziger");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Reiziger reiziger = new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum"));

                reizigers.add(reiziger);
                reiziger.setAdres(adao.findByReiziger(reiziger));
                reiziger.setOvChipkaarten(odao.findByReiziger(reiziger));

            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reizigers;
    }
}
