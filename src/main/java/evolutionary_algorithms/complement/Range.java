/**
 * @file Range.java
 * @brief Clase para representar un rango de probabilidad
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

/**
 * @class Range
 * @brief Representa un rango con valores mínimo, máximo y datos de probabilidad
 */
public class Range {
   /** @brief Datos de probabilidad asociados al rango */
   private Probability data;
   /** @brief Valor máximo del rango */
   private float max;
   /** @brief Valor mínimo del rango */
   private float min;
   
   /**
    * @brief Obtiene una copia de los datos de probabilidad
    * @return Copia de Probability o null si no hay datos
    */
   public Probability getData() {
      if (data == null) return null;
      Probability copy = new Probability();
      copy.setKey(data.getKey());
      copy.setValue(data.getValue());
      copy.setProbability(data.getProbability());
      return copy;
   }
   /**
    * @brief Establece los datos de probabilidad (realiza copia defensiva)
    * @param data Datos de probabilidad a establecer
    */
   public void setData(Probability data) {
      if (data == null) {
         this.data = null;
      } else {
         Probability copy = new Probability();
         copy.setKey(data.getKey());
         copy.setValue(data.getValue());
         copy.setProbability(data.getProbability());
         this.data = copy;
      }
   }
   /**
    * @brief Obtiene el valor máximo del rango
    * @return Valor máximo
    */
   public float getMax() {
	  return max;
   }
   /**
    * @brief Establece el valor máximo del rango
    * @param max Nuevo valor máximo
    */
   public void setMax(float max) {
	  this.max = max;
   }
   /**
    * @brief Obtiene el valor mínimo del rango
    * @return Valor mínimo
    */
   public float getMin() {
	  return min;
   }
   /**
    * @brief Establece el valor mínimo del rango
    * @param min Nuevo valor mínimo
    */
   public void setMin(float min) {
	  this.min = min;
   }
}
