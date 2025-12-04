package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * Tests exhaustivos para la clase SteadyStateReplace
 */
@DisplayName("Tests para SteadyStateReplace")
class SteadyStateReplaceTest {

    private SteadyStateReplace replace;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        replace = new SteadyStateReplace();
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
    @DisplayName("Debe reemplazar el peor en maximización si candidato es mejor")
    void testReplaceWorstInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        State worst = createState(5.0);
        population.add(createState(10.0));
        population.add(worst);
        population.add(createState(15.0));
        
        State candidate = createState(12.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertFalse(result.contains(worst));
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("No debe reemplazar en maximización si candidato es peor")
    void testNoReplaceInMaximizationIfWorse() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        State worst = createState(5.0);
        population.add(createState(10.0));
        population.add(worst);
        population.add(createState(15.0));
        
        State candidate = createState(3.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(worst));
        assertFalse(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe reemplazar el peor en minimización si candidato es mejor")
    void testReplaceWorstInMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        List<State> population = new ArrayList<>();
        State worst = createState(15.0);
        population.add(createState(10.0));
        population.add(worst);
        population.add(createState(5.0));
        
        State candidate = createState(8.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertFalse(result.contains(worst));
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("No debe reemplazar en minimización si candidato es peor")
    void testNoReplaceInMinimizationIfWorse() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        List<State> population = new ArrayList<>();
        State worst = createState(15.0);
        population.add(createState(10.0));
        population.add(worst);
        population.add(createState(5.0));
        
        State candidate = createState(20.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(worst));
        assertFalse(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe ser instancia de Replace")
    void testInstanceOfReplace() {
        assertTrue(replace instanceof Replace);
    }

    @Test
    @DisplayName("Debe mantener tamaño de población al reemplazar")
    void testMaintainsPopulationSizeWhenReplacing() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            population.add(createState(i * 10.0));
        }
        
        State candidate = createState(25.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("Debe mantener tamaño de población sin reemplazar")
    void testMaintainsPopulationSizeWithoutReplacing() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            population.add(createState(i * 10.0));
        }
        
        State candidate = createState(-10.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("Debe reemplazar con candidato igual al peor en maximización")
    void testReplaceEqualInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        State worst = createState(5.0);
        population.add(createState(10.0));
        population.add(worst);
        
        State candidate = createState(5.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe encontrar el mínimo correctamente con MinValue")
    void testMinValue() {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(5.0));
        population.add(createState(15.0));
        
        State min = replace.MinValue(population);
        
        assertEquals(5.0, min.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe encontrar el máximo correctamente con MaxValue")
    void testMaxValue() {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(5.0));
        population.add(createState(15.0));
        
        State max = replace.MaxValue(population);
        
        assertEquals(15.0, max.getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar población con un solo elemento")
    void testSingleElementPopulation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        State single = createState(10.0);
        population.add(single);
        
        State candidate = createState(15.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe manejar población con dos elementos")
    void testTwoElementPopulation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(5.0));
        
        State candidate = createState(12.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas en maximización")
    void testNegativeEvaluationsMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(-5.0));
        population.add(createState(-10.0));
        population.add(createState(-2.0));
        
        State candidate = createState(-7.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe reemplazar en posición correcta")
    void testReplacesInCorrectPosition() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        State worst = createState(5.0);
        population.add(worst);
        population.add(createState(15.0));
        
        State candidate = createState(12.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(candidate, result.get(1));
    }

    @Test
    @DisplayName("Múltiples reemplazos consecutivos deben funcionar")
    void testMultipleConsecutiveReplacements() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(5.0));
        population.add(createState(15.0));
        
        State candidate1 = createState(12.0);
        List<State> result1 = replace.replace(candidate1, population);
        
        State candidate2 = createState(14.0);
        List<State> result2 = replace.replace(candidate2, result1);
        
        assertEquals(3, result2.size());
        assertTrue(result2.contains(candidate2));
    }

    @Test
    @DisplayName("Debe manejar población con evaluaciones idénticas")
    void testIdenticalEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            population.add(createState(10.0));
        }
        
        State candidate = createState(15.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate));
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Debe manejar población grande")
    void testLargePopulation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            population.add(createState(i * 1.0));
        }
        
        State candidate = createState(50.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(100, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe manejar valores extremos")
    void testExtremeValues() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(Double.MAX_VALUE));
        population.add(createState(Double.MIN_VALUE));
        
        State candidate = createState(1000.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("MinValue debe encontrar el mínimo con valores negativos")
    void testMinValueWithNegatives() {
        List<State> population = new ArrayList<>();
        population.add(createState(-5.0));
        population.add(createState(-10.0));
        population.add(createState(-2.0));
        
        State min = replace.MinValue(population);
        
        assertEquals(-10.0, min.getEvaluation().get(0));
    }

    @Test
    @DisplayName("MaxValue debe encontrar el máximo con valores negativos")
    void testMaxValueWithNegatives() {
        List<State> population = new ArrayList<>();
        population.add(createState(-5.0));
        population.add(createState(-10.0));
        population.add(createState(-2.0));
        
        State max = replace.MaxValue(population);
        
        assertEquals(-2.0, max.getEvaluation().get(0));
    }
}
