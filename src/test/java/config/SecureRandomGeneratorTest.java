package config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para SecureRandomGenerator")
class SecureRandomGeneratorTest {

    @Test
    @DisplayName("nextDouble() debe generar números entre 0.0 y 1.0")
    void testNextDouble() {
        double value = SecureRandomGenerator.nextDouble();
        assertTrue(value >= 0.0 && value < 1.0, 
            "El valor debe estar entre 0.0 (inclusivo) y 1.0 (exclusivo)");
    }

    @RepeatedTest(100)
    @DisplayName("nextDouble() debe generar diferentes valores")
    void testNextDoubleRandomness() {
        double value1 = SecureRandomGenerator.nextDouble();
        double value2 = SecureRandomGenerator.nextDouble();
        // Con 100 repeticiones, es extremadamente improbable que todos sean iguales
        assertTrue(value1 >= 0.0 && value1 < 1.0);
        assertTrue(value2 >= 0.0 && value2 < 1.0);
    }

    @Test
    @DisplayName("nextInt() debe generar números dentro del rango especificado")
    void testNextInt() {
        int bound = 10;
        int value = SecureRandomGenerator.nextInt(bound);
        assertTrue(value >= 0 && value < bound, 
            "El valor debe estar entre 0 (inclusivo) y " + bound + " (exclusivo)");
    }

    @Test
    @DisplayName("nextInt() con bound = 1 debe retornar 0")
    void testNextIntWithBoundOne() {
        int value = SecureRandomGenerator.nextInt(1);
        assertEquals(0, value, "Con bound=1, el único valor posible es 0");
    }

    @RepeatedTest(50)
    @DisplayName("nextInt() debe generar diferentes valores")
    void testNextIntRandomness() {
        int bound = 100;
        int value = SecureRandomGenerator.nextInt(bound);
        assertTrue(value >= 0 && value < bound);
    }

    @Test
    @DisplayName("nextFloat() debe generar números entre 0.0 y 1.0")
    void testNextFloat() {
        float value = SecureRandomGenerator.nextFloat();
        assertTrue(value >= 0.0f && value < 1.0f, 
            "El valor debe estar entre 0.0 (inclusivo) y 1.0 (exclusivo)");
    }

    @RepeatedTest(100)
    @DisplayName("nextFloat() debe generar diferentes valores")
    void testNextFloatRandomness() {
        float value1 = SecureRandomGenerator.nextFloat();
        float value2 = SecureRandomGenerator.nextFloat();
        assertTrue(value1 >= 0.0f && value1 < 1.0f);
        assertTrue(value2 >= 0.0f && value2 < 1.0f);
    }

    @Test
    @DisplayName("Constructor debe lanzar UnsupportedOperationException")
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            java.lang.reflect.Constructor<SecureRandomGenerator> constructor = 
                SecureRandomGenerator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }, "El constructor debe lanzar UnsupportedOperationException");
    }

    @Test
    @DisplayName("nextInt() con valores grandes debe funcionar correctamente")
    void testNextIntWithLargeBound() {
        int bound = 1000000;
        int value = SecureRandomGenerator.nextInt(bound);
        assertTrue(value >= 0 && value < bound);
    }
}
