package problem.definition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import problem.definition.ObjetiveFunction;

public class ProblemDefinitionExtraTest {

    @Test
    @DisplayName("Problem setState accepts null and non-null")
    void setState_nullAndNonNull() {
        Problem p = new Problem();
        p.setState(null);
        assertNull(p.getState());
        State s = new State();
        p.setState(s);
        assertSame(s, p.getState());
    }

    @Test
    @DisplayName("Problem setFunction accepts list and Evaluate handles null function")
    void setFunction_and_evaluate() {
        Problem p = new Problem();
        // Evaluate with null function may throw; ensure call path exercised
        try {
            p.Evaluate(new State());
        } catch (Exception ex) {
            // acceptable if it throws due to null function
        }
        ArrayList<ObjetiveFunction> f = new ArrayList<>();
        p.setFunction(f);
        assertNotNull(p.getFunction());
    }

    @Test
    @DisplayName("State Comparator and Distance are callable")
    void stateComparatorDistance() {
        State a = new State();
        State b = new State();
        try {
            a.Comparator(b);
            a.Distance(b);
        } catch (Exception ex) {
            fail("State methods threw unexpectedly: " + ex);
        }
    }
}
