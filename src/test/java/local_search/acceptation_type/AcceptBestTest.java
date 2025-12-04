package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * Tests exhaustivos para la clase AcceptBest
 */
@DisplayName("Tests para AcceptBest")
class AcceptBestTest {

    private AcceptBest acceptBest;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        acceptBest = new AcceptBest();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        strategy.setProblem(problem);
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe aceptar candidato mejor en problema de maximización")
    void testAcceptBetterCandidateMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(15.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato mejor en maximización");
    }

    @Test
    @DisplayName("Debe rechazar candidato peor en problema de maximización")
    void testRejectWorseCandidateMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(5.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertFalse(result, "Debe rechazar candidato peor en maximización");
    }

    @Test
    @DisplayName("Debe aceptar candidato igual en problema de maximización")
    void testAcceptEqualCandidateMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(10.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato igual en maximización");
    }

    @Test
    @DisplayName("Debe aceptar candidato mejor en problema de minimización")
    void testAcceptBetterCandidateMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(5.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato mejor en minimización");
    }

    @Test
    @DisplayName("Debe rechazar candidato peor en problema de minimización")
    void testRejectWorseCandidateMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(15.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertFalse(result, "Debe rechazar candidato peor en minimización");
    }

    @Test
    @DisplayName("Debe aceptar candidato igual en problema de minimización")
    void testAcceptEqualCandidateMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(10.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato igual en minimización");
    }

    @ParameterizedTest
    @ValueSource(doubles = {15.0, 20.0, 100.0, 1000.0})
    @DisplayName("Debe aceptar múltiples candidatos mejores en maximización")
    void testAcceptMultipleBetterCandidatesMax(double evaluation) throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(evaluation);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato con evaluación " + evaluation);
    }

    @ParameterizedTest
    @ValueSource(doubles = {5.0, 2.0, 1.0, 0.5})
    @DisplayName("Debe aceptar múltiples candidatos mejores en minimización")
    void testAcceptMultipleBetterCandidatesMin(double evaluation) throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(evaluation);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato con evaluación " + evaluation);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas en maximización")
    void testNegativeEvaluationsMaximization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(-5.0);
        State betterCandidate = createState(-2.0);
        State worseCandidate = createState(-10.0);
        
        assertTrue(acceptBest.acceptCandidate(current, betterCandidate));
        assertFalse(acceptBest.acceptCandidate(current, worseCandidate));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas en minimización")
    void testNegativeEvaluationsMinimization() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(-5.0);
        State betterCandidate = createState(-10.0);
        State worseCandidate = createState(-2.0);
        
        assertTrue(acceptBest.acceptCandidate(current, betterCandidate));
        assertFalse(acceptBest.acceptCandidate(current, worseCandidate));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones cero")
    void testZeroEvaluations() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(0.0);
        State candidate = createState(0.0);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato con evaluación cero igual");
    }

    @Test
    @DisplayName("Debe manejar valores extremos positivos en maximización")
    void testExtremePositiveValuesMax() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(Double.MAX_VALUE - 1);
        State candidate = createState(Double.MAX_VALUE);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato con MAX_VALUE");
    }

    @Test
    @DisplayName("Debe manejar valores extremos negativos en minimización")
    void testExtremeNegativeValuesMin() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current = createState(-Double.MAX_VALUE + 1);
        State candidate = createState(-Double.MAX_VALUE);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar candidato con -MAX_VALUE");
    }

    @Test
    @DisplayName("Debe ser instancia de AcceptableCandidate")
    void testInstanceOfAcceptableCandidate() {
        assertTrue(acceptBest instanceof AcceptableCandidate,
            "AcceptBest debe ser instancia de AcceptableCandidate");
    }

    @Test
    @DisplayName("Transición de peor a mejor en maximización")
    void testTransitionWorseToBetterMax() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current1 = createState(5.0);
        State candidate1 = createState(3.0);
        assertFalse(acceptBest.acceptCandidate(current1, candidate1));
        
        State current2 = createState(5.0);
        State candidate2 = createState(10.0);
        assertTrue(acceptBest.acceptCandidate(current2, candidate2));
    }

    @Test
    @DisplayName("Transición de peor a mejor en minimización")
    void testTransitionWorseToBetterMin() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State current1 = createState(5.0);
        State candidate1 = createState(8.0);
        assertFalse(acceptBest.acceptCandidate(current1, candidate1));
        
        State current2 = createState(5.0);
        State candidate2 = createState(2.0);
        assertTrue(acceptBest.acceptCandidate(current2, candidate2));
    }

    @Test
    @DisplayName("Debe rechazar mejora mínima insuficiente en maximización")
    void testRejectMinimalImprovementMax() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(9.999);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertFalse(result, "Debe rechazar mejora insuficiente");
    }

    @Test
    @DisplayName("Debe aceptar mejora mínima en maximización")
    void testAcceptMinimalImprovementMax() throws Exception {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State current = createState(10.0);
        State candidate = createState(10.001);
        
        Boolean result = acceptBest.acceptCandidate(current, candidate);
        assertTrue(result, "Debe aceptar mejora mínima");
    }

    @Test
    @DisplayName("Debe manejar cambio de tipo de problema")
    void testProblemTypeSwitch() throws Exception {
        State current = createState(10.0);
        State candidate = createState(15.0);
        
        // Primero maximización
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        assertTrue(acceptBest.acceptCandidate(current, candidate));
        
        // Luego minimización
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        assertFalse(acceptBest.acceptCandidate(current, candidate));
    }
}
