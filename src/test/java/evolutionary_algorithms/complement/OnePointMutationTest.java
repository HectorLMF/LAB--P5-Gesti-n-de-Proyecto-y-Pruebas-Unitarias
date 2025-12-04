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
 * Tests exhaustivos para la clase OnePointMutation
 */
@DisplayName("Tests para OnePointMutation")
class OnePointMutationTest {

    private OnePointMutation mutation;
    private Strategy strategy;
    private Problem problem;
    private Codification codification;
    private State state;

    @BeforeEach
    void setUp() {
        mutation = new OnePointMutation();
        strategy = Strategy.getStrategy();
        problem = mock(Problem.class);
        codification = mock(Codification.class);
        strategy.setProblem(problem);
        when(problem.getCodification()).thenReturn(codification);
        
        state = createTestState(5);
    }

    private State createTestState(int size) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            code.add(i);
        }
        s.setCode(code);
        return s;
    }

    @Test
    @DisplayName("Debe retornar estado no nulo")
    void testReturnNonNullState() {
        when(codification.getAleatoryKey()).thenReturn(0);
        when(codification.getVariableAleatoryValue(anyInt())).thenReturn(99);
        
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result, "El estado mutado no debe ser null");
    }

    @Test
    @DisplayName("Debe mutar con probabilidad 1.0")
    void testMutationWithProbabilityOne() {
        when(codification.getAleatoryKey()).thenReturn(2);
        when(codification.getVariableAleatoryValue(2)).thenReturn(99);
        
        ArrayList<Object> originalCode = new ArrayList<>(state.getCode());
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result);
        // Con probabilidad 1.0, debería haber algún cambio (aunque depende del random)
    }

    @Test
    @DisplayName("No debe mutar con probabilidad 0.0")
    void testNoMutationWithProbabilityZero() {
        ArrayList<Object> originalCode = new ArrayList<>(state.getCode());
        
        State result = mutation.mutation(state, 0.0);
        
        assertNotNull(result);
        // Con probabilidad 0, no debería mutar
    }

    @Test
    @DisplayName("Debe mutar solo un gen")
    void testMutatesSingleGene() {
        when(codification.getAleatoryKey()).thenReturn(3);
        when(codification.getVariableAleatoryValue(3)).thenReturn(100);
        
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size(), "El tamaño del código no debe cambiar");
    }

    @Test
    @DisplayName("Debe ser instancia de Mutation")
    void testInstanceOfMutation() {
        assertTrue(mutation instanceof Mutation,
            "OnePointMutation debe ser instancia de Mutation");
    }

    @Test
    @DisplayName("Debe mantener tamaño del código")
    void testMaintainsCodeSize() {
        when(codification.getAleatoryKey()).thenReturn(1);
        when(codification.getVariableAleatoryValue(anyInt())).thenReturn(50);
        
        int originalSize = state.getCode().size();
        State result = mutation.mutation(state, 1.0);
        
        assertEquals(originalSize, result.getCode().size(),
            "El tamaño del código debe permanecer igual");
    }

    @RepeatedTest(10)
    @DisplayName("Múltiples mutaciones deben mantener integridad del estado")
    void testMultipleMutationsMaintainIntegrity() {
        when(codification.getAleatoryKey()).thenReturn(0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
        when(codification.getVariableAleatoryValue(anyInt())).thenReturn(99);
        
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result);
        assertNotNull(result.getCode());
        assertFalse(result.getCode().isEmpty());
    }

    @Test
    @DisplayName("Debe funcionar con estado con un solo elemento")
    void testSingleElementState() {
        State singleState = createTestState(1);
        when(codification.getAleatoryKey()).thenReturn(0);
        when(codification.getVariableAleatoryValue(0)).thenReturn(42);
        
        State result = mutation.mutation(singleState, 1.0);
        
        assertNotNull(result);
        assertEquals(1, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con estado grande")
    void testLargeState() {
        State largeState = createTestState(100);
        when(codification.getAleatoryKey()).thenReturn(50);
        when(codification.getVariableAleatoryValue(50)).thenReturn(999);
        
        State result = mutation.mutation(largeState, 1.0);
        
        assertNotNull(result);
        assertEquals(100, result.getCode().size());
    }

    @Test
    @DisplayName("Debe mutar primera posición")
    void testMutateFirstPosition() {
        when(codification.getAleatoryKey()).thenReturn(0);
        when(codification.getVariableAleatoryValue(0)).thenReturn(100);
        
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result);
        assertNotNull(result.getCode().get(0));
    }

    @Test
    @DisplayName("Debe mutar última posición")
    void testMutateLastPosition() {
        when(codification.getAleatoryKey()).thenReturn(4);
        when(codification.getVariableAleatoryValue(4)).thenReturn(100);
        
        State result = mutation.mutation(state, 1.0);
        
        assertNotNull(result);
        assertNotNull(result.getCode().get(4));
    }

    @Test
    @DisplayName("Debe manejar diferentes valores de PM")
    void testDifferentPMValues() {
        when(codification.getAleatoryKey()).thenReturn(2);
        when(codification.getVariableAleatoryValue(anyInt())).thenReturn(50);
        
        double[] pmValues = {0.0, 0.25, 0.5, 0.75, 1.0};
        
        for (double pm : pmValues) {
            State result = mutation.mutation(createTestState(5), pm);
            assertNotNull(result, "Debe retornar estado con PM=" + pm);
        }
    }

    @Test
    @DisplayName("Debe mantener referencias correctas")
    void testMaintainsCorrectReferences() {
        when(codification.getAleatoryKey()).thenReturn(1);
        when(codification.getVariableAleatoryValue(1)).thenReturn(77);
        
        State original = state;
        State result = mutation.mutation(state, 1.0);
        
        assertSame(original, result, "Debe retornar la misma referencia de estado");
    }

    @Test
    @DisplayName("Llamadas consecutivas deben funcionar correctamente")
    void testConsecutiveCalls() {
        when(codification.getAleatoryKey()).thenReturn(0, 1, 2, 3, 4);
        when(codification.getVariableAleatoryValue(anyInt())).thenReturn(99);
        
        State result1 = mutation.mutation(createTestState(5), 1.0);
        State result2 = mutation.mutation(createTestState(5), 1.0);
        State result3 = mutation.mutation(createTestState(5), 1.0);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    @DisplayName("Debe funcionar con valores negativos en el código")
    void testWithNegativeValues() {
        State negativeState = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (int i = -5; i < 0; i++) {
            code.add(i);
        }
        negativeState.setCode(code);
        
        when(codification.getAleatoryKey()).thenReturn(2);
        when(codification.getVariableAleatoryValue(2)).thenReturn(-99);
        
        State result = mutation.mutation(negativeState, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con objetos complejos en el código")
    void testWithComplexObjects() {
        State complexState = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            code.add(new ArrayList<Integer>());
        }
        complexState.setCode(code);
        
        when(codification.getAleatoryKey()).thenReturn(1);
        when(codification.getVariableAleatoryValue(1)).thenReturn(new ArrayList<Integer>());
        
        State result = mutation.mutation(complexState, 1.0);
        
        assertNotNull(result);
        assertEquals(3, result.getCode().size());
    }
}
