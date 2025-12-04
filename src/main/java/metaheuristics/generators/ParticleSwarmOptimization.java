/**
 * @file ParticleSwarmOptimization.java
 * @brief Implementación del algoritmo de optimización por enjambre de partículas (PSO)
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

/**
 * @class ParticleSwarmOptimization
 * @brief Algoritmo PSO que simula el comportamiento social de enjambres
 * 
 * Esta clase implementa el algoritmo PSO que utiliza una población de partículas
 * que se mueven en el espacio de búsqueda influenciadas por su mejor posición personal
 * y la mejor posición global del enjambre.
 */
public class ParticleSwarmOptimization extends Generator {
	
	private State stateReferencePSO;
	private List<State> listStateReference = new ArrayList<State>(); 
	private List<Particle> listParticle =  new ArrayList<Particle> ();
	private GeneratorType generatorType;
	public static int countRef = 0;            // CANTIDAD DE PARTICULAS TOTAL = coutSwarm * countParticleSwarm
	public static int countParticle = 0;       // CANTIDAD DE PARTICULAS QUE SE HAN MOVIDO EN CADA CUMULO
	public static int coutSwarm = 0;           //CANTIDAD DE CUMULOS
	public static int countParticleBySwarm = 0; //CANTIDAD DE PARTICULAS POR CUMULO
	private float weight = 0;
	public static double wmax = 0.9;
	public static double wmin = 0.2;
	public static int learning1 = 2, learning2 = 2;
	public static double constriction;
	public static boolean binary = false;
	public static State[] lBest; 
	public static State gBest;
	public static int countCurrentIterPSO;
	//problemas dinamicos
    private float[] listTrace = new float[1200000];
			
	public ParticleSwarmOptimization(){
		super();
		countRef = coutSwarm * countParticleBySwarm;
		List<Particle> refParticles = getListStateRef();
		if (refParticles != null) {
			for (Particle p : refParticles) {
				listParticle.add(p);
			}
		}

		// If no particles were populated but RandomSearch has states, convert them into particles
		if (listParticle.isEmpty() && RandomSearch.listStateReference != null && RandomSearch.listStateReference.size() > 0) {
			// If swarm sizing not configured, create one swarm containing all states
			if (coutSwarm <= 0) {
				coutSwarm = 1;
			}
			if (countParticleBySwarm <= 0) {
				countParticleBySwarm = RandomSearch.listStateReference.size();
			}
			countRef = coutSwarm * countParticleBySwarm;
			for (int j = 0; j < RandomSearch.listStateReference.size(); j++) {
				if (countRef == 0 || getListParticle().size() < countRef) {
					ArrayList<Object> velocity = new ArrayList<Object>();
					State stateAct = (State) RandomSearch.listStateReference.get(j).getCopy();
					stateAct.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
					stateAct.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
					State statePBest = (State) RandomSearch.listStateReference.get(j).getCopy();
					statePBest.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
					statePBest.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
					Particle particle = new Particle(stateAct, statePBest, velocity);
					getListParticle().add(particle);
				}
			}
		}
//		this.setListParticle(getListStateRef()); 
//		listStateReference = new ArrayList<State>(Strategy.getStrategy().listBest);
		this.generatorType = GeneratorType.PARTICLE_SWARM_OPTIMIZATION;
		this.weight = 0;
		if (lBest == null || lBest.length < Math.max(1, coutSwarm)) {
			lBest = new State[Math.max(1, coutSwarm)];
		}
		// Ensure countParticle starts at 0 for initialization routines
		countParticle = 0;
		if(!listParticle.isEmpty()){
			countCurrentIterPSO = 1;
			// ensure we have sensible swarm sizing
			if (coutSwarm <= 0) coutSwarm = 1;
			if (countParticleBySwarm <= 0) countParticleBySwarm = Math.max(1, listParticle.size());
			if (lBest == null || lBest.length < Math.max(1, coutSwarm)) {
				lBest = new State[Math.max(1, coutSwarm)];
			}
			inicialiceLBest();
			// Ensure gBest initialization is safe even if some lBest entries are null
			try {
				gBest = gBestInicial();
			} catch (Exception e) {
				// fallback: pick first available particle state's pbest if present
				if(!listParticle.isEmpty() && listParticle.get(0).getStatePBest() != null){
					gBest = listParticle.get(0).getStatePBest();
				}
			}
		}
		countParticle = 0;
		listTrace[0] = this.weight;
		this.listCountBetterGender = new int[10];
		this.listCountBetterGender[0] = 0;
		this.countGender = 0;
		this.countBetterGender = 0;
	}

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{ //PSO
		if (countRef <= 0 || listParticle == null || listParticle.isEmpty()) return null;
		if (countParticle >= countRef)
			countParticle = 0;
		// generate using current particle
		listParticle.get(countParticle).generate(1);
		State result = listParticle.get(countParticle).getStateActual();
		// increment the counter after generation so first call moves it to 1
		countParticle++;
		return result;
	}
   	
	public void inicialiceLBest (){
		// initialize local bests per swarm defensively
		int totalParticles = listParticle == null ? 0 : listParticle.size();
		for (int j = 0; j < Math.max(1, coutSwarm); j++) {
			State reference = null;
			int start = j * Math.max(1, countParticleBySwarm);
			int end = Math.min(start + Math.max(1, countParticleBySwarm), totalParticles);
			// pick an initial reference from available particles
			if (start < totalParticles) {
				reference = listParticle.get(start).getStatePBest();
			} else if (totalParticles > 0) {
				reference = listParticle.get(0).getStatePBest();
			}
			if (reference == null) {
				lBest[j] = null;
				continue;
			}
			// scan the particles in the swarm and choose best according to problem type if possible
			boolean maxim = true;
			try {
				if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null) {
					maxim = Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR);
				}
			} catch (Throwable t) {
				maxim = true; // default to maximization
			}
			for (int i = start; i < end; i++) {
				if (i >= totalParticles) break;
				State pbest = listParticle.get(i).getStatePBest();
				if (pbest == null) continue;
				try {
					if (maxim) {
						if (pbest.getEvaluation().get(0) > reference.getEvaluation().get(0)) reference = pbest;
					} else {
						if (pbest.getEvaluation().get(0) < reference.getEvaluation().get(0)) reference = pbest;
					}
				} catch (Exception e) {
					// ignore malformed evaluation and skip
				}
			}
			lBest[j] = reference;
		}
	}
	
	
	@Override
	public State getReference() {
		return null;
	}
	
	private List<Particle> getListStateRef() {
		Boolean found = false;
		// If RandomSearch global list is null or empty, return empty particle list
		if (RandomSearch.listStateReference == null || RandomSearch.listStateReference.size() == 0) {
			return this.setListParticle(new ArrayList<Particle>());
		}
		// If Strategy singleton is not available, convert RandomSearch states into particles directly
		if (Strategy.getStrategy() == null || Strategy.getStrategy().getListKey() == null || Strategy.getStrategy().mapGenerators == null) {
			for (int j = 0; j < RandomSearch.listStateReference.size(); j++) {
				if (countRef == 0 || getListParticle().size() < countRef) {
					ArrayList<Object> velocity = new ArrayList<Object>();
					State stateAct = (State) RandomSearch.listStateReference.get(j).getCopy();
					stateAct.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
					stateAct.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
					State statePBest = (State) RandomSearch.listStateReference.get(j).getCopy();
					statePBest.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
					statePBest.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
					Particle particle = new Particle(stateAct, statePBest, velocity);
					getListParticle().add(particle);
				}
			}
			return getListParticle();
		}
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			//recorrer la lista de generadores, hasta que encuentre el PSO
			if(key.get(count).equals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION.toString())){
				//creo el generador PSO, y si su lista de particulas esta vacia entonces es la primera vez que lo estoy creando, y cada estado lo convierto en particulas
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				ParticleSwarmOptimization generator = (ParticleSwarmOptimization) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if (generator.getListParticle().isEmpty()){
					// Convertir los estados en particulas
					for (int j = 0; j < RandomSearch.listStateReference.size(); j++) {
						// Si countRef is zero, allow conversion of all states
						if (countRef == 0 || getListParticle().size() < countRef) {
							ArrayList<Object> velocity = new ArrayList<Object>();
							State stateAct = (State) RandomSearch.listStateReference.get(j).getCopy();
							stateAct.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							stateAct.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
                            
							State statePBest = (State) RandomSearch.listStateReference.get(j).getCopy();
							statePBest.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							statePBest.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
                            
							Particle particle = new Particle(stateAct, statePBest, velocity);
							getListParticle().add(particle);
						}
					}  
				}
				else{
					List<Particle> particles = new ArrayList<>();
					for (State state : generator.getListStateReference()) {
						Particle particle = new Particle();
						particle.setStateActual(state);
						particle.setStatePBest(state);
						particles.add(particle);
					}
					setListParticle(particles);
				}
			        found = true;
			}
			count++;
		}
		return getListParticle();
	}


	public State getStateReferencePSO() {
		return stateReferencePSO;
	}

	public void setStateReferencePSO(State stateReferencePSO) {
		this.stateReferencePSO = stateReferencePSO;
	}

	public List<State> getListStateReference() {
		return this.listStateReference;
	}

	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = listStateReference;
	}

	public List<Particle> getListParticle() {
		return listParticle;
	}

	public List<Particle> setListParticle(List<Particle> listParticle) {
		this.listParticle = listParticle;
		return listParticle;
	}

	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	public static int getCountRef() {
		return countRef;
	}

	public static void setCountRef(int countRef) {
		ParticleSwarmOptimization.countRef = countRef;
	}


	//*****************************************
	@Override
	public void updateReference(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException,SecurityException, ClassNotFoundException, InstantiationException,IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Particle particle = new Particle();
		particle = listParticle.get(countParticle);
		int swarm = countParticle/countParticleBySwarm;
		// Guard against null lBest entries
		boolean maxim = true;
		try {
			if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null) {
				maxim = Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR);
			}
		} catch (Throwable t) {
			maxim = true;
		}
		if (lBest == null || swarm < 0 || swarm >= lBest.length) {
			// Ensure array sizing
			int newSize = Math.max(1, coutSwarm);
			State[] newLBest = new State[newSize];
			if (lBest != null) System.arraycopy(lBest, 0, newLBest, 0, Math.min(lBest.length, newSize));
			lBest = newLBest;
		}
		if (lBest[swarm] == null) {
			lBest[swarm] = particle.getStatePBest();
		}
		if (maxim) {
			if (lBest[swarm] != null && particle.getStatePBest() != null && lBest[swarm].getEvaluation().get(0) < particle.getStatePBest().getEvaluation().get(0)){
				lBest[swarm] = particle.getStatePBest();
				if(lBest[swarm] != null && !getReferenceList().isEmpty() && lBest[swarm].getEvaluation().get(0) > getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
					gBest = new State();
					gBest.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
					gBest.setEvaluation(lBest[swarm].getEvaluation());
					gBest.setTypeGenerator(lBest[swarm].getTypeGenerator());
				}
			}
		} else {
			particle.updateReference(stateCandidate, countIterationsCurrent);
			if (lBest[swarm] != null && particle.getStatePBest() != null && lBest[swarm].getEvaluation().get(0) > particle.getStatePBest().getEvaluation().get(0)){
				lBest[swarm] = particle.getStatePBest();
				if(lBest[swarm] != null && !getReferenceList().isEmpty() && lBest[swarm].getEvaluation().get(0) < getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
					gBest = new State();
					gBest.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
					gBest.setEvaluation(lBest[swarm].getEvaluation());
					gBest.setTypeGenerator(lBest[swarm].getTypeGenerator());
				}
			}
		}
		listStateReference.add(gBest);
		countParticle++;
		countCurrentIterPSO++;
	}
	
	public State gBestInicial (){
		// Find first non-null lBest to initialize
		State stateBest = null;
		for (int i = 0; i < lBest.length; i++) {
			if (lBest[i] != null) { stateBest = lBest[i]; break; }
		}
		if (stateBest == null) {
			// fallback to first particle's pbest if available
			if (!listParticle.isEmpty() && listParticle.get(0).getStatePBest() != null) return listParticle.get(0).getStatePBest();
			return null;
		}
		boolean maxim = true;
		try {
			if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null) {
				maxim = Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR);
			}
		} catch (Throwable t) {
			maxim = true;
		}
		for (int i = 0; i < lBest.length; i++) {
			if (lBest[i] == null) continue;
			try {
				if (maxim) {
					if (lBest[i].getEvaluation().get(0) > stateBest.getEvaluation().get(0)) stateBest = lBest[i];
				} else {
					if (lBest[i].getEvaluation().get(0) < stateBest.getEvaluation().get(0)) stateBest = lBest[i];
				}
			} catch (Exception e) {
				// ignore malformed evaluation
			}
		}
		return stateBest;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public GeneratorType getType() {
		// TODO Auto-generated method stub
		return this.generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
		return this.listStateReference;
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
		this.weight = weight;

	}

	@Override
	public float getWeight() {
		return this.weight;
	}

	@Override
	public int[] getListCountBetterGender() {
		return this.listCountBetterGender;
	}

	@Override
	public int[] getListCountGender() {
		int[] listCountGender = new int[10];
		listCountGender[0] = this.countGender;
		return listCountGender;
	}

	@Override
	public float[] getTrace() {
		return this.listTrace;
	}


	
}
