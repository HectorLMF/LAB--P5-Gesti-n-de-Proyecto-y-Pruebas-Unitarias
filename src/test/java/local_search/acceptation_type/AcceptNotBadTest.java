package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * Tests exhaustivos para AcceptNotBad
 */
@DisplayName("Tests para AcceptNotBad")
class AcceptNotBadTest {
    
    private AcceptNotBad acceptNotBad;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        acceptNotBad = new AcceptNotBad();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        strategy.setProblem(problem);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(mock(ObjetiveFunction.class));
        when(problem.getFunction()).thenReturn(functions);
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe aceptar mejor candidato en maximización")
    void testAcceptBetterInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(15.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe rechazar peor candidato en maximización")
    void testRejectWorseInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(5.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Debe aceptar igual candidato en maximización")
    void testAcceptEqualInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(10.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe aceptar mejor candidato en minimización")
    void testAcceptBetterInMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(5.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe rechazar peor candidato en minimización")
    void testRejectWorseInMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(15.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Debe ser instancia de AcceptableCandidate")
    void testInstanceOfAcceptableCandidate() {
        assertTrue(acceptNotBad instanceof AcceptableCandidate);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas")
    void testNegativeEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(-5.0);
        State candidate = createState(-2.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe manejar evaluación cero")
    void testZeroEvaluation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(0.0);
        State candidate = createState(0.0);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Múltiples llamadas deben funcionar")
    void testMultipleCalls() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        
        assertTrue(acceptNotBad.acceptCandidate(current, createState(15.0)));
        assertFalse(acceptNotBad.acceptCandidate(current, createState(5.0)));
        assertTrue(acceptNotBad.acceptCandidate(current, createState(10.0)));
    }

    @Test
    @DisplayName("Debe manejar mejora mínima en maximización")
    void testMinimalImprovementMax() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(10.001);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe manejar deterioro mínimo en maximización")
    void testMinimalDeteriorationMax() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(9.999);
        
        Boolean result = acceptNotBad.acceptCandidate(current, candidate);
        assertFalse(result);
    }
}
