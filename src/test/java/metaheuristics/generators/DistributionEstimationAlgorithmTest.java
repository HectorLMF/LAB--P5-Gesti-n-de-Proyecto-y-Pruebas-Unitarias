package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para DistributionEstimationAlgorithm")
class DistributionEstimationAlgorithmTest {

    @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("DistributionEstimationAlgorithm", 
            DistributionEstimationAlgorithm.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            DistributionEstimationAlgorithm.class.getModifiers()));
    }

    @Test
    @DisplayName("sonList debe estar inicializado")
    void testSonListInitialized() {
        assertNotNull(DistributionEstimationAlgorithm.sonList);
    }

    @Test
    @DisplayName("sonList debe ser una List")
    void testSonListType() {
        assertTrue(DistributionEstimationAlgorithm.sonList instanceof java.util.List);
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            DistributionEstimationAlgorithm.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
