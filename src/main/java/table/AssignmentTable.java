package table;

import entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentTable {

    private Driver drivers;

    private List<LineTurn> lineTurns;
}
