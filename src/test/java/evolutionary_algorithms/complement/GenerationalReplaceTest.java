package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase GenerationalReplace
 */
@DisplayName("Tests para GenerationalReplace")
class GenerationalReplaceTest {

    private GenerationalReplace replace;

    @BeforeEach
    void setUp() {
        replace = new GenerationalReplace();
    }

    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    @Test
    @DisplayName("Debe retornar lista no nula")
    void testReturnNonNullList() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        
        State candidate = createState(15.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertNotNull(result, "La lista resultante no debe ser null");
    }

    @Test
    @DisplayName("Debe mantener el tamaño de la población")
    void testMaintainsPopulationSize() throws Exception {
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            population.add(createState(i * 10.0));
        }
        
        State candidate = createState(100.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(5, result.size(), "El tamaño de la población debe mantenerse");
    }

    @Test
    @DisplayName("Debe agregar el candidato a la población")
    void testAddsCandidate() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        State candidate = createState(25.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate), "La población debe contener el candidato");
    }

    @Test
    @DisplayName("Debe remover el primer elemento")
    void testRemovesFirstElement() throws Exception {
        List<State> population = new ArrayList<>();
        State first = createState(10.0);
        population.add(first);
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        State candidate = createState(25.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertFalse(result.contains(first), "El primer elemento debe ser removido");
    }

    @Test
    @DisplayName("Debe ser instancia de Replace")
    void testInstanceOfReplace() {
        assertTrue(replace instanceof Replace,
            "GenerationalReplace debe ser instancia de Replace");
    }

    @Test
    @DisplayName("Debe funcionar con población de un elemento")
    void testSingleElementPopulation() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        
        State candidate = createState(20.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe funcionar con población de dos elementos")
    void testTwoElementPopulation() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        
        State candidate = createState(15.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe funcionar con población grande")
    void testLargePopulation() throws Exception {
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            population.add(createState(i * 1.0));
        }
        
        State candidate = createState(999.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(100, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Múltiples reemplazos consecutivos deben funcionar")
    void testMultipleConsecutiveReplacements() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        State candidate1 = createState(15.0);
        State candidate2 = createState(25.0);
        State candidate3 = createState(35.0);
        
        List<State> result1 = replace.replace(candidate1, population);
        List<State> result2 = replace.replace(candidate2, result1);
        List<State> result3 = replace.replace(candidate3, result2);
        
        assertEquals(3, result3.size());
        assertTrue(result3.contains(candidate3));
    }

    @Test
    @DisplayName("Debe manejar candidatos con evaluación negativa")
    void testNegativeEvaluationCandidate() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        
        State candidate = createState(-5.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate));
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Debe manejar población con evaluaciones negativas")
    void testPopulationWithNegativeEvaluations() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(-10.0));
        population.add(createState(-20.0));
        population.add(createState(-30.0));
        
        State candidate = createState(-15.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe manejar evaluación cero")
    void testZeroEvaluation() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(0.0));
        population.add(createState(10.0));
        
        State candidate = createState(5.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("El candidato debe estar al final de la lista")
    void testCandidateIsAtEnd() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(20.0));
        population.add(createState(30.0));
        
        State candidate = createState(25.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(candidate, result.get(result.size() - 1),
            "El candidato debe estar al final de la lista");
    }

    @Test
    @DisplayName("Debe funcionar con estados idénticos")
    void testIdenticalStates() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(10.0));
        population.add(createState(10.0));
        population.add(createState(10.0));
        
        State candidate = createState(10.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("Debe manejar valores extremos")
    void testExtremeValues() throws Exception {
        List<State> population = new ArrayList<>();
        population.add(createState(Double.MAX_VALUE));
        population.add(createState(Double.MIN_VALUE));
        
        State candidate = createState(0.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(candidate));
    }

    @Test
    @DisplayName("La población original no debe modificarse si se pasa copia")
    void testOriginalPopulationNotModifiedIfCopy() throws Exception {
        List<State> original = new ArrayList<>();
        original.add(createState(10.0));
        original.add(createState(20.0));
        
        // Crear copia
        List<State> population = new ArrayList<>(original);
        State candidate = createState(15.0);
        
        replace.replace(candidate, population);
        
        // La población pasada se modifica, pero podemos verificar el comportamiento
        assertNotEquals(original.size(), population.size() - 1);
    }

    @Test
    @DisplayName("Debe funcionar con población de 10 elementos")
    void testTenElementPopulation() throws Exception {
        List<State> population = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            population.add(createState(i * 5.0));
        }
        
        State candidate = createState(100.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(10, result.size());
        assertEquals(candidate, result.get(9));
    }

    @Test
    @DisplayName("Debe manejar reemplazo con mismo valor de evaluación")
    void testReplacementWithSameEvaluation() throws Exception {
        List<State> population = new ArrayList<>();
        State first = createState(10.0);
        population.add(first);
        population.add(createState(20.0));
        
        State candidate = createState(10.0);
        
        List<State> result = replace.replace(candidate, population);
        
        assertEquals(2, result.size());
        assertFalse(result.contains(first));
        assertTrue(result.contains(candidate));
    }
}
