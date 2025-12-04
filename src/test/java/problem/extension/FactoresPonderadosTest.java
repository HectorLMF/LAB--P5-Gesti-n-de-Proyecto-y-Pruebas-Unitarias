package problem.extension;

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
 * Tests exhaustivos para la clase FactoresPonderados
 */
@DisplayName("Tests para FactoresPonderados")
class FactoresPonderadosTest {

    private FactoresPonderados factoresPonderados;
    private Strategy strategy;
    private Problem problem;
    private State state;

    @BeforeEach
    void setUp() {
        factoresPonderados = new FactoresPonderados();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        strategy.setProblem(problem);
        
        state = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        code.add(2);
        code.add(3);
        state.setCode(code);
    }

    private ObjetiveFunction createMockFunction(double evaluation, double weight, ProblemType type) {
        ObjetiveFunction function = mock(ObjetiveFunction.class);
        when(function.Evaluation(any(State.class))).thenReturn(evaluation);
        when(function.getWeight()).thenReturn(weight);
        when(function.getTypeProblem()).thenReturn(type);
        return function;
    }

    @Test
    @DisplayName("Debe evaluar estado con una función en maximización")
    void testSingleFunctionMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(1, state.getEvaluation().size());
        assertEquals(10.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe evaluar estado con múltiples funciones en maximización")
    void testMultipleFunctionsMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 0.5, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(20.0, 0.5, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(1, state.getEvaluation().size());
        assertEquals(15.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe evaluar estado con una función en minimización")
    void testSingleFunctionMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 1.0, ProblemType.MINIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(1, state.getEvaluation().size());
        assertEquals(10.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe ser instancia de SolutionMethod")
    void testInstanceOfSolutionMethod() {
        assertTrue(factoresPonderados instanceof SolutionMethod,
            "FactoresPonderados debe ser instancia de SolutionMethod");
    }

    @Test
    @DisplayName("Debe manejar funciones con pesos diferentes")
    void testDifferentWeights() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 0.3, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(20.0, 0.7, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // 10*0.3 + 20*0.7 = 3 + 14 = 17
        assertEquals(17.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar función de maximización en problema de minimización")
    void testMaxFunctionInMinProblem() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.8, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // En minimización con función de maximización: 1 - 0.8 = 0.2
        assertEquals(0.2, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar función de minimización en problema de maximización")
    void testMinFunctionInMaxProblem() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.3, 1.0, ProblemType.MINIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // En maximización con función de minimización: 1 - 0.3 = 0.7
        assertEquals(0.7, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar tres funciones objetivo")
    void testThreeObjectiveFunctions() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 0.33, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(20.0, 0.33, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(30.0, 0.34, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // 10*0.33 + 20*0.33 + 30*0.34 = 3.3 + 6.6 + 10.2 = 20.1
        assertEquals(20.1, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones cero")
    void testZeroEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.0, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(0.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones negativas")
    void testNegativeEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(-10.0, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(-10.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar peso cero")
    void testZeroWeight() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(100.0, 0.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(0.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar funciones mixtas en maximización")
    void testMixedFunctionsMaximization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.6, 0.5, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(0.4, 0.5, ProblemType.MINIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // 0.6*0.5 + (1-0.4)*0.5 = 0.3 + 0.3 = 0.6
        assertEquals(0.6, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar funciones mixtas en minimización")
    void testMixedFunctionsMinimization() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.3, 0.5, ProblemType.MINIMIZAR));
        functions.add(createMockFunction(0.7, 0.5, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        // 0.3*0.5 + (1-0.7)*0.5 = 0.15 + 0.15 = 0.3
        assertEquals(0.3, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Múltiples evaluaciones consecutivas")
    void testMultipleConsecutiveEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(10.0, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        factoresPonderados.evaluationState(state);
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(10.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones muy grandes")
    void testLargeEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(1000000.0, 1.0, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        assertEquals(1000000.0, state.getEvaluation().get(0), 0.001);
    }

    @Test
    @DisplayName("Debe manejar evaluaciones decimales precisas")
    void testPreciseDecimalEvaluations() {
        when(problem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(createMockFunction(0.123456, 0.5, ProblemType.MAXIMIZAR));
        functions.add(createMockFunction(0.654321, 0.5, ProblemType.MAXIMIZAR));
        when(problem.getFunction()).thenReturn(functions);
        
        factoresPonderados.evaluationState(state);
        
        assertNotNull(state.getEvaluation());
        double expected = 0.123456 * 0.5 + 0.654321 * 0.5;
        assertEquals(expected, state.getEvaluation().get(0), 0.000001);
    }
}
