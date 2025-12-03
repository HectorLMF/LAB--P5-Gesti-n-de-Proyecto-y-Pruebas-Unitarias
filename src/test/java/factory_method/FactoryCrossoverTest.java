package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.CrossoverType;
import factory_interface.IFFactoryCrossover;

@DisplayName("Tests para FactoryCrossover")
class FactoryCrossoverTest {

    private FactoryCrossover factoryCrossover;

    @BeforeEach
    void setUp() {
        factoryCrossover = new FactoryCrossover();
    }

    @Test
    @DisplayName("FactoryCrossover debe ser instanciable")
    void testFactoryCrossoverInstantiation() {
        assertNotNull(factoryCrossover);
    }

    @Test
    @DisplayName("FactoryCrossover debe implementar IFFactoryCrossover")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryCrossover.class, factoryCrossover);
    }

    @Test
    @DisplayName("createCrossover() con tipo null debe lanzar excepción")
    void testCreateCrossoverWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryCrossover.createCrossover(null);
        });
    }
}
