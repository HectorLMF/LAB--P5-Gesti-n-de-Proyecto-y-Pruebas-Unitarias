package local_search.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests exhaustivos para la clase StopExecute
 */
@DisplayName("Tests para StopExecute")
class StopExecuteTest {

    private StopExecute stopExecute;

    @BeforeEach
    void setUp() {
        stopExecute = new StopExecute();
    }

    @Test
    @DisplayName("Debe retornar false cuando iteración actual es menor que máximo")
    void testReturnFalseWhenBelowMax() {
        Boolean result = stopExecute.stopIterations(5, 10);
        assertFalse(result, "Debe continuar cuando actual < máximo");
    }

    @Test
    @DisplayName("Debe retornar true cuando iteración actual es igual al máximo")
    void testReturnTrueWhenEqualToMax() {
        Boolean result = stopExecute.stopIterations(10, 10);
        assertTrue(result, "Debe detenerse cuando actual = máximo");
    }

    @Test
    @DisplayName("Debe retornar true cuando iteración actual es mayor que máximo")
    void testReturnTrueWhenAboveMax() {
        Boolean result = stopExecute.stopIterations(15, 10);
        assertTrue(result, "Debe detenerse cuando actual > máximo");
    }

    @Test
    @DisplayName("Debe retornar false cuando iteración actual es cero y máximo es positivo")
    void testZeroCurrentPositiveMax() {
        Boolean result = stopExecute.stopIterations(0, 100);
        assertFalse(result, "Debe continuar desde iteración 0");
    }

    @Test
    @DisplayName("Debe retornar true cuando ambos son cero")
    void testBothZero() {
        Boolean result = stopExecute.stopIterations(0, 0);
        assertTrue(result, "Debe detenerse cuando ambos son 0");
    }

    @Test
    @DisplayName("Debe retornar false una iteración antes del máximo")
    void testOneBeforeMax() {
        Boolean result = stopExecute.stopIterations(99, 100);
        assertFalse(result, "Debe continuar una iteración antes del máximo");
    }

    @Test
    @DisplayName("Debe retornar true una iteración después del máximo")
    void testOneAfterMax() {
        Boolean result = stopExecute.stopIterations(101, 100);
        assertTrue(result, "Debe detenerse una iteración después del máximo");
    }

    @Test
    @DisplayName("Debe funcionar con números grandes")
    void testLargeNumbers() {
        Boolean result1 = stopExecute.stopIterations(999999, 1000000);
        assertFalse(result1, "Debe continuar con números grandes");
        
        Boolean result2 = stopExecute.stopIterations(1000000, 1000000);
        assertTrue(result2, "Debe detenerse al alcanzar el límite grande");
    }

    @Test
    @DisplayName("Debe funcionar con máximo de 1")
    void testMaxOfOne() {
        Boolean result1 = stopExecute.stopIterations(0, 1);
        assertFalse(result1, "Debe continuar en iteración 0 con máx 1");
        
        Boolean result2 = stopExecute.stopIterations(1, 1);
        assertTrue(result2, "Debe detenerse en iteración 1 con máx 1");
    }

    @Test
    @DisplayName("Debe manejar números negativos")
    void testNegativeNumbers() {
        Boolean result = stopExecute.stopIterations(-5, 10);
        assertFalse(result, "Debe continuar con actual negativo");
    }

    @Test
    @DisplayName("Secuencia completa de iteraciones debe funcionar correctamente")
    void testCompleteIterationSequence() {
        int max = 10;
        
        for (int i = 0; i < max; i++) {
            Boolean result = stopExecute.stopIterations(i, max);
            assertFalse(result, "Debe continuar en iteración " + i + " de " + max);
        }
        
        Boolean result = stopExecute.stopIterations(max, max);
        assertTrue(result, "Debe detenerse al alcanzar el máximo");
    }

    @Test
    @DisplayName("Múltiples llamadas con mismos parámetros deben dar mismo resultado")
    void testConsistentResults() {
        Boolean result1 = stopExecute.stopIterations(5, 10);
        Boolean result2 = stopExecute.stopIterations(5, 10);
        Boolean result3 = stopExecute.stopIterations(5, 10);
        
        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertFalse(result1);
    }

    @Test
    @DisplayName("Debe funcionar con iteración actual muy grande")
    void testVeryLargeCurrentIteration() {
        Boolean result = stopExecute.stopIterations(Integer.MAX_VALUE, 100);
        assertTrue(result, "Debe detenerse con iteración actual muy grande");
    }

    @Test
    @DisplayName("Debe funcionar con máximo muy grande")
    void testVeryLargeMaxIteration() {
        Boolean result = stopExecute.stopIterations(100, Integer.MAX_VALUE);
        assertFalse(result, "Debe continuar con máximo muy grande");
    }

    @Test
    @DisplayName("Casos límite alrededor del punto de parada")
    void testBoundaryConditions() {
        int max = 50;
        
        assertFalse(stopExecute.stopIterations(max - 2, max), "2 antes del máx");
        assertFalse(stopExecute.stopIterations(max - 1, max), "1 antes del máx");
        assertTrue(stopExecute.stopIterations(max, max), "En el máx");
        assertTrue(stopExecute.stopIterations(max + 1, max), "1 después del máx");
        assertTrue(stopExecute.stopIterations(max + 2, max), "2 después del máx");
    }

    @Test
    @DisplayName("Debe retornar Boolean no nulo")
    void testReturnsNonNullBoolean() {
        Boolean result = stopExecute.stopIterations(5, 10);
        assertNotNull(result, "El resultado no debe ser null");
    }

    @Test
    @DisplayName("Diferencia de 1 entre actual y máximo")
    void testDifferenceOfOne() {
        assertFalse(stopExecute.stopIterations(9, 10));
        assertTrue(stopExecute.stopIterations(10, 9));
    }

    @Test
    @DisplayName("Diferencia muy grande entre actual y máximo")
    void testLargeDifference() {
        assertFalse(stopExecute.stopIterations(10, 1000));
        assertTrue(stopExecute.stopIterations(1000, 10));
    }

    @Test
    @DisplayName("Patrón de ejecución típico")
    void testTypicalExecutionPattern() {
        int maxIterations = 100;
        int currentIteration = 0;
        
        while (!stopExecute.stopIterations(currentIteration, maxIterations)) {
            currentIteration++;
        }
        
        assertEquals(maxIterations, currentIteration, 
            "Debe detenerse exactamente en el máximo de iteraciones");
    }

    @Test
    @DisplayName("Debe funcionar con valores pequeños positivos")
    void testSmallPositiveValues() {
        assertTrue(stopExecute.stopIterations(1, 1));
        assertFalse(stopExecute.stopIterations(1, 2));
        assertTrue(stopExecute.stopIterations(2, 1));
    }

    @Test
    @DisplayName("Comportamiento con iteración actual en el límite")
    void testCurrentAtBoundary() {
        Boolean result = stopExecute.stopIterations(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        assertFalse(result);
        
        Boolean result2 = stopExecute.stopIterations(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertTrue(result2);
    }

    @Test
    @DisplayName("Múltiples instancias deben funcionar independientemente")
    void testMultipleInstancesIndependent() {
        StopExecute stop1 = new StopExecute();
        StopExecute stop2 = new StopExecute();
        
        Boolean result1 = stop1.stopIterations(5, 10);
        Boolean result2 = stop2.stopIterations(15, 10);
        
        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    @DisplayName("Verificar tipos de retorno")
    void testReturnTypes() {
        Boolean result = stopExecute.stopIterations(5, 10);
        assertTrue(result instanceof Boolean, "Debe retornar Boolean");
        assertFalse(result.getClass().equals(boolean.class), "Debe ser Boolean wrapper");
    }
}
