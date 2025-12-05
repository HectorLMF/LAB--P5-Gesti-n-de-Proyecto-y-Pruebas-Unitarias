package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para MultiobjectiveHillClimbingRestart")
class MultiobjectiveHillClimbingRestartTest {

    @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("MultiobjectiveHillClimbingRestart", 
            MultiobjectiveHillClimbingRestart.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            MultiobjectiveHillClimbingRestart.class.getModifiers()));
    }

    @Test
    @DisplayName("Clase debe tener campos protegidos definidos")
    void testProtectedFields() {
        try {
            MultiobjectiveHillClimbingRestart.class.getDeclaredField("candidatevalue");
            MultiobjectiveHillClimbingRestart.class.getDeclaredField("typeAcceptation");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campos protegidos no encontrados");
        }
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            MultiobjectiveHillClimbingRestart.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }

    @Test
    @DisplayName("Clase debe extender Generator")
    void testExtendsGenerator() {
        assertTrue(Generator.class.isAssignableFrom(MultiobjectiveHillClimbingRestart.class));
    }
}
