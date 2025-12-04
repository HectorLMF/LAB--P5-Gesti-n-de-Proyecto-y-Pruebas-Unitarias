package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests exhaustivos para la clase Range
 */
@DisplayName("Tests para Range")
class RangeTest {

    private Range range;

    @BeforeEach
    void setUp() {
        range = new Range();
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener valor mínimo")
    void testSetAndGetMin() {
        range.setMin(0.0f);
        assertEquals(0.0f, range.getMin(), 0.001f);
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener valor máximo")
    void testSetAndGetMax() {
        range.setMax(1.0f);
        assertEquals(1.0f, range.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener datos de probabilidad")
    void testSetAndGetData() {
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        prob.setKey("key");
        prob.setValue(10);
        
        range.setData(prob);
        Probability retrieved = range.getData();
        
        assertNotNull(retrieved);
        assertEquals(0.5f, retrieved.getProbability(), 0.001f);
        assertEquals("key", retrieved.getKey());
        assertEquals(10, retrieved.getValue());
    }

    @Test
    @DisplayName("getData debe retornar copia defensiva")
    void testGetDataReturnsDefensiveCopy() {
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        range.setData(prob);
        
        Probability retrieved1 = range.getData();
        Probability retrieved2 = range.getData();
        
        assertNotSame(retrieved1, retrieved2, "Debe retornar copias diferentes");
    }

    @Test
    @DisplayName("setData debe crear copia defensiva")
    void testSetDataCreatesDefensiveCopy() {
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        range.setData(prob);
        
        // Modificar el objeto original no debe afectar al almacenado
        prob.setProbability(0.9f);
        
        Probability retrieved = range.getData();
        assertEquals(0.5f, retrieved.getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar datos null")
    void testNullData() {
        range.setData(null);
        assertNull(range.getData());
    }

    @Test
    @DisplayName("Valores por defecto deben ser cero o null")
    void testDefaultValues() {
        assertEquals(0.0f, range.getMin(), 0.001f);
        assertEquals(0.0f, range.getMax(), 0.001f);
        assertNull(range.getData());
    }

    @Test
    @DisplayName("Debe manejar rango [0, 1]")
    void testRangeZeroToOne() {
        range.setMin(0.0f);
        range.setMax(1.0f);
        
        assertEquals(0.0f, range.getMin(), 0.001f);
        assertEquals(1.0f, range.getMax(), 0.001f);
        assertTrue(range.getMax() > range.getMin());
    }

    @Test
    @DisplayName("Debe manejar rango negativo")
    void testNegativeRange() {
        range.setMin(-10.0f);
        range.setMax(-5.0f);
        
        assertEquals(-10.0f, range.getMin(), 0.001f);
        assertEquals(-5.0f, range.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Debe permitir min mayor que max")
    void testMinGreaterThanMax() {
        range.setMin(10.0f);
        range.setMax(5.0f);
        
        assertEquals(10.0f, range.getMin(), 0.001f);
        assertEquals(5.0f, range.getMax(), 0.001f);
        // La clase no valida, solo almacena
    }

    @Test
    @DisplayName("Debe manejar min igual a max")
    void testMinEqualsMax() {
        range.setMin(5.0f);
        range.setMax(5.0f);
        
        assertEquals(5.0f, range.getMin(), 0.001f);
        assertEquals(5.0f, range.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar valores decimales precisos")
    void testPreciseDecimalValues() {
        range.setMin(0.123456f);
        range.setMax(0.789012f);
        
        assertEquals(0.123456f, range.getMin(), 0.000001f);
        assertEquals(0.789012f, range.getMax(), 0.000001f);
    }

    @Test
    @DisplayName("Debe manejar valores extremos")
    void testExtremeValues() {
        range.setMin(Float.MIN_VALUE);
        range.setMax(Float.MAX_VALUE);
        
        assertEquals(Float.MIN_VALUE, range.getMin(), 0.001f);
        assertEquals(Float.MAX_VALUE, range.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Múltiples modificaciones consecutivas deben funcionar")
    void testMultipleConsecutiveModifications() {
        range.setMin(1.0f);
        range.setMin(2.0f);
        range.setMin(3.0f);
        
        assertEquals(3.0f, range.getMin(), 0.001f);
    }

    @Test
    @DisplayName("Debe mantener independencia entre instancias")
    void testIndependenceBetweenInstances() {
        Range range1 = new Range();
        Range range2 = new Range();
        
        range1.setMin(0.0f);
        range1.setMax(10.0f);
        
        range2.setMin(20.0f);
        range2.setMax(30.0f);
        
        assertEquals(0.0f, range1.getMin(), 0.001f);
        assertEquals(10.0f, range1.getMax(), 0.001f);
        assertEquals(20.0f, range2.getMin(), 0.001f);
        assertEquals(30.0f, range2.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Debe poder establecer todos los campos")
    void testSetAllFields() {
        Probability prob = new Probability();
        prob.setProbability(0.75f);
        prob.setKey(100);
        prob.setValue("test");
        
        range.setMin(5.0f);
        range.setMax(15.0f);
        range.setData(prob);
        
        assertEquals(5.0f, range.getMin(), 0.001f);
        assertEquals(15.0f, range.getMax(), 0.001f);
        assertNotNull(range.getData());
        assertEquals(0.75f, range.getData().getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Datos deben ser independientes después de setData")
    void testDataIndependenceAfterSet() {
        Probability prob1 = new Probability();
        prob1.setProbability(0.3f);
        range.setData(prob1);
        
        Probability prob2 = new Probability();
        prob2.setProbability(0.7f);
        range.setData(prob2);
        
        assertEquals(0.7f, range.getData().getProbability(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar rango muy grande")
    void testVeryLargeRange() {
        range.setMin(0.0f);
        range.setMax(1000000.0f);
        
        assertEquals(0.0f, range.getMin(), 0.001f);
        assertEquals(1000000.0f, range.getMax(), 0.001f);
    }

    @Test
    @DisplayName("Debe manejar rango muy pequeño")
    void testVerySmallRange() {
        range.setMin(0.0001f);
        range.setMax(0.0002f);
        
        assertEquals(0.0001f, range.getMin(), 0.00001f);
        assertEquals(0.0002f, range.getMax(), 0.00001f);
    }

    @Test
    @DisplayName("Transición de null a no-null en datos")
    void testTransitionFromNullToNonNull() {
        assertNull(range.getData());
        
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        range.setData(prob);
        
        assertNotNull(range.getData());
    }

    @Test
    @DisplayName("Transición de no-null a null en datos")
    void testTransitionFromNonNullToNull() {
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        range.setData(prob);
        
        assertNotNull(range.getData());
        
        range.setData(null);
        
        assertNull(range.getData());
    }

    @Test
    @DisplayName("Debe preservar datos con probabilidad nula en clave/valor")
    void testPreserveDataWithNullKeyValue() {
        Probability prob = new Probability();
        prob.setProbability(0.5f);
        prob.setKey(null);
        prob.setValue(null);
        
        range.setData(prob);
        Probability retrieved = range.getData();
        
        assertNotNull(retrieved);
        assertEquals(0.5f, retrieved.getProbability(), 0.001f);
        assertNull(retrieved.getKey());
        assertNull(retrieved.getValue());
    }

    @Test
    @DisplayName("Múltiples llamadas a getData deben ser consistentes")
    void testMultipleGetDataCallsConsistent() {
        Probability prob = new Probability();
        prob.setProbability(0.6f);
        range.setData(prob);
        
        Probability retrieved1 = range.getData();
        Probability retrieved2 = range.getData();
        
        assertEquals(retrieved1.getProbability(), retrieved2.getProbability(), 0.001f);
    }
}
