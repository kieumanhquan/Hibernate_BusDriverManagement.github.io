package dao;

import table.AssignmentTable;
import entity.Assignment;

import java.util.List;

public interface AssignmentDao {

    void addNewAssgnment (Assignment assignment);

    List<AssignmentTable> getAll();

    void update (Assignment assignment);
}