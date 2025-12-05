package targeted;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import evolutionary_algorithms.complement.RouletteSelection;
import problem.definition.State;

public class RouletteSelectionTargetedTest {

    @Test
    public void selection_returnsStatesFromInput() {
        RouletteSelection sel = new RouletteSelection();
        List<State> pool = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(1.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(2.0)));
        State s3 = new State(); s3.setEvaluation(new ArrayList<Double>(List.of(3.0)));
        pool.add(s1); pool.add(s2); pool.add(s3);

        List<State> parents = sel.selection(pool, 0);
        assertEquals(pool.size(), parents.size());
        for (State p : parents) {
            assertTrue(pool.contains(p));
        }
    }
}
