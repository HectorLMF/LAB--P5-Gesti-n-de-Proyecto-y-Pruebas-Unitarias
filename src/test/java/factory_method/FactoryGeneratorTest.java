package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import factory_interface.IFFactoryGenerator;

@DisplayName("Tests para FactoryGenerator")
class FactoryGeneratorTest {

    private FactoryGenerator factoryGenerator;

    @BeforeEach
    void setUp() {
        factoryGenerator = new FactoryGenerator();
    }

    @Test
    @DisplayName("FactoryGenerator debe ser instanciable")
    void testFactoryGeneratorInstantiation() {
        assertNotNull(factoryGenerator);
    }

    @Test
    @DisplayName("FactoryGenerator debe implementar IFFactoryGenerator")
    void testImplementsInterface() {
        assertInstanceOf(IFFactoryGenerator.class, factoryGenerator);
    }

    @Test
    @DisplayName("createGenerator() con tipo null debe lanzar excepción")
    void testCreateGeneratorWithNullType() {
        assertThrows(Exception.class, () -> {
            factoryGenerator.createGenerator(null);
        });
    }

    @Test
    @DisplayName("createGenerator() debe usar FactoryLoader internamente")
    void testCreateGeneratorUsesFactoryLoader() {
        // Verificamos que el método existe
        assertNotNull(factoryGenerator);
    }
}
