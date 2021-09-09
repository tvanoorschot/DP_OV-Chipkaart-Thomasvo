package com.chipkaart.domein.reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    Connection connection;

    public ReizigerDAOPsql(Connection connection) throws SQLException {
        this.connection = connection;
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
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
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
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode verwijderd een reiziger uit de database.
     */
    @Override
    public boolean delete(Reiziger reiziger) {
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

                reizigers.add(new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum")));

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

                reizigers.add(new Reiziger(
                        result.getInt("reiziger_id"),
                        result.getString("voorletters"),
                        result.getString("tussenvoegsel"),
                        result.getString("achternaam"),
                        result.getDate("geboortedatum")));

            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return reizigers;
    }
}
