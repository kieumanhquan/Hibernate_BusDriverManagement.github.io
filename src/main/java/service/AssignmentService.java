package service;

import table.AssignmentTable;
import table.LineTurn;
import entity.Assignment;
import entity.Driver;
import entity.Line;
import main.MainRun;
import repository.AssigmnetDaoImpl;
import dao.AssignmentDao;
import util.CollectionUtil;
import util.DataUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AssignmentService {

    private final AssignmentDao assignmentDao = new AssigmnetDaoImpl();

    private static boolean isNullOfEmptyDriverOrLine() {
        return CollectionUtil.isNullOrEmpty(MainRun.driverList) || CollectionUtil.isNullOrEmpty(MainRun.lineList);
    }

    public void showAssignmentTable() {
        for (AssignmentTable assignmentTable : MainRun.assignmentTableList)
            System.out.println(assignmentTable);
    }

    public void creatAssignmentTable() {
        if (isNullOfEmptyDriverOrLine()) {
            return;
        }
        Driver drivers = inputDriverId();

        int lineNumber = inputLineNumber();
        int indexAgssignmentTableExits = findIndexAgssignmentTableExits(drivers.getId());

        if (indexAgssignmentTableExits < 0) {
            // Thêm mới
            List<LineTurn> lineTurns = new ArrayList<>();
            createAssignmentTableList(lineTurns, lineNumber, drivers);
            MainRun.assignmentTableList.add(new AssignmentTable(drivers, lineTurns));
        } else {
            // Update bảng phân công đã có trong hệ thống
            updateOrAddAssignmentTableExits(indexAgssignmentTableExits, lineNumber, drivers);
        }
    }

    private void updateOrAddAssignmentTableExits(int indexAgssignmentTableExitsed, int lineNumber, Driver drivers) {
        for (int i = 0; i < lineNumber; i++) {
            Line line = inputLineId(i, drivers);
            int turnNumber = inputTurnNumber(line);
            int indexAssignmentExits = findIndexAssignmentExits(indexAgssignmentTableExitsed, line.getId());
            int turnSumCurrent = 0;
            turnSumCurrent = MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getLineTurns()
                    .stream().mapToInt(LineTurn::getTurnNumber).sum();
            // Tuyến chưa tồn tại thêm mới vào
            if (indexAssignmentExits < 0) {
                if (turnNumber + turnSumCurrent > 15) {
                    System.out.println("Số tuyến vượt 15 !");
                } else {
                    MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getLineTurns()
                            .add(new LineTurn(line, turnNumber));

                    //Ghi vào DB
                    Assignment assignment = new Assignment(drivers, line, turnNumber);
                    assignmentDao.addNewAssgnment(assignment);
                }
            }
            //Tuyến đã tồn tại update lại
            else {
                turnSumCurrent = turnSumCurrent - MainRun.assignmentTableList.get(indexAgssignmentTableExitsed)
                        .getLineTurns().get(indexAssignmentExits).getTurnNumber();
                if (turnSumCurrent + turnSumCurrent > 15) {
                    System.out.println("Số tuyến vượt 15 !");
                } else {
                    MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getLineTurns()
                            .set(indexAssignmentExits, new LineTurn(line, turnNumber));

                    //Update vào DB
                    Assignment assignment = new Assignment(drivers, line, turnNumber);
                    assignmentDao.update(assignment);
                }
            }
        }
    }

    private int findIndexAssignmentExits(int indexAgssignmentTableExitsed, int id) {
        for (int i = 0; i < MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getLineTurns().size(); i++) {
            if (MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getLineTurns().get(i).getLine().getId() == id)
                return i;
        }
        return -1;
    }

    private void createAssignmentTableList(List<LineTurn> lineTurnList, int lineNumber, Driver drivers) {
        for (int i = 0; i < lineNumber; i++) {
            Line line = inputLineId(i, drivers);
            int turnNumber = inputTurnNumber(line);
            int turnSumCurrent = 0;
            turnSumCurrent = lineTurnList.stream().mapToInt(LineTurn::getTurnNumber).sum();
            if (turnNumber + turnSumCurrent > 15) {
                System.out.println("Số tuyến vượt 15 !");
            } else {
                lineTurnList.add(new LineTurn(line, turnNumber));

                //Ghi vào DB
                Assignment assignment = new Assignment(drivers, line, turnNumber);
                assignmentDao.addNewAssgnment(assignment);
            }
        }
    }

    private Driver inputDriverId() {
        System.out.print("Nhập ID của lái xe mà bạn muốn thêm điểm: ");
        Driver drivers;
        do {
            try {
                int driverId = new Scanner(System.in).nextInt();
                drivers = MainRun.driverService.findDriverById(driverId);
                if (!DataUtil.isEmptyOrNull(drivers)) {
                    break;
                }
                System.out.print("ID lãi xe vừa nhập không tồn tại trong hệ thống, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("ID lái xe phải là một số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return drivers;
    }

    private int inputLineNumber() {
        System.out.print("Nhập số lượng tuyến lãi xe chạy: ");
        int lineNumber = -1;
        do {
            try {
                lineNumber = new Scanner(System.in).nextInt();
                if (lineNumber > 0 && lineNumber <= MainRun.lineList.size()) {
                    break;
                }
                System.out.print("Số lượng tuyến là số nguyên và không vượt qua số tuyến có sẵn, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tuyến là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        return lineNumber;
    }

    private int findIndexAgssignmentTableExits(int driverId) {
        for (int i = 0; i < MainRun.assignmentTableList.size(); i++) {
            if (driverId == MainRun.assignmentTableList.get(i).getDrivers().getId())
                return i;
        }
        return -1;
    }

    private Line inputLineId(int j, Driver drivers) {
        System.out.print("Nhập ID tuyến đường thứ " + (j + 1) + " mà lái xe " + drivers.getFullName() + " lái: ");
        Line line;
        do {
            try {
                int lineId = new Scanner(System.in).nextInt();
                line = MainRun.lineService.findLineById(lineId);
                if (!DataUtil.isEmptyOrNull(line)) {
                    break;
                }
                System.out.print("ID môn học vừa nhập không tồn tại trong hệ thống, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("ID môn học phải là số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return line;
    }

    private int inputTurnNumber(Line line) {
        System.out.print("Nhập số lượt của tuyến " + line.getId() + " :");
        int turnNumber = -1;
        do {
            try {
                turnNumber = new Scanner(System.in).nextInt();
                if (turnNumber > 0) {
                    break;
                }
                System.out.print("Số lượt là số nguyên dương, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lượt là số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return turnNumber;
    }

    public void sortByNameDriver() {
        MainRun.assignmentTableList.sort((o1, o2) -> {
            String[] ten1 = o1.getDrivers().getFullName().split("\\s+");
            String[] ten2 = o2.getDrivers().getFullName().split("\\s+");
            if (ten1[ten1.length - 1].equalsIgnoreCase(ten2[ten2.length - 1])) {
                return o2.getDrivers().getFullName().compareToIgnoreCase(o1.getDrivers().getFullName());
            } else {
                return ten2[ten2.length - 1].compareToIgnoreCase(ten1[ten1.length - 1]);
            }
        });
    }

    public void sortByTurnNumber() {
        MainRun.assignmentTableList.sort((o1, o2) -> o2.getLineTurns().size() - o1.getLineTurns().size());
    }

    public void distanceStatistics() {
        for (AssignmentTable assignmentTable : MainRun.assignmentTableList) {
            System.out.println(assignmentTable.getDrivers() + "Tổng khoảng chạy trong ngày: "
                    + assignmentTable.getLineTurns().stream().mapToDouble(LineTurn::getDistance).sum());
        }
    }

    public void initializeAssignmentTableData() {
        MainRun.assignmentTableList = assignmentDao.getAll();
        if (CollectionUtil.isNullOrEmpty(MainRun.assignmentTableList)) {
            MainRun.assignmentTableList = new ArrayList<>();
        }
    }
}
