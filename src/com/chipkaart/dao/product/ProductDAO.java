package com.chipkaart.dao.product;

import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Product;

import java.util.List;

public interface ProductDAO {

    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    List<Product> findAll();

}
