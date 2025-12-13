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
import java.lang.reflect.Method;
import java.util.TreeMap;
import static org.mockito.Mockito.*;

public class ParticleSwarmOptimizationTest {

    private List<State> seedStates;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        // Ensure Strategy.mapGenerators is initialized to avoid NPE in getListKey
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        // Use a fresh list instance to avoid accidental aliasing across tests
        RandomSearch.listStateReference = new ArrayList<>();
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
        // add a copy so RandomSearch holds its own independent list
        RandomSearch.listStateReference.addAll(new ArrayList<>(seedStates));
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
    @DisplayName("generate resets countParticle when it reaches countRef")
    void testGenerateResetsCountParticleWhenReachesCountRef() throws Exception {
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertTrue(ParticleSwarmOptimization.countRef > 0);
        // put countParticle at countRef to trigger reset branch
        ParticleSwarmOptimization.countParticle = ParticleSwarmOptimization.countRef;
        State out = pso.generate(1);
        assertNotNull(out);
        assertEquals(1, ParticleSwarmOptimization.countParticle);
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
    @DisplayName("inicialiceLBest handles null pBest and still sets lBest entries")
    void testInicialiceLBest_NullReference_AndFallbacks() {
        // Build PSO with seeded particles
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertFalse(pso.getListParticle().isEmpty());

        // Force first particle pBest to null so reference==null branch executes
        pso.getListParticle().get(0).setStatePBest(null);
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = Math.max(1, pso.getListParticle().size());
        ParticleSwarmOptimization.lBest = new State[Math.max(1, ParticleSwarmOptimization.coutSwarm)];

        pso.inicialiceLBest();
        assertNull(ParticleSwarmOptimization.lBest[0]);
    }

    @Test
    @DisplayName("inicialiceLBest picks best pBest based on ProblemType MAX/MIN")
    void testInicialiceLBest_PicksBestAccordingToProblemType() {
        // Ensure Strategy and problem exist
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new TreeMap<>();

        // Seed RandomSearch with 2 states
        RandomSearch.listStateReference.clear();
        State s1 = new State(); s1.setCode(new ArrayList<>()); s1.setEvaluation(new ArrayList<>(List.of(10.0)));
        State s2 = new State(); s2.setCode(new ArrayList<>()); s2.setEvaluation(new ArrayList<>(List.of(5.0)));
        RandomSearch.listStateReference.add(s1);
        RandomSearch.listStateReference.add(s2);

        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 2;
        ParticleSwarmOptimization.lBest = new State[1];

        // MAX picks 10
        Problem pMax = new Problem();
        pMax.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(pMax);
        ParticleSwarmOptimization psoMax = new ParticleSwarmOptimization();
        psoMax.inicialiceLBest();
        assertNotNull(ParticleSwarmOptimization.lBest[0]);
        assertEquals(10.0, ParticleSwarmOptimization.lBest[0].getEvaluation().get(0), 1e-9);

        // MIN picks 5
        Problem pMin = new Problem();
        pMin.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        Strategy.setProblem(pMin);
        ParticleSwarmOptimization psoMin = new ParticleSwarmOptimization();
        psoMin.inicialiceLBest();
        assertNotNull(ParticleSwarmOptimization.lBest[0]);
        assertEquals(5.0, ParticleSwarmOptimization.lBest[0].getEvaluation().get(0), 1e-9);
    }

    @Test
    @DisplayName("updateReference resizes lBest and initializes null lBest[swarm]")
    void testUpdateReference_ResizesLBest_AndInitializesNullEntry() throws Exception {
        // MIN or MAX doesn't matter for resizing/null init; keep MAX for simplicity
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(p);

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertFalse(pso.getListParticle().isEmpty());

        // Force swarm index to be out of bounds and lBest small
        ParticleSwarmOptimization.coutSwarm = 2;
        ParticleSwarmOptimization.countParticleBySwarm = 1;
        ParticleSwarmOptimization.countParticle = 1; // swarm=1
        ParticleSwarmOptimization.lBest = new State[1]; // too small
        // Ensure particle has pBest
        Particle part = pso.getListParticle().get(1 % pso.getListParticle().size());
        State pb = new State(); pb.setCode(new ArrayList<>()); pb.setEvaluation(new ArrayList<>(List.of(7.0)));
        part.setStatePBest(pb);

        // Need a reference list to avoid NPE in gBest creation branch checks
        State ref = new State(); ref.setEvaluation(new ArrayList<>(List.of(0.0))); ref.setCode(new ArrayList<>());
        pso.getReferenceList().add(ref);

        pso.updateReference(new State(), 0);
        assertTrue(ParticleSwarmOptimization.lBest.length >= 2);
        assertNotNull(ParticleSwarmOptimization.lBest[1]);
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

    @Test
    @DisplayName("Counters/trace getters return expected arrays")
    void testCountersAndTraceGetters() {
        Strategy.destroyExecute();
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        RandomSearch.listStateReference.clear();
        State s = new State();
        ArrayList<Double> ev = new ArrayList<>(); ev.add(1.0);
        s.setEvaluation(ev);
        ArrayList<Object> code = new ArrayList<>(); code.add(0);
        s.setCode(code);
        RandomSearch.listStateReference.add(s);
        ParticleSwarmOptimization.coutSwarm = 1;
        ParticleSwarmOptimization.countParticleBySwarm = 1;
        ParticleSwarmOptimization.countRef = 1;

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        pso.countGender = 7;
        pso.listCountBetterGender = new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
        assertSame(pso.listCountBetterGender, pso.getListCountBetterGender());
        int[] usage = pso.getListCountGender();
        assertEquals(10, usage.length);
        assertEquals(7, usage[0]);
        float[] trace1 = pso.getTrace();
        float[] trace2 = pso.getTrace();
        assertNotNull(trace1);
        assertSame(trace1, trace2);
    }
}

