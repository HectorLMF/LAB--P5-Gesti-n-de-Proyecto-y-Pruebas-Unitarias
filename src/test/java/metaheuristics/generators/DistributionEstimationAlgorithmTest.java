package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class DistributionEstimationAlgorithmTest {

    @BeforeEach
    public void setUp() throws Exception {
        Strategy.destroyExecute();
        Strategy.setProblem(new Problem());
        // Initialize generator map so DEA constructor getListStateRef() does not NPE
        Strategy.getStrategy().initialize();
    }

    @Test
    public void testConstructorAndType() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        assertEquals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM, dea.getType());
    }

    @Test
    public void testListReferenceAccessors() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        State a = new State();
        ArrayList<Double> ea = new ArrayList<>(); ea.add(1.0); a.setEvaluation(ea);
        State b = new State();
        ArrayList<Double> eb = new ArrayList<>(); eb.add(2.0); b.setEvaluation(eb);

        List<State> list = new ArrayList<>();
        list.add(a); list.add(b);
        dea.setListReference(list);

        // getListReference returns the same internal list (by design)
        assertSame(list, dea.getListReference());

        // getReferenceList returns a copy
        List<State> copy = dea.getReferenceList();
        assertNotSame(list, copy);
        assertEquals(2, copy.size());
    }

    @Test
    public void testMaxValuePicksHighestEvaluation() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        State a = new State();
        ArrayList<Double> ea = new ArrayList<>(); ea.add(1.0); a.setEvaluation(ea);
        State b = new State();
        ArrayList<Double> eb = new ArrayList<>(); eb.add(5.5); b.setEvaluation(eb);
        List<State> list = new ArrayList<>(); list.add(a); list.add(b);

        State best = dea.MaxValue(list);
        assertEquals(5.5, best.getEvaluation().get(0));
    }

    @Test
    public void testAwardUpdateRefChecksPresenceByIdentity() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        State x = new State();
        State y = new State();
        List<State> list = new ArrayList<>(); list.add(x);
        dea.setListReference(list);
        assertTrue(dea.awardUpdateREF(x));
        assertFalse(dea.awardUpdateREF(y));
    }

    @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("DistributionEstimationAlgorithm", 
            DistributionEstimationAlgorithm.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            DistributionEstimationAlgorithm.class.getModifiers()));
    }

    @Test
    @DisplayName("sonList debe estar inicializado")
    void testSonListInitialized() {
        assertNotNull(DistributionEstimationAlgorithm.sonList);
    }

    @Test
    @DisplayName("sonList debe ser una List")
    void testSonListType() {
        assertTrue(DistributionEstimationAlgorithm.sonList instanceof java.util.List);
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            DistributionEstimationAlgorithm.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
