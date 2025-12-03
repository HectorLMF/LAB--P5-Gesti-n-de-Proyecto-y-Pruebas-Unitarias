package problem.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import metaheuristics.generators.GeneratorType;

public class StateConstructorsTest {
    @Test
    void defaultConstructor() {
        State s = new State();
        assertNotNull(s.getCode());
        assertEquals(0, s.getCode().size());
    }

    @Test
    void constructorWithCode() {
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        code.add(2);
        State s = new State(code);
        assertEquals(2, s.getCode().size());
    }

    @Test
    void copyConstructor() {
        State original = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        original.setCode(code);
        original.setNumber(5);
        original.setTypeGenerator(GeneratorType.GENETIC_ALGORITHM);
        
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(10.0);
        original.setEvaluation(eval);

        State copy = new State(original);
        assertEquals(1, copy.getCode().size());
        assertEquals(5, copy.getNumber());
        assertEquals(GeneratorType.GENETIC_ALGORITHM, copy.getTypeGenerator());
        assertEquals(1, copy.getEvaluation().size());
    }
}
