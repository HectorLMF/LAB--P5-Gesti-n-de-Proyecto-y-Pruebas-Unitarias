package evolutionary_algorithms.complement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@DisplayName("Tests para AIOMutation")
class AIOMutationTest {

    @Test
    @DisplayName("Constructor debe crear instancia correctamente")
    void testConstructor() {
        AIOMutation aiomMutation = new AIOMutation();
        assertNotNull(aiomMutation);
    }

    @Test
    @DisplayName("Clase debe extender de Mutation")
    void testInheritance() {
        AIOMutation aiomMutation = new AIOMutation();
        assertTrue(aiomMutation instanceof Mutation);
    }

    @Test
    @DisplayName("Instancia debe ser de tipo AIOMutation")
    void testInstanceType() {
        AIOMutation aiomMutation = new AIOMutation();
        assertEquals("AIOMutation", aiomMutation.getClass().getSimpleName());
    }

    @Test
    @DisplayName("path debe ser un ArrayList")
    void testPathIsArrayList() {
        assertNotNull(AIOMutation.path);
        assertTrue(AIOMutation.path instanceof ArrayList);
    }

    @Test
    @DisplayName("path debe poder ser limpiado")
    void testPathCanBeCleared() {
        AIOMutation.path.clear();
        assertEquals(0, AIOMutation.path.size());
    }

    @Test
    @DisplayName("Debe poder crear múltiples instancias")
    void testMultipleInstances() {
        AIOMutation instance1 = new AIOMutation();
        AIOMutation instance2 = new AIOMutation();
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
}
