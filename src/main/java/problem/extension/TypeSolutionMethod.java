/**
 * @file TypeSolutionMethod.java
 * @brief Enumeración de tipos de métodos de solución disponibles.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem.extension;

/**
 * @enum TypeSolutionMethod
 * @brief Define los tipos de métodos de solución para problemas multiobjetivo.
 */
public enum TypeSolutionMethod {

	/** @brief Método de factores ponderados */
	FACTORES_PONDERADOS,
	
	/** @brief Método de optimización multiobjetivo puro */
	MULTI_OBJETIVO_PURO; //OrdenamientoLexicografico,
	
	/**
	 * @brief Convierte el nombre del enum al nombre de la clase correspondiente.
	 * @return Nombre de la clase en formato PascalCase
	 */
	public String toClassName() {
		String[] words = this.name().split("_");
		StringBuilder className = new StringBuilder();
		for (String word : words) {
			className.append(Character.toUpperCase(word.charAt(0)));
			className.append(word.substring(1).toLowerCase());
		}
		return className.toString();
	}
}
