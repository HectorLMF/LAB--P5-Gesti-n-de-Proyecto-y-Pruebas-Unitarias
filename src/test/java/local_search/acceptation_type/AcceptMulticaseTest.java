package local_search.acceptation_type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para AcceptMulticase")
class AcceptMulticaseTest {

    @Test
    @DisplayName("Constructor debe crear instancia correctamente")
    void testConstructor() {
        AcceptMulticase acceptMulticase = new AcceptMulticase();
        assertNotNull(acceptMulticase);
    }

    @Test
    @DisplayName("Clase debe extender de AcceptableCandidate")
    void testInheritance() {
        AcceptMulticase acceptMulticase = new AcceptMulticase();
        assertTrue(acceptMulticase instanceof AcceptableCandidate);
    }

    @Test
    @DisplayName("Instancia debe ser de tipo AcceptMulticase")
    void testInstanceType() {
        AcceptMulticase acceptMulticase = new AcceptMulticase();
        assertEquals("AcceptMulticase", acceptMulticase.getClass().getSimpleName());
    }

    @Test
    @DisplayName("Debe poder crear múltiples instancias")
    void testMultipleInstances() {
        AcceptMulticase instance1 = new AcceptMulticase();
        AcceptMulticase instance2 = new AcceptMulticase();
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
}
