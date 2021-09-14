package com.chipkaart.dao.adres;

import com.chipkaart.domein.Reiziger;
import com.chipkaart.domein.Adres;

import java.util.List;

public interface AdresDAO {

    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();

}
