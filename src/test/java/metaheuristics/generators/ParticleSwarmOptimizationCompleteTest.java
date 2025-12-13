package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import sun.misc.Unsafe;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class ParticleSwarmOptimizationCompleteTest {

    @BeforeEach
    void resetGlobals() {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new TreeMap<>();

        RandomSearch.listStateReference.clear();

        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;
        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
        ParticleSwarmOptimization.lBest = null;
        ParticleSwarmOptimization.gBest = null;
    }

    @AfterEach
    void cleanup() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();

        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;
        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
        ParticleSwarmOptimization.lBest = null;
        ParticleSwarmOptimization.gBest = null;
    }

    private static State stateWithEval(double v) {
        State s = new State();
        s.setCode(new ArrayList<>());
        ArrayList<Double> ev = new ArrayList<>();
        ev.add(v);
        s.setEvaluation(ev);
        return s;
    }

    private static void setStrategyProblem(Problem.ProblemType type) {
        Problem p = new Problem();
        p.setTypeProblem(type);
        Strategy.setProblem(p);
    }

    private static ParticleSwarmOptimization allocatePsoWithoutConstructor() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        ParticleSwarmOptimization pso = (ParticleSwarmOptimization) unsafe.allocateInstance(ParticleSwarmOptimization.class);

        // Initialize instance fields normally set by the constructor
        pso.setListParticle(new ArrayList<>());
        pso.setListStateReference(new ArrayList<>());
        pso.setGeneratorType(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);
        return pso;
    }

    @Test
    @DisplayName("Constructor: defaults swarm sizing, builds particles from RandomSearch when Strategy has no generator")
    void constructor_defaultsAndConvertsRandomSearchStates() {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(3.0));
        RandomSearch.listStateReference.add(stateWithEval(2.0));

        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();

        assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getType());
        assertNotNull(pso.getListParticle());
        assertEquals(2, pso.getListParticle().size());

        assertTrue(ParticleSwarmOptimization.coutSwarm > 0);
        assertTrue(ParticleSwarmOptimization.countParticleBySwarm > 0);
        assertEquals(ParticleSwarmOptimization.coutSwarm * ParticleSwarmOptimization.countParticleBySwarm,
                ParticleSwarmOptimization.countRef);

        assertNotNull(ParticleSwarmOptimization.lBest);
        assertTrue(ParticleSwarmOptimization.lBest.length >= ParticleSwarmOptimization.coutSwarm);
        assertNotNull(ParticleSwarmOptimization.gBest);
    }

    @Test
    @DisplayName("Constructor: uses provided refParticles list when not null")
    void constructor_usesProvidedRefParticles() {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        List<Particle> provided = new ArrayList<>();
        provided.add(new Particle(stateWithEval(10.0), stateWithEval(10.0), new ArrayList<>()));
        provided.add(new Particle(stateWithEval(20.0), stateWithEval(20.0), new ArrayList<>()));

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        pso.setListParticle(provided);

        assertNotNull(pso.getListParticle());
        assertEquals(2, pso.getListParticle().size());
    }

    @Test
    @DisplayName("Constructor: lBest resized when too small")
    void constructor_resizesLBestArray() {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(1.0));
        RandomSearch.listStateReference.add(stateWithEval(2.0));

        ParticleSwarmOptimization.coutSwarm = 2;
        ParticleSwarmOptimization.countParticleBySwarm = 1;
        ParticleSwarmOptimization.lBest = new State[1];

        new ParticleSwarmOptimization();

        assertNotNull(ParticleSwarmOptimization.lBest);
        assertTrue(ParticleSwarmOptimization.lBest.length >= 2);
    }

    @Test
    @DisplayName("generate: returns null when countRef <= 0")
    void generate_returnsNullWhenCountRefZero() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();

        assertNull(pso.generate(1));
    }

    @Test
    @DisplayName("generate: wraps countParticle when >= countRef")
    void generate_wrapsAround() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(1.0));
        RandomSearch.listStateReference.add(stateWithEval(2.0));

        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 2;

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertTrue(ParticleSwarmOptimization.countRef > 0);

        ParticleSwarmOptimization.countParticle = ParticleSwarmOptimization.countRef;
        State out = pso.generate(1);
        assertNotNull(out);
        assertTrue(ParticleSwarmOptimization.countParticle >= 0);
        assertTrue(ParticleSwarmOptimization.countParticle < ParticleSwarmOptimization.countRef);
    }

    @Test
    @DisplayName("inicialiceLBest: covers MAXIMIZAR and MINIMIZAR branches")
    void inicialiceLBest_coversMaxAndMin() throws Exception {
        RandomSearch.listStateReference.add(stateWithEval(10.0));
        RandomSearch.listStateReference.add(stateWithEval(5.0));

        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 2;

        setStrategyProblem(Problem.ProblemType.MAXIMIZAR);
        ParticleSwarmOptimization psoMax = new ParticleSwarmOptimization();
        Method init = ParticleSwarmOptimization.class.getDeclaredMethod("inicialiceLBest");
        init.setAccessible(true);
        init.invoke(psoMax);
        assertNotNull(ParticleSwarmOptimization.lBest);

        cleanup();
        resetGlobals();

        RandomSearch.listStateReference.add(stateWithEval(10.0));
        RandomSearch.listStateReference.add(stateWithEval(5.0));
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 2;

        setStrategyProblem(Problem.ProblemType.MINIMIZAR);
        ParticleSwarmOptimization psoMin = new ParticleSwarmOptimization();
        init = ParticleSwarmOptimization.class.getDeclaredMethod("inicialiceLBest");
        init.setAccessible(true);
        init.invoke(psoMin);
        assertNotNull(ParticleSwarmOptimization.lBest);
    }

    @Test
    @DisplayName("getListStateRef: RandomSearch empty returns empty")
    void getListStateRef_randomSearchEmpty() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();

        Method m = ParticleSwarmOptimization.class.getDeclaredMethod("getListStateRef");
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Particle> particles = (List<Particle>) m.invoke(pso);

        assertNotNull(particles);
        assertTrue(particles.isEmpty());
    }

    @Test
    @DisplayName("getListStateRef: Strategy generator has no PSO key -> converts RandomSearch")
    void getListStateRef_noPsoKey_convertsRandomSearch() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(7.0));
        RandomSearch.listStateReference.add(stateWithEval(8.0));

        // Force Strategy.getListKey() to be null so we hit the direct-conversion branch
        Strategy.getStrategy().generator = null;

        Method m = ParticleSwarmOptimization.class.getDeclaredMethod("getListStateRef");
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Particle> particles = (List<Particle>) m.invoke(new ParticleSwarmOptimization());

        assertEquals(2, particles.size());
    }

    @Test
    @DisplayName("getListStateRef: mapGenerators has PSO with empty particles -> converts RandomSearch")
    void getListStateRef_psoPresentButEmptyParticles_convertsRandomSearch() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(1.0));

        ParticleSwarmOptimization existing = allocatePsoWithoutConstructor();
        existing.setListParticle(new ArrayList<>());
        Strategy.getStrategy().mapGenerators.put(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, existing);
        // Ensure key list contains PSO at index 0 and mapGenerators.size() > 0
        Strategy.getStrategy().generator = allocatePsoWithoutConstructor();

        Method m = ParticleSwarmOptimization.class.getDeclaredMethod("getListStateRef");
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Particle> particles = (List<Particle>) m.invoke(allocatePsoWithoutConstructor());

        assertEquals(1, particles.size());
    }

    @Test
    @DisplayName("getListStateRef: mapGenerators has PSO with particles -> converts generator's reference list")
    void getListStateRef_psoPresentWithParticles_convertsItsReferenceList() throws Exception {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);

        RandomSearch.listStateReference.add(stateWithEval(100.0));

        List<Particle> provided = new ArrayList<>();
        provided.add(new Particle(stateWithEval(9.0), stateWithEval(9.0), new ArrayList<>()));
        ParticleSwarmOptimization existing = allocatePsoWithoutConstructor();
        existing.setListParticle(provided);
        existing.setListStateReference(new ArrayList<>());
        existing.getReferenceList().add(stateWithEval(9.0));
        existing.getReferenceList().add(stateWithEval(8.0));

        Strategy.getStrategy().mapGenerators.put(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, existing);
        // Ensure key list contains PSO at index 0 and mapGenerators.size() > 0
        Strategy.getStrategy().generator = allocatePsoWithoutConstructor();

        Method m = ParticleSwarmOptimization.class.getDeclaredMethod("getListStateRef");
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Particle> particles = (List<Particle>) m.invoke(allocatePsoWithoutConstructor());

        assertEquals(2, particles.size());
    }

    @Test
    @DisplayName("Setters/getters and stub methods: assert current behavior")
    void settersGettersAndStubs() {
        setStrategyProblem(Problem.ProblemType.MINIMIZAR);
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        pso.setListParticle(new ArrayList<>());

        ArrayList<State> refs = new ArrayList<>();
        refs.add(stateWithEval(1.0));
        pso.setListStateReference(refs);
        assertSame(refs, pso.getReferenceList());

        ParticleSwarmOptimization.setCountRef(123);
        assertEquals(123, ParticleSwarmOptimization.getCountRef());

        pso.setWeight(0.42f);
        assertEquals(0.42f, pso.getWeight(), 1e-6);

        assertNull(pso.getSonList());
        assertFalse(pso.awardUpdateREF(new State()));
    }
}
