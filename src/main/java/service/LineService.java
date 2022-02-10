package service;

import entity.Line;
import main.MainRun;
import dao.LineDao;
import repository.LineDaoImpl;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LineService {

    public LineDao lineDao = new LineDaoImpl();

    public void showLine() {
        for (Line line : MainRun.lineList) System.out.println(line);
    }

    public void addNewLine() {
        System.out.print("Nhập số tuyến muốn thêm mới: ");
        int lineNumber = -1;
        do {
            try {
                lineNumber = new Scanner(System.in).nextInt();
                if (lineNumber > 0) {
                    break;
                }
                System.out.print("Số tuyến muốn thêm mới phải là số nguyên, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số tuyến muốn thêm mới phải là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        for (int i = 0; i < lineNumber; i++) {
            Line line = new Line();
            line.inputInfo();
            MainRun.lineList.add(line);
            lineDao.addNewLine(line);
        }
    }

    public Line findLineById(int lineId) {
        for (Line line : MainRun.lineList) {
            if (line.getId() == lineId)
                return line;
        }
        return null;
    }

    public void initializeLineData() {
        List<Line> lineList = lineDao.getAll();
        if (!CollectionUtil.isNullOrEmpty(lineList)) {
            MainRun.lineList = lineList;
        } else {
            MainRun.lineList = new ArrayList<>();
        }
    }
}