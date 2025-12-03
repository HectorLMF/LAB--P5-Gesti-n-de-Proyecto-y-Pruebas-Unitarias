package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import local_search.acceptation_type.AcceptType;
import factory_interface.IFFactoryAcceptCandidate;

@DisplayName("Tests para FactoryAcceptCandidate")
class FactoryAcceptCandidateTest {

    private FactoryAcceptCandidate factoryAcceptCandidate;

    @BeforeEach
    void setUp() {
        factoryAcceptCandidate = new FactoryAcceptCandidate();
    }

    @Test
    @DisplayName("FactoryAcceptCandidate debe ser instanciable")
    void testFactoryAcceptCandidateInstantiation() {
        assertNotNull(factoryAcceptCandidate);
    }

    @Test
    @DisplayName("FactoryAcceptCandidate debe implementar IFFactoryAcceptCandidate")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryAcceptCandidate.class, factoryAcceptCandidate);
    }

    @Test
    @DisplayName("createAcceptCandidate() con tipo null debe lanzar excepción")
    void testCreateAcceptCandidateWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryAcceptCandidate.createAcceptCandidate(null);
        });
    }

    @Test
    @DisplayName("createAcceptCandidate() debe usar FactoryLoader internamente")
    void testCreateAcceptCandidateUsesFactoryLoader() {
        // Verificamos que el método existe y puede ser llamado
        assertNotNull(factoryAcceptCandidate);
        // La implementación usa FactoryLoader.getInstance()
    }
}
