package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase MultiobjectiveStochasticHillClimbing
 */
class MultiobjectiveStochasticHillClimbingTest {

    @Test
    @DisplayName("La clase MultiobjectiveStochasticHillClimbing existe")
    void testClassExists() {
        assertEquals("MultiobjectiveStochasticHillClimbing", MultiobjectiveStochasticHillClimbing.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase MultiobjectiveStochasticHillClimbing es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MultiobjectiveStochasticHillClimbing.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase MultiobjectiveStochasticHillClimbing extiende Generator")
    void testExtendsGenerator() {
        assertTrue(Generator.class.isAssignableFrom(MultiobjectiveStochasticHillClimbing.class));
    }

    @Test
    @DisplayName("La clase tiene el campo candidatevalue")
    void testCandidatevalueFieldExists() {
        try {
            MultiobjectiveStochasticHillClimbing.class.getDeclaredField("candidatevalue");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo candidatevalue no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo typeAcceptation")
    void testTypeAcceptationFieldExists() {
        try {
            MultiobjectiveStochasticHillClimbing.class.getDeclaredField("typeAcceptation");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo typeAcceptation no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo listStateReference")
    void testListStateReferenceFieldExists() {
        try {
            MultiobjectiveStochasticHillClimbing.class.getDeclaredField("listStateReference");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo listStateReference no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo weight")
    void testWeightFieldExists() {
        try {
            MultiobjectiveStochasticHillClimbing.class.getDeclaredField("weight");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo weight no existe");
        }
    }
}
