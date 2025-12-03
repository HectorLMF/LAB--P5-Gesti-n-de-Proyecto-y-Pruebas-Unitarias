package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StateCoverageTest {
    @Test
    void cloneCreatesDefensiveCopies() {
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        code.add(2);
        State s = new State(code);
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(1.0);
        s.setEvaluation(eval);

        State c = s.getCopy();
        // Ensure collections are different instances
        assertNotSame(s.getCode(), c.getCode());
        assertNotSame(s.getEvaluation(), c.getEvaluation());
        // Values preserved
        assertEquals(s.getCode().size(), c.getCode().size());
        assertEquals(s.getEvaluation().size(), c.getEvaluation().size());
    }

    @Test
    void comparatorAndDistanceBehaviors() {
        ArrayList<Object> code1 = new ArrayList<>();
        code1.add(1);
        code1.add(2);
        State a = new State(code1);

        ArrayList<Object> code2 = new ArrayList<>();
        code2.add(1);
        code2.add(3);
        State b = new State(code2);

        assertTrue(!a.Comparator(b));
        assertEquals(1.0, a.Distance(b));
    }
}
