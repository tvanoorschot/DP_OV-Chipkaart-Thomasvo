package com.chipkaart.dao.reiziger;

import com.chipkaart.domein.Reiziger;

import java.util.List;

public interface ReizigerDAO {

    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(int id);
    List<Reiziger> findByGbdatum(String datum);
    List<Reiziger> findAll();

}
