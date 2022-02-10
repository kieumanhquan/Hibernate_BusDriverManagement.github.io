package dao;

import entity.Line;

import java.util.List;

public interface LineDao {

    void addNewLine (Line line);

    List<Line> getAll ();
}
