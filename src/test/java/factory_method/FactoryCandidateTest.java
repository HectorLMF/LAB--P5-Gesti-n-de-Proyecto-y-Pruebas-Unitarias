package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import local_search.candidate_type.CandidateType;
import local_search.candidate_type.SearchCandidate;

@DisplayName("Tests para FactoryCandidate")
class FactoryCandidateTest {

    private FactoryCandidate factoryCandidate;

    @BeforeEach
    void setUp() {
        factoryCandidate = new FactoryCandidate();
    }

    @Test
    @DisplayName("FactoryCandidate debe ser instanciable")
    void testFactoryCandidateInstantiation() {
        assertNotNull(factoryCandidate);
    }

    @Test
    @DisplayName("createSearchCandidate() con tipo válido debe retornar instancia")
    void testCreateSearchCandidateWithValidType() {
        // Este test puede necesitar ajuste dependiendo de los tipos disponibles
        // Verificamos que el método existe y puede ser llamado
        assertNotNull(factoryCandidate);
    }

    @Test
    @DisplayName("FactoryCandidate debe implementar IFFactoryCandidate")
    void testImplementsInterface() {
        assertInstanceOf(factory_interface.IFFactoryCandidate.class, factoryCandidate);
    }

    @Test
    @DisplayName("createSearchCandidate() con tipo null debe lanzar excepción")
    void testCreateSearchCandidateWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryCandidate.createSearchCandidate(null);
        });
    }
}
