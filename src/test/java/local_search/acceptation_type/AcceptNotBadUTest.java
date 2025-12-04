package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * Tests exhaustivos para AcceptNotBadU
 */
@DisplayName("Tests para AcceptNotBadU")
class AcceptNotBadUTest {
    
    private AcceptNotBadU acceptNotBadU;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        acceptNotBadU = new AcceptNotBadU();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        strategy.setProblem(problem);
        strategy.setThreshold(-5.0); // Umbral por defecto
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe aceptar dentro del umbral en maximización")
    void testAcceptWithinThresholdMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(-5.0);
        
        State current = createState(10.0);
        State candidate = createState(8.0); // Diferencia: -2, dentro del umbral -5
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe rechazar fuera del umbral en maximización")
    void testRejectOutsideThresholdMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(-5.0);
        
        State current = createState(10.0);
        State candidate = createState(4.0); // Diferencia: -6, fuera del umbral
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Debe aceptar mejor en maximización")
    void testAcceptBetterMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(-5.0);
        
        State current = createState(10.0);
        State candidate = createState(15.0); // Mejora
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe aceptar dentro del umbral en minimización")
    void testAcceptWithinThresholdMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        strategy.setThreshold(5.0);
        
        State current = createState(10.0);
        State candidate = createState(12.0); // Diferencia: 2, dentro del umbral 5
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe rechazar fuera del umbral en minimización")
    void testRejectOutsideThresholdMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        strategy.setThreshold(5.0);
        
        State current = createState(10.0);
        State candidate = createState(16.0); // Diferencia: 6, fuera del umbral
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Debe ser instancia de AcceptableCandidate")
    void testInstanceOfAcceptableCandidate() {
        assertTrue(acceptNotBadU instanceof AcceptableCandidate);
    }

    @Test
    @DisplayName("Debe manejar umbral cero")
    void testZeroThreshold() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(0.0);
        
        State current = createState(10.0);
        State candidate = createState(9.5);
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas")
    void testNegativeEvaluations() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(-3.0);
        
        State current = createState(-5.0);
        State candidate = createState(-7.0);
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe aceptar en el límite exacto del umbral")
    void testExactThresholdBoundary() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(-5.0);
        
        State current = createState(10.0);
        State candidate = createState(5.0); // Diferencia exacta: -5
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertFalse(result); // Debe ser < threshold, no <=
    }

    @Test
    @DisplayName("Múltiples evaluaciones con diferentes umbrales")
    void testMultipleThresholds() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(7.0);
        
        strategy.setThreshold(-5.0);
        assertTrue(acceptNotBadU.acceptCandidate(current, candidate));
        
        strategy.setThreshold(-2.0);
        assertFalse(acceptNotBadU.acceptCandidate(current, candidate));
    }

    @Test
    @DisplayName("Debe manejar umbral positivo en maximización")
    void testPositiveThresholdMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        strategy.setThreshold(5.0);
        
        State current = createState(10.0);
        State candidate = createState(8.0);
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe manejar umbral negativo en minimización")
    void testNegativeThresholdMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        strategy.setThreshold(-5.0);
        
        State current = createState(10.0);
        State candidate = createState(12.0);
        
        Boolean result = acceptNotBadU.acceptCandidate(current, candidate);
        assertFalse(result);
    }
}
