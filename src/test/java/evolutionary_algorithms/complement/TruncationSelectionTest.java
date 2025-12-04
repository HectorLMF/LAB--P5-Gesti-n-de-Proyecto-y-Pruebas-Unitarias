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
 * Tests exhaustivos para la clase TruncationSelection
 */
@DisplayName("Tests para TruncationSelection")
class TruncationSelectionTest {

    private TruncationSelection selection;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        selection = new TruncationSelection();
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
    @DisplayName("Debe seleccionar los mejores individuos en maximización")
    void testSelectBestInMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(30.0));
        population.add(createState(20.0));
        population.add(createState(40.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
        assertEquals(40.0, selected.get(0).getEvaluation().get(0));
        assertEquals(30.0, selected.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar los mejores individuos en minimización")
    void testSelectBestInMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(30.0));
        population.add(createState(20.0));
        population.add(createState(5.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
        assertEquals(5.0, selected.get(0).getEvaluation().get(0));
        assertEquals(10.0, selected.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe ordenar correctamente en orden descendente")
    void testOrderBetter() {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(30.0));
        population.add(createState(20.0));
        population.add(createState(40.0));
        
        List<State> ordered = selection.OrderBetter(population);
        
        assertEquals(40.0, ordered.get(0).getEvaluation().get(0));
        assertEquals(30.0, ordered.get(1).getEvaluation().get(0));
        assertEquals(20.0, ordered.get(2).getEvaluation().get(0));
        assertEquals(10.0, ordered.get(3).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe ordenar correctamente en orden ascendente")
    void testAscOrderBetter() {
        List<State> population = new ArrayList<>();
        population.add(createState(40.0));
        population.add(createState(10.0));
        population.add(createState(30.0));
        population.add(createState(20.0));
        
        List<State> ordered = selection.ascOrderBetter(population);
        
        assertEquals(10.0, ordered.get(0).getEvaluation().get(0));
        assertEquals(20.0, ordered.get(1).getEvaluation().get(0));
        assertEquals(30.0, ordered.get(2).getEvaluation().get(0));
        assertEquals(40.0, ordered.get(3).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe seleccionar toda la población si truncation >= tamaño")
    void testSelectAllIfTruncationLarge() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        List<State> selected = selection.selection(population, 10);
        
        assertNotNull(selected);
        assertTrue(selected.size() >= 3);
    }

    @Test
    @DisplayName("Debe ser instancia de FatherSelection")
    void testInstanceOfFatherSelection() {
        assertTrue(selection instanceof FatherSelection,
            "TruncationSelection debe ser instancia de FatherSelection");
    }

    @Test
    @DisplayName("Debe manejar población con un solo individuo")
    void testSingleIndividualPopulation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        
        List<State> selected = selection.selection(population, 1);
        
        assertNotNull(selected);
        assertEquals(1, selected.size());
        assertEquals(10.0, selected.get(0).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas en maximización")
    void testNegativeEvaluationsMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(-10.0));
        population.add(createState(-5.0));
        population.add(createState(-20.0));
        population.add(createState(-2.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
        assertEquals(-2.0, selected.get(0).getEvaluation().get(0));
        assertEquals(-5.0, selected.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas en minimización")
    void testNegativeEvaluationsMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(-10.0));
        population.add(createState(-5.0));
        population.add(createState(-20.0));
        population.add(createState(-2.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
        assertEquals(-20.0, selected.get(0).getEvaluation().get(0));
        assertEquals(-10.0, selected.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe manejar población grande")
    void testLargePopulation() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            population.add(createState(i * 1.0));
        }
        
        List<State> selected = selection.selection(population, 10);
        
        assertNotNull(selected);
        assertEquals(10, selected.size());
        // Verificar que están ordenados de mejor a peor
        for (int i = 0; i < selected.size() - 1; i++) {
            assertTrue(selected.get(i).getEvaluation().get(0) >= 
                      selected.get(i + 1).getEvaluation().get(0));
        }
    }

    @Test
    @DisplayName("Debe manejar evaluaciones idénticas")
    void testIdenticalEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            population.add(createState(42.0));
        }
        
        List<State> selected = selection.selection(population, 3);
        
        assertNotNull(selected);
        assertEquals(3, selected.size());
        for (State s : selected) {
            assertEquals(42.0, s.getEvaluation().get(0));
        }
    }

    @Test
    @DisplayName("Debe manejar evaluaciones con ceros")
    void testZeroEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(0.0));
        population.add(createState(10.0));
        population.add(createState(-5.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
    }

    @Test
    @DisplayName("Ordenamiento descendente con dos elementos")
    void testOrderBetterTwoElements() {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        
        List<State> ordered = selection.OrderBetter(population);
        
        assertEquals(20.0, ordered.get(0).getEvaluation().get(0));
        assertEquals(10.0, ordered.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Ordenamiento ascendente con dos elementos")
    void testAscOrderBetterTwoElements() {
        List<State> population = new ArrayList<>();
        population.add(createState(20.0));
        population.add(createState(10.0));
        
        List<State> ordered = selection.ascOrderBetter(population);
        
        assertEquals(10.0, ordered.get(0).getEvaluation().get(0));
        assertEquals(20.0, ordered.get(1).getEvaluation().get(0));
    }

    @Test
    @DisplayName("Debe mantener estabilidad en ordenamiento")
    void testOrderingStability() {
        List<State> population = new ArrayList<>();
        State s1 = createState(10.0);
        State s2 = createState(20.0);
        State s3 = createState(30.0);
        population.add(s1);
        population.add(s2);
        population.add(s3);
        
        List<State> ordered = selection.OrderBetter(population);
        
        assertEquals(s3, ordered.get(0));
        assertEquals(s2, ordered.get(1));
        assertEquals(s1, ordered.get(2));
    }

    @Test
    @DisplayName("Debe manejar valores extremos")
    void testExtremeValues() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(Double.MAX_VALUE));
        population.add(createState(Double.MIN_VALUE));
        population.add(createState(0.0));
        
        List<State> selected = selection.selection(population, 2);
        
        assertNotNull(selected);
        assertEquals(2, selected.size());
    }

    @Test
    @DisplayName("Debe manejar truncation de 1")
    void testTruncationOne() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        List<State> selected = selection.selection(population, 1);
        
        assertNotNull(selected);
        assertEquals(1, selected.size());
        assertEquals(30.0, selected.get(0).getEvaluation().get(0));
    }
}
