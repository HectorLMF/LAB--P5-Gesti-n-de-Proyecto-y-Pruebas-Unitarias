package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveHillClimbingRestartTest {

    @BeforeEach
    public void setUp() {
        // ensure Strategy has a problem to avoid NPE in tests that touch Strategy
        Strategy.setProblem(new Problem());
    }

    @Test
    public void testConstructorAndType() {
        MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();
        assertNotNull(gen);
        // default generator type set in constructor
        assertEquals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_RESTART, gen.getType());
    }

    @Test
    public void testSetAndGetReferenceAndList() {
        MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();

        // build a simple state with evaluation and code
        State s = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(1.23);
        eval.add(4.56);
        s.setEvaluation(eval);
        ArrayList<Object> code = new ArrayList<>();
        code.add("id-1");
        s.setCode(code);

        // set as initial reference and verify
        gen.setInitialReference(s);
        assertSame(s, gen.getReference(), "getReference should return the same object passed to setInitialReference");

        // getReferenceList should add a copy of the reference to its internal list
        java.util.List<State> refs = gen.getReferenceList();
        assertNotNull(refs);
        assertTrue(refs.size() >= 1, "Reference list must contain at least one element after getReferenceList");
        State copied = refs.get(refs.size() - 1);
        assertNotSame(s, copied, "getReferenceList should store a copy, not the same instance");
        assertEquals(s.getEvaluation().size(), copied.getEvaluation().size(), "Copied state must preserve evaluation size");
    }

    @Test
    public void testSetStateRefAndGeneratorTypeAccessors() {
        MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();
        State s = new State();
        gen.setStateRef(s);
        assertSame(s, gen.getReference());

        gen.setGeneratorType(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_RESTART);
        assertEquals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_RESTART, gen.getGeneratorType());
    }

}

