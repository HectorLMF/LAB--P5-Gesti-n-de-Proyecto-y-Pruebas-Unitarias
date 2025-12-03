package config;

import java.security.SecureRandom;

/**
 * Generador seguro de números aleatorios usando SecureRandom.
 * Reemplaza los usos inseguros de Math.random() en toda la aplicación.
 */
public final class SecureRandomGenerator {
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    // Constructor privado para prevenir instanciación
    private SecureRandomGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Genera un número aleatorio double entre 0.0 (inclusivo) y 1.0 (exclusivo).
     * 
     * @return número aleatorio double
     */
    public static double nextDouble() {
        return SECURE_RANDOM.nextDouble();
    }
    
    /**
     * Genera un número aleatorio int entre 0 (inclusivo) y bound (exclusivo).
     * 
     * @param bound límite superior (exclusivo)
     * @return número aleatorio int
     */
    public static int nextInt(int bound) {
        return SECURE_RANDOM.nextInt(bound);
    }
    
    /**
     * Genera un número aleatorio float entre 0.0 (inclusivo) y 1.0 (exclusivo).
     * 
     * @return número aleatorio float
     */
    public static float nextFloat() {
        return SECURE_RANDOM.nextFloat();
    }
}
