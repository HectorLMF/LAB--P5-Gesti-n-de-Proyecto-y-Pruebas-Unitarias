package problem.operators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase MutationOperator en problem.operators
 */
class MutationOperatorTest {

    @Test
    @DisplayName("La clase MutationOperator existe")
    void testClassExists() {
        assertEquals("MutationOperator", MutationOperator.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase MutationOperator es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MutationOperator.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase MutationOperator extiende Operator")
    void testExtendsOperator() {
        assertTrue(problem.definition.Operator.class.isAssignableFrom(MutationOperator.class));
    }

    @Test
    @DisplayName("El paquete es problem.operators")
    void testPackageName() {
        assertEquals("problem.operators", MutationOperator.class.getPackage().getName());
    }

    @Test
    @DisplayName("La clase tiene constructor público")
    void testHasPublicConstructor() {
        try {
            MutationOperator.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("No se encontró constructor público sin argumentos");
        }
    }
}
