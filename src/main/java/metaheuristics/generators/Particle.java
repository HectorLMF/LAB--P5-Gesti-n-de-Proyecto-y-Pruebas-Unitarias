/**
 * @file Particle.java
 * @brief Implementación de una partícula para el algoritmo PSO
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;
import config.SecureRandomGenerator;

/**
 * @class Particle
 * @brief Representa una partícula individual en el algoritmo PSO
 * 
 * Esta clase encapsula el comportamiento de una partícula con su posición actual,
 * mejor posición personal y velocidad, actualizando su movimiento en el espacio de búsqueda.
 */
public class Particle extends Generator {

	/** @brief Mejor posición personal (pBest) de la partícula */
	private State statePBest;
	
	/** @brief Posición actual de la partícula */
	private State stateActual;
	
	/** @brief Vector de velocidad de la partícula */
	private ArrayList<Object> velocity;
	
	/**
	 * @brief Constructor por defecto de la partícula
	 * 
	 * Inicializa una partícula con estados y velocidad vacíos.
	 */
	public Particle() {
		super();
		this.stateActual = new State();
		this.statePBest = new State();
		this.velocity = new ArrayList<Object>();
	}
	
	/**
	 * @brief Constructor parametrizado de la partícula
	 * @param statePBest Mejor posición personal
	 * @param stateActual Posición actual
	 * @param velocity Vector de velocidad
	 */
	public Particle(State statePBest, State stateActual, ArrayList<Object> velocity) {
		super();
		this.statePBest = statePBest;
		this.stateActual = stateActual;
		this.velocity = velocity;
	}

	/**
	 * @brief Obtiene el vector de velocidad
	 * @return Vector de velocidad de la partícula
	 */
	public ArrayList<Object> getVelocity() {
		return velocity;
	}

	/**
	 * @brief Establece el vector de velocidad
	 * @param velocity Nuevo vector de velocidad
	 */
	public void setVelocity(ArrayList<Object> velocity) {
		this.velocity = velocity;
	}

	/**
	 * @brief Obtiene la mejor posición personal
	 * @return Estado pBest de la partícula
	 */
	public State getStatePBest() {
		return statePBest;
	}

	/**
	 * @brief Establece la mejor posición personal
	 * @param statePBest Nueva mejor posición personal
	 */
	public void setStatePBest(State statePBest) {
		this.statePBest = statePBest;
	}

	/**
	 * @brief Obtiene la posición actual
	 * @return Estado actual de la partícula
	 */
	public State getStateActual() {
		return stateActual;
	}

	/**
	 * @brief Establece la posición actual
	 * @param stateActual Nueva posición actual
	 */
	public void setStateActual(State stateActual) {
		this.stateActual = stateActual;
	}

	/**
	 * @brief Actualiza la velocidad y posición de la partícula
	 * @param operatornumber Número de operador (no utilizado)
	 * @return null (la posición se actualiza en stateActual)
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State generate(Integer operatornumber)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
		
		ArrayList<Object> actualVelocity = UpdateVelocity();
		ArrayList<Object> newCode = UpdateCode(actualVelocity);
		this.velocity = actualVelocity;
		this.stateActual.setCode(newCode);
		return null;
	}
	
	
	private ArrayList<Object> UpdateVelocity(){ // actualizar velocidad
		// compute inertia factor safely (Strategy may be null during tests)
		double countMax = 1.0;
		try {
			if (Strategy.getStrategy() != null) countMax = Math.max(1, Strategy.getStrategy().getCountMax());
		} catch (Throwable t) {
			countMax = 1.0;
		}
		double w = ParticleSwarmOptimization.wmax - ((ParticleSwarmOptimization.wmax - ParticleSwarmOptimization.wmin) / countMax) * ParticleSwarmOptimization.countCurrentIterPSO;  //CALCULO DE LA INERCIA
    	double rand1 = SecureRandomGenerator.nextDouble();
    	double rand2 = SecureRandomGenerator.nextDouble();
    	double inertia, cognitive, social;
		int learning = ParticleSwarmOptimization.learning1 + ParticleSwarmOptimization.learning2; // ratios de aprendizaje cognitivo y social
		// Use double arithmetic to avoid integer subtraction pitfalls
		ParticleSwarmOptimization.constriction = 2.0/(Math.abs(2.0 - (double)learning - Math.sqrt(((double)learning * (double)learning) - 4.0 * (double)learning)));   // Factor de costriccion
    	ArrayList<Object> actualVelocity = new ArrayList<Object>();
		// determine code/vector size using available state information
		int codeSize = 0;
		if (stateActual != null && stateActual.getCode() != null && stateActual.getCode().size() > 0) {
			codeSize = stateActual.getCode().size();
		} else {
			try {
				if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null && Strategy.getStrategy().getProblem().getState() != null && Strategy.getStrategy().getProblem().getState().getCode() != null) {
					codeSize = Strategy.getStrategy().getProblem().getState().getCode().size();
				}
			} catch (Throwable t) {
				codeSize = 0;
			}
		}
		if (velocity.isEmpty()){
			for (int i = 0; i < codeSize; i++){
				velocity.add(0.0);
			}
		}
		// recorre el vector velocidad y lo actualiza
		for (int i = 0; i < codeSize; i++) {
    		// cumulo donde se encuentra la particula
    		int swarm = ParticleSwarmOptimization.countParticle / ParticleSwarmOptimization.countParticleBySwarm; 
           	inertia = w * (Double)velocity.get(i);  
           	if(ParticleSwarmOptimization.binary == true){
				// safe casts for binary representation
				try {
					cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Integer)(this.statePBest.getCode().get(i)) - (Integer)(stateActual.getCode().get(i))));
				} catch (Throwable t) { cognitive = 0.0; }
				try {
					social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Integer)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Integer)(stateActual.getCode().get(i)))));
				} catch (Throwable t) { social = 0.0; }
           	}
           	else{
				try {
					cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Double)(this.statePBest.getCode().get(i)) - (Double)(stateActual.getCode().get(i))));
				} catch (Throwable t) { cognitive = 0.0; }
				try {
					social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Double)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Double)(stateActual.getCode().get(i)))));
				} catch (Throwable t) { social = 0.0; }
           	}
        	actualVelocity.add(ParticleSwarmOptimization.constriction*(inertia + cognitive + social));
        }
    /*    if (ParticleSwarmOptimization.binary == true){
    		for (int i = 0; i < actualVelocity.size(); i++) {
				binaryVelocity.add((3 + (Integer)actualVelocity.get(i))%3 - 1);     //FORMULA DE LA VELOCIDAD PARA CODIFICACION BINARIA  
			}
    		return binaryVelocity;
    	}*/
        return actualVelocity;
    }
	
	private ArrayList<Object> UpdateCode(ArrayList<Object> actualVelocity) {  // CALCULO DE LA NUEA POSICION DE LA PARTICULA
		ArrayList<Object> newCode = new ArrayList<Object>();
		ArrayList<Object> binaryCode = new ArrayList<Object>();
		//poner la condicion de si se esta trabajando con valores continuos o binarios
		if(ParticleSwarmOptimization.binary == false){	
			for (int i = 0; i < stateActual.getCode().size(); i++) {
	                newCode.add( (Double)(stateActual.getCode().get(i)) + (Double)(actualVelocity.get(i)) );
		    }
			return newCode;
	    }
		 else{                                                  //clculo de la posicion para codificacion binaria
			  for (int i = 0; i < stateActual.getCode().size(); i++){
				  double rand = SecureRandomGenerator.nextDouble();
				  double s = 1/(1 + 1.72 * (Double)(actualVelocity.get(i))); // 
				  if (rand < s){
				     binaryCode.add(1);
				  }
				   else{
				    	binaryCode.add(0);
				    	}
			  }
	          return binaryCode;
    }
			/*	if(ParticleSwarmOptimization.binary == true){//wendy
					for (int i = 0; i < newCode.size(); i++) {
						binaryCode.add(4 + ((Integer)newCode.get(i))%2);     //FORMULA DE LA POSICION PARA CODIFICACION BINARIA  
					}
					return binaryCode;
				}*/
		
	}
	

	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)){
			if(stateActual.getEvaluation().get(0) > statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateActual.getCode()));
				statePBest.setEvaluation(stateActual.getEvaluation());
			}
		}
		else{
			if(stateCandidate.getEvaluation().get(0) < statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateCandidate.getCode()));
				statePBest.setEvaluation(stateCandidate.getEvaluation());
			}
		}
		
	}

	@Override
	public State getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public GeneratorType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return null;
	}


}
