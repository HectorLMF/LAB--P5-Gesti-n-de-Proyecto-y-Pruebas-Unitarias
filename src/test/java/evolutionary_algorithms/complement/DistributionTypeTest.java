package evolutionary_algorithms.complement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para el enum DistributionType
 */
class DistributionTypeTest {

    @Test
    @DisplayName("El enum DistributionType existe")
    void testEnumExists() {
        assertEquals("DistributionType", DistributionType.class.getSimpleName());
    }

    @Test
    @DisplayName("El enum DistributionType es público")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(DistributionType.class.getModifiers()));
    }

    @Test
    @DisplayName("El enum tiene el valor UNIVARIATE")
    void testUnivariateExists() {
        assertNotNull(DistributionType.UNIVARIATE);
    }

    @Test
    @DisplayName("El método valueOf funciona con UNIVARIATE")
    void testValueOf() {
        assertEquals(DistributionType.UNIVARIATE, DistributionType.valueOf("UNIVARIATE"));
    }

    @Test
    @DisplayName("El enum tiene exactamente 1 valor")
    void testEnumValuesCount() {
        assertEquals(1, DistributionType.values().length);
    }
}
