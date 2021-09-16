package com.chipkaart.dao.ovchipkaart;

import com.chipkaart.dao.reiziger.ReizigerDAO;
import com.chipkaart.dao.reiziger.ReizigerDAOPsql;
import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

            PreparedStatement statement = connection.prepareStatement("INSERT INTO reiziger VALUES (?, ?, ?, ?, ?)");

            statement.setInt(1, reiziger.getId());
            statement.setString(2, reiziger.getVoorletters());
            statement.setString(3, reiziger.getTussenvoegsel());
            statement.setString(4, reiziger.getAchternaam());
            statement.setDate(5, reiziger.getGeboortedatum());

            if(statement.executeUpdate() != 0) {
                if(reiziger.getAdres() == null) return true;
                if (adao.save(reiziger.getAdres())) return true;
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
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public OVChipkaart findByReiziger(Reiziger reiziger) {
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() {
        return null;
    }
}
