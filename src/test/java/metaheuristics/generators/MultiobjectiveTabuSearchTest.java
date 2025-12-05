package metaheuristics.generators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para la clase MultiobjectiveTabuSearch
 */
class MultiobjectiveTabuSearchTest {

    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch existe")
    void testClassExists() {
        assertEquals("MultiobjectiveTabuSearch", MultiobjectiveTabuSearch.class.getSimpleName());
    }

    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch es pública")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MultiobjectiveTabuSearch.class.getModifiers()));
    }

    @Test
    @DisplayName("La clase MultiobjectiveTabuSearch extiende Generator")
    void testExtendsGenerator() {
        assertTrue(Generator.class.isAssignableFrom(MultiobjectiveTabuSearch.class));
    }

    @Test
    @DisplayName("La clase tiene el campo candidatevalue")
    void testCandidatevalueFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("candidatevalue");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo candidatevalue no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo typeAcceptation")
    void testTypeAcceptationFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("typeAcceptation");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo typeAcceptation no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo stateReferenceTS")
    void testStateReferenceTSFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("stateReferenceTS");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo stateReferenceTS no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo typeGenerator")
    void testTypeGeneratorFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("typeGenerator");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo typeGenerator no existe");
        }
    }

    @Test
    @DisplayName("La clase tiene el campo strategy")
    void testStrategyFieldExists() {
        try {
            MultiobjectiveTabuSearch.class.getDeclaredField("strategy");
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            fail("El campo strategy no existe");
        }
    }
}
