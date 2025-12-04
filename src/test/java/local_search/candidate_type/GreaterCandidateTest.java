package local_search.candidate_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase GreaterCandidate
 */
@DisplayName("Tests para GreaterCandidate")
class GreaterCandidateTest {

    private GreaterCandidate greaterCandidate;
    private List<State> neighborhood;

    @BeforeEach
    void setUp() {
        greaterCandidate = new GreaterCandidate();
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
    @DisplayName("Debe seleccionar el único estado cuando hay uno solo")
    void testSelectSingleElement() throws Exception {
        neighborhood.add(createState(10.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el estado con mayor evaluación")
    void testSelectGreatestCandidate() throws Exception {
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(3.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(15.0, result.getEvaluation().get(0), "Debe seleccionar el estado con evaluación 15.0");
    }

    @Test
    @DisplayName("Debe seleccionar el mayor cuando hay dos elementos")
    void testSelectGreaterFromTwoElements() throws Exception {
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(20.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(20.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas")
    void testNegativeEvaluations() throws Exception {
        neighborhood.add(createState(-10.0));
        neighborhood.add(createState(-5.0));
        neighborhood.add(createState(-20.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(-5.0, result.getEvaluation().get(0), "Debe seleccionar -5.0 como el mayor");
    }

    @Test
    @DisplayName("Debe manejar evaluaciones mixtas positivas y negativas")
    void testMixedEvaluations() throws Exception {
        neighborhood.add(createState(-10.0));
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(-20.0));
        neighborhood.add(createState(15.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(15.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar primero cuando todos tienen misma evaluación")
    void testAllEqualEvaluations() throws Exception {
        for (int i = 0; i < 5; i++) {
            neighborhood.add(createState(42.0));
        }
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(42.0, result.getEvaluation().get(0));
        // Cuando todos son iguales, debe seleccionar aleatoriamente
        assertTrue(neighborhood.contains(result));
    }

    @Test
    @DisplayName("Debe seleccionar el mayor en vecindario grande")
    void testLargeNeighborhood() throws Exception {
        for (int i = 0; i < 1000; i++) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(999.0, result.getEvaluation().get(0), "Debe seleccionar 999.0 como el mayor");
    }

    @Test
    @DisplayName("Debe manejar evaluación cero")
    void testZeroEvaluation() throws Exception {
        neighborhood.add(createState(-5.0));
        neighborhood.add(createState(0.0));
        neighborhood.add(createState(-10.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el mayor cuando el primero es el mayor")
    void testFirstIsGreatest() throws Exception {
        neighborhood.add(createState(100.0));
        neighborhood.add(createState(50.0));
        neighborhood.add(createState(25.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(100.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el mayor cuando el último es el mayor")
    void testLastIsGreatest() throws Exception {
        neighborhood.add(createState(25.0));
        neighborhood.add(createState(50.0));
        neighborhood.add(createState(100.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(100.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar valores decimales pequeños")
    void testSmallDecimalValues() throws Exception {
        neighborhood.add(createState(0.001));
        neighborhood.add(createState(0.002));
        neighborhood.add(createState(0.0015));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.002, result.getEvaluation().get(0), 0.0001);
    }

    @Test
    @DisplayName("Debe manejar Double.MAX_VALUE")
    void testMaxDoubleValue() throws Exception {
        neighborhood.add(createState(1000.0));
        neighborhood.add(createState(Double.MAX_VALUE));
        neighborhood.add(createState(5000.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(Double.MAX_VALUE, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar valores muy cercanos")
    void testVeryCloseValues() throws Exception {
        neighborhood.add(createState(10.0000001));
        neighborhood.add(createState(10.0000002));
        neighborhood.add(createState(10.0000000));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0000002, result.getEvaluation().get(0), 1e-10);
    }

    @Test
    @DisplayName("Debe ser instancia de SearchCandidate")
    void testInstanceOfSearchCandidate() {
        assertTrue(greaterCandidate instanceof SearchCandidate,
            "GreaterCandidate debe ser instancia de SearchCandidate");
    }

    @Test
    @DisplayName("Múltiples llamadas consecutivas deben retornar el mayor")
    void testMultipleConsecutiveCalls() throws Exception {
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(10.0));
        
        for (int i = 0; i < 10; i++) {
            State result = greaterCandidate.stateSearch(neighborhood);
            assertNotNull(result);
            assertEquals(15.0, result.getEvaluation().get(0));
        }
    }

    @Test
    @DisplayName("Debe manejar evaluaciones con infinito positivo")
    void testPositiveInfinity() throws Exception {
        neighborhood.add(createState(1000.0));
        neighborhood.add(createState(Double.POSITIVE_INFINITY));
        neighborhood.add(createState(5000.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(Double.POSITIVE_INFINITY, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar correctamente con duplicados del máximo")
    void testDuplicateMaxValues() throws Exception {
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(20.0));
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(20.0));
        neighborhood.add(createState(5.0));
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(20.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar secuencia descendente")
    void testDescendingSequence() throws Exception {
        for (int i = 100; i > 0; i--) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(100.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar secuencia ascendente")
    void testAscendingSequence() throws Exception {
        for (int i = 1; i <= 100; i++) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = greaterCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(100.0, result.getEvaluation().get(0));
    }
}
