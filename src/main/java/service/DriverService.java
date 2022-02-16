package service;

import entity.Driver;
import main.MainRun;
import dao.DriverDao;
import repository.DriverDaoImpl;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DriverService {
    public DriverDao driverDao = new DriverDaoImpl();

    public void showDriver() {
        for (Driver drivers : MainRun.driverList) System.out.println(drivers);
    }

    public void addNewDriver() {
        System.out.print("Nhập số lái xe muốn thêm mới: ");
        int driverNumber = -1;
        do {
            try {
                driverNumber = new Scanner(System.in).nextInt();
                if (driverNumber > 0) {
                    break;
                }
                System.out.print("Số lái xe muốn thêm mới phải là số nguyên, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lái xe muốn thêm mới phải là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        for (int i = 0; i < driverNumber; i++) {
            Driver drivers = new Driver();
            drivers.inputInfo();
            MainRun.driverList.add(drivers);
            driverDao.addNewDriver(drivers);
        }
    }

    public Driver findDriverById(int driverId) {
        for (Driver drivers : MainRun.driverList) {
            if (drivers.getId() == driverId)
                return drivers;
        }
        return null;
    }

    public void initializeDriverData() {
        List<Driver> driverList = driverDao.getAll();
        if (!CollectionUtil.isNullOrEmpty(driverList)) {
            MainRun.driverList = driverList;
        } else {
            MainRun.driverList = new ArrayList<>();
        }
    }
}
