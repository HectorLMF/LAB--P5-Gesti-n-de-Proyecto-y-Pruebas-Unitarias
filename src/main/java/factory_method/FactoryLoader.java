package factory_method;
import java.lang.reflect.InvocationTargetException;


public class FactoryLoader {

	public static Object getInstance(String className) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		@SuppressWarnings("rawtypes")
		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// Clase no encontrada: comportamiento esperado es retornar null
			return null;
		}

		try {
			return c.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// No se puede instanciar (abstracta, sin ctor por defecto, etc.)
			return null;
		}
	}
}
