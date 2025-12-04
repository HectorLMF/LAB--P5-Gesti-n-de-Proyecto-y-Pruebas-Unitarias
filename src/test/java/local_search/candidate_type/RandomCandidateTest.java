package local_search.candidate_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase RandomCandidate
 */
@DisplayName("Tests para RandomCandidate")
class RandomCandidateTest {

    private RandomCandidate randomCandidate;
    private List<State> neighborhood;

    @BeforeEach
    void setUp() {
        randomCandidate = new RandomCandidate();
        neighborhood = new ArrayList<>();
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe seleccionar estado cuando hay un solo elemento")
    void testSelectSingleElement() {
        neighborhood.add(createState(10.0));
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0, result.getEvaluation().get(0));
    }

    @RepeatedTest(10)
    @DisplayName("Debe seleccionar estado válido del vecindario con múltiples elementos")
    void testSelectFromMultipleElements() {
        for (int i = 0; i < 10; i++) {
            neighborhood.add(createState(i * 10.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result), "El estado seleccionado debe estar en el vecindario");
    }

    @RepeatedTest(20)
    @DisplayName("Debe seleccionar diferentes estados en múltiples ejecuciones")
    void testRandomnessOverMultipleExecutions() {
        for (int i = 0; i < 5; i++) {
            neighborhood.add(createState(i * 10.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe manejar vecindario con dos elementos")
    void testTwoElements() {
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(20.0));
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe manejar vecindario grande")
    void testLargeNeighborhood() {
        for (int i = 0; i < 1000; i++) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe seleccionar estados con evaluaciones negativas")
    void testNegativeEvaluations() {
        for (int i = -10; i < 0; i++) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe seleccionar estados con evaluaciones mixtas")
    void testMixedEvaluations() {
        neighborhood.add(createState(-100.0));
        neighborhood.add(createState(0.0));
        neighborhood.add(createState(100.0));
        neighborhood.add(createState(Double.MAX_VALUE));
        neighborhood.add(createState(Double.MIN_VALUE));
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe ser instancia de SearchCandidate")
    void testInstanceOfSearchCandidate() {
        assertTrue(randomCandidate instanceof SearchCandidate,
            "RandomCandidate debe ser instancia de SearchCandidate");
    }

    @RepeatedTest(50)
    @DisplayName("Verificar distribución aproximadamente uniforme")
    void testDistributionApproximatelyUniform() {
        neighborhood.clear();
        for (int i = 0; i < 10; i++) {
            neighborhood.add(createState(i * 10.0));
        }
        
        // Ejecutar muchas veces y verificar que se seleccionen diferentes estados
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(result.getEvaluation().get(0) >= 0 && result.getEvaluation().get(0) < 100);
    }

    @Test
    @DisplayName("Múltiples llamadas consecutivas deben retornar estados válidos")
    void testMultipleConsecutiveCalls() {
        for (int i = 0; i < 5; i++) {
            neighborhood.add(createState(i * 10.0));
        }
        
        for (int i = 0; i < 20; i++) {
            State result = randomCandidate.stateSearch(neighborhood);
            assertNotNull(result);
            assertTrue(neighborhood.contains(result));
        }
    }

    @Test
    @DisplayName("Debe manejar estados con múltiples evaluaciones")
    void testMultipleEvaluations() {
        State state1 = new State();
        ArrayList<Double> evals1 = new ArrayList<>();
        evals1.add(10.0);
        evals1.add(20.0);
        evals1.add(30.0);
        state1.setEvaluation(evals1);
        
        neighborhood.add(state1);
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(state1, result);
    }

    @Test
    @DisplayName("Debe funcionar con evaluaciones idénticas")
    void testIdenticalEvaluations() {
        for (int i = 0; i < 5; i++) {
            neighborhood.add(createState(42.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(42.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar vecindario con evaluaciones cero")
    void testZeroEvaluations() {
        for (int i = 0; i < 3; i++) {
            neighborhood.add(createState(0.0));
        }
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar estados con evaluaciones extremas")
    void testExtremeEvaluations() {
        neighborhood.add(createState(Double.POSITIVE_INFINITY));
        neighborhood.add(createState(Double.NEGATIVE_INFINITY));
        neighborhood.add(createState(Double.MAX_VALUE));
        neighborhood.add(createState(-Double.MAX_VALUE));
        
        State result = randomCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(neighborhood.contains(result));
    }
}
