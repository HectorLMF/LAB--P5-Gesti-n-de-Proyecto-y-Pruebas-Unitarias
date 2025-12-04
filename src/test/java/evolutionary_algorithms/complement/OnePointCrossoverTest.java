package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.Problem;
import problem.definition.State;

/**
 * Tests exhaustivos para la clase OnePointCrossover
 */
@DisplayName("Tests para OnePointCrossover")
class OnePointCrossoverTest {

    private OnePointCrossover crossover;
    private Strategy strategy;
    private Problem problem;
    private Codification codification;

    @BeforeEach
    void setUp() {
        crossover = new OnePointCrossover();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        codification = mock(Codification.class);
        strategy.setProblem(problem);
        when(problem.getCodification()).thenReturn(codification);
        when(codification.getVariableCount()).thenReturn(5);
    }

    private State createState(int... values) {
        State state = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (int val : values) {
            code.add(val);
        }
        state.setCode(code);
        return state;
    }

    @Test
    @DisplayName("Debe retornar estado no nulo")
    void testReturnNonNullState() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result, "El hijo no debe ser null");
    }

    @Test
    @DisplayName("Debe mantener tamaño del código")
    void testMaintainsCodeSize() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertEquals(5, result.getCode().size(), "El tamaño debe ser igual a los padres");
    }

    @Test
    @DisplayName("Con probabilidad 0 debe retornar copia del padre1")
    void testNoCrossoverWithProbabilityZero() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 0.0);
        
        assertNotNull(result);
        // Con PC=0, debería retornar copia del padre1
        assertEquals(father1.getCode().size(), result.getCode().size());
    }

    @Test
    @DisplayName("Con probabilidad 1 debe realizar cruce")
    void testCrossoverWithProbabilityOne() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
    }

    @Test
    @DisplayName("Debe ser instancia de Crossover")
    void testInstanceOfCrossover() {
        assertTrue(crossover instanceof Crossover,
            "OnePointCrossover debe ser instancia de Crossover");
    }

    @Test
    @DisplayName("Debe funcionar con padres de un solo gen")
    void testSingleGeneParents() {
        when(codification.getVariableCount()).thenReturn(1);
        State father1 = createState(1);
        State father2 = createState(2);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(1, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con padres de dos genes")
    void testTwoGeneParents() {
        when(codification.getVariableCount()).thenReturn(2);
        State father1 = createState(1, 2);
        State father2 = createState(3, 4);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(2, result.getCode().size());
    }

    @RepeatedTest(20)
    @DisplayName("Múltiples cruces deben producir resultados válidos")
    void testMultipleCrossoversProduceValidResults() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
        // Verificar que los genes vienen de uno de los padres
        for (Object gene : result.getCode()) {
            int value = (Integer) gene;
            assertTrue(value >= 1 && value <= 10, 
                "El gen debe venir de alguno de los padres");
        }
    }

    @Test
    @DisplayName("Debe funcionar con padres grandes")
    void testLargeParents() {
        when(codification.getVariableCount()).thenReturn(100);
        int[] father1Genes = new int[100];
        int[] father2Genes = new int[100];
        for (int i = 0; i < 100; i++) {
            father1Genes[i] = i;
            father2Genes[i] = i + 100;
        }
        
        State father1 = createState(father1Genes);
        State father2 = createState(father2Genes);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(100, result.getCode().size());
    }

    @Test
    @DisplayName("Debe manejar diferentes probabilidades de cruce")
    void testDifferentCrossoverProbabilities() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        double[] probabilities = {0.0, 0.25, 0.5, 0.75, 1.0};
        
        for (double pc : probabilities) {
            State result = crossover.crossover(father1, father2, pc);
            assertNotNull(result, "Debe retornar estado con PC=" + pc);
            assertEquals(5, result.getCode().size());
        }
    }

    @Test
    @DisplayName("Hijo debe contener genes de ambos padres con PC=1.0")
    void testChildContainsGenesFromBothParents() {
        State father1 = createState(1, 1, 1, 1, 1);
        State father2 = createState(2, 2, 2, 2, 2);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        // Verificar que todos los genes son 1 o 2
        for (Object gene : result.getCode()) {
            int value = (Integer) gene;
            assertTrue(value == 1 || value == 2, 
                "El gen debe ser 1 o 2");
        }
    }

    @Test
    @DisplayName("Llamadas consecutivas deben funcionar")
    void testConsecutiveCalls() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result1 = crossover.crossover(father1, father2, 1.0);
        State result2 = crossover.crossover(father1, father2, 1.0);
        State result3 = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    @DisplayName("Debe funcionar con valores negativos")
    void testWithNegativeValues() {
        State father1 = createState(-1, -2, -3, -4, -5);
        State father2 = createState(-6, -7, -8, -9, -10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con valores mixtos")
    void testWithMixedValues() {
        State father1 = createState(-5, 0, 5, 10, -10);
        State father2 = createState(3, -3, 7, -7, 0);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
    }

    @Test
    @DisplayName("No debe modificar padres originales")
    void testDoesNotModifyParents() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        ArrayList<Object> original1 = new ArrayList<>(father1.getCode());
        ArrayList<Object> original2 = new ArrayList<>(father2.getCode());
        
        crossover.crossover(father1, father2, 1.0);
        
        assertEquals(original1, father1.getCode(), "Padre1 no debe modificarse");
        assertEquals(original2, father2.getCode(), "Padre2 no debe modificarse");
    }

    @Test
    @DisplayName("Debe funcionar con padres idénticos")
    void testWithIdenticalParents() {
        State father1 = createState(5, 5, 5, 5, 5);
        State father2 = createState(5, 5, 5, 5, 5);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        for (Object gene : result.getCode()) {
            assertEquals(5, (Integer) gene, "Todos los genes deben ser 5");
        }
    }

    @Test
    @DisplayName("Debe funcionar con códigos de 3 elementos")
    void testWithThreeElements() {
        when(codification.getVariableCount()).thenReturn(3);
        State father1 = createState(1, 2, 3);
        State father2 = createState(4, 5, 6);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(3, result.getCode().size());
    }
}
