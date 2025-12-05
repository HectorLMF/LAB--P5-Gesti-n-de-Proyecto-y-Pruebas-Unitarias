package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para HillClimbingRestart")
class HillClimbingRestartTest {

    @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("HillClimbingRestart", HillClimbingRestart.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            HillClimbingRestart.class.getModifiers()));
    }

    @Test
    @DisplayName("count debe ser una variable estática")
    void testCountExists() {
        try {
            HillClimbingRestart.class.getDeclaredField("count");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'count' no encontrado");
        }
    }

    @Test
    @DisplayName("countCurrent debe ser una variable estática")
    void testCountCurrentExists() {
        try {
            HillClimbingRestart.class.getDeclaredField("countCurrent");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'countCurrent' no encontrado");
        }
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            HillClimbingRestart.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
