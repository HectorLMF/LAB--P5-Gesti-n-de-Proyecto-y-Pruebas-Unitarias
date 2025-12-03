package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import evolutionary_algorithms.complement.ReplaceType;
import factory_interface.IFFactoryReplace;

@DisplayName("Tests para FactoryReplace")
class FactoryReplaceTest {

    private FactoryReplace factoryReplace;

    @BeforeEach
    void setUp() {
        factoryReplace = new FactoryReplace();
    }

    @Test
    @DisplayName("FactoryReplace debe ser instanciable")
    void testFactoryReplaceInstantiation() {
        assertNotNull(factoryReplace);
    }

    @Test
    @DisplayName("FactoryReplace debe implementar IFFactoryReplace")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryReplace.class, factoryReplace);
    }

    @Test
    @DisplayName("createReplace() con tipo null debe lanzar excepción")
    void testCreateReplaceWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryReplace.createReplace(null);
        });
    }
}
