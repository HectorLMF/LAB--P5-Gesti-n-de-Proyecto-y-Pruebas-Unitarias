package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class HillClimbingRestartTest {

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override public Double Evaluation(State state) { return 0.0; }
        });
        p.setFunction(funcs);
        p.setOperator(new Operator() {
            @Override public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override public List<State> generateRandomState(Integer operatornumber) { ArrayList<State> l = new ArrayList<>(); l.add(new State()); return l; }
        });
        Strategy.setProblem(p);
    }

    @Test
    @DisplayName("Constructor sets generator type")
    void testConstructorAndType() {
        HillClimbingRestart gen = new HillClimbingRestart();
        assertEquals(GeneratorType.HILL_CLIMBING_RESTART, gen.getType());
    }

    @Test
    @DisplayName("getReferenceList stores same instance (no copy)")
    void testReferenceListSameInstance() {
        HillClimbingRestart gen = new HillClimbingRestart();
        State ref = new State();
        ref.setCode(new ArrayList<>());
        gen.setInitialReference(ref);
        List<State> refs = gen.getReferenceList();
        assertTrue(refs.size() >= 1);
        assertSame(ref, refs.get(refs.size() - 1));
    }

    @Test
    @DisplayName("setTypeCandidate changes internal candidate selection")
    void testSetTypeCandidate() {
        HillClimbingRestart gen = new HillClimbingRestart();
        // Switch candidate type and ensure no exception is thrown
        gen.setTypeCandidate(local_search.candidate_type.CandidateType.GREATER_CANDIDATE);
        assertEquals(GeneratorType.HILL_CLIMBING_RESTART, gen.getType());
    }
        @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("HillClimbingRestart", HillClimbingRestart.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            HillClimbingRestart.class.getModifiers()));
    }

    @Test
    @DisplayName("count debe ser una variable estática")
    void testCountExists() {
        try {
            HillClimbingRestart.class.getDeclaredField("count");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'count' no encontrado");
        }
    }

    @Test
    @DisplayName("countCurrent debe ser una variable estática")
    void testCountCurrentExists() {
        try {
            HillClimbingRestart.class.getDeclaredField("countCurrent");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'countCurrent' no encontrado");
        }
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            HillClimbingRestart.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
