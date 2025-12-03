package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.MutationType;
import factory_interface.IFFactoryMutation;

@DisplayName("Tests para FactoryMutation")
class FactoryMutationTest {

    private FactoryMutation factoryMutation;

    @BeforeEach
    void setUp() {
        factoryMutation = new FactoryMutation();
    }

    @Test
    @DisplayName("FactoryMutation debe ser instanciable")
    void testFactoryMutationInstantiation() {
        assertNotNull(factoryMutation);
    }

    @Test
    @DisplayName("FactoryMutation debe implementar IFFactoryMutation")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryMutation.class, factoryMutation);
    }

    @Test
    @DisplayName("createMutation() con tipo null debe lanzar excepción")
    void testCreateMutationWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryMutation.createMutation(null);
        });
    }
}
