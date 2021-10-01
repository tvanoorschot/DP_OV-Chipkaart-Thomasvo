package com.chipkaart.domein;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private int productNummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    private List<OVChipkaart> ovChipkaarten;

    public Product(int kaartNummer, String naam, String beschrijving, double prijs) {
        this.productNummer = kaartNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaarten = new ArrayList<>();
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void addOvChipkaart(OVChipkaart ovChipkaarten) {
        this.ovChipkaarten.add(ovChipkaarten);
    }

    public void removeOvChipkaart(OVChipkaart ovChipkaarten) {
        this.ovChipkaarten.remove(ovChipkaarten);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNummer=" + productNummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                ", ovChipkaarten=" + ovChipkaarten.size() +
                '}';
    }
}
