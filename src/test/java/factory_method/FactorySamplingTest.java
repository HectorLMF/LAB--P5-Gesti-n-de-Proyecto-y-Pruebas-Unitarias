package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.SamplingType;
import factory_interface.IFFSampling;

@DisplayName("Tests para FactorySampling")
class FactorySamplingTest {

    private FactorySampling factorySampling;

    @BeforeEach
    void setUp() {
        factorySampling = new FactorySampling();
    }

    @Test
    @DisplayName("FactorySampling debe ser instanciable")
    void testFactorySamplingInstantiation() {
        assertNotNull(factorySampling);
    }

    @Test
    @DisplayName("FactorySampling debe implementar IFFSampling")
    void testImplementsInterface() {
        assertInstanceOf(IFFSampling.class, factorySampling);
    }

    @Test
    @DisplayName("createSampling() con tipo null debe lanzar excepción")
    void testCreateSamplingWithNullType() {
        assertThrows(Exception.class, () -> {
            factorySampling.createSampling(null);
        });
    }
}
