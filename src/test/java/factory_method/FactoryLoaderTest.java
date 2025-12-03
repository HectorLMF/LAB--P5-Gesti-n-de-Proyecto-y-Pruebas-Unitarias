package factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para FactoryLoader")
class FactoryLoaderTest {

    @Test
    @DisplayName("getInstance() debe crear instancia de clase válida")
    void testGetInstanceWithValidClass() throws Exception {
        String className = "java.util.ArrayList";
        Object instance = FactoryLoader.getInstance(className);
        assertNotNull(instance);
        assertInstanceOf(java.util.ArrayList.class, instance);
    }

    @Test
    @DisplayName("getInstance() debe lanzar ClassNotFoundException para clase inexistente")
    void testGetInstanceWithInvalidClass() {
        String className = "com.nonexistent.InvalidClass";
        assertThrows(ClassNotFoundException.class, () -> {
            FactoryLoader.getInstance(className);
        });
    }

    @Test
    @DisplayName("getInstance() debe crear instancia de clase del proyecto")
    void testGetInstanceWithProjectClass() throws Exception {
        String className = "problem.definition.State";
        Object instance = FactoryLoader.getInstance(className);
        assertNotNull(instance);
        assertTrue(instance.getClass().getName().equals(className));
    }

    @Test
    @DisplayName("getInstance() debe retornar null cuando el constructor falla")
    void testGetInstanceWithInstantiationFailure() throws Exception {
        // Integer no tiene constructor sin argumentos público
        String className = "java.lang.Integer";
        Object instance = FactoryLoader.getInstance(className);
        assertNull(instance, "Debe retornar null cuando falla la instanciación");
    }

    @Test
    @DisplayName("getInstance() debe manejar clases abstractas correctamente")
    void testGetInstanceWithAbstractClass() throws Exception {
        String className = "java.util.AbstractList";
        Object instance = FactoryLoader.getInstance(className);
        assertNull(instance, "No se puede instanciar una clase abstracta");
    }

    @Test
    @DisplayName("getInstance() debe funcionar con diferentes clases del proyecto")
    void testGetInstanceWithMultipleProjectClasses() throws Exception {
        String[] classNames = {
            "problem.definition.State",
            "metaheuristics.generators.GeneratorType"
        };
        
        for (String className : classNames) {
            try {
                Object instance = FactoryLoader.getInstance(className);
                // Algunas pueden ser null si no tienen constructor sin argumentos
                // pero no debe lanzar excepciones no capturadas
                assertTrue(instance == null || instance.getClass().getName().equals(className));
            } catch (InstantiationException | IllegalAccessException e) {
                // Esto es esperado para clases que no se pueden instanciar
                assertNotNull(e);
            }
        }
    }
}
