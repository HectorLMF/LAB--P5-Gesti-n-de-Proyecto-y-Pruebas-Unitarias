package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests exhaustivos para la clase Probability
 */
@DisplayName("Tests para Probability")
class ProbabilityTest {

    private Probability probability;

    @BeforeEach
    void setUp() {
        probability = new Probability();
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener probabilidad")
    void testSetAndGetProbability() {
        probability.setProbability(0.5f);
        assertEquals(0.5f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener clave")
    void testSetAndGetKey() {
        Object key = "testKey";
        probability.setKey(key);
        assertEquals(key, probability.getKey());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener valor")
    void testSetAndGetValue() {
        Object value = 42;
        probability.setValue(value);
        assertEquals(value, probability.getValue());
    }

    @Test
    @DisplayName("Valores por defecto deben ser nulos")
    void testDefaultValues() {
        assertNull(probability.getKey(), "La clave debe ser null por defecto");
        assertNull(probability.getValue(), "El valor debe ser null por defecto");
        assertEquals(0.0f, probability.getProbability(), 0.001f, 
            "La probabilidad debe ser 0.0 por defecto");
    }

    @Test
    @DisplayName("Debe manejar probabilidad 0.0")
    void testZeroProbability() {
        probability.setProbability(0.0f);
        assertEquals(0.0f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar probabilidad 1.0")
    void testOneProbability() {
        probability.setProbability(1.0f);
        assertEquals(1.0f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar probabilidades decimales")
    void testDecimalProbabilities() {
        float[] probs = {0.1f, 0.25f, 0.33f, 0.5f, 0.75f, 0.999f};
        
        for (float prob : probs) {
            probability.setProbability(prob);
            assertEquals(prob, probability.getProbability(), 0.0001f);
        }
    }

    @Test
    @DisplayName("Debe manejar claves de diferentes tipos")
    void testDifferentKeyTypes() {
        Object[] keys = {1, "string", 3.14, true, new Object()};
        
        for (Object key : keys) {
            probability.setKey(key);
            assertEquals(key, probability.getKey());
        }
    }

    @Test
    @DisplayName("Debe manejar valores de diferentes tipos")
    void testDifferentValueTypes() {
        Object[] values = {100, "test", 2.718, false, new Object()};
        
        for (Object value : values) {
            probability.setValue(value);
            assertEquals(value, probability.getValue());
        }
    }

    @Test
    @DisplayName("Debe permitir establecer clave null")
    void testSetNullKey() {
        probability.setKey("notNull");
        probability.setKey(null);
        assertNull(probability.getKey());
    }

    @Test
    @DisplayName("Debe permitir establecer valor null")
    void testSetNullValue() {
        probability.setValue(42);
        probability.setValue(null);
        assertNull(probability.getValue());
    }

    @Test
    @DisplayName("Debe manejar probabilidades negativas")
    void testNegativeProbabilities() {
        probability.setProbability(-0.5f);
        assertEquals(-0.5f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar probabilidades mayores que 1")
    void testProbabilitiesGreaterThanOne() {
        probability.setProbability(2.5f);
        assertEquals(2.5f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe poder establecer todos los campos")
    void testSetAllFields() {
        Object key = 10;
        Object value = "testValue";
        float prob = 0.75f;
        
        probability.setKey(key);
        probability.setValue(value);
        probability.setProbability(prob);
        
        assertEquals(key, probability.getKey());
        assertEquals(value, probability.getValue());
        assertEquals(prob, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Múltiples modificaciones consecutivas deben funcionar")
    void testMultipleConsecutiveModifications() {
        probability.setProbability(0.1f);
        probability.setProbability(0.2f);
        probability.setProbability(0.3f);
        
        assertEquals(0.3f, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar valores extremos de probabilidad")
    void testExtremeFloatValues() {
        probability.setProbability(Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, probability.getProbability(), 0.001f);
        
        probability.setProbability(Float.MIN_VALUE);
        assertEquals(Float.MIN_VALUE, probability.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe mantener independencia entre instancias")
    void testIndependenceBetweenInstances() {
        Probability prob1 = new Probability();
        Probability prob2 = new Probability();
        
        prob1.setProbability(0.5f);
        prob1.setKey("key1");
        
        prob2.setProbability(0.8f);
        prob2.setKey("key2");
        
        assertEquals(0.5f, prob1.getProbability(), 0.001f);
        assertEquals(0.8f, prob2.getProbability(), 0.001f);
        assertEquals("key1", prob1.getKey());
        assertEquals("key2", prob2.getKey());
    }

    @Test
    @DisplayName("Debe manejar claves complejas")
    void testComplexKeys() {
        Object complexKey = new java.util.ArrayList<>();
        probability.setKey(complexKey);
        assertEquals(complexKey, probability.getKey());
    }

    @Test
    @DisplayName("Debe manejar valores complejos")
    void testComplexValues() {
        Object complexValue = new java.util.HashMap<>();
        probability.setValue(complexValue);
        assertEquals(complexValue, probability.getValue());
    }

    @Test
    @DisplayName("Debe permitir reasignación de valores")
    void testValueReassignment() {
        probability.setValue(100);
        assertEquals(100, probability.getValue());
        
        probability.setValue(200);
        assertEquals(200, probability.getValue());
        
        probability.setValue("string");
        assertEquals("string", probability.getValue());
    }

    @Test
    @DisplayName("Debe manejar probabilidades muy pequeñas")
    void testVerySmallProbabilities() {
        probability.setProbability(0.0001f);
        assertEquals(0.0001f, probability.getProbability(), 0.00001f);
    }

    @Test
    @DisplayName("Debe manejar probabilidades muy grandes")
    void testVeryLargeProbabilities() {
        probability.setProbability(1000000.0f);
        assertEquals(1000000.0f, probability.getProbability(), 0.1f);
    }
}
