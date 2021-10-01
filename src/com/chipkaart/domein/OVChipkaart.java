package com.chipkaart.domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {

    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    private List<Product> producten;

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.producten = new ArrayList<>();
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void addProduct(Product product) {
        if (!producten.contains(product)) this.producten.add(product);
        if (!product.getOvChipkaarten().contains(this)) product.addOvChipkaart(this);
    }

    public void removeProduct(Product product) {
        this.producten.remove(product);
        product.removeOvChipkaart(this);
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot='" + geldigTot + '\'' +
                ", klasse='" + klasse + '\'' +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger.getNaam() +
                ", producten=" + producten +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OVChipkaart that = (OVChipkaart) o;
        return kaartNummer == that.kaartNummer;
    }
}
