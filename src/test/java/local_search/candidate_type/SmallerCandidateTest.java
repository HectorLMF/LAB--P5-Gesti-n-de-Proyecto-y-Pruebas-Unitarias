package local_search.candidate_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase SmallerCandidate
 */
@DisplayName("Tests para SmallerCandidate")
class SmallerCandidateTest {

    private SmallerCandidate smallerCandidate;
    private List<State> neighborhood;

    @BeforeEach
    void setUp() {
        smallerCandidate = new SmallerCandidate();
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
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el estado con menor evaluación")
    void testSelectSmallestCandidate() throws Exception {
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(3.0));
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(5.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(3.0, result.getEvaluation().get(0), "Debe seleccionar el estado con evaluación 3.0");
    }

    @Test
    @DisplayName("Debe seleccionar el menor cuando hay dos elementos")
    void testSelectSmallerFromTwoElements() throws Exception {
        neighborhood.add(createState(20.0));
        neighborhood.add(createState(10.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas")
    void testNegativeEvaluations() throws Exception {
        neighborhood.add(createState(-10.0));
        neighborhood.add(createState(-5.0));
        neighborhood.add(createState(-20.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(-20.0, result.getEvaluation().get(0), "Debe seleccionar -20.0 como el menor");
    }

    @Test
    @DisplayName("Debe manejar evaluaciones mixtas positivas y negativas")
    void testMixedEvaluations() throws Exception {
        neighborhood.add(createState(-10.0));
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(-20.0));
        neighborhood.add(createState(15.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(-20.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar todos con misma evaluación")
    void testAllEqualEvaluations() throws Exception {
        for (int i = 0; i < 5; i++) {
            neighborhood.add(createState(42.0));
        }
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        // Cuando todos son iguales, devuelve null o el primero según implementación
        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe seleccionar el menor en vecindario grande")
    void testLargeNeighborhood() throws Exception {
        for (int i = 1000; i > 0; i--) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(1.0, result.getEvaluation().get(0), "Debe seleccionar 1.0 como el menor");
    }

    @Test
    @DisplayName("Debe manejar evaluación cero")
    void testZeroEvaluation() throws Exception {
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(0.0));
        neighborhood.add(createState(10.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el menor cuando el primero es el menor")
    void testFirstIsSmallest() throws Exception {
        neighborhood.add(createState(25.0));
        neighborhood.add(createState(50.0));
        neighborhood.add(createState(100.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(25.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar el menor cuando el último es el menor")
    void testLastIsSmallest() throws Exception {
        neighborhood.add(createState(100.0));
        neighborhood.add(createState(50.0));
        neighborhood.add(createState(25.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(25.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar valores decimales pequeños")
    void testSmallDecimalValues() throws Exception {
        neighborhood.add(createState(0.002));
        neighborhood.add(createState(0.001));
        neighborhood.add(createState(0.0015));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.001, result.getEvaluation().get(0), 0.0001);
    }

    @Test
    @DisplayName("Debe manejar -Double.MAX_VALUE")
    void testMinDoubleValue() throws Exception {
        neighborhood.add(createState(1000.0));
        neighborhood.add(createState(-Double.MAX_VALUE));
        neighborhood.add(createState(5000.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(-Double.MAX_VALUE, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar valores muy cercanos")
    void testVeryCloseValues() throws Exception {
        neighborhood.add(createState(10.0000002));
        neighborhood.add(createState(10.0000000));
        neighborhood.add(createState(10.0000001));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0000000, result.getEvaluation().get(0), 1e-10);
    }

    @Test
    @DisplayName("Debe ser instancia de SearchCandidate")
    void testInstanceOfSearchCandidate() {
        assertTrue(smallerCandidate instanceof SearchCandidate,
            "SmallerCandidate debe ser instancia de SearchCandidate");
    }

    @Test
    @DisplayName("Múltiples llamadas consecutivas deben retornar el menor")
    void testMultipleConsecutiveCalls() throws Exception {
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(5.0));
        neighborhood.add(createState(10.0));
        
        for (int i = 0; i < 10; i++) {
            State result = smallerCandidate.stateSearch(neighborhood);
            assertNotNull(result);
            assertEquals(5.0, result.getEvaluation().get(0));
        }
    }

    @Test
    @DisplayName("Debe manejar evaluaciones con infinito negativo")
    void testNegativeInfinity() throws Exception {
        neighborhood.add(createState(1000.0));
        neighborhood.add(createState(Double.NEGATIVE_INFINITY));
        neighborhood.add(createState(5000.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(Double.NEGATIVE_INFINITY, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar correctamente con duplicados del mínimo")
    void testDuplicateMinValues() throws Exception {
        neighborhood.add(createState(20.0));
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(15.0));
        neighborhood.add(createState(10.0));
        neighborhood.add(createState(25.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(10.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar secuencia descendente")
    void testDescendingSequence() throws Exception {
        for (int i = 100; i > 0; i--) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(1.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar secuencia ascendente")
    void testAscendingSequence() throws Exception {
        for (int i = 1; i <= 100; i++) {
            neighborhood.add(createState(i * 1.0));
        }
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(1.0, result.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar valores extremadamente pequeños")
    void testVerySmallValues() throws Exception {
        neighborhood.add(createState(Double.MIN_VALUE));
        neighborhood.add(createState(Double.MIN_VALUE * 2));
        neighborhood.add(createState(0.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertTrue(result.getEvaluation().get(0) <= Double.MIN_VALUE * 2);
    }

    @Test
    @DisplayName("Debe manejar comparación con cero positivo y negativo")
    void testPositiveAndNegativeZero() throws Exception {
        neighborhood.add(createState(0.0));
        neighborhood.add(createState(-0.0));
        neighborhood.add(createState(1.0));
        
        State result = smallerCandidate.stateSearch(neighborhood);
        
        assertNotNull(result);
        assertEquals(0.0, Math.abs(result.getEvaluation().get(0)), 0.0001);
    }
}
