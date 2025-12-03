package evolutionary_algorithms.complement;

public class Range {
   private Probability data;
   private float max;
   private float min;
   
   public Probability getData() {
      if (data == null) return null;
      Probability copy = new Probability();
      copy.setKey(data.getKey());
      copy.setValue(data.getValue());
      copy.setProbability(data.getProbability());
      return copy;
   }
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
   public float getMax() {
	  return max;
   }
   public void setMax(float max) {
	  this.max = max;
   }
   public float getMin() {
	  return min;
   }
   public void setMin(float min) {
	  this.min = min;
   }
}
