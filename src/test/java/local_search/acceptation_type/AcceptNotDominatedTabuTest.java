package local_search.acceptation_type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para AcceptNotDominatedTabu")
class AcceptNotDominatedTabuTest {

    @Test
    @DisplayName("Constructor debe crear instancia correctamente")
    void testConstructor() {
        AcceptNotDominatedTabu acceptNotDominatedTabu = new AcceptNotDominatedTabu();
        assertNotNull(acceptNotDominatedTabu);
    }

    @Test
    @DisplayName("Clase debe extender de AcceptableCandidate")
    void testInheritance() {
        AcceptNotDominatedTabu acceptNotDominatedTabu = new AcceptNotDominatedTabu();
        assertTrue(acceptNotDominatedTabu instanceof AcceptableCandidate);
    }

    @Test
    @DisplayName("Instancia debe ser de tipo AcceptNotDominatedTabu")
    void testInstanceType() {
        AcceptNotDominatedTabu acceptNotDominatedTabu = new AcceptNotDominatedTabu();
        assertEquals("AcceptNotDominatedTabu", acceptNotDominatedTabu.getClass().getSimpleName());
    }

    @Test
    @DisplayName("Debe poder crear múltiples instancias")
    void testMultipleInstances() {
        AcceptNotDominatedTabu instance1 = new AcceptNotDominatedTabu();
        AcceptNotDominatedTabu instance2 = new AcceptNotDominatedTabu();
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
}
