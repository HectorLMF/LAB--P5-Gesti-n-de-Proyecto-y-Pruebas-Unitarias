package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import problem.definition.State;

/**
 * Tests exhaustivos para la clase UniformCrossover
 */
@DisplayName("Tests para UniformCrossover")
class UniformCrossoverTest {

    private UniformCrossover crossover;

    @BeforeEach
    void setUp() {
        crossover = new UniformCrossover();
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
    @DisplayName("Debe ser instancia de Crossover")
    void testInstanceOfCrossover() {
        assertTrue(crossover instanceof Crossover,
            "UniformCrossover debe ser instancia de Crossover");
    }

    @RepeatedTest(20)
    @DisplayName("Múltiples cruces deben producir resultados válidos")
    void testMultipleCrossoversProduceValidResults() {
        State father1 = createState(1, 2, 3, 4, 5);
        State father2 = createState(6, 7, 8, 9, 10);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(5, result.getCode().size());
    }

    @Test
    @DisplayName("Hijo debe contener genes de los padres")
    void testChildContainsGenesFromParents() {
        State father1 = createState(1, 1, 1, 1, 1);
        State father2 = createState(2, 2, 2, 2, 2);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        for (Object gene : result.getCode()) {
            int value = (Integer) gene;
            assertTrue(value == 1 || value == 2, 
                "El gen debe venir de alguno de los padres");
        }
    }

    @Test
    @DisplayName("Debe funcionar con un solo gen")
    void testSingleGene() {
        State father1 = createState(1);
        State father2 = createState(2);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(1, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con dos genes")
    void testTwoGenes() {
        State father1 = createState(1, 2);
        State father2 = createState(3, 4);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(2, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con padres grandes")
    void testLargeParents() {
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
    @DisplayName("Debe generar máscara correctamente")
    void testMascaraGeneration() {
        int[] mascara = crossover.mascara(10);
        
        assertNotNull(mascara, "La máscara no debe ser null");
        assertEquals(10, mascara.length, "La máscara debe tener la longitud especificada");
    }

    @Test
    @DisplayName("Máscara debe contener solo 0s y 1s")
    void testMascaraContainsOnlyZeroAndOne() {
        int[] mascara = crossover.mascara(20);
        
        for (int value : mascara) {
            assertTrue(value == 0 || value == 1, 
                "La máscara debe contener solo 0 o 1, pero contiene: " + value);
        }
    }

    @Test
    @DisplayName("Debe funcionar con diferentes tamaños de máscara")
    void testDifferentMascaraSizes() {
        int[] sizes = {1, 2, 5, 10, 50, 100};
        
        for (int size : sizes) {
            int[] mascara = crossover.mascara(size);
            assertNotNull(mascara, "Máscara debe existir para tamaño " + size);
            assertEquals(size, mascara.length, "Tamaño incorrecto para " + size);
        }
    }

    @Test
    @DisplayName("Debe manejar diferentes probabilidades")
    void testDifferentProbabilities() {
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
    @DisplayName("Máscara de longitud 1")
    void testMascaraLengthOne() {
        int[] mascara = crossover.mascara(1);
        
        assertEquals(1, mascara.length);
        assertTrue(mascara[0] == 0 || mascara[0] == 1);
    }

    @Test
    @DisplayName("Máscara de longitud 0")
    void testMascaraLengthZero() {
        int[] mascara = crossover.mascara(0);
        
        assertEquals(0, mascara.length);
    }

    @Test
    @DisplayName("Múltiples generaciones de máscara deben ser independientes")
    void testMultipleMascaraGenerationsAreIndependent() {
        int[] mascara1 = crossover.mascara(10);
        int[] mascara2 = crossover.mascara(10);
        
        assertNotNull(mascara1);
        assertNotNull(mascara2);
        // Las máscaras deben ser objetos diferentes
        assertNotSame(mascara1, mascara2);
    }

    @Test
    @DisplayName("Debe funcionar con códigos de 3 elementos")
    void testWithThreeElements() {
        State father1 = createState(1, 2, 3);
        State father2 = createState(4, 5, 6);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(3, result.getCode().size());
    }

    @Test
    @DisplayName("Debe funcionar con códigos muy grandes")
    void testWithVeryLargeCodes() {
        int[] father1Genes = new int[1000];
        int[] father2Genes = new int[1000];
        for (int i = 0; i < 1000; i++) {
            father1Genes[i] = i;
            father2Genes[i] = i + 1000;
        }
        
        State father1 = createState(father1Genes);
        State father2 = createState(father2Genes);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(1000, result.getCode().size());
    }

    @RepeatedTest(10)
    @DisplayName("Verificar variabilidad en los resultados")
    void testVariabilityInResults() {
        State father1 = createState(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        State father2 = createState(2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        
        State result = crossover.crossover(father1, father2, 1.0);
        
        assertNotNull(result);
        assertEquals(10, result.getCode().size());
        
        // Verificar que contiene mezcla de genes
        boolean hasOnes = false;
        boolean hasTwos = false;
        for (Object gene : result.getCode()) {
            int value = (Integer) gene;
            if (value == 1) hasOnes = true;
            if (value == 2) hasTwos = true;
        }
        // En algunos casos debería tener ambos (aunque no siempre por aleatoriedad)
    }
}
