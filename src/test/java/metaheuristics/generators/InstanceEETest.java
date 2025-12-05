package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase InstanceEE
 */
class InstanceEETest {

    @Test
    @DisplayName("La clase InstanceEE existe")
    void testClassExists() {
        assertEquals("InstanceEE", InstanceEE.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase InstanceEE es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(InstanceEE.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase InstanceEE implementa Runnable")
    void testImplementsRunnable() {
        assertTrue(Runnable.class.isAssignableFrom(InstanceEE.class));
    }

    @Test
    @DisplayName("La clase InstanceEE tiene el campo terminate")
    void testTerminateFieldExists() {
        try {
            InstanceEE.class.getDeclaredField("terminate");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo terminate no existe");
        }
    }

    @Test
    @DisplayName("La clase InstanceEE tiene el método run")
    void testRunMethodExists() {
        try {
            InstanceEE.class.getMethod("run");
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("El método run no existe");
        }
    }
}
