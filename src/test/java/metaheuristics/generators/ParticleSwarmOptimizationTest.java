package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import metaheuristics.generators.RandomSearch;
import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.Particle;
import metaheuristics.generators.GeneratorType;

public class ParticleSwarmOptimizationTest {

    private List<State> seedStates;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        // Ensure Strategy.mapGenerators is initialized to avoid NPE in getListKey
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        RandomSearch.listStateReference.clear();
        seedStates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            State s = new State();
            ArrayList<Object> code = new ArrayList<>();
            code.add(1.0 * i);
            code.add(2.0 * i);
            s.setCode(code);
            ArrayList<Double> eval = new ArrayList<>();
            eval.add(10.0 - i);
            s.setEvaluation(eval);
            seedStates.add(s);
        }
        RandomSearch.listStateReference.addAll(seedStates);
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = seedStates.size();
        ParticleSwarmOptimization.countRef = ParticleSwarmOptimization.coutSwarm * ParticleSwarmOptimization.countParticleBySwarm;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.gBest = null;
        ParticleSwarmOptimization.lBest = null;
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
    }

    @Test
    @DisplayName("Constructor sets type and builds particles from RandomSearch")
    void testConstructorAndParticles() {
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getType());
        assertEquals(seedStates.size(), pso.getListParticle().size());
        assertTrue(ParticleSwarmOptimization.lBest != null && ParticleSwarmOptimization.lBest.length >= 1);
    }

    @Test
    @DisplayName("Constructor with empty RandomSearch keeps particles empty and iter 0")
    void testConstructorWithEmptyRandomSearch() {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        RandomSearch.listStateReference.clear();
        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;
        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.gBest = null;
        ParticleSwarmOptimization.lBest = null;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertTrue(pso.getListParticle() == null || pso.getListParticle().isEmpty());
        assertEquals(0, ParticleSwarmOptimization.countCurrentIterPSO);
    }

    @Test
    @DisplayName("generate returns a state and advances counter")
    void testGenerateAdvancesCounter() throws Exception {
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        int before = ParticleSwarmOptimization.countParticle;
        State out = pso.generate(1);
        assertNotNull(out);
        assertEquals((before + 1) % Math.max(1, ParticleSwarmOptimization.countRef), ParticleSwarmOptimization.countParticle % Math.max(1, ParticleSwarmOptimization.countRef));
    }

    @Test
    @DisplayName("generate returns null when no particles or countRef=0")
    void testGenerateReturnsNullWhenNoParticles() throws Exception {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        RandomSearch.listStateReference.clear();
        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;
        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertNull(pso.generate(1));
    }

    @Test
    @DisplayName("updateReference appends to reference list and increments iter counter")
    void testUpdateReferenceAppends() throws Exception {
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() { @Override public Double Evaluation(State state) { return 0.0; }});
        p.setFunction(funcs);
        Strategy.setProblem(p);

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        int sizeBefore = pso.getReferenceList().size();
        ParticleSwarmOptimization.countParticle = 0;
        State cand = new State();
        ArrayList<Double> ev = new ArrayList<>(); ev.add(1.0); cand.setEvaluation(ev);
        pso.updateReference(cand, 0);
        assertEquals(sizeBefore + 1, pso.getReferenceList().size());
        assertTrue(ParticleSwarmOptimization.countCurrentIterPSO >= 1);
    }

    @Test
    @DisplayName("updateReference (max) updates lBest and sets gBest when improved")
    void testUpdateReference_Maximization_UpdatesLBestAndGBest() throws Exception {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        // Seed RandomSearch
        RandomSearch.listStateReference.clear();
        for (int i = 0; i < 3; i++) {
            State s = new State();
            s.setCode(new ArrayList<>());
            ArrayList<Double> ev = new ArrayList<>(); ev.add(5.0 + i); s.setEvaluation(ev);
            RandomSearch.listStateReference.add(s);
        }
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 3;
        // Problem MAXIMIZAR
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(p);

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // Ensure reference list has a low value so gBest condition triggers when improved
        State ref = new State();
        ArrayList<Double> refEv = new ArrayList<>(); refEv.add(4.0); ref.setEvaluation(refEv);
        pso.getReferenceList().add(ref);

        // Force current particle pBest to be higher than current lBest
        ParticleSwarmOptimization.countParticle = 0;
        Particle part0 = pso.getListParticle().get(0);
        State better = new State(); better.setCode(new ArrayList<>());
        ArrayList<Double> be = new ArrayList<>(); be.add(99.0); better.setEvaluation(be);
        part0.setStatePBest(better);

        // Lower current lBest to ensure update happens
        ParticleSwarmOptimization.lBest[0] = new State();
        ArrayList<Double> lb = new ArrayList<>(); lb.add(1.0); ParticleSwarmOptimization.lBest[0].setEvaluation(lb);

        pso.updateReference(new State(), 0);
        assertEquals(99.0, ParticleSwarmOptimization.lBest[0].getEvaluation().get(0), 1e-9);
        assertNotNull(ParticleSwarmOptimization.gBest);
        assertEquals(99.0, ParticleSwarmOptimization.gBest.getEvaluation().get(0), 1e-9);
    }

    @Test
    @DisplayName("updateReference (min) updates lBest and sets gBest when improved")
    void testUpdateReference_Minimization_UpdatesLBestAndGBest() throws Exception {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        // Seed RandomSearch
        RandomSearch.listStateReference.clear();
        for (int i = 0; i < 2; i++) {
            State s = new State();
            s.setCode(new ArrayList<>());
            ArrayList<Double> ev = new ArrayList<>(); ev.add(50.0 + i); s.setEvaluation(ev);
            RandomSearch.listStateReference.add(s);
        }
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 2;
        // Problem MINIMIZAR
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){ @Override public Double Evaluation(State s){ return s.getEvaluation().get(0); }});
        p.setFunction(funcs);
        Strategy.setProblem(p);

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // Ensure reference list has high value so new lBest (lower) sets gBest
        State ref = new State();
        ArrayList<Double> refEv = new ArrayList<>(); refEv.add(100.0); ref.setEvaluation(refEv);
        pso.getReferenceList().add(ref);

        ParticleSwarmOptimization.countParticle = 0;
        Particle part0 = pso.getListParticle().get(0);
        // Set current pBest high so candidate lower improves it
        State high = new State(); high.setCode(new ArrayList<>());
        ArrayList<Double> he = new ArrayList<>(); he.add(60.0); high.setEvaluation(he);
        part0.setStatePBest(high);

        // Current lBest higher than new pBest
        ParticleSwarmOptimization.lBest[0] = new State();
        ArrayList<Double> lb = new ArrayList<>(); lb.add(80.0); ParticleSwarmOptimization.lBest[0].setEvaluation(lb);

        // Candidate with lower value triggers particle.updateReference and lBest update
        State cand = new State(); cand.setCode(new ArrayList<>());
        ArrayList<Double> ce = new ArrayList<>(); ce.add(10.0); cand.setEvaluation(ce);
        pso.updateReference(cand, 0);

        assertEquals(10.0, ParticleSwarmOptimization.lBest[0].getEvaluation().get(0), 1e-9);
        assertNotNull(ParticleSwarmOptimization.gBest);
        assertEquals(10.0, ParticleSwarmOptimization.gBest.getEvaluation().get(0), 1e-9);
    }

    @Test
    @DisplayName("gBestInicial selects best for maximization")
    void testGBestInicial_Maximization() {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        Problem p = new Problem(); p.setTypeProblem(Problem.ProblemType.MAXIMIZAR); Strategy.setProblem(p);
        // Create PSO with any setup
        RandomSearch.listStateReference.clear();
        State s = new State(); ArrayList<Double> ev = new ArrayList<>(); ev.add(1.0); s.setEvaluation(ev); s.setCode(new ArrayList<>());
        RandomSearch.listStateReference.add(s);
        ParticleSwarmOptimization.coutSwarm = 1; ParticleSwarmOptimization.countParticleBySwarm = 1;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // Override lBest with known values
        ParticleSwarmOptimization.lBest = new State[3];
        for (int i = 0; i < 3; i++) {
            State st = new State(); ArrayList<Double> e = new ArrayList<>(); e.add(10.0 + i); st.setEvaluation(e);
            ParticleSwarmOptimization.lBest[i] = st;
        }
        State g = pso.gBestInicial();
        assertEquals(12.0, g.getEvaluation().get(0), 1e-9);
    }

    @Test
    @DisplayName("gBestInicial selects best for minimization and fallback when lBest null")
    void testGBestInicial_Minimization_AndFallback() {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        Problem p = new Problem(); p.setTypeProblem(Problem.ProblemType.MINIMIZAR); Strategy.setProblem(p);
        // Seed particles
        RandomSearch.listStateReference.clear();
        for (int i = 0; i < 2; i++) {
            State st = new State(); ArrayList<Double> e = new ArrayList<>(); e.add(20.0 + i); st.setEvaluation(e); st.setCode(new ArrayList<>());
            RandomSearch.listStateReference.add(st);
        }
        ParticleSwarmOptimization.coutSwarm = 1; ParticleSwarmOptimization.countParticleBySwarm = 2;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // Minimization best
        ParticleSwarmOptimization.lBest = new State[3];
        for (int i = 0; i < 3; i++) {
            State st = new State(); ArrayList<Double> e = new ArrayList<>(); e.add(30.0 - i); st.setEvaluation(e);
            ParticleSwarmOptimization.lBest[i] = st;
        }
        State gmin = pso.gBestInicial();
        assertEquals(28.0, gmin.getEvaluation().get(0), 1e-9);

        // Fallback when all lBest null -> first particle pBest
        ParticleSwarmOptimization.lBest = new State[2];
        State gfallback = pso.gBestInicial();
        assertNotNull(gfallback);
        assertEquals(RandomSearch.listStateReference.get(0).getEvaluation().get(0), gfallback.getEvaluation().get(0), 1e-9);
    }
}

