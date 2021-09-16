package com.chipkaart;

import com.chipkaart.dao.ovchipkaart.OVChipkaartDAO;
import com.chipkaart.dao.ovchipkaart.OVChipkaartDAOPsql;
import com.chipkaart.domein.Adres;
import com.chipkaart.dao.adres.AdresDAO;
import com.chipkaart.dao.adres.AdresDAOPsql;
import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Reiziger;
import com.chipkaart.dao.reiziger.ReizigerDAO;
import com.chipkaart.dao.reiziger.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static Connection connection;

    /**
     * Deze methode maakt de connectie naar de database
     *
     * @throws SQLException
     * @throws NullPointerException
     */
    private static void getConnection() throws SQLException, NullPointerException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip?user=postgres&password=0000");
        }
    }

    /**
     * Deze methode sluit de connectie met de database
     *
     * @throws SQLException
     */
    private static void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Main methode
     */
    public static void main(String[] args) throws SQLException {
        getConnection();

        ReizigerDAO rdao = new ReizigerDAOPsql(connection, new AdresDAOPsql(connection), new OVChipkaartDAOPsql(connection));
        AdresDAO adao = new AdresDAOPsql(connection, new ReizigerDAOPsql(connection, new OVChipkaartDAOPsql(connection)));
        OVChipkaartDAO odao = new OVChipkaartDAOPsql(connection, new ReizigerDAOPsql(connection, new AdresDAOPsql(connection)));

        testReizigerDAO(rdao);
        testAdresDAO(adao, rdao);
        testOVChipkaartDAO(odao, rdao);

        closeConnection();
    }


    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     */
    private static void testReizigerDAO(ReizigerDAO rdao) {
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

    /**
     * Deze methode test de CRUD-functionaliteit van AdresDAO
     *
     */
    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Voeg een nieuw adres toe in de database
        System.out.println("[Test] Reiziger voor toevoeging adres:");
        System.out.println(rdao.findById(5));
        Adres a = new Adres(5, "3411AH", "1A", "Wilhelmusplein", "Vlissingen", rdao.findById(5));
        adao.save(a);
        System.out.println("[Test] Reiziger na toevoeging adres:");
        System.out.println(rdao.findById(5));
        System.out.println();

        // Verander het adres in de database
        System.out.println("[Test] Adres voor update:");
        System.out.println(a);
        a.setHuisnummer("2B");
        a.setPostcode("3214FD");
        adao.update(a);
        System.out.println("[Test] Adres na update:");
        System.out.println(a);
        System.out.println();

        // Haal adres van reiziger op uit de database
        System.out.println("[Test] Adres van reiziger " + rdao.findById(4).getNaam() + ":");
        System.out.println(adao.findByReiziger(rdao.findById(4)));
        System.out.println();

        // Verwijderd het nieuwe adres in de database
        adressen = adao.findAll();
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(a);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

    }

    /**
     * Deze methode test de CRUD-functionaliteit van OVChipkaartDAO
     *
     */
    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao) {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle adressen op uit de database
        List<OVChipkaart> ovChipkaartList = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende ov-chipkaarten:");
        for (OVChipkaart o : ovChipkaartList) {
            System.out.println(o);
        }
        System.out.println();

        // Voeg een nieuwe ov-chipkaart aan de reiziger toe in de database
        System.out.println("[Test] Reiziger voor toevoeging ov-chipkaart:");
        System.out.println(rdao.findById(1));
        OVChipkaart o = new OVChipkaart(18329, java.sql.Date.valueOf("2017-03-14"), 1, 25.50 , rdao.findById(1));
        odao.save(o);
        System.out.println("[Test] Reiziger na toevoeging ov-chipkaart:");
        System.out.println(rdao.findById(1));
        System.out.println();

        // Verander de ov-chipkaart in de database
        System.out.println("[Test] OV-Chipkaart voor update:");
        System.out.println(o);
        o.setKlasse(2);
        o.setSaldo(135.50);
        odao.update(o);
        System.out.println("[Test] OV-Chipkaart na update:");
        System.out.println(o);
        System.out.println();

        // Haal ov-chipkaarten van reiziger op uit de database
        System.out.println("[Test] OV-Chipkaarten van reiziger " + rdao.findById(4).getNaam() + ":");
        System.out.println(odao.findByReiziger(rdao.findById(4)));
        System.out.println();

        // Verwijderd het nieuwe adres in de database
        ovChipkaartList = odao.findAll();
        System.out.print("[Test] Eerst " + ovChipkaartList.size() + " ov-chipkaarten, na OVChipkaartDAO.delete() ");
        odao.delete(o);
        ovChipkaartList = odao.findAll();
        System.out.println(ovChipkaartList.size() + " ov-chipkaarten\n");

    }
}
