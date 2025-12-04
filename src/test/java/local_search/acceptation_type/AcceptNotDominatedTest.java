/**
 * @file AcceptNotDominatedTest.java
 * @brief Comprehensive unit tests for AcceptNotDominated (Pareto dominance acceptance)
 * @author Test Suite
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class AcceptNotDominatedTest
 * @brief Exhaustive test suite for Pareto dominance-based acceptance strategy
 * 
 * Tests cover:
 * - Dominance relationships in maximization/minimization
 * - Empty and non-empty Pareto front management
 * - Multi-objective scenarios (2, 3+ objectives)
 * - Equal solutions handling
 * - Dominated/non-dominated/incomparable solutions
 * - Edge cases: null, empty, identical objectives
 */
public class AcceptNotDominatedTest {

    private AcceptNotDominated acceptNotDominated;
    private Strategy strategy;
    private Problem mockProblem;
    private State stateCurrent;
    private State stateCandidate;

    @BeforeEach
    void setUp() {
        acceptNotDominated = new AcceptNotDominated();
        
        // Create and configure strategy
        strategy = Strategy.getStrategy();
        strategy.listRefPoblacFinal = new ArrayList<>();
        
        // Create mock problem
        mockProblem = mock(Problem.class);
        strategy.setProblem(mockProblem);
        
        // Create mock generator
        MultiGenerator mockGenerator = mock(MultiGenerator.class);
        when(mockGenerator.getType()).thenReturn(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING);
        strategy.generator = mockGenerator;
        
        // Create base states
        stateCurrent = mock(State.class);
        stateCandidate = mock(State.class);
    }

    // ==================== BASIC FUNCTIONALITY TESTS ====================

    @Test
    @DisplayName("Test instance is AcceptableCandidate subclass")
    void testInstanceOfAcceptableCandidate() {
        assertTrue(acceptNotDominated instanceof AcceptableCandidate);
    }

    @Test
    @DisplayName("Test empty Pareto front initialization")
    void testEmptyParetoFrontInitialization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 4.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        
        strategy.listRefPoblacFinal.clear();
        
        acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        // Pareto front should have been initialized
        assertTrue(strategy.listRefPoblacFinal.size() > 0);
    }

    // ==================== MAXIMIZATION TESTS ====================

    @Test
    @DisplayName("Test candidate dominates current in maximization - Accept")
    void testCandidateDominatesCurrentMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 4.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test candidate dominated by current in maximization - Reject")
    void testCandidateDominatedByCurrentMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(6.0, 5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(4.0, 3.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test non-dominated solutions in maximization - both in front")
    void testNonDominatedSolutionsMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        // Current: better in obj1, worse in obj2
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(8.0, 3.0));
        // Candidate: worse in obj1, better in obj2
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 6.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test candidate equal to current in maximization - Reject duplicate")
    void testCandidateEqualToCurrentMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.Comparator(any())).thenReturn(true); // Equal solutions
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    // ==================== MINIMIZATION TESTS ====================

    @Test
    @DisplayName("Test candidate dominates current in minimization - Accept")
    void testCandidateDominatesCurrentMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 4.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(3.0, 2.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test candidate dominated by current in minimization - Reject")
    void testCandidateDominatedByCurrentMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(2.0, 3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 6.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test non-dominated solutions in minimization")
    void testNonDominatedSolutionsMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        // Current: better in obj1, worse in obj2
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(2.0, 8.0));
        // Candidate: worse in obj1, better in obj2
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 3.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    // ==================== MULTI-OBJECTIVE TESTS (3+ objectives) ====================

    @Test
    @DisplayName("Test 3-objective dominance in maximization")
    void testThreeObjectiveDominanceMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 4.0, 3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 5.0, 4.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test 3-objective non-dominated in maximization")
    void testThreeObjectiveNonDominatedMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        // Better in obj1, equal in obj2, worse in obj3
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(8.0, 5.0, 2.0));
        // Worse in obj1, equal in obj2, better in obj3
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 5.0, 7.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test 4-objective complete dominance in minimization")
    void testFourObjectiveDominanceMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 6.0, 7.0, 8.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(4.0, 5.0, 6.0, 7.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    // ==================== PARETO FRONT MANAGEMENT TESTS ====================

    @Test
    @DisplayName("Test candidate removes dominated solution from Pareto front")
    void testCandidateRemovesDominatedFromFront() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Add dominated solution to front
        State dominatedState = mock(State.class);
        when(dominatedState.getEvaluation()).thenReturn(Arrays.asList(3.0, 2.0));
        strategy.listRefPoblacFinal.clear();
        strategy.listRefPoblacFinal.add(dominatedState);
        
        // Current state
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 4.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        
        // Candidate dominates the one in front
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 5.0));
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test candidate rejected when dominated by Pareto front member")
    void testCandidateRejectedByFrontMember() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Add dominant solution to front
        State dominantState = mock(State.class);
        when(dominantState.getEvaluation()).thenReturn(Arrays.asList(10.0, 10.0));
        strategy.listRefPoblacFinal.clear();
        strategy.listRefPoblacFinal.add(dominantState);
        
        // Current state
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(8.0, 8.0));
        
        // Candidate is worse than front member
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(7.0, 7.0));
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test multiple non-dominated solutions added to front")
    void testMultipleNonDominatedAddedToFront() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // First solution
        State state1 = mock(State.class);
        when(state1.getEvaluation()).thenReturn(Arrays.asList(10.0, 2.0));
        when(state1.getCopy()).thenReturn(state1);
        strategy.listRefPoblacFinal.clear();
        strategy.listRefPoblacFinal.add(state1);
        
        // Current state
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(8.0, 4.0));
        
        // Candidate: non-dominated (trade-off)
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 8.0));
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(state1)).thenReturn(false);
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Test with zero values in maximization")
    void testWithZeroValuesMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(0.0, 0.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(1.0, 1.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with negative values in minimization")
    void testWithNegativeValuesMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(-2.0, -3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(-5.0, -6.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with very close values (epsilon)")
    void testWithEpsilonValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(1.0000001, 2.0000001));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(1.0000002, 2.0000002));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test partial dominance - better in one, equal in others")
    void testPartialDominanceBetterInOneMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0, 5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(6.0, 5.0, 5.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test no dominance - worse in one, better in another")
    void testNoDominanceTradeoff() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(10.0, 2.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(3.0, 9.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    // ==================== SINGLE OBJECTIVE TESTS ====================

    @Test
    @DisplayName("Test single objective maximization - better")
    void testSingleObjectiveMaximizationBetter() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(7.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test single objective minimization - worse")
    void testSingleObjectiveMinimizationWorse() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(3.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test single objective equal values")
    void testSingleObjectiveEqual() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.Comparator(any())).thenReturn(true);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    // ==================== EXTREME VALUE TESTS ====================

    @Test
    @DisplayName("Test with very large values")
    void testWithVeryLargeValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(1e10, 1e10));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(1e11, 1e11));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with very small positive values")
    void testWithVerySmallValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(1e-5, 1e-5));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(1e-6, 1e-6));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test mixed positive and negative values")
    void testMixedPositiveNegativeValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(-5.0, 10.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(-3.0, 12.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    // ==================== COMPLEX PARETO FRONT SCENARIOS ====================

    @Test
    @DisplayName("Test large Pareto front with multiple non-dominated solutions")
    void testLargeParetoFront() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Populate front with multiple non-dominated solutions
        strategy.listRefPoblacFinal.clear();
        for (int i = 1; i <= 5; i++) {
            State state = mock(State.class);
            when(state.getEvaluation()).thenReturn(Arrays.asList(10.0 - i, 2.0 + i));
            strategy.listRefPoblacFinal.add(state);
        }
        
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0));
        
        // Candidate adds new trade-off
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(4.0, 8.0));
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test candidate already exists in Pareto front")
    void testCandidateAlreadyInFront() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Add solution to front
        State existingState = mock(State.class);
        when(existingState.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0));
        strategy.listRefPoblacFinal.clear();
        strategy.listRefPoblacFinal.add(existingState);
        
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(4.0, 4.0));
        
        // Candidate is identical
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0));
        when(stateCandidate.Comparator(existingState)).thenReturn(true);
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test all objectives equal except one in minimization")
    void testAllEqualExceptOneMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0, 5.0, 5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 5.0, 5.0, 3.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test symmetrical non-dominance")
    void testSymmetricalNonDominance() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        // Perfect symmetry: A = (10, 5), B = (5, 10)
        when(stateCurrent.getEvaluation()).thenReturn(Arrays.asList(10.0, 5.0));
        when(stateCandidate.getEvaluation()).thenReturn(Arrays.asList(5.0, 10.0));
        when(stateCurrent.getCopy()).thenReturn(stateCurrent);
        when(stateCandidate.getCopy()).thenReturn(stateCandidate);
        when(stateCandidate.Comparator(any())).thenReturn(false);
        
        strategy.listRefPoblacFinal.clear();
        
        Boolean result = acceptNotDominated.acceptCandidate(stateCurrent, stateCandidate);
        
        assertTrue(result);
    }
}
