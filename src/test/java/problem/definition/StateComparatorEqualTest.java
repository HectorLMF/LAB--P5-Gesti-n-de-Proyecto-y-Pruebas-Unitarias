package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StateComparatorEqualTest {
    @Test
    void comparatorTrueWhenCodesEqual() {
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        code.add(2);
        State a = new State(code);
        State b = new State(code);
        assertTrue(a.Comparator(b));
        assertEquals(0.0, a.Distance(b));
    }
}
