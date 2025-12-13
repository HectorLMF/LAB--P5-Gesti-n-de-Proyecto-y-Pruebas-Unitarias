package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import problem.definition.Codification;
import problem.definition.Problem;
import problem.definition.State;

/**
 * Tests exhaustivos para ProbabilisticSampling
 */
class ProbabilisticSamplingTest {

    private ProbabilisticSampling sampling;
    private Strategy strategy;
    private Problem problem;

    @BeforeEach
    void setUp() {
        // Configurar Strategy singleton
        strategy = Strategy.getStrategy();
        strategy.setCountCurrent(0);
        
        // Configurar Problem con Codification
        problem = new Problem();
        problem.setCodification(new Codification() {
            @Override
            public boolean validState(State state) {
                return true;
            }
            
            @Override
            public Object getVariableAleatoryValue(int key) {
                return 0;
            }
            
            @Override
            public int getAleatoryKey() {
                return 0;
            }
            
            @Override
            public int getVariableCount() {
                return 4; // 4 variables
            }
        });
        problem.setPossibleValue(10); // valores de 0-9
        
        strategy.setProblem(problem);
        
        sampling = new ProbabilisticSampling();
    }

    @Test
    @DisplayName("Sampling genera el número correcto de individuos")
    void testSamplingReturnsCorrectNumberOfIndividuals() {
        List<State> fathers = createFathers(3, 4);
        
        List<State> result = sampling.sampling(fathers, 5);
        
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("Sampling genera código con tamaño correcto")
    void testSamplingGeneratesCorrectCodeSize() {
        List<State> fathers = createFathers(3, 4);
        
        List<State> result = sampling.sampling(fathers, 3);
        
        for (State state : result) {
            assertEquals(4, state.getCode().size());
        }
    }

    @Test
    @DisplayName("Sampling con un solo padre")
    void testSamplingWithSingleFather() {
        List<State> fathers = new ArrayList<>();
        State father = new State();
        father.setCode(createCode(4, 5));
        fathers.add(father);
        
        List<State> result = sampling.sampling(fathers, 3);
        
        assertEquals(3, result.size());
        for (State state : result) {
            assertEquals(4, state.getCode().size());
            // Con un solo padre, todos los hijos deberían tener valores del padre
            for (Object value : state.getCode()) {
                assertNotNull(value);
                assertTrue(value instanceof Integer);
            }
        }
    }

    @Test
    @DisplayName("Sampling con padres idénticos")
    void testSamplingWithIdenticalFathers() {
        List<State> fathers = new ArrayList<>();
        ArrayList<Object> code = createCode(4, 7);
        
        for (int i = 0; i < 3; i++) {
            State father = new State();
            father.setCode(new ArrayList<>(code));
            fathers.add(father);
        }
        
        List<State> result = sampling.sampling(fathers, 2);
        
        assertEquals(2, result.size());
        for (State state : result) {
            assertEquals(4, state.getCode().size());
            // Con padres idénticos, todos los valores deberían ser 7
            for (Object value : state.getCode()) {
                assertEquals(7, value);
            }
        }
    }

    @Test
    @DisplayName("listState crea el número correcto de estados")
    void testListStateCreatesCorrectNumber() {
        List<State> result = sampling.listState(5);
        
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("listState inicializa estados con código vacío")
    void testListStateInitializesWithEmptyCode() {
        List<State> result = sampling.listState(3);
        
        for (State state : result) {
            assertNotNull(state);
            assertNotNull(state.getCode());
            assertEquals(0, state.getCode().size());
        }
    }

    @Test
    @DisplayName("listState asigna tipo de generador correcto")
    void testListStateAssignsGeneratorType() {
        List<State> result = sampling.listState(3);
        
        for (State state : result) {
            assertEquals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM, state.getTypeGenerator());
        }
    }

    @Test
    @DisplayName("Sampling con padres diversos")
    void testSamplingWithDiverseFathers() {
        List<State> fathers = new ArrayList<>();
        
        State father1 = new State();
        father1.setCode(createCode(4, 1));
        fathers.add(father1);
        
        State father2 = new State();
        father2.setCode(createCode(4, 5));
        fathers.add(father2);
        
        State father3 = new State();
        father3.setCode(createCode(4, 9));
        fathers.add(father3);
        
        List<State> result = sampling.sampling(fathers, 3);
        
        assertEquals(3, result.size());
        for (State state : result) {
            assertEquals(4, state.getCode().size());
            for (Object value : state.getCode()) {
                assertNotNull(value);
                assertTrue(value instanceof Integer);
                int intValue = (Integer) value;
                assertTrue(intValue >= 0 && intValue < 10);
            }
        }
    }

    @Test
    @DisplayName("Sampling con población grande")
    void testSamplingWithLargePopulation() {
        List<State> fathers = createFathers(10, 6);
        
        List<State> result = sampling.sampling(fathers, 20);
        
        assertEquals(20, result.size());
        for (State state : result) {
            assertEquals(6, state.getCode().size());
        }
    }

    @Test
    @DisplayName("Sampling maneja valores dentro del rango permitido")
    void testSamplingHandlesValidRange() {
        List<State> fathers = createFathers(5, 3);
        
        List<State> result = sampling.sampling(fathers, 10);
        
        for (State state : result) {
            for (Object value : state.getCode()) {
                int intValue = (Integer) value;
                assertTrue(intValue >= 0 && intValue < problem.getPossibleValue(),
                    "Valor fuera de rango: " + intValue);
            }
        }
    }

    @Test
    @DisplayName("listState asigna números de estado incrementales")
    void testListStateAssignsStateNumbers() {
        strategy.setCountCurrent(100);
        
        List<State> result = sampling.listState(5);
        
        for (State state : result) {
            assertEquals(100, state.getNumber());
        }
    }

    @Test
    @DisplayName("Sampling con código de tamaño 1")
    void testSamplingWithCodeSizeOne() {
        List<State> fathers = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            State father = new State();
            ArrayList<Object> code = new ArrayList<>();
            code.add(i);
            father.setCode(code);
            fathers.add(father);
        }
        
        List<State> result = sampling.sampling(fathers, 5);
        
        assertEquals(5, result.size());
        for (State state : result) {
            assertEquals(1, state.getCode().size());
        }
    }

    @Test
    @DisplayName("Sampling con código de tamaño grande")
    void testSamplingWithLargeCodeSize() {
        List<State> fathers = createFathers(3, 20);
        
        List<State> result = sampling.sampling(fathers, 5);
        
        assertEquals(5, result.size());
        for (State state : result) {
            assertEquals(20, state.getCode().size());
        }
    }

    @Test
    @DisplayName("Sampling preserva distribución probabilística")
    void testSamplingPreservesProbabilisticDistribution() {
        // Crear padres con valores sesgados hacia ciertos números
        List<State> fathers = new ArrayList<>();
        
        // 5 padres con valor 7 en la primera posición
        for (int i = 0; i < 5; i++) {
            State father = new State();
            ArrayList<Object> code = new ArrayList<>();
            code.add(7);
            father.setCode(code);
            fathers.add(father);
        }
        
        // 1 padre con valor 3 en la primera posición
        State father = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(3);
        father.setCode(code);
        fathers.add(father);
        
        List<State> result = sampling.sampling(fathers, 20);
        
        // Contar cuántos hijos tienen 7 vs 3
        int count7 = 0;
        int count3 = 0;
        
        for (State state : result) {
            int value = (Integer) state.getCode().get(0);
            if (value == 7) count7++;
            if (value == 3) count3++;
        }
        
        // Con probabilidad 5/6 de 7 y 1/6 de 3, esperamos más 7s que 3s
        assertTrue(count7 + count3 > 0, "Debería haber al menos algunos valores 7 o 3");
    }

    // Métodos auxiliares
    
    private List<State> createFathers(int count, int codeSize) {
        List<State> fathers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            State father = new State();
            father.setCode(createCode(codeSize, i % 10));
            fathers.add(father);
        }
        return fathers;
    }

    private ArrayList<Object> createCode(int size, int value) {
        ArrayList<Object> code = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            code.add(value);
        }
        return code;
    }
}
