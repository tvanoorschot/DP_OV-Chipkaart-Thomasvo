package com.chipkaart.dao.ovchipkaart;

import com.chipkaart.dao.reiziger.ReizigerDAO;
import com.chipkaart.dao.reiziger.ReizigerDAOPsql;
import com.chipkaart.domein.Adres;
import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection connection;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    public OVChipkaartDAOPsql(Connection connection, ReizigerDAOPsql rdao) {
        this.connection = connection;
        rdao.setOdao(this);
        this.rdao = rdao;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    /**
     * Deze methode slaat een ov-chipkaart op in de database.
     */
    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO ov_chipkaart VALUES (?, ?, ?, ?, ?)");

            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.setDate(2, ovChipkaart.getGeldigTot());
            statement.setInt(3, ovChipkaart.getKlasse());
            statement.setDouble(4, ovChipkaart.getSaldo());
            statement.setInt(5, ovChipkaart.getReiziger().getId());

            if(statement.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode past een ov-chipkaart aan in de database.
     */
    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE ov_chipkaart SET klasse = ?, saldo = ? WHERE kaart_nummer = ?");

            statement.setInt(1, ovChipkaart.getKlasse());
            statement.setDouble(2, ovChipkaart.getSaldo());
            statement.setInt(3, ovChipkaart.getKaartNummer());

            if(statement.executeUpdate() != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode verwijderd een ov-chipkaart uit de database.
     */
    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");

            statement.setInt(1, ovChipkaart.getKaartNummer());

            if(statement.executeUpdate() != 0){
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode zoekt een ov-chipkaart op kaartnummer uit de database.
     */
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?");

            statement.setInt(1, reiziger.getId());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ovChipkaartList.add(new OVChipkaart(
                        result.getInt("kaart_nummer"),
                        result.getDate("geldig_tot"),
                        result.getInt("klasse"),
                        result.getDouble("saldo"),
                        reiziger));
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ovChipkaartList;
    }

    /**
     * Deze methode haalt alle ov-chipkaarten op uit de database.
     */
    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ov_chipkaart");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ovChipkaartList.add(new OVChipkaart(
                        result.getInt("kaart_nummer"),
                        result.getDate("geldig_tot"),
                        result.getInt("klasse"),
                        result.getDouble("saldo"),
                        rdao.findById(result.getInt("reiziger_id"))));
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ovChipkaartList;
    }
}
