package com.chipkaart.domein.adres;

import com.chipkaart.domein.reiziger.Reiziger;
import com.chipkaart.domein.reiziger.ReizigerDAO;
import com.chipkaart.domein.reiziger.ReizigerDAOPsql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    Connection connection;
    ReizigerDAO rdao;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }

    /**
     * Deze methode slaat een adres op in de database.
     */
    @Override
    public boolean save(Adres adres) {
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO adres VALUES (?, ?, ?, ?, ?, ?)");

            statement.setInt(1, adres.getId());
            statement.setString(2, adres.getPostcode());
            statement.setString(3, adres.getHuisnummer());
            statement.setString(4, adres.getStraat());
            statement.setString(5, adres.getWoonplaats());
            statement.setInt(6, adres.getReiziger().getId());

            if(statement.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode past een adres aan in de database.
     */
    @Override
    public boolean update(Adres adres) {
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");

            statement.setString(1, adres.getPostcode());
            statement.setString(2, adres.getHuisnummer());
            statement.setString(3, adres.getStraat());
            statement.setString(4, adres.getWoonplaats());
            statement.setInt(5, adres.getReiziger().getId());
            statement.setInt(6, adres.getId());

            if(statement.executeUpdate() != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode verwijderd een adres uit de database.
     */
    @Override
    public boolean delete(Adres adres) {
        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM adres WHERE adres_id = ?");

            statement.setInt(1, adres.getId());

            if(statement.executeUpdate() != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode zoekt een adres op reiziger uit de database.
     */
    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Adres adres = null;
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");

            statement.setInt(1, reiziger.getId());

            ResultSet result = statement.executeQuery();

            // misschien overbodig
            while (result.next()) {
            // ^^^^
                adres = new Adres(
                        result.getInt("adres_id"),
                        result.getString("postcode"),
                        result.getString("huisnummer"),
                        result.getString("straat"),
                        result.getString("woonplaats"),
                        reiziger);

            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return adres;
    }

    /**
     * Deze methode haalt alle adressen op uit de database.
     */
    @Override
    public List<Adres> findAll() {
        rdao = new ReizigerDAOPsql(connection);

        List<Adres> adressen = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM adres");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Adres adres = new Adres(
                        result.getInt("adres_id"),
                        result.getString("postcode"),
                        result.getString("huisnummer"),
                        result.getString("straat"),
                        result.getString("woonplaats"),
                        rdao.findById(result.getInt("reiziger_id")));

                adressen.add(adres);
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return adressen;
    }

}
