package dao;

import entity.Drivers;

import java.util.List;

public interface DriverDao {

    void addNewDriver (Drivers drivers);

    List<Drivers> getAll ();
}
