package table;

import entity.Drivers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentTable {

    private Drivers drivers;

    private List<LineTurn> lineTurns;
}
