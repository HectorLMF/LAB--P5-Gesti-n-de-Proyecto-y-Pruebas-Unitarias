package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase InstanceGA
 */
class InstanceGATest {

    @Test
    @DisplayName("La clase InstanceGA existe")
    void testClassExists() {
        assertEquals("InstanceGA", InstanceGA.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase InstanceGA es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(InstanceGA.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase InstanceGA implementa Runnable")
    void testImplementsRunnable() {
        assertTrue(Runnable.class.isAssignableFrom(InstanceGA.class));
    }

    @Test
    @DisplayName("La clase InstanceGA tiene el campo terminate")
    void testTerminateFieldExists() {
        try {
            InstanceGA.class.getDeclaredField("terminate");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo terminate no existe");
        }
    }

    @Test
    @DisplayName("La clase InstanceGA tiene el método run")
    void testRunMethodExists() {
        try {
            InstanceGA.class.getMethod("run");
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("El método run no existe");
        }
    }
}
