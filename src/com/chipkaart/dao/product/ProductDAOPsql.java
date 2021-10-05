package com.chipkaart.dao.product;

import com.chipkaart.dao.ovchipkaart.OVChipkaartDAO;
import com.chipkaart.dao.ovchipkaart.OVChipkaartDAOPsql;
import com.chipkaart.dao.reiziger.ReizigerDAOPsql;
import com.chipkaart.domein.Adres;
import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    private Connection connection;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
    }

    public ProductDAOPsql(Connection connection, OVChipkaartDAOPsql odao, ReizigerDAOPsql rdao) {
        this.connection = connection;
        odao.setRdao(rdao);
        odao.setPdao(this);
        this.odao = odao;
    }

    /**
     * Deze methode slaat een product op in de database.
     */
    @Override
    public boolean save(Product product) {
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?)");

            statement.setInt(1, product.getProductNummer());
            statement.setString(2, product.getNaam());
            statement.setString(3, product.getBeschrijving());
            statement.setDouble(4, product.getPrijs());

            if (statement.executeUpdate() != 0) {
                if (product.getOvChipkaarten() != null) {
                    product.getOvChipkaarten().forEach(ovChipkaart -> addOvChipkaartProduct(ovChipkaart, product));
                }
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode past een product aan in de database.
     */
    @Override
    public boolean update(Product product) {
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");

            statement.setString(1, product.getNaam());
            statement.setString(2, product.getBeschrijving());
            statement.setDouble(3, product.getPrijs());
            statement.setInt(4, product.getProductNummer());

            if (statement.executeUpdate() != 0) {
                if (product.getOvChipkaarten() != null) {
                    product.getOvChipkaarten().forEach(ovChipkaart -> {
                        if (odao.findByProduct(product).contains(ovChipkaart))
                            updateOvChipkaartProduct(ovChipkaart, product);
                        else
                            deleteOvChipkaartProduct(ovChipkaart, product);
                    });
                }
                return true;
            }

        } catch (Exception ignore) {}
        return false;
    }

    /**
     * Deze methode verwijderd een product uit de database.
     */
    @Override
    public boolean delete(Product product) {

        if (product.getOvChipkaarten() != null) {
            product.getOvChipkaarten().forEach(ovChipkaart -> deleteOvChipkaartProduct(ovChipkaart, product));
        }

        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE product_nummer = ?");

            statement.setInt(1, product.getProductNummer());

            if (statement.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode vind alle producten van een ov-chipkaart uit de database.
     */
    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product o " +
                            "JOIN product p on p.product_nummer = o.product_nummer WHERE kaart_nummer = ? ");

            statement.setInt(1, ovChipkaart.getKaartNummer());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Product product = new Product(
                        result.getInt("product_nummer"),
                        result.getString("naam"),
                        result.getString("beschrijving"),
                        result.getDouble("prijs"));

                product.addOvChipkaart(ovChipkaart);

                producten.add(product);
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return producten;
    }

    /**
     * Deze methode vind alle producten uit de database.
     */
    @Override
    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Product product = new Product(
                        result.getInt("product_nummer"),
                        result.getString("naam"),
                        result.getString("beschrijving"),
                        result.getDouble("prijs"));

                odao.findByProduct(product).forEach(ovChipkaart -> product.addOvChipkaart(ovChipkaart));

                producten.add(product);
            }

            result.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return producten;
    }

    /**
     * Deze methode voegt de koppeling tussen product en ov-chipkaart toe in de database.
     */
    private boolean addOvChipkaartProduct(OVChipkaart ovChipkaart, Product product) {
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)");

            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.setInt(2, product.getProductNummer());
            statement.setString(3, "actief");
            statement.setDate(4, new java.sql.Date(System.currentTimeMillis()));

            if (statement.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode past de koppeling tussen product en ov-chipkaart aan in de database.
     */
    private boolean updateOvChipkaartProduct(OVChipkaart ovChipkaart, Product product) {
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE ov_chipkaart_product SET last_update = ?, status = ? WHERE product_nummer = ? AND kaart_nummer = ?");

            statement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(2, "Test");
            statement.setInt(3, product.getProductNummer());
            statement.setInt(4, ovChipkaart.getKaartNummer());

            if (statement.executeUpdate() != 0) {
                return true;
            } else {
                if(addOvChipkaartProduct(ovChipkaart, product)) return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Deze methode verwijderd de koppeling tussen product en ov-chipkaart in de database.
     */
    private boolean deleteOvChipkaartProduct(OVChipkaart ovChipkaart, Product product) {
        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ? AND kaart_nummer = ?");

            statement.setInt(1, ovChipkaart.getKaartNummer());
            statement.setInt(2, product.getProductNummer());

            if (statement.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
