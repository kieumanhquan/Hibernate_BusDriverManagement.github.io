package dao;

import entity.Driver;

import java.util.List;

public interface DriverDao {

    void addNewDriver (Driver drivers);

    List<Driver> getAll ();
}
