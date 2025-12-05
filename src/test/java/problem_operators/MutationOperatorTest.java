package problem_operators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase MutationOperator en problem_operators
 */
class MutationOperatorTest {

    @Test
    @DisplayName("La clase MutationOperator existe")
    void testClassExists() {
        assertEquals("MutationOperator", problem_operators.MutationOperator.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase MutationOperator es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(problem_operators.MutationOperator.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase MutationOperator extiende Operator")
    void testExtendsOperator() {
        assertTrue(problem.definition.Operator.class.isAssignableFrom(problem_operators.MutationOperator.class));
    }

    @Test
    @DisplayName("El paquete es problem_operators")
    void testPackageName() {
        assertEquals("problem_operators", problem_operators.MutationOperator.class.getPackage().getName());
    }

    @Test
    @DisplayName("La clase tiene constructor público")
    void testHasPublicConstructor() {
        try {
            problem_operators.MutationOperator.class.getConstructor();
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("No se encontró constructor público sin argumentos");
        }
    }
}
