package com.chipkaart.dao.ovchipkaart;

import com.chipkaart.domein.OVChipkaart;
import com.chipkaart.domein.Product;
import com.chipkaart.domein.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findByProduct(Product product);
    List<OVChipkaart> findAll();

}
