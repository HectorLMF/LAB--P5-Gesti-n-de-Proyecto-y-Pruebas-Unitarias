package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StateNullHandlingTest {
    @Test
    void getSetCodeHandlesNullSafely() {
        State s = new State();
        s.setCode(null);
        assertNotNull(s.getCode());
        assertEquals(0, s.getCode().size());
    }

    @Test
    void getSetEvaluationHandlesNullSafely() {
        State s = new State();
        s.setEvaluation(null);
        // getEvaluation should return null when internal is null
        assertEquals(null, s.getEvaluation());

        ArrayList<Double> eval = new ArrayList<>();
        eval.add(2.0);
        s.setEvaluation(eval);
        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
    }
}
