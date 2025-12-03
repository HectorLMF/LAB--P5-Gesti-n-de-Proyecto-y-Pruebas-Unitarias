package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.DistributionType;
import factory_interface.IFFactoryDistribution;

@DisplayName("Tests para FactoryDistribution")
class FactoryDistributionTest {

    private FactoryDistribution factoryDistribution;

    @BeforeEach
    void setUp() {
        factoryDistribution = new FactoryDistribution();
    }

    @Test
    @DisplayName("FactoryDistribution debe ser instanciable")
    void testFactoryDistributionInstantiation() {
        assertNotNull(factoryDistribution);
    }

    @Test
    @DisplayName("FactoryDistribution debe implementar IFFactoryDistribution")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryDistribution.class, factoryDistribution);
    }

    @Test
    @DisplayName("createDistribution() con tipo null debe lanzar excepción")
    void testCreateDistributionWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryDistribution.createDistribution(null);
        });
    }
}
