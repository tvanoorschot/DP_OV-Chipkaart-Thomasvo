package com.chipkaart;

import com.chipkaart.domein.reiziger.Reiziger;
import com.chipkaart.domein.reiziger.ReizigerDAO;
import com.chipkaart.domein.reiziger.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static Connection connection;

    private static void getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip?user=postgres&password=0000");
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }


    public static void main(String[] args) throws SQLException {
        getConnection();
        testReizigerDAO(new ReizigerDAOPsql(connection));
        closeConnection();
    }




    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal een reizigers op via id uit de database
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reiziger:");
        System.out.println(rdao.findById(77));
        System.out.println();

        // Haal een reizigers op via geboortedatum uit de database
        reizigers = rdao.findByGbdatum(gbdatum);
        System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reiziger(s):");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Verander de reiziger in de database
        System.out.println("[Test] Reiziger voor update:");
        System.out.println(rdao.findById(77));
        sietske.setVoorletters("A");
        sietske.setTussenvoegsel("de");
        sietske.setAchternaam("Bakker");
        sietske.setGeboortedatum(java.sql.Date.valueOf("1982-11-27"));
        rdao.update(sietske);
        System.out.println("[Test] Reiziger na update:");
        System.out.println(rdao.findById(77));
        System.out.println();

        // Verwijderd de nieuwe reiziger in de database
        reizigers = rdao.findAll();
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


    }
}
