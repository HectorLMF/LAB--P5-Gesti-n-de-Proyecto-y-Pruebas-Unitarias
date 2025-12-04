/**
 * @file DominanceTest.java
 * @brief Comprehensive unit tests for Dominance (Pareto dominance operations)
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
 * @class DominanceTest
 * @brief Exhaustive test suite for Pareto dominance operations
 * 
 * Tests cover:
 * - Basic dominance relationships (dominates/dominated/non-dominated)
 * - ListDominance operations (front management)
 * - Maximization and minimization scenarios
 * - Multi-objective cases (2, 3, 4+ objectives)
 * - Edge cases: equal solutions, empty lists, duplicates
 * - Boundary conditions: zero, negative, extreme values
 */
public class DominanceTest {

    private Dominance dominance;
    private Strategy strategy;
    private Problem mockProblem;
    private State solutionX;
    private State solutionY;

    @BeforeEach
    void setUp() {
        dominance = new Dominance();
        
        // Configure strategy
        strategy = Strategy.getStrategy();
        strategy.listRefPoblacFinal = new ArrayList<>();
        
        // Create mock problem
        mockProblem = mock(Problem.class);
        strategy.setProblem(mockProblem);
        
        // Create mock generator
        MultiGenerator mockGenerator = mock(MultiGenerator.class);
        when(mockGenerator.getType()).thenReturn(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE);
        strategy.generator = mockGenerator;
        
        // Create base solutions
        solutionX = mock(State.class);
        solutionY = mock(State.class);
    }

    // ==================== BASIC DOMINANCE TESTS - MAXIMIZATION ====================

    @Test
    @DisplayName("Test clear dominance in maximization - X dominates Y")
    void testClearDominanceMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(4.0, 3.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test no dominance in maximization - Y better")
    void testNoDominanceYBetterMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 2.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 6.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test partial dominance - better in one objective only")
    void testPartialDominanceMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(7.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(7.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result); // Equal, not dominating
    }

    @Test
    @DisplayName("Test dominance with one better, others equal in maximization")
    void testDominanceOneBetterMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 5.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test trade-off solutions - non-dominated in maximization")
    void testTradeoffNonDominatedMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 2.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 9.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    // ==================== BASIC DOMINANCE TESTS - MINIMIZATION ====================

    @Test
    @DisplayName("Test clear dominance in minimization - X dominates Y")
    void testClearDominanceMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(2.0, 3.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 6.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test no dominance in minimization - Y better")
    void testNoDominanceYBetterMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(8.0, 9.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(2.0, 3.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test dominance with one better, others equal in minimization")
    void testDominanceOneBetterMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 5.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test trade-off solutions - non-dominated in minimization")
    void testTradeoffNonDominatedMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(2.0, 10.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(9.0, 3.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    // ==================== EQUAL SOLUTIONS TESTS ====================

    @Test
    @DisplayName("Test equal solutions in maximization")
    void testEqualSolutionsMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result); // Equal solutions don't dominate
    }

    @Test
    @DisplayName("Test equal solutions in minimization")
    void testEqualSolutionsMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 3.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 3.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    // ==================== MULTI-OBJECTIVE TESTS (3+ objectives) ====================

    @Test
    @DisplayName("Test 3-objective dominance in maximization")
    void testThreeObjectiveDominanceMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 7.0, 8.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 6.0, 7.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test 3-objective non-dominance")
    void testThreeObjectiveNonDominance() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(8.0, 5.0, 2.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 7.0, 9.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Test 4-objective complete dominance in minimization")
    void testFourObjectiveDominanceMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(2.0, 3.0, 4.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test 5-objective partial dominance")
    void testFiveObjectivePartialDominance() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(7.0, 6.0, 5.0, 5.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 5.0, 5.0, 5.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    // ==================== LIST DOMINANCE TESTS ====================

    @Test
    @DisplayName("Test ListDominance with empty list")
    void testListDominanceEmptyList() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(any())).thenReturn(false);
        
        List<State> list = new ArrayList<>();
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test ListDominance adds non-dominated solution")
    void testListDominanceAddsNonDominated() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Existing solution
        State existing = mock(State.class);
        when(existing.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 2.0)));
        List<State> list = new ArrayList<>();
        list.add(existing);
        
        // New non-dominated solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 9.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(existing)).thenReturn(false);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Test ListDominance removes dominated solution")
    void testListDominanceRemovesDominated() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Dominated solution in list
        State dominated = mock(State.class);
        when(dominated.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 3.0)));
        List<State> list = new ArrayList<>();
        list.add(dominated);
        
        // Dominant solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 6.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(any())).thenReturn(false);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(1, list.size());
        assertEquals(solutionX, list.get(0));
    }

    @Test
    @DisplayName("Test ListDominance rejects dominated solution")
    void testListDominanceRejectsDominated() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Dominant solution in list
        State dominant = mock(State.class);
        when(dominant.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 10.0)));
        List<State> list = new ArrayList<>();
        list.add(dominant);
        
        // Dominated solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertFalse(result);
        assertEquals(1, list.size());
        assertEquals(dominant, list.get(0));
    }

    @Test
    @DisplayName("Test ListDominance rejects duplicate solution")
    void testListDominanceRejectsDuplicate() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Existing solution
        State existing = mock(State.class);
        when(existing.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        List<State> list = new ArrayList<>();
        list.add(existing);
        
        // Duplicate solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        when(solutionX.Comparator(existing)).thenReturn(true);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertFalse(result);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test ListDominance with multiple solutions in list")
    void testListDominanceMultipleSolutions() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Multiple non-dominated solutions
        State state1 = mock(State.class);
        when(state1.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 2.0)));
        State state2 = mock(State.class);
        when(state2.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 7.0)));
        List<State> list = new ArrayList<>();
        list.add(state1);
        list.add(state2);
        
        // New non-dominated solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 9.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(state1)).thenReturn(false);
        when(solutionX.Comparator(state2)).thenReturn(false);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Test ListDominance removes multiple dominated solutions")
    void testListDominanceRemovesMultipleDominated() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        // Multiple dominated solutions
        State state1 = mock(State.class);
        when(state1.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 6.0)));
        State state2 = mock(State.class);
        when(state2.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(7.0, 8.0)));
        List<State> list = new ArrayList<>();
        list.add(state1);
        list.add(state2);
        
        // Dominant solution
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0, 4.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(any())).thenReturn(false);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(1, list.size());
        assertEquals(solutionX, list.get(0));
    }

    // ==================== SINGLE OBJECTIVE TESTS ====================

    @Test
    @DisplayName("Test single objective dominance in maximization")
    void testSingleObjectiveDominanceMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(7.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test single objective dominance in minimization")
    void testSingleObjectiveDominanceMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(3.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test single objective equal values")
    void testSingleObjectiveEqual() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertFalse(result);
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Test with zero values in maximization")
    void testZeroValuesMaximization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1.0, 0.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(0.0, 0.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with negative values in minimization")
    void testNegativeValuesMinimization() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(-5.0, -6.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(-3.0, -4.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with mixed positive and negative values")
    void testMixedPositiveNegativeValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(-2.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(-5.0, 3.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with very small differences (epsilon)")
    void testEpsilonDifferences() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1.0000001, 2.0000001)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1.0, 2.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with very large values")
    void testVeryLargeValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1e10, 1e11)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1e9, 1e10)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test with very small positive values")
    void testVerySmallValues() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1e-10, 1e-11)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(1e-9, 1e-10)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    // ==================== BOUNDARY CONDITION TESTS ====================

    @Test
    @DisplayName("Test ListDominance with single element becoming dominated")
    void testListDominanceSingleElementDominated() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State dominated = mock(State.class);
        when(dominated.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(2.0, 2.0)));
        List<State> list = new ArrayList<>();
        list.add(dominated);
        
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        when(solutionX.getCopy()).thenReturn(solutionX);
        when(solutionX.Comparator(any())).thenReturn(false);
        
        boolean result = dominance.ListDominance(solutionX, list);
        
        assertTrue(result);
        assertEquals(1, list.size());
        assertEquals(solutionX, list.get(0));
    }

    @Test
    @DisplayName("Test all objectives better except one equal - dominance")
    void testAllBetterExceptOneEqual() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 5.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 5.0, 5.0)));
        
        boolean result = dominance.dominance(solutionX, solutionY);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test symmetry of dominance relation")
    void testDominanceSymmetry() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 5.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 10.0)));
        
        boolean xDominatesY = dominance.dominance(solutionX, solutionY);
        boolean yDominatesX = dominance.dominance(solutionY, solutionX);
        
        assertFalse(xDominatesY);
        assertFalse(yDominatesX);
    }

    @Test
    @DisplayName("Test transitivity consideration - X>Y>Z implies X>Z")
    void testDominanceTransitivity() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State solutionZ = mock(State.class);
        when(solutionX.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(8.0, 8.0)));
        when(solutionY.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(6.0, 6.0)));
        when(solutionZ.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(4.0, 4.0)));
        
        boolean xDominatesY = dominance.dominance(solutionX, solutionY);
        boolean yDominatesZ = dominance.dominance(solutionY, solutionZ);
        boolean xDominatesZ = dominance.dominance(solutionX, solutionZ);
        
        assertTrue(xDominatesY);
        assertTrue(yDominatesZ);
        assertTrue(xDominatesZ); // Transitivity
    }

    @Test
    @DisplayName("Test ListDominance preserves non-dominated set property")
    void testListDominancePreservesNonDominatedProperty() {
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Build a Pareto front
        State state1 = mock(State.class);
        when(state1.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(10.0, 3.0)));
        when(state1.getCopy()).thenReturn(state1);
        when(state1.Comparator(any())).thenReturn(false);
        
        State state2 = mock(State.class);
        when(state2.getEvaluation()).thenReturn(new ArrayList<>(Arrays.asList(5.0, 8.0)));
        when(state2.getCopy()).thenReturn(state2);
        when(state2.Comparator(any())).thenReturn(false);
        
        List<State> list = new ArrayList<>();
        
        dominance.ListDominance(state1, list);
        dominance.ListDominance(state2, list);
        
        // Both should be in front
        assertEquals(2, list.size());
    }
}

