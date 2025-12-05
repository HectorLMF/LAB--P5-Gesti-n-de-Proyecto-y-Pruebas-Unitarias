package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase InstanceDE
 */
class InstanceDETest {

    @Test
    @DisplayName("La clase InstanceDE existe")
    void testClassExists() {
        assertEquals("InstanceDE", InstanceDE.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase InstanceDE es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(InstanceDE.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase InstanceDE implementa Runnable")
    void testImplementsRunnable() {
        assertTrue(Runnable.class.isAssignableFrom(InstanceDE.class));
    }

    @Test
    @DisplayName("La clase InstanceDE tiene el campo terminate")
    void testTerminateFieldExists() {
        try {
            InstanceDE.class.getDeclaredField("terminate");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo terminate no existe");
        }
    }

    @Test
    @DisplayName("La clase InstanceDE tiene el método run")
    void testRunMethodExists() {
        try {
            InstanceDE.class.getMethod("run");
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("El método run no existe");
        }
    }
}
