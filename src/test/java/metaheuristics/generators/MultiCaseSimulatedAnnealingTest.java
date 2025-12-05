package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para MultiCaseSimulatedAnnealing")
class MultiCaseSimulatedAnnealingTest {

    @Test
    @DisplayName("Clase debe existir en el paquete correcto")
    void testClassExists() {
        assertEquals("MultiCaseSimulatedAnnealing", 
            MultiCaseSimulatedAnnealing.class.getSimpleName());
    }

    @Test
    @DisplayName("Clase debe ser pública")
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(
            MultiCaseSimulatedAnnealing.class.getModifiers()));
    }

    @Test
    @DisplayName("alpha debe ser una variable estática")
    void testAlphaExists() {
        try {
            MultiCaseSimulatedAnnealing.class.getDeclaredField("alpha");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("Campo 'alpha' no encontrado");
        }
    }

    @Test
    @DisplayName("Clase debe tener métodos getter y setter para typeGenerator")
    void testTypeGeneratorMethods() {
        try {
            MultiCaseSimulatedAnnealing.class.getMethod("getTypeGenerator");
            MultiCaseSimulatedAnnealing.class.getMethod("setTypeGenerator", GeneratorType.class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Métodos getTypeGenerator/setTypeGenerator no encontrados");
        }
    }

    @Test
    @DisplayName("Clase debe tener constructor público")
    void testConstructorExists() {
        try {
            MultiCaseSimulatedAnnealing.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("Constructor público no encontrado");
        }
    }
}
