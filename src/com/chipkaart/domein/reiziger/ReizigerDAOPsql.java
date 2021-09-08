package com.chipkaart.domein.reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    Connection connection;

    public ReizigerDAOPsql(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {

            Statement statement = connection.createStatement();

            if(statement.executeUpdate("INSERT INTO reiziger VALUES (" +
                    reiziger.getId() + ", '" +
                    reiziger.getVoorletters() + "', '" +
                    reiziger.getTussenvoegsel() + "', '" +
                    reiziger.getAchternaam() + "', '" +
                    reiziger.getGeboortedatum() + "')") != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {

            Statement statement = connection.createStatement();

            if(statement.executeUpdate("UPDATE reiziger SET " +
                    "voorletters = '" + reiziger.getVoorletters() + "', " +
                    "tussenvoegsel = '" + reiziger.getTussenvoegsel() + "', " +
                    "achternaam = '" + reiziger.getAchternaam() + "', " +
                    "geboortedatum = '" + reiziger.getGeboortedatum() + "' " +
                    "WHERE reiziger_id = '" + reiziger.getId() + "'") != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {

            Statement statement = connection.createStatement();

            if(statement.executeUpdate("DELETE FROM reiziger WHERE reiziger_id = '" + reiziger.getId() + "'") != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger = null;
        try {

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM reiziger WHERE reiziger_id = '" + id + "'");

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

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        List<Reiziger> reizigers = new ArrayList<>();
        try {

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM reiziger WHERE geboortedatum = '" + datum + "'");

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

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();
        try {

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM reiziger");

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
