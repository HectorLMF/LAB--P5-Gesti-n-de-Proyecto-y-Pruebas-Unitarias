package evolutionary_algorithms.complement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase Distribution
 */
class DistributionTest {

    @Test
    @DisplayName("La clase Distribution existe")
    void testClassExists() {
        assertEquals("Distribution", Distribution.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase Distribution es abstracta")
    void testIsAbstract() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(Distribution.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase Distribution es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Distribution.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase Distribution tiene el método distribution")
    void testDistributionMethodExists() {
        try {
            Distribution.class.getDeclaredMethod("distribution", java.util.List.class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("El método distribution no existe");
        }
    }

    @Test
    @DisplayName("El método distribution es abstracto")
    void testDistributionMethodIsAbstract() {
        try {
            java.lang.reflect.Method method = Distribution.class.getDeclaredMethod("distribution", java.util.List.class);
            assertTrue(java.lang.reflect.Modifier.isAbstract(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("El método distribution no existe");
        }
    }

    @Test
    @DisplayName("El método distribution es público")
    void testDistributionMethodIsPublic() {
        try {
            java.lang.reflect.Method method = Distribution.class.getDeclaredMethod("distribution", java.util.List.class);
            assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("El método distribution no existe");
        }
    }

    @Test
    @DisplayName("El método distribution retorna List")
    void testDistributionMethodReturnType() {
        try {
            java.lang.reflect.Method method = Distribution.class.getDeclaredMethod("distribution", java.util.List.class);
            assertEquals(java.util.List.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("El método distribution no existe");
        }
    }
}
